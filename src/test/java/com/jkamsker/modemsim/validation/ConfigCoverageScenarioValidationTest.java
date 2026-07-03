package com.jkamsker.modemsim.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigCoverageScenarioValidationTest {
    @TempDir
    Path tempDir;

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

    @Test
    void rejectsCoverageStatusDriftAndMissingRequiredCommands() throws Exception {
        Path coverage = tempDir.resolve("coverage.json");
        Files.writeString(coverage, """
                {
                  "profile": "generic-hayes-v250",
                  "source": "test",
                  "commands_total": 1,
                  "implemented_full": 2,
                  "implemented_stub": 0,
                  "unsupported_declared": 0,
                  "not_applicable": 0,
                  "unknown": 0,
                  "commands": [
                    {"command": "AT", "status": "implemented_full", "handler": "HayesHandler"}
                  ]
                }
                """);

        ValidationReport report = new CoverageValidator().validate(coverage);

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error ->
                assertThat(error).contains("implemented_full count"));
        assertThat(report.errors()).anySatisfy(error ->
                assertThat(error).contains("missing required coverage command"));
    }

    @Test
    void rejectsScenarioSemanticErrors() throws Exception {
        Path scenario = tempDir.resolve("bad-scenario.xml");
        Files.writeString(scenario, """
                <?xml version="1.0" encoding="UTF-8"?>
                <scenario id="bad" version="1.0" clock="virtual">
                  <initial-state>
                    <signal rssi="32" ber="0"/>
                  </initial-state>
                  <step atMs="10">
                    <set path="state.unknown.value" value="1"/>
                  </step>
                  <step atMs="5"/>
                  <expect command="AT"/>
                </scenario>
                """);

        ValidationReport report = new ScenarioValidator().validate(scenario);

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("rssi"));
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("unknown state path"));
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("ordered by atMs"));
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("expectation"));
    }
}
