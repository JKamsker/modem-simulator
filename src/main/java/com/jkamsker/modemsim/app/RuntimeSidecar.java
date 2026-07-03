package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.transport.SerialEndpoint;

import java.io.IOException;

record RuntimeSidecar(PortBinding binding, SerialEndpoint endpoint) implements AutoCloseable {
    boolean isSniffer() {
        return binding.role() == PortRole.SNIFFER;
    }

    boolean isManualDceInjection() {
        return binding.role() == PortRole.MANUAL_DCE_INJECTION;
    }

    void mirror(Direction direction, RawBytes bytes) throws IOException {
        if (!isSniffer() || bytes.isEmpty()) {
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
        if (bytes.isEmpty()) {
            return;
        }
        byte[] raw = bytes.toByteArray();
        endpoint.write(raw, 0, raw.length);
    }

    @Override
    public void close() {
        endpoint.close();
    }
}
