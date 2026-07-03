package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.transport.SerialEndpoint;

import java.io.IOException;

final class RuntimeSidecar implements AutoCloseable {
    private final PortBinding binding;
    private final SerialEndpoint endpoint;
    private boolean active = true;

    RuntimeSidecar(PortBinding binding, SerialEndpoint endpoint) {
        this.binding = binding;
        this.endpoint = endpoint;
    }

    PortBinding binding() {
        return binding;
    }

    SerialEndpoint endpoint() {
        return endpoint;
    }

    boolean active() {
        return active;
    }

    boolean isSniffer() {
        return binding.role() == PortRole.SNIFFER;
    }

    boolean isManualDceInjection() {
        return binding.role() == PortRole.MANUAL_DCE_INJECTION;
    }

    void mirror(Direction direction, RawBytes bytes) throws IOException {
        if (!active || !isSniffer() || bytes.isEmpty()) {
            return;
        }
        RawBytes payload = switch (binding.snifferFormat()) {
            case "raw-dte" -> direction == Direction.DTE_TO_DCE ? bytes : RawBytes.empty();
            case "raw-dce" -> direction == Direction.DCE_TO_DTE ? bytes : RawBytes.empty();
            case "raw-both" -> bytes;
            case "jsonl-events" -> RawBytes.ascii("{\"direction\":\"" + direction.name()
                    + "\",\"rawHex\":\"" + bytes.toHex() + "\"}\n");
            default -> RawBytes.ascii(direction.name() + " " + bytes.toHex() + "\n");
        };
        write(payload);
    }

    void write(RawBytes bytes) throws IOException {
        if (!active || bytes.isEmpty()) {
            return;
        }
        byte[] raw = bytes.toByteArray();
        endpoint.write(raw, 0, raw.length);
    }

    void retire() {
        close();
    }

    @Override
    public void close() {
        active = false;
        endpoint.close();
    }
}
