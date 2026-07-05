package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.transport.SerialEndpoint;

@FunctionalInterface
interface EndpointFactory {
    SerialEndpoint create(PortBinding binding);
}
