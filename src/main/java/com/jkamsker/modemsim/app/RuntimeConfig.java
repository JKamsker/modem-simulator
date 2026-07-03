package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.transport.SerialConfig;

import java.util.List;

record RuntimeConfig(
        long sessionSeed,
        ClockMode clockMode,
        boolean strictOptionalPorts,
        SerialConfig serialLine,
        List<PortBinding> ports
) {
    RuntimeConfig {
        ports = List.copyOf(ports);
    }

    PortBinding modemPort() {
        return ports.stream()
                .filter(PortBinding::isModemSimulation)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No modem-simulation port configured"));
    }

    List<PortBinding> enabledSidecars() {
        return ports.stream()
                .filter(PortBinding::isOptionalSidecar)
                .filter(PortBinding::enabled)
                .toList();
    }
}
