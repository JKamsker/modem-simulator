package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.state.ModemLines;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;
import com.jkamsker.modemsim.transport.SerialEndpoint;
import com.jkamsker.modemsim.transport.SerialRead;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;

class RuntimeLineControlTest {
    @TempDir
    Path tempDir;

    @Test
    void dtrDropClearsCarrierAndDcdWhenAmpD2IsActive() throws Exception {
        LineDropEndpoint endpoint = new LineDropEndpoint();
        endpoint.enqueue(RawBytes.ascii("ATD123\r"), 0);
        endpoint.enqueue(RawBytes.empty(), 5_000_000_000L);
        RuntimeConfig config = new RuntimeConfig(
                12345, ClockMode.VIRTUAL, false, false, tempDir.resolve("dtr.jsonl"),
                new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE),
                List.of(new PortBinding("modem", EndpointType.HEADLESS, PortRole.MODEM_SIMULATION,
                        null, true, "sierra-hl6-hl8-v20", "tagged-text")));

        RuntimeResult result = new ModemRuntime(binding -> endpoint).run(config, List.of(), 2);

        assertThat(result.output().ascii()).contains("CONNECT");
        assertThat(endpoint.lastLines.dcd()).isFalse();
        assertThat(endpoint.lastLines.dtr()).isFalse();
        assertThat(Files.readString(result.eventLogPath())).contains("dtr-drop").contains("\"dtr\":false");
    }

    @Test
    void dtrDropUnderAmpD0UpdatesLineStateWithoutDroppingCarrier() throws Exception {
        LineDropEndpoint endpoint = new LineDropEndpoint();
        endpoint.enqueue(RawBytes.ascii("AT&D0\r"), 0);
        endpoint.enqueue(RawBytes.ascii("ATD123\r"), 1);
        endpoint.enqueue(RawBytes.empty(), 5_000_000_000L);
        RuntimeConfig config = new RuntimeConfig(
                12345, ClockMode.VIRTUAL, false, false, tempDir.resolve("dtr-ampd0.jsonl"),
                new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE),
                List.of(new PortBinding("modem", EndpointType.HEADLESS, PortRole.MODEM_SIMULATION,
                        null, true, "sierra-hl6-hl8-v20", "tagged-text")));

        RuntimeResult result = new ModemRuntime(binding -> endpoint).run(config, List.of(), 2);

        assertThat(result.output().ascii()).contains("CONNECT");
        assertThat(endpoint.lastLines.dtr()).isFalse();
        assertThat(endpoint.lastLines.dcd()).isTrue();
        assertThat(Files.readString(result.eventLogPath())).contains("line-change").contains("\"dtr\":false");
    }

    private static final class LineDropEndpoint implements SerialEndpoint {
        private final Queue<SerialRead> reads = new ArrayDeque<>();
        private RawBytes written = RawBytes.empty();
        private ModemLines lastLines = ModemLines.ready();

        void enqueue(RawBytes bytes, long nanos) {
            reads.add(new SerialRead(bytes, nanos, nanos));
        }

        @Override
        public void open(SerialConfig config) {
        }

        @Override
        public SerialRead read() {
            return reads.poll();
        }

        @Override
        public void write(byte[] buffer, int offset, int length) {
            written = written.append(RawBytes.copyOf(java.util.Arrays.copyOfRange(buffer, offset, offset + length)));
        }

        @Override
        public ModemLines readLines() {
            return written.ascii().contains("CONNECT")
                    ? new ModemLines(false, true, true, false, true, true)
                    : ModemLines.ready();
        }

        @Override
        public void writeLines(ModemLines lines) {
            lastLines = lines;
        }

        @Override
        public void close() {
        }
    }
}
