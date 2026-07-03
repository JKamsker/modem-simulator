package com.jkamsker.modemsim.app;

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
        HeadlessSession session = new HeadlessSession("main", profile, config.sessionSeed());
        SerialEndpoint modemEndpoint = endpointFactory.create(modemPort);
        List<SerialEndpoint> sidecars = new ArrayList<>();
        try (modemEndpoint) {
            modemEndpoint.open(config.serialLine());
            openSidecars(config, sidecars);
            enqueueHeadlessInputs(modemEndpoint, headlessInputs);
            return processReads(session, modemEndpoint, maxReadsFor(modemEndpoint, headlessInputs, maxReads));
        } catch (IOException e) {
            throw new IllegalStateException("Runtime failed: " + e.getMessage(), e);
        } finally {
            closeSidecars(sidecars);
        }
    }

    private RuntimeResult processReads(HeadlessSession session, SerialEndpoint endpoint, int maxReads)
            throws IOException {
        RawBytes output = RawBytes.empty();
        int reads = 0;
        while (!Thread.currentThread().isInterrupted() && (maxReads < 0 || reads < maxReads)) {
            SerialRead read = read(endpoint);
            if (read == null) {
                break;
            }
            if (read.bytes().isEmpty()) {
                continue;
            }
            SessionResponse response = session.receive(read.bytes());
            write(endpoint, response.output());
            output = output.append(response.output());
            reads++;
        }
        return new RuntimeResult("main", reads, output);
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

    private void openSidecars(RuntimeConfig config, List<SerialEndpoint> sidecars) throws SerialException {
        for (PortBinding binding : config.enabledSidecars()) {
            SerialEndpoint endpoint = endpointFactory.create(binding);
            try {
                endpoint.open(config.serialLine());
                sidecars.add(endpoint);
            } catch (SerialException e) {
                endpoint.close();
                if (config.strictOptionalPorts()) {
                    throw e;
                }
            }
        }
    }

    private void closeSidecars(List<SerialEndpoint> sidecars) {
        for (SerialEndpoint sidecar : sidecars) {
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
