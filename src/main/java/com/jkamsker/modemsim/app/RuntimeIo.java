package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.ModemLifecycle;
import com.jkamsker.modemsim.state.ModemLines;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.transport.HeadlessEndpoint;
import com.jkamsker.modemsim.transport.SerialEndpoint;
import com.jkamsker.modemsim.transport.SerialOverflowException;
import com.jkamsker.modemsim.transport.SerialPortLostException;
import com.jkamsker.modemsim.transport.SerialRead;

import java.io.IOException;
import java.util.List;

final class RuntimeIo {
    RawBytes processLineInputs(
            HeadlessSession session, SerialEndpoint endpoint, List<RuntimeSidecar> sidecars) throws IOException {
        ModemLines lines = endpoint.readLines();
        var current = session.snapshot();
        if (lines.rts() != current.lines().rts()) {
            var next = current.withLines(current.lines().withRts(lines.rts()).withCts(lines.rts()));
            SessionResponse response = session.applyState(next, "line-change");
            writeResponse(endpoint, session, sidecars, response);
            return response.output();
        }
        if (lines.dtr() && !current.lines().dtr()) {
            var next = current.withLines(current.lines().withDtr(true));
            SessionResponse response = session.applyState(next, "line-change");
            writeResponse(endpoint, session, sidecars, response);
            return response.output();
        }
        if (lines.dtr() || !current.lines().dtr()) {
            return RawBytes.empty();
        }
        if (current.settings().ampD() == 0) {
            var next = current.withLines(current.lines().withDtr(false));
            SessionResponse response = session.applyState(next, "line-change");
            writeResponse(endpoint, session, sidecars, response);
            return response.output();
        }
        var next = switch (current.settings().ampD()) {
            case 1 -> current.withCall(current.call().withMode(CallMode.ONLINE_COMMAND)).withLines(current.lines().withDtr(false));
            case 3 -> current.withModem(current.modem().withLifecycle(ModemLifecycle.REBOOTING))
                    .withLines(current.lines().withDtr(false).withDcd(dcdAfterDisconnect(current)));
            default -> current.call().carrier()
                    ? current.withCall(current.call().disconnected()).withLines(current.lines().withDtr(false).withDcd(dcdAfterDisconnect(current)))
                    : current.withLines(current.lines().withDtr(false));
        };
        SessionResponse response = session.applyState(next, "dtr-drop");
        writeResponse(endpoint, session, sidecars, response);
        return response.output();
    }

    RawBytes processSidecars(
            HeadlessSession session, SerialEndpoint modemEndpoint, List<RuntimeSidecar> sidecars) throws IOException {
        RawBytes output = RawBytes.empty();
        for (RuntimeSidecar sidecar : sidecars) {
            if (!sidecar.active()) {
                continue;
            }
            SerialRead read;
            try {
                read = read(sidecar.endpoint());
            } catch (SerialOverflowException e) {
                diagnostic(session, EventType.RX_OVERFLOW, sidecar,
                        "optional-rx-overflow:" + sidecar.binding().id() + ":" + e.getMessage());
                continue;
            } catch (SerialPortLostException e) {
                diagnostic(session, EventType.PORT_LOST, sidecar,
                        "optional-port-lost:" + sidecar.binding().id() + ":" + e.getMessage());
                sidecar.retire();
                continue;
            } catch (IOException e) {
                diagnostic(session, EventType.PORT_LOST, sidecar,
                        "optional-port-io-failed:" + sidecar.binding().id() + ":" + e.getMessage());
                sidecar.retire();
                continue;
            }
            if (read == null || read.bytes().isEmpty()) {
                continue;
            }
            if (sidecar.isSniffer()) {
                diagnostic(session, EventType.POLICY_DENIED, sidecar,
                        "sniffer-input-ignored:" + sidecar.binding().id());
            } else if (sidecar.isManualDceInjection()) {
                SessionResponse response = session.injectDce(read.bytes(), "raw-dce-to-dte");
                writeResponse(modemEndpoint, session, sidecars, response);
                output = output.append(response.output());
            }
        }
        return output;
    }

    RawBytes processQueuedDce(
            HeadlessSession session, SerialEndpoint endpoint, List<RuntimeSidecar> sidecars,
            java.util.Queue<RawBytes> writes, boolean unsafeAllowed) throws IOException {
        RawBytes output = RawBytes.empty();
        RawBytes bytes;
        while ((bytes = writes.poll()) != null) {
            if (!unsafeAllowed) {
                session.diagnostic(EventType.POLICY_DENIED, "raw-dce-to-dte-disabled:queued");
                continue;
            }
            SessionResponse response = session.injectDce(bytes, "raw-dce-to-dte");
            writeResponse(endpoint, session, sidecars, response);
            output = output.append(response.output());
        }
        return output;
    }

    void writeResponse(
            SerialEndpoint endpoint, HeadlessSession session, List<RuntimeSidecar> sidecars, SessionResponse response)
            throws IOException {
        writeDce(endpoint, session, sidecars, response.output());
    }

    void writeDce(SerialEndpoint endpoint, HeadlessSession session, List<RuntimeSidecar> sidecars, RawBytes bytes)
            throws IOException {
        try {
            write(endpoint, bytes);
        } catch (SerialOverflowException e) {
            session.diagnostic(EventType.TX_OVERFLOW, e.getMessage());
            return;
        }
        endpoint.writeLines(session.snapshot().lines());
        mirror(session, sidecars, Direction.DCE_TO_DTE, bytes);
    }

    SerialRead read(SerialEndpoint endpoint) throws IOException {
        try {
            return endpoint.read();
        } catch (IOException e) {
            if (endpoint instanceof HeadlessEndpoint) {
                return null;
            }
            throw e;
        }
    }

    void mirror(
            HeadlessSession session, List<RuntimeSidecar> sidecars, Direction direction, RawBytes bytes)
            throws IOException {
        for (RuntimeSidecar sidecar : sidecars) {
            try {
                sidecar.mirror(direction, bytes);
            } catch (SerialOverflowException e) {
                diagnostic(session, EventType.TX_OVERFLOW, sidecar,
                        "optional-tx-overflow:" + sidecar.binding().id() + ":" + e.getMessage());
                sidecar.retire();
            } catch (SerialPortLostException e) {
                diagnostic(session, EventType.PORT_LOST, sidecar,
                        "optional-port-lost:" + sidecar.binding().id() + ":" + e.getMessage());
                sidecar.retire();
            } catch (IOException e) {
                diagnostic(session, EventType.PORT_LOST, sidecar,
                        "optional-port-io-failed:" + sidecar.binding().id() + ":" + e.getMessage());
                sidecar.retire();
            }
        }
    }

    private void diagnostic(HeadlessSession session, EventType type, RuntimeSidecar sidecar, String result) {
        PortBinding binding = sidecar.binding();
        String port = binding.name() == null || binding.name().isBlank() ? binding.id() : binding.name();
        session.diagnostic(type, result, port, binding.role().configName());
    }

    private boolean dcdAfterDisconnect(ModemState state) {
        return state.settings().ampC() == 0;
    }

    private void write(SerialEndpoint endpoint, RawBytes output) throws IOException {
        if (output.isEmpty()) {
            return;
        }
        byte[] bytes = output.toByteArray();
        endpoint.write(bytes, 0, bytes.length);
    }
}
