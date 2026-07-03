package com.jkamsker.modemsim.validation;

import com.jkamsker.modemsim.profiles.ProfileXmlLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileValidationTest {
    private final ProfileXmlLoader loader = new ProfileXmlLoader();

    @TempDir
    Path tempDir;

    @Test
    void validatesAndLoadsSampleProfile() {
        Path sample = Path.of("docs/Tasks/Initial-Spec/examples/modem-profile.sample.xml");

        ValidationReport report = loader.validate(sample);
        var profile = loader.load(sample);

        assertThat(report.valid()).as(report.errors().toString()).isTrue();
        assertThat(profile.id()).isEqualTo("sierra-hl6-hl8-test");
        assertThat(profile.initialState().settings().echo()).isTrue();
        assertThat(profile.initialState().network().smsRateLimit().rejectCmsError()).isEqualTo(500);
    }

    @Test
    void rejectsLockedSimThatIsRegistered() {
        ValidationReport report = loader.validate(Path.of("src/test/resources/profiles/locked-registered.xml"));

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error ->
                assertThat(error).contains("locked or failed SIM"));
    }

    @Test
    void rejectsDoctypeBeforeEntityExpansion() {
        ValidationReport report = loader.validate(Path.of("src/test/resources/profiles/xxe-profile.xml"));

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error ->
                assertThat(error).containsIgnoringCase("DOCTYPE"));
    }

    @Test
    void rejectsReservedSmsRateLimitCms310() throws Exception {
        ValidationReport report = loader.validate(writeProfile("cms-310", """
                  <initial-state>
                    <sim state="READY"/>
                    <network cregN="2" stat="1">
                      <sms-rate-limit maxMessages="1" windowSeconds="60" scope="session" rejectCmsError="310"/>
                    </network>
                    <signal rssi="18" ber="0"/>
                  </initial-state>
                """));

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error ->
                assertThat(error).contains("CMS 310"));
    }

    @Test
    void rejectsInvalidRssiRange() throws Exception {
        ValidationReport report = loader.validate(writeProfile("bad-rssi", """
                  <initial-state>
                    <sim state="READY"/>
                    <network cregN="2" stat="1"/>
                    <signal rssi="32" ber="0"/>
                  </initial-state>
                """));

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error ->
                assertThat(error).contains("CsqRssiType"));
    }

    @Test
    void rejectsMissingRequiredCellularXmlBlocks() throws Exception {
        ValidationReport report = loader.validate(writeBareProfile("missing-signal", "", """
                  <initial-state>
                    <sim state="READY"/>
                    <network cregN="2" stat="1">
                      <operator selectionMode="automatic" format="long"
                                longName="Telekom.de" numeric="26201" mcc="262" mnc="01"/>
                    </network>
                  </initial-state>
                """));

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("signal state"));
    }

    @Test
    void rejectsMissingOperatorDuplicateDelayAndUnknownParent() throws Exception {
        ValidationReport report = loader.validate(writeBareProfile("bad-semantics", "does-not-exist", """
                  <initial-state>
                    <sim state="READY"/>
                    <network cregN="2" stat="1">
                      <delays>
                        <delay operation="sms-submit" minMs="1" maxMs="2"/>
                        <delay operation="sms-submit" minMs="3" maxMs="4"/>
                      </delays>
                    </network>
                    <signal rssi="18" ber="0"/>
                  </initial-state>
                """));

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("missing parent"));
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("operator metadata"));
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("duplicate delay"));
    }

    @Test
    void rejectsProfileInheritanceCycles() throws Exception {
        Path path = tempDir.resolve("cycle.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="a" vendor="test" status="candidate" profileKind="cellular" extends="b">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state>
                      <sim state="READY"/>
                      <network cregN="2" stat="1">
                        <operator selectionMode="automatic" format="long"
                                  longName="Telekom.de" numeric="26201" mcc="262" mnc="01"/>
                      </network>
                      <signal rssi="18" ber="0"/>
                    </initial-state>
                  </profile>
                  <profile id="b" vendor="test" status="candidate" profileKind="cellular" extends="a">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state>
                      <sim state="READY"/>
                      <network cregN="2" stat="1">
                        <operator selectionMode="automatic" format="long"
                                  longName="Telekom.de" numeric="26201" mcc="262" mnc="01"/>
                      </network>
                      <signal rssi="18" ber="0"/>
                      <call mode="command" carrier="false"/>
                      <modem-lines dtr="true" dsr="true" dcd="false" ri="false" rts="true" cts="true"/>
                    </initial-state>
                  </profile>
                </modem-simulator>
                """);

        ValidationReport report = loader.validate(path);

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("cyclic parent profile"));
    }

    @Test
    void pinQueryEnabledWithoutStateStartsLocked() throws Exception {
        var profile = loader.load(writeProfile("pin-query", """
                  <initial-state>
                    <sim pinQueryEnabled="true" pinRef="TEST_SIM_PIN"/>
                      <network cregN="2" stat="0">
                        <operator selectionMode="automatic" format="long"
                                  longName="Telekom.de" numeric="26201" mcc="262" mnc="01"/>
                      </network>
                      <signal rssi="18" ber="0"/>
                      <call mode="command" carrier="false"/>
                      <modem-lines dtr="true" dsr="true" dcd="false" ri="false" rts="true" cts="true"/>
                    </initial-state>
                """));

        assertThat(profile.initialState().sim().state().name()).isEqualTo("SIM_PIN_REQUIRED");
    }

    @Test
    void dialectTerminatorsInitializeSessionRegisters() throws Exception {
        Path path = tempDir.resolve("lf-profile.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="lf-profile" vendor="test" status="candidate" profileKind="cellular">
                    <dialect commandTerminator="LF" responseTerminator="CR"/>
                    <initial-state>
                      <sim state="READY"/>
                      <network cregN="2" stat="1">
                        <operator selectionMode="automatic" format="long"
                                  longName="Telekom.de" numeric="26201" mcc="262" mnc="01"/>
                      </network>
                      <signal rssi="18" ber="0"/>
                      <call mode="command" carrier="false"/>
                      <modem-lines dtr="true" dsr="true" dcd="false" ri="false" rts="true" cts="true"/>
                    </initial-state>
                  </profile>
                </modem-simulator>
                """);

        var profile = loader.load(path);

        assertThat(profile.initialState().settings().s3()).isEqualTo(10);
        assertThat(profile.initialState().settings().s4()).isEqualTo(13);
    }

    @Test
    void loadsInheritedProfileWithChildStateOverrides() throws Exception {
        Path path = tempDir.resolve("inherited.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="child-profile" vendor="test" status="candidate" profileKind="cellular" extends="parent-profile">
                    <dialect commandTerminator="LF" responseTerminator="CR" defaultEcho="true"/>
                    <initial-state>
                      <signal rssi="7" ber="1"/>
                    </initial-state>
                  </profile>
                  <profile id="parent-profile" vendor="test" status="candidate" profileKind="cellular">
                    <identity manufacturer="ParentCo" model="ParentModel" imei="359762080000001"/>
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state>
                      <sim state="READY"/>
                      <network cregN="2" stat="1" lac="00C3" ci="00001234" act="7">
                        <operator selectionMode="automatic" format="long"
                                  longName="Telekom.de" numeric="26201" mcc="262" mnc="01"/>
                      </network>
                      <signal rssi="18" ber="0"/>
                      <call mode="command" carrier="false"/>
                      <modem-lines dtr="true" dsr="true" dcd="false" ri="false" rts="true" cts="true"/>
                    </initial-state>
                  </profile>
                </modem-simulator>
                """);

        var report = loader.validate(path);
        var profile = loader.load(path, "child-profile");

        assertThat(report.valid()).as(report.errors().toString()).isTrue();
        assertThat(profile.id()).isEqualTo("child-profile");
        assertThat(profile.identity().manufacturer()).isEqualTo("ParentCo");
        assertThat(profile.initialState().network().stat()).isEqualTo(1);
        assertThat(profile.initialState().signal().rssi()).isEqualTo(7);
        assertThat(profile.initialState().settings().s3()).isEqualTo(10);
        assertThat(profile.initialState().settings().echo()).isTrue();
    }

    private Path writeProfile(String id, String stateXml) throws Exception {
        return writeBareProfile(id, "", stateXml);
    }

    private Path writeBareProfile(String id, String parent, String stateXml) throws Exception {
        Path path = tempDir.resolve(id + ".xml");
        String extendsAttr = parent.isBlank() ? "" : " extends=\"" + parent + "\"";
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="%s" vendor="test" status="candidate" profileKind="cellular"%s>
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                %s
                  </profile>
                </modem-simulator>
                """.formatted(id, extendsAttr, completeState(stateXml)));
        return path;
    }

    private String completeState(String xml) {
        if (!xml.contains("<initial-state>")) {
            return xml;
        }
        String additions = "";
        if (!xml.contains("<call")) {
            additions += "    <call mode=\"command\" carrier=\"false\"/>\n";
        }
        if (!xml.contains("<modem-lines")) {
            additions += "    <modem-lines dtr=\"true\" dsr=\"true\" dcd=\"false\" ri=\"false\" rts=\"true\" cts=\"true\"/>\n";
        }
        return additions.isBlank() ? xml : xml.replace("</initial-state>", additions + "  </initial-state>");
    }
}
