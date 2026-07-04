package com.jkamsker.modemsim.validation;

import com.jkamsker.modemsim.profiles.ProfileXmlLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileSecretValidationTest {
    @TempDir
    Path tempDir;

    @Test
    void rejectsInlinePinAndPukUnlessProfileIsExplicitTestFixture() throws Exception {
        assertInlineSecretRejected("candidate", "pin", "1234");
        assertInlineSecretRejected("stub", "puk", "87654321");

        ValidationReport report = new ProfileXmlLoader().validate(profile("fixture", "test-fixture",
                "<sim state=\"SIM_PIN_REQUIRED\" pinQueryEnabled=\"true\" pin=\"1234\"/>"));

        assertThat(report.valid()).as(report.errors().toString()).isTrue();
    }

    private void assertInlineSecretRejected(String status, String secret, String value) throws Exception {
        ValidationReport report = new ProfileXmlLoader().validate(profile(status + "-" + secret, status,
                "<sim state=\"SIM_PIN_REQUIRED\" pinQueryEnabled=\"true\" " + secret + "=\"" + value + "\"/>"));

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("inline test " + secret));
    }

    private Path profile(String id, String status, String simXml) throws Exception {
        Path path = tempDir.resolve(id + ".xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="%s" vendor="test" status="%s" profileKind="cellular">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state>
                      %s
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
                """.formatted(id, status, simXml));
        return path;
    }
}
