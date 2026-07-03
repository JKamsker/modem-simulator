package com.jkamsker.modemsim.validation;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigCoverageScenarioValidationTest {
    @Test
    void validatesConfigAgainstSchemaAndPortRules() {
        ConfigValidator validator = new ConfigValidator();

        assertThat(validator.validate(Path.of("src/test/resources/config/valid.yaml")).valid()).isTrue();
        assertThat(validator.validate(Path.of("src/test/resources/config/invalid-port-override.yaml")).valid())
                .isFalse();
        ValidationReport manualDce = validator.validate(
                Path.of("src/test/resources/config/invalid-manual-dce-permission.yaml"));
        assertThat(manualDce.valid()).isFalse();
        assertThat(manualDce.errors()).anyMatch(error -> error.contains("allowUnsafeDceTransmit"));
    }

    @Test
    void verifiesCoverageForAllV1Targets() {
        ValidationReport report = new CoverageValidator()
                .verifyV1Targets(Path.of("src/main/resources/coverage/v1-targets"));

        assertThat(report.valid()).as(report.errors().toString()).isTrue();
    }

    @Test
    void validatesScenarioExample() {
        ValidationReport report = new ScenarioValidator()
                .validate(Path.of("docs/Tasks/Initial-Spec/examples/scenario.no-network.xml"));

        assertThat(report.valid()).as(report.errors().toString()).isTrue();
    }
}
