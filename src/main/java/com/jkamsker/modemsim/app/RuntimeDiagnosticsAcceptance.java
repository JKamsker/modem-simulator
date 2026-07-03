package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.InMemoryEventSink;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.state.ModemLines;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.HeadlessEndpoint;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;
import com.jkamsker.modemsim.transport.SerialEndpoint;
import com.jkamsker.modemsim.transport.SerialException;
import com.jkamsker.modemsim.transport.SerialOverflowException;
import com.jkamsker.modemsim.transport.SerialPortLostException;
import com.jkamsker.modemsim.transport.SerialRead;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public final class RuntimeDiagnosticsAcceptance {
    public void run() {
        try {
            openFailuresUseDistinctDiagnostics();
            overflowAndPortLossAreAudited();
        } catch (IOException e) {
            throw new IllegalStateException("runtime diagnostics acceptance failed", e);
        }
    }

    private void openFailuresUseDistinctDiagnostics() throws IOException {
        HeadlessEndpoint main = new HeadlessEndpoint();
        main.enqueueRead(RawBytes.ascii("AT\r"), 0);
        InMemoryEventSink sink = new InMemoryEventSink();
        new ModemRuntime(binding -> switch (binding.id()) {
            case "missing" -> new OpenFailEndpoint("PORT_NOT_FOUND");
            case "unsupported" -> new OpenFailEndpoint("UNSUPPORTED_PARAMETERS");
            default -> main;
        }).run(config(List.of(sidecar("missing"), sidecar("unsupported"))), List.of(), 1, sink, session -> { });

        require(hasResult(sink, EventType.PORT_OPEN_FAILED, "PORT_NOT_FOUND"));
        require(hasResult(sink, EventType.PORT_OPEN_FAILED, "UNSUPPORTED_PARAMETERS"));
    }

    private void overflowAndPortLossAreAudited() throws IOException {
        InMemoryEventSink overflowSink = new InMemoryEventSink();
        OverflowEndpoint overflow = new OverflowEndpoint();
        overflow.enqueue(RawBytes.ascii("AT\r"), 0);
        new ModemRuntime(binding -> overflow).run(config(List.of()), List.of(), 1, overflowSink, session -> { });
        require(overflowSink.events().stream()
                .anyMatch(event -> event.eventType() == EventType.RX_OVERFLOW && event.replayDivergent()));

        InMemoryEventSink lostSink = new InMemoryEventSink();
        new ModemRuntime(binding -> new LostEndpoint()).run(config(List.of()), List.of(), -1, lostSink, session -> { });
        require(hasResult(lostSink, EventType.PORT_LOST, "port-lost"));
    }

    private RuntimeConfig config(List<PortBinding> sidecars) throws IOException {
        var ports = new java.util.ArrayList<PortBinding>();
        ports.add(new PortBinding("modem", EndpointType.HEADLESS, PortRole.MODEM_SIMULATION,
                null, true, "sierra-hl6-hl8-v20", "tagged-text"));
        ports.addAll(sidecars);
        return new RuntimeConfig(12345, ClockMode.VIRTUAL, false, true,
                Files.createTempFile("modemsim-diagnostics", ".jsonl"),
                new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE), ports);
    }

    private PortBinding sidecar(String id) {
        return new PortBinding(id, EndpointType.HEADLESS, PortRole.SNIFFER, null, true, null, "tagged-text");
    }

    private boolean hasResult(InMemoryEventSink sink, EventType type, String token) {
        return sink.events().stream().anyMatch(event -> matches(event, type, token));
    }

    private boolean matches(ModemEvent event, EventType type, String token) {
        return event.eventType() == type && event.result() != null && event.result().contains(token);
    }

    private void require(boolean condition) {
        if (!condition) {
            throw new IllegalStateException("acceptance check failed");
        }
    }

    private static final class OpenFailEndpoint implements SerialEndpoint {
        private final String code;

        private OpenFailEndpoint(String code) {
            this.code = code;
        }

        @Override
        public void open(SerialConfig config) throws SerialException {
            throw new SerialException("open failed", code);
        }

        @Override public SerialRead read() { return null; }
        @Override public void write(byte[] buffer, int offset, int length) { }
        @Override public ModemLines readLines() { return ModemLines.ready(); }
        @Override public void writeLines(ModemLines lines) { }
        @Override public void close() { }
    }

    private static final class OverflowEndpoint implements SerialEndpoint {
        private final Queue<SerialRead> reads = new ArrayDeque<>();
        private boolean overflow = true;

        void enqueue(RawBytes bytes, long nanos) {
            reads.add(new SerialRead(bytes, nanos, nanos));
        }

        @Override public void open(SerialConfig config) { }
        @Override public void write(byte[] buffer, int offset, int length) { }
        @Override public ModemLines readLines() { return ModemLines.ready(); }
        @Override public void writeLines(ModemLines lines) { }
        @Override public void close() { }

        @Override
        public SerialRead read() throws IOException {
            if (overflow) {
                overflow = false;
                throw new SerialOverflowException("forced rx overflow");
            }
            return reads.poll();
        }
    }

    private static final class LostEndpoint implements SerialEndpoint {
        @Override public void open(SerialConfig config) { }
        @Override public void write(byte[] buffer, int offset, int length) { }
        @Override public ModemLines readLines() { return ModemLines.ready(); }
        @Override public void writeLines(ModemLines lines) { }
        @Override public void close() { }

        @Override
        public SerialRead read() throws IOException {
            throw new SerialPortLostException("lost");
        }
    }
}
