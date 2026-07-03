package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.JsonlEventSink;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.transport.HeadlessEndpoint;
import com.jkamsker.modemsim.transport.JSerialCommEndpoint;
import com.jkamsker.modemsim.transport.SerialEndpoint;
import com.jkamsker.modemsim.transport.SerialException;
import com.jkamsker.modemsim.transport.SerialRead;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class ModemRuntime {
    private final EndpointFactory endpointFactory;
    private final ProfileResolver profileResolver = new ProfileResolver();

    ModemRuntime() {
        this(ModemRuntime::defaultEndpoint);
    }

    ModemRuntime(EndpointFactory endpointFactory) {
        this.endpointFactory = endpointFactory;
    }

    RuntimeResult run(RuntimeConfig config, List<RawBytes> headlessInputs, int maxReads) {
        PortBinding modemPort = config.modemPort();
        Profile profile = profileResolver.resolve(modemPort.profile());
        SerialEndpoint modemEndpoint = endpointFactory.create(modemPort);
        List<RuntimeSidecar> sidecars = new ArrayList<>();
        try (JsonlEventSink eventSink = new JsonlEventSink(config.eventLogPath()); modemEndpoint) {
            HeadlessSession session = new HeadlessSession("main", profile, config.sessionSeed(), eventSink);
            modemEndpoint.open(config.serialLine());
            openSidecars(config, session, sidecars);
            enqueueHeadlessInputs(modemEndpoint, headlessInputs);
            RuntimeResult result = processReads(
                    session, modemEndpoint, sidecars, maxReadsFor(modemEndpoint, headlessInputs, maxReads));
            session.diagnostic(EventType.SESSION_STOP, "normal-stop");
            return new RuntimeResult(result.sessionId(), result.readsProcessed(), result.output(), config.eventLogPath());
        } catch (IOException e) {
            throw new IllegalStateException("Runtime failed: " + e.getMessage(), e);
        } finally {
            closeSidecars(sidecars);
        }
    }

    private RuntimeResult processReads(
            HeadlessSession session, SerialEndpoint endpoint, List<RuntimeSidecar> sidecars, int maxReads)
            throws IOException {
        RawBytes output = RawBytes.empty();
        int reads = 0;
        while (!Thread.currentThread().isInterrupted()) {
            output = output.append(processSidecars(session, endpoint, sidecars));
            if (maxReads >= 0 && reads >= maxReads) {
                break;
            }
            SerialRead read = read(endpoint);
            if (read == null) {
                break;
            }
            if (read.bytes().isEmpty()) {
                continue;
            }
            mirror(sidecars, Direction.DTE_TO_DCE, read.bytes());
            SessionResponse response = session.receive(read.bytes());
            write(endpoint, response.output());
            mirror(sidecars, Direction.DCE_TO_DTE, response.output());
            output = output.append(response.output());
            reads++;
        }
        return new RuntimeResult("main", reads, output);
    }

    private RawBytes processSidecars(
            HeadlessSession session, SerialEndpoint modemEndpoint, List<RuntimeSidecar> sidecars) throws IOException {
        RawBytes output = RawBytes.empty();
        for (RuntimeSidecar sidecar : sidecars) {
            SerialRead read = read(sidecar.endpoint());
            if (read == null || read.bytes().isEmpty()) {
                continue;
            }
            if (sidecar.isSniffer()) {
                session.diagnostic(EventType.AUDIT_FAILURE, "sniffer-input-ignored:" + sidecar.binding().id());
            } else if (sidecar.isManualDceInjection()) {
                SessionResponse response = session.injectDce(read.bytes(), "raw-dce-to-dte");
                write(modemEndpoint, response.output());
                mirror(sidecars, Direction.DCE_TO_DTE, response.output());
                output = output.append(response.output());
            }
        }
        return output;
    }

    private SerialRead read(SerialEndpoint endpoint) throws IOException {
        try {
            return endpoint.read();
        } catch (IOException e) {
            if (endpoint instanceof HeadlessEndpoint) {
                return null;
            }
            throw e;
        }
    }

    private void write(SerialEndpoint endpoint, RawBytes output) throws IOException {
        if (output.isEmpty()) {
            return;
        }
        byte[] bytes = output.toByteArray();
        endpoint.write(bytes, 0, bytes.length);
    }

    private void enqueueHeadlessInputs(SerialEndpoint endpoint, List<RawBytes> inputs) {
        if (!(endpoint instanceof HeadlessEndpoint headless)) {
            return;
        }
        long now = 0;
        for (RawBytes input : inputs) {
            headless.enqueueRead(input, now++);
        }
    }

    private int maxReadsFor(SerialEndpoint endpoint, List<RawBytes> inputs, int requested) {
        if (requested >= 0 || !(endpoint instanceof HeadlessEndpoint)) {
            return requested;
        }
        return inputs.size();
    }

    private void openSidecars(RuntimeConfig config, HeadlessSession session, List<RuntimeSidecar> sidecars)
            throws SerialException {
        for (PortBinding binding : config.enabledSidecars()) {
            if (binding.role() == PortRole.MANUAL_DCE_INJECTION && !config.allowUnsafeDceTransmit()) {
                session.diagnostic(EventType.AUDIT_FAILURE, "manual-dce-disabled:" + binding.id());
                if (config.strictOptionalPorts()) {
                    throw new SerialException("manual-dce-injection requires allowUnsafeDceTransmit");
                }
                continue;
            }
            SerialEndpoint endpoint = endpointFactory.create(binding);
            try {
                endpoint.open(config.serialLine());
                sidecars.add(new RuntimeSidecar(binding, endpoint));
            } catch (SerialException e) {
                endpoint.close();
                session.diagnostic(EventType.PORT_LOST, "optional-port-open-failed:" + binding.id() + ":" + e.getMessage());
                if (config.strictOptionalPorts()) {
                    throw e;
                }
            }
        }
    }

    private void mirror(List<RuntimeSidecar> sidecars, Direction direction, RawBytes bytes) throws IOException {
        for (RuntimeSidecar sidecar : sidecars) {
            sidecar.mirror(direction, bytes);
        }
    }

    private void closeSidecars(List<RuntimeSidecar> sidecars) {
        for (RuntimeSidecar sidecar : sidecars) {
            sidecar.close();
        }
    }

    private static SerialEndpoint defaultEndpoint(PortBinding binding) {
        if (binding.type() == EndpointType.HEADLESS) {
            return new HeadlessEndpoint();
        }
        if (binding.name() == null || binding.name().isBlank()) {
            throw new IllegalArgumentException("Serial port name is required for " + binding.id());
        }
        return new JSerialCommEndpoint(binding.name());
    }
}
