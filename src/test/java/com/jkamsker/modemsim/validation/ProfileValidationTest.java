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

    private Path writeProfile(String id, String stateXml) throws Exception {
        Path path = tempDir.resolve(id + ".xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="%s" vendor="test" status="candidate" profileKind="cellular">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                %s
                  </profile>
                </modem-simulator>
                """.formatted(id, stateXml));
        return path;
    }
}
