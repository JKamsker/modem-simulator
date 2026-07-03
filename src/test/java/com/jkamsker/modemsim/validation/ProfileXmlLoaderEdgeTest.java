package com.jkamsker.modemsim.validation;

import com.jkamsker.modemsim.profiles.ProfileXmlLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileXmlLoaderEdgeTest {
    @TempDir
    Path tempDir;

    @Test
    void singleProfileLoadRejectsAmbiguousMultiProfileXml() throws Exception {
        Path path = multiProfile();
        ProfileXmlLoader loader = new ProfileXmlLoader();

        assertThatThrownBy(() -> loader.load(path))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("multiple profiles");
        assertThat(loader.load(path, "child").id()).isEqualTo("child");
    }

    @Test
    void baseAndIsdnProfilesWithoutInitialStateDoNotGetCellularNetworkDefaults() throws Exception {
        Path path = tempDir.resolve("non-cellular.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="base-profile" vendor="test" status="candidate" profileKind="base">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                  </profile>
                  <profile id="isdn-profile" vendor="test" status="candidate" profileKind="isdn">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                  </profile>
                </modem-simulator>
                """);
        ProfileXmlLoader loader = new ProfileXmlLoader();

        assertThat(loader.load(path, "base-profile").initialState().network()).isNull();
        assertThat(loader.load(path, "isdn-profile").initialState().network()).isNull();
    }

    @Test
    void profileMetadataIsParsedAndMergedFromParents() throws Exception {
        Path path = tempDir.resolve("metadata.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="parent" vendor="test" status="candidate" profileKind="base">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <commands><command name="AT" status="implemented_full" handler="HayesHandler"/></commands>
                    <registers><register name="S3" default="13" min="0" max="127"/></registers>
                    <coverage source="parent.md" commandsTotal="1" unknown="0">
                      <command name="AT" status="implemented_full" handler="HayesHandler"/>
                    </coverage>
                    <deviations><deviation id="parent-dev" severity="info">Parent note</deviation></deviations>
                  </profile>
                  <profile id="child" vendor="test" status="candidate" profileKind="base" extends="parent">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <commands><command name="AT+X" status="implemented_stub" reason="fixture"/></commands>
                    <registers><register name="S4" default="10" min="0" max="127"/></registers>
                    <coverage source="child.md" commandsTotal="2" unknown="0">
                      <command name="AT+X" status="implemented_stub"/>
                    </coverage>
                    <deviations><deviation id="child-dev" severity="warning">Child note</deviation></deviations>
                  </profile>
                </modem-simulator>
                """);

        var child = new ProfileXmlLoader().load(path, "child");

        assertThat(child.commands()).extracting(command -> command.name()).containsExactly("AT", "AT+X");
        assertThat(child.registers()).extracting(register -> register.name()).containsExactly("S3", "S4");
        assertThat(child.coverage().source()).isEqualTo("child.md");
        assertThat(child.coverage().commands()).extracting(command -> command.name()).containsExactly("AT", "AT+X");
        assertThat(child.deviations()).extracting(deviation -> deviation.id()).containsExactly("parent-dev", "child-dev");
    }

    @Test
    void profileCoverageMetadataIsSemanticallyValidated() throws Exception {
        Path path = tempDir.resolve("bad-coverage.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="coverage-profile" vendor="test" status="candidate" profileKind="base">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <coverage source="coverage.md" commandsTotal="1" unknown="1">
                      <command name="AT" status="implemented_full" handler="HayesHandler"/>
                      <command name="AT+X" status="implemented_stub"/>
                      <command name="AT" status="implemented_full" handler="HayesHandler"/>
                    </coverage>
                  </profile>
                </modem-simulator>
                """);

        ValidationReport report = new ProfileXmlLoader().validate(path);

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("coverage unknown"));
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("duplicate coverage command"));
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("commandsTotal"));
    }

    @Test
    void rejectsInlinePinForRuntimeDeviceTargets() throws Exception {
        Path path = tempDir.resolve("inline-pin.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="inline-pin" vendor="test" status="device-target" profileKind="cellular">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state>
                      <sim state="SIM_PIN_REQUIRED" pinQueryEnabled="true" pin="1234"/>
                      <network cregN="2" stat="0">
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

        ValidationReport report = new ProfileXmlLoader().validate(path);

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("inline test pin"));
    }

    @Test
    void reportsInheritedMetadataConflicts() throws Exception {
        Path path = tempDir.resolve("conflict.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="left" vendor="test" status="candidate" profileKind="base">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <commands><command name="AT" status="implemented_full"/></commands>
                    <registers><register name="S3" default="13"/></registers>
                  </profile>
                  <profile id="right" vendor="test" status="candidate" profileKind="base">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <commands><command name="AT" status="implemented_stub"/></commands>
                    <registers><register name="S3" default="10"/></registers>
                  </profile>
                  <profile id="child" vendor="test" status="candidate" profileKind="base" extends="left right">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                  </profile>
                </modem-simulator>
                """);

        ValidationReport report = new ProfileXmlLoader().validate(path);

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("command conflict AT"));
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("register conflict S3"));
    }

    @Test
    void compatibilityDeviationAllowsNonRegisteredLocationFields() throws Exception {
        Path path = tempDir.resolve("deviation.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="deviation-profile" vendor="test" status="candidate" profileKind="cellular">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state>
                      <sim state="READY"/>
                      <network cregN="2" stat="4" lac="00C3" ci="00001234" act="7">
                        <operator selectionMode="automatic" format="long"
                                  longName="Telekom.de" numeric="26201" mcc="262" mnc="01"/>
                      </network>
                      <signal rssi="18" ber="0"/>
                      <call mode="command" carrier="false"/>
                      <modem-lines dtr="true" dsr="true" dcd="false" ri="false" rts="true" cts="true"/>
                    </initial-state>
                    <deviations><deviation id="location-while-denied" severity="compatibility">fixture</deviation></deviations>
                  </profile>
                </modem-simulator>
                """);

        ValidationReport report = new ProfileXmlLoader().validate(path);

        assertThat(report.valid()).as(report.errors().toString()).isTrue();
    }

    private Path multiProfile() throws Exception {
        Path path = tempDir.resolve("multi.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="parent" vendor="test" status="candidate" profileKind="cellular">
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
                  <profile id="child" vendor="test" status="candidate" profileKind="cellular" extends="parent">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state><signal rssi="12" ber="1"/></initial-state>
                  </profile>
                </modem-simulator>
                """);
        return path;
    }
}
