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
    void rejectsUnsupportedSerialBaudRates() throws Exception {
        Path config = tempDir.resolve("bad-baud.yaml");
        Files.writeString(config, """
                sessionSeed: 12345
                clockMode: virtual
                serialLine: {baudRate: 12345, dataBits: 8, stopBits: 1, parity: NONE, flowControl: NONE}
                ports:
                  - {id: modem, type: headless, role: modem-simulation, enabled: true, profile: sierra-hl6-hl8-v20}
                redaction: {enabled: true, maskPin: true, maskPuk: true, maskImsi: true, maskIccid: true, maskImei: true, maskMsisdn: true, maskSmsBody: true}
                """);

        ValidationReport report = new ConfigValidator().validate(config);

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("baudRate"));
    }

    @Test
    void verifiesCoverageForAllV1Targets() {
        ValidationReport report = new CoverageValidator()
                .verifyV1Targets(Path.of("src/main/resources/coverage/v1-targets"));

        assertThat(report.valid()).as(report.errors().toString()).isTrue();
    }

    @Test
    void rejectsUnexpectedCoverageTargetFiles() throws Exception {
        Path source = Path.of("src/main/resources/coverage/v1-targets");
        try (var files = Files.list(source)) {
            files.forEach(path -> {
                try {
                    Files.copy(path, tempDir.resolve(path.getFileName().toString()));
                } catch (java.io.IOException e) {
                    throw new java.io.UncheckedIOException(e);
                }
            });
        }
        Files.writeString(tempDir.resolve("extra.json"), """
                {
                  "profile": "unexpected-profile",
                  "source": "test",
                  "commands_total": 0,
                  "unknown": 0,
                  "commands": []
                }
                """);

        ValidationReport report = new CoverageValidator().verifyV1Targets(tempDir);

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("Unexpected v1 coverage report"));
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
	                    <sim state="BROKEN"/>
	                    <network cregN="4" stat="12"/>
	                    <signal rssi="32" ber="0"/>
	                  </initial-state>
                  <step atMs="10">
                    <set path="state.unknown.value" value="1"/>
                  </step>
                  <step atMs="11">
                    <fault type="network-restore" stat="99" rssi="18" ber="0"/>
                  </step>
                  <step atMs="5"/>
                  <expect command="AT"/>
                </scenario>
                """);

        ValidationReport report = new ScenarioValidator().validate(scenario);

	        assertThat(report.valid()).isFalse();
	        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("state.sim.state"));
	        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("state.network.cregN"));
	        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("rssi"));
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("unknown state path"));
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("state.network.stat"));
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("ordered by atMs"));
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("expectation"));
    }
}
