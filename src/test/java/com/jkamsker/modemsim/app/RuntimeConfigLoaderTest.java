package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.Parity;
import org.junit.jupiter.api.Test;

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
        assertThat(config.modemPort().profile()).isEqualTo("sierra-hl6-hl8-v20");
        assertThat(config.enabledSidecars()).isEmpty();
    }
}
