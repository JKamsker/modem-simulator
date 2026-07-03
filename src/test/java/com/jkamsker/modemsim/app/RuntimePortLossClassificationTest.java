package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.state.ModemLines;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;
import com.jkamsker.modemsim.transport.SerialEndpoint;
import com.jkamsker.modemsim.transport.SerialRead;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RuntimePortLossClassificationTest {
    @TempDir
    Path tempDir;

    @Test
    void genericMainPortIoFailureIsNotClassifiedAsPortLost() {
        ModemRuntime runtime = new ModemRuntime(binding -> new GenericReadFailureEndpoint());

        assertThatThrownBy(() -> runtime.run(config(), List.of(), 1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Runtime failed: transient read failure");
    }

    private RuntimeConfig config() {
        return new RuntimeConfig(
                12345, ClockMode.VIRTUAL, false, false, tempDir.resolve("runtime.jsonl"),
                new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE),
                List.of(new PortBinding("modem", EndpointType.HEADLESS, PortRole.MODEM_SIMULATION,
                        null, true, "sierra-hl6-hl8-v20", "tagged-text")));
    }

    private static final class GenericReadFailureEndpoint implements SerialEndpoint {
        @Override public void open(SerialConfig config) { }
        @Override public SerialRead read() throws IOException { throw new IOException("transient read failure"); }
        @Override public void write(byte[] buffer, int offset, int length) { }
        @Override public ModemLines readLines() { return ModemLines.ready(); }
        @Override public void writeLines(ModemLines lines) { }
        @Override public void close() { }
    }
}
