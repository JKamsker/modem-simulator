package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.InMemoryEventSink;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.state.ModemLines;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.HeadlessEndpoint;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;
import com.jkamsker.modemsim.transport.SerialEndpoint;
import com.jkamsker.modemsim.transport.SerialOverflowException;
import com.jkamsker.modemsim.transport.SerialRead;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class RuntimeIoIntegrationTest {
    @TempDir
    Path tempDir;

    @Test
    void runtimePublishesToInjectedSinkAndExposesSession() {
        HeadlessEndpoint endpoint = new HeadlessEndpoint();
        endpoint.enqueueRead(RawBytes.ascii("AT\r"), 0);
        InMemoryEventSink sink = new InMemoryEventSink();
        AtomicReference<HeadlessSession> ready = new AtomicReference<>();

        RuntimeResult result = new ModemRuntime(binding -> endpoint).run(config(List.of()), List.of(), 1, sink, ready::set);

        assertThat(result.output().ascii()).isEqualTo("\r\nOK\r\n");
        assertThat(ready.get()).isNotNull();
        assertThat(sink.events()).extracting(event -> event.eventType())
                .contains(EventType.SESSION_START, EventType.RX_BYTES, EventType.TX_BYTES, EventType.SESSION_STOP);
    }

    @Test
    void optionalSidecarReadFailureDoesNotAbortMainPort() throws Exception {
        HeadlessEndpoint main = new HeadlessEndpoint();
        main.enqueueRead(RawBytes.ascii("AT\r"), 0);
        ReadFailEndpoint sidecar = new ReadFailEndpoint();

        RuntimeResult result = new ModemRuntime(binding -> binding.isModemSimulation() ? main : sidecar)
                .run(config(List.of(sniffer())), List.of(), 1);

        assertThat(result.output().ascii()).isEqualTo("\r\nOK\r\n");
        assertThat(Files.readString(result.eventLogPath())).contains("optional-port-io-failed:sniffer:sidecar read failed");
        assertThat(sidecar.reads).isEqualTo(1);
    }

    @Test
    void dtrReassertionRestoresLineStateAfterDrop() throws Exception {
        ReassertingDtrEndpoint endpoint = new ReassertingDtrEndpoint();
        endpoint.enqueue(RawBytes.ascii("ATD123\r"), 0);
        endpoint.enqueue(RawBytes.empty(), 5_000_000_000L);
        endpoint.enqueue(RawBytes.empty(), 6_000_000_000L);

        RuntimeResult result = new ModemRuntime(binding -> endpoint).run(config(List.of()), List.of(), -1);

        assertThat(result.output().ascii()).contains("CONNECT");
        assertThat(endpoint.lastLines.dtr()).isTrue();
        assertThat(Files.readString(result.eventLogPath())).contains("line-change").contains("\"dtr\":true");
    }

    @Test
    void configHashChangesWhenRuntimeSerialConfigChanges() {
        InMemoryEventSink first = new InMemoryEventSink();
        InMemoryEventSink second = new InMemoryEventSink();

        new ModemRuntime(binding -> new HeadlessEndpoint()).run(config(List.of(), 9600), List.of(), 0, first, session -> { });
        new ModemRuntime(binding -> new HeadlessEndpoint()).run(config(List.of(), 115200), List.of(), 0, second, session -> { });

        assertThat(first.events().getFirst().configHash()).isNotEqualTo(second.events().getFirst().configHash());
    }

    @Test
    void queuedDceWritesReachMainEndpoint() {
        HeadlessEndpoint endpoint = new HeadlessEndpoint();
        Queue<RawBytes> dceWrites = new ConcurrentLinkedQueue<>();
        dceWrites.add(RawBytes.ascii("+CREG: 4\r\n"));

        RuntimeResult result = new ModemRuntime(binding -> endpoint)
                .run(config(List.of(), 115200, true), List.of(), 0, new InMemoryEventSink(), session -> { }, dceWrites);

        assertThat(result.output().ascii()).isEqualTo("+CREG: 4\r\n");
        assertThat(endpoint.written().ascii()).contains("+CREG: 4\r\n");
    }

    @Test
    void queuedDceWritesAreRejectedWhenUnsafeTransmitIsDisabled() throws Exception {
        HeadlessEndpoint endpoint = new HeadlessEndpoint();
        Queue<RawBytes> dceWrites = new ConcurrentLinkedQueue<>();
        InMemoryEventSink sink = new InMemoryEventSink();
        dceWrites.add(RawBytes.ascii("+CREG: 4\r\n"));

        RuntimeResult result = new ModemRuntime(binding -> endpoint)
                .run(config(List.of()), List.of(), 0, sink, session -> { }, dceWrites);

        assertThat(result.output().isEmpty()).isTrue();
        assertThat(endpoint.written().isEmpty()).isTrue();
        assertThat(sink.events()).anySatisfy(event -> {
            assertThat(event.eventType()).isEqualTo(EventType.AUDIT_FAILURE);
            assertThat(event.result()).isEqualTo("raw-dce-to-dte-disabled:queued");
        });
    }

    @Test
    void shortMainWriteIsAuditedAsTxOverflow() throws Exception {
        OverflowEndpoint endpoint = new OverflowEndpoint(true, false);
        endpoint.enqueue(RawBytes.ascii("AT\r"), 0);

        RuntimeResult result = new ModemRuntime(binding -> endpoint).run(config(List.of()), List.of(), 1);

        assertThat(result.output().ascii()).isEqualTo("\r\nOK\r\n");
        assertThat(Files.readString(result.eventLogPath()))
                .contains("\"eventType\":\"TX_OVERFLOW\"")
                .contains("\"replayDivergent\":true")
                .doesNotContain("\"eventType\":\"PORT_LOST\"");
    }

    @Test
    void rxOverflowIsAuditedAndNextReadStillRuns() throws Exception {
        OverflowEndpoint endpoint = new OverflowEndpoint(false, true);
        endpoint.enqueue(RawBytes.ascii("AT\r"), 1);

        RuntimeResult result = new ModemRuntime(binding -> endpoint).run(config(List.of()), List.of(), 1);

        assertThat(result.output().ascii()).isEqualTo("\r\nOK\r\n");
        assertThat(Files.readString(result.eventLogPath()))
                .contains("\"eventType\":\"RX_OVERFLOW\"")
                .contains("\"replayDivergent\":true");
    }

    private RuntimeConfig config(List<PortBinding> sidecars) {
        return config(sidecars, 115200);
    }

    private RuntimeConfig config(List<PortBinding> sidecars, int baudRate) {
        return config(sidecars, baudRate, false);
    }

    private RuntimeConfig config(List<PortBinding> sidecars, int baudRate, boolean allowUnsafeDce) {
        return new RuntimeConfig(
                12345, ClockMode.VIRTUAL, false, allowUnsafeDce, tempDir.resolve("runtime.jsonl"),
                new SerialConfig(baudRate, 8, 1, Parity.NONE, FlowControl.NONE),
                ports(sidecars));
    }

    private List<PortBinding> ports(List<PortBinding> sidecars) {
        var ports = new java.util.ArrayList<PortBinding>();
        ports.add(new PortBinding("modem", EndpointType.HEADLESS, PortRole.MODEM_SIMULATION,
                null, true, "sierra-hl6-hl8-v20", "tagged-text"));
        ports.addAll(sidecars);
        return ports;
    }

    private PortBinding sniffer() {
        return new PortBinding("sniffer", EndpointType.HEADLESS, PortRole.SNIFFER,
                null, true, null, "tagged-text");
    }

    private static final class ReadFailEndpoint implements SerialEndpoint {
        private int reads;

        @Override
        public void open(SerialConfig config) {
        }

        @Override
        public SerialRead read() throws IOException {
            reads++;
            throw new IOException("sidecar read failed");
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

    private static final class ReassertingDtrEndpoint implements SerialEndpoint {
        private final Queue<SerialRead> reads = new ArrayDeque<>();
        private RawBytes written = RawBytes.empty();
        private ModemLines lastLines = ModemLines.ready();
        private boolean observedDrop;

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
            if (observedDrop) {
                return ModemLines.ready();
            }
            return written.ascii().contains("CONNECT")
                    ? new ModemLines(false, true, true, false, true, true)
                    : ModemLines.ready();
        }

        @Override
        public void writeLines(ModemLines lines) {
            lastLines = lines;
            observedDrop = observedDrop || !lines.dtr();
        }

        @Override
        public void close() {
        }
    }

    private static final class OverflowEndpoint implements SerialEndpoint {
        private final Queue<SerialRead> reads = new ArrayDeque<>();
        private final boolean txOverflow;
        private boolean rxOverflow;

        private OverflowEndpoint(boolean txOverflow, boolean rxOverflow) {
            this.txOverflow = txOverflow;
            this.rxOverflow = rxOverflow;
        }

        void enqueue(RawBytes bytes, long nanos) {
            reads.add(new SerialRead(bytes, nanos, nanos));
        }

        @Override
        public void open(SerialConfig config) {
        }

        @Override
        public SerialRead read() throws IOException {
            if (rxOverflow) {
                rxOverflow = false;
                throw new SerialOverflowException("forced rx overflow");
            }
            return reads.poll();
        }

        @Override
        public void write(byte[] buffer, int offset, int length) throws IOException {
            if (txOverflow && length > 0) {
                throw new SerialOverflowException("forced tx overflow");
            }
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
