package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemLines;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.HeadlessEndpoint;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;
import com.jkamsker.modemsim.transport.SerialEndpoint;
import com.jkamsker.modemsim.transport.SerialException;
import com.jkamsker.modemsim.transport.SerialRead;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ModemRuntimeTest {
    @Test
    void headlessRuntimeProcessesInputThroughConfiguredProfile() {
        ModemRuntime runtime = new ModemRuntime(binding -> new HeadlessEndpoint());

        RuntimeResult result = runtime.run(config(false, false), List.of(RawBytes.ascii("AT\r")), -1);

        assertThat(result.readsProcessed()).isEqualTo(1);
        assertThat(result.output().toHex()).isEqualTo("0D0A4F4B0D0A");
    }

    @Test
    void optionalSidecarOpenFailureIsIgnoredUnlessStrictPolicyIsEnabled() {
        ModemRuntime runtime = new ModemRuntime(this::endpoint);

        RuntimeResult result = runtime.run(config(true, false), List.of(RawBytes.ascii("AT\r")), -1);

        assertThat(result.readsProcessed()).isEqualTo(1);
        assertThatThrownBy(() -> runtime.run(config(true, true), List.of(), 0))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Runtime failed: sidecar unavailable");
    }

    private RuntimeConfig config(boolean sidecarEnabled, boolean strict) {
        return new RuntimeConfig(
                12345,
                com.jkamsker.modemsim.scheduler.ClockMode.VIRTUAL,
                strict,
                new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE),
                List.of(
                        new PortBinding("modem", EndpointType.HEADLESS, PortRole.MODEM_SIMULATION,
                                null, true, "sierra-hl6-hl8-v20", "tagged-text"),
                        new PortBinding("sniffer", EndpointType.HEADLESS, PortRole.SNIFFER,
                                null, sidecarEnabled, null, "tagged-text")));
    }

    private SerialEndpoint endpoint(PortBinding binding) {
        return binding.isModemSimulation() ? new HeadlessEndpoint() : new FailingEndpoint();
    }

    private static final class FailingEndpoint implements SerialEndpoint {
        @Override
        public void open(SerialConfig config) throws SerialException {
            throw new SerialException("sidecar unavailable");
        }

        @Override
        public SerialRead read() throws IOException {
            throw new IOException("not opened");
        }

        @Override
        public void write(byte[] buffer, int offset, int length) {
        }

        @Override
        public ModemLines readLines() {
            return ModemLines.ready();
        }

        @Override
        public void writeLines(ModemLines lines) {
        }

        @Override
        public void close() {
        }
    }
}
