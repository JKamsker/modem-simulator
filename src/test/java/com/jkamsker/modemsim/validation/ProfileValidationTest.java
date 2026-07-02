package com.jkamsker.modemsim.validation;

import com.jkamsker.modemsim.profiles.ProfileXmlLoader;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileValidationTest {
    private final ProfileXmlLoader loader = new ProfileXmlLoader();

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
}
