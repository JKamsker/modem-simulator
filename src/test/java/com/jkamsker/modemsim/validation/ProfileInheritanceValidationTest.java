package com.jkamsker.modemsim.validation;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.ProfileXmlLoader;
import com.jkamsker.modemsim.session.HeadlessSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileInheritanceValidationTest {
    @TempDir
    Path tempDir;

    @Test
    void inheritedNetworkOverridesCanReuseParentOperatorMetadata() throws Exception {
        Path path = tempDir.resolve("network-override.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="parent" vendor="test" status="candidate" profileKind="cellular">
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
                  <profile id="child" vendor="test" status="candidate" profileKind="cellular" extends="parent">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state><network cregN="2" stat="1"/></initial-state>
                  </profile>
                </modem-simulator>
                """);

        ValidationReport report = new ProfileXmlLoader().validate(path);
        var child = new ProfileXmlLoader().load(path, "child");

        assertThat(report.valid()).as(report.errors().toString()).isTrue();
        assertThat(new HeadlessSession("inherited", child, 12345)
                .receive(RawBytes.ascii("AT+COPS?\r")).outputAscii()).contains("Telekom.de");
    }

    @Test
    void childMetadataOverridesParentWithoutConflict() throws Exception {
        Path path = tempDir.resolve("child-command-override.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="parent" vendor="test" status="candidate" profileKind="base">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <commands><command name="AT" status="implemented_full" handler="HayesHandler"/></commands>
                  </profile>
                  <profile id="child" vendor="test" status="candidate" profileKind="base" extends="parent">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <commands><command name="AT" status="implemented_stub" handler="HayesHandler"/></commands>
                  </profile>
                </modem-simulator>
                """);

        ValidationReport report = new ProfileXmlLoader().validate(path);
        var child = new ProfileXmlLoader().load(path, "child");

        assertThat(report.valid()).as(report.errors().toString()).isTrue();
        assertThat(child.commands()).singleElement().satisfies(command ->
                assertThat(command.status()).isEqualTo("implemented_stub"));
    }

    @Test
    void warnsAboutConflictsBetweenBuiltInParentProfiles() throws Exception {
        Path path = tempDir.resolve("builtin-parent-conflict.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="child" vendor="test" status="candidate" profileKind="base"
                           extends="generic-hayes-v250 3gpp-27007-r18">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                  </profile>
                </modem-simulator>
                """);

        ValidationReport report = new ProfileXmlLoader().validate(path);

        assertThat(report.valid()).as(report.errors().toString()).isTrue();
        assertThat(report.warnings()).anySatisfy(warning -> assertThat(warning).contains("command conflict AT"));
        assertThat(report.warnings()).anySatisfy(warning -> assertThat(warning).contains("register conflict S3"));
    }

    @Test
    void laterLocalParentUsesInheritedEffectiveMetadata() throws Exception {
        Path path = tempDir.resolve("recursive-parent-conflict.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="left" vendor="test" status="candidate" profileKind="base">
                    <identity manufacturer="Left" model="LeftModel"/>
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <commands><command name="AT" status="implemented_full"/></commands>
                    <registers><register name="S3" default="13"/></registers>
                  </profile>
                  <profile id="grand-right" vendor="test" status="candidate" profileKind="base">
                    <identity manufacturer="Right" model="RightModel"/>
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <commands><command name="AT" status="implemented_stub"/></commands>
                    <registers><register name="S3" default="10"/></registers>
                  </profile>
                  <profile id="right" vendor="test" status="candidate" profileKind="base" extends="grand-right">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                  </profile>
                  <profile id="child" vendor="test" status="candidate" profileKind="base" extends="left right">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                  </profile>
                </modem-simulator>
                """);

        ValidationReport report = new ProfileXmlLoader().validate(path);
        var child = new ProfileXmlLoader().load(path, "child");

        assertThat(report.valid()).as(report.errors().toString()).isTrue();
        assertThat(report.warnings()).anySatisfy(warning -> assertThat(warning).contains("command conflict AT"));
        assertThat(child.identity().manufacturer()).isEqualTo("Right");
        assertThat(child.commands()).singleElement().satisfies(command ->
                assertThat(command.status()).isEqualTo("implemented_stub"));
        assertThat(child.registers()).singleElement().satisfies(register ->
                assertThat(register.defaultValue()).isEqualTo(10));
    }

    @Test
    void laterParentNetworkPreservesEarlierUndeclaredSubfields() throws Exception {
        Path path = tempDir.resolve("parent-network-subfield-merge.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="rate-parent" vendor="test" status="candidate" profileKind="base">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state>
                      <sim state="READY"/>
                      <network cregN="2" stat="1" lac="00C3" ci="00001234" act="7">
                        <operator selectionMode="automatic" format="long"
                                  longName="RateParent" numeric="26201" mcc="262" mnc="01"/>
                        <sms-rate-limit maxMessages="2" windowSeconds="30" scope="session" rejectCmsError="500"/>
                        <delays><delay operation="sms-submit" minMs="100" maxMs="200"/></delays>
                      </network>
                      <signal rssi="18" ber="0"/>
                      <call mode="command" carrier="false"/>
                      <modem-lines dtr="false" dsr="false" dcd="true" ri="true" rts="false" cts="false"/>
                    </initial-state>
                  </profile>
                  <profile id="operator-parent" vendor="test" status="candidate" profileKind="base">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state>
                      <network cregN="2" stat="1" lac="00D4" ci="00005678" act="7">
                        <operator selectionMode="automatic" format="long"
                                  longName="OperatorParent" numeric="26202" mcc="262" mnc="02"/>
                      </network>
                    </initial-state>
                  </profile>
                  <profile id="child" vendor="test" status="candidate" profileKind="cellular"
                           extends="rate-parent operator-parent">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                  </profile>
                </modem-simulator>
                """);

        ValidationReport report = new ProfileXmlLoader().validate(path);
        var child = new ProfileXmlLoader().load(path, "child");
        var network = child.initialState().network();

        assertThat(report.valid()).as(report.errors().toString()).isTrue();
        assertThat(network.operator().longName()).isEqualTo("OperatorParent");
        assertThat(network.smsRateLimit().maxMessages()).isEqualTo(2);
        assertThat(network.delays()).containsKey("sms-submit");
        assertThat(child.initialState().lines().dtr()).isFalse();
        assertThat(child.initialState().lines().dcd()).isTrue();
        assertThat(child.initialState().lines().ri()).isTrue();
    }

    @Test
    void childNetworkOverridesRequireParentOperatorMetadata() throws Exception {
        Path path = tempDir.resolve("network-override-without-operator.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="child" vendor="test" status="candidate" profileKind="cellular"
                           extends="generic-hayes-v250">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state><network cregN="2" stat="1"/></initial-state>
                  </profile>
                </modem-simulator>
                """);

        ValidationReport report = new ProfileXmlLoader().validate(path);

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("operator metadata"));
    }

    @Test
    void inheritedNetworkRequiresExplicitOperatorMetadata() throws Exception {
        Path path = tempDir.resolve("inherited-network-without-operator.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="parent" vendor="test" status="candidate" profileKind="base">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state><network cregN="2" stat="1"/></initial-state>
                  </profile>
                  <profile id="child" vendor="test" status="candidate" profileKind="cellular" extends="parent">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state>
                      <sim state="READY"/>
                      <signal rssi="18" ber="0"/>
                      <call mode="command" carrier="false"/>
                      <modem-lines dtr="true" dsr="true" dcd="false" ri="false" rts="true" cts="true"/>
                    </initial-state>
                  </profile>
                </modem-simulator>
                """);

        ValidationReport report = new ProfileXmlLoader().validate(path);

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("operator metadata"));
    }

    @Test
    void rejectsDuplicateProfileIds() throws Exception {
        Path path = tempDir.resolve("duplicate-profile-id.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="duplicate" vendor="test" status="candidate" profileKind="base">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                  </profile>
                  <profile id="duplicate" vendor="test" status="candidate" profileKind="base">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                  </profile>
                </modem-simulator>
                """);

        ValidationReport report = new ProfileXmlLoader().validate(path);

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("duplicate profile id"));
    }

    @Test
    void rejectsLocalProfileIdsThatShadowBuiltIns() throws Exception {
        Path path = tempDir.resolve("builtin-shadow.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="generic-hayes-v250" vendor="test" status="candidate" profileKind="base">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                  </profile>
                </modem-simulator>
                """);

        ValidationReport report = new ProfileXmlLoader().validate(path);

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error ->
                assertThat(error).contains("duplicate built-in profile id"));
    }
}
