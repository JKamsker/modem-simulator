package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.Parity;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class RuntimeConfigLoaderTest {
    @Test
    void loadsValidatedRuntimeConfig() {
        RuntimeConfig config = new RuntimeConfigLoader().load(Path.of("src/test/resources/config/valid.yaml"));

        assertThat(config.sessionSeed()).isEqualTo(12345);
        assertThat(config.clockMode()).isEqualTo(ClockMode.VIRTUAL);
        assertThat(config.serialLine().baudRate()).isEqualTo(115200);
        assertThat(config.serialLine().parity()).isEqualTo(Parity.NONE);
        assertThat(config.serialLine().flowControl()).isEqualTo(FlowControl.NONE);
        assertThat(config.allowUnsafeDceTransmit()).isFalse();
        assertThat(config.eventLogPath()).hasFileName("main-12345.jsonl");
        assertThat(config.initialScenario()).isNull();
        assertThat(config.modemPort().profile()).isEqualTo("sierra-hl6-hl8-v20");
        assertThat(config.enabledSidecars()).isEmpty();
    }

    @Test
    void loadsMacroTimerDefinitions(@org.junit.jupiter.api.io.TempDir Path tempDir) throws Exception {
        Path configPath = tempDir.resolve("config.yaml");
        Files.writeString(configPath, """
                sessionSeed: 12345
                clockMode: virtual
                macros: macros.xml
                macroTimers:
                  - id: heartbeat
                    atMs: 1000
                serialLine: {baudRate: 115200, dataBits: 8, stopBits: 1, parity: NONE, flowControl: NONE}
                ports:
                  - {id: modem, type: headless, role: modem-simulation, enabled: true, profile: sierra-hl6-hl8-v20}
                redaction: {enabled: true, maskPin: true, maskPuk: true, maskImsi: true, maskIccid: true, maskImei: true, maskMsisdn: true, maskSmsBody: true}
                """);

        RuntimeConfig config = new RuntimeConfigLoader().load(configPath);

        assertThat(config.macros()).isEqualTo(tempDir.resolve("macros.xml").normalize());
        assertThat(config.macroTimers()).containsExactly(new RuntimeTimer("heartbeat", 1000));
    }
}
