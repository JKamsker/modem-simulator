package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.transport.HeadlessEndpoint;
import com.jkamsker.modemsim.transport.JSerialCommEndpoint;
import com.jkamsker.modemsim.transport.SerialEndpoint;

final class DefaultEndpointFactory {
    private DefaultEndpointFactory() {
    }

    static SerialEndpoint create(PortBinding binding) {
        if (binding.type() == EndpointType.HEADLESS) {
            return new HeadlessEndpoint();
        }
        if (binding.name() == null || binding.name().isBlank()) {
            throw new IllegalArgumentException("Serial port name is required for " + binding.id());
        }
        return new JSerialCommEndpoint(binding.name());
    }
}
