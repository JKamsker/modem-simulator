package com.jkamsker.modemsim.validation;

import com.jkamsker.modemsim.profiles.ProfileXmlLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileSemanticNegativeValidationTest {
    @TempDir
    Path tempDir;

    @Test
    void pstnProfileCannotClaimCellularCoverageCommands() throws Exception {
        ValidationReport report = validate("pstn-cellular-coverage.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="pstn-profile" vendor="test" status="candidate" profileKind="pstn">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <coverage source="pstn.md" commandsTotal="1" unknown="0">
                      <command name="+CREG" status="implemented_full" handler="CellularHandler"/>
                    </coverage>
                  </profile>
                </modem-simulator>
                """);

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("cellular coverage command +CREG"));
    }

    @Test
    void nonRegisteredCregStateCannotExposeLocationWithoutDeviation() throws Exception {
        ValidationReport report = validate("nonregistered-location.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="bad-creg" vendor="test" status="candidate" profileKind="cellular">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state>
                      <sim state="READY"/>
                      <network cregN="2" stat="0" lac="00C3" ci="00001234" act="7">
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

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("lac/ci/act"));
    }

    @Test
    void invalidUnknownAtCommandPolicyIsRejectedByProfileValidation() throws Exception {
        ValidationReport report = validate("unknown-policy.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="bad-policy" vendor="test" status="candidate" profileKind="base">
                    <dialect commandTerminator="CR" responseTerminator="CRLF" unknownAtCommand="what"/>
                  </profile>
                </modem-simulator>
                """);

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("what").contains("enumeration"));
    }

    private ValidationReport validate(String name, String content) throws Exception {
        Path path = tempDir.resolve(name);
        Files.writeString(path, content);
        return new ProfileXmlLoader().validate(path);
    }
}
