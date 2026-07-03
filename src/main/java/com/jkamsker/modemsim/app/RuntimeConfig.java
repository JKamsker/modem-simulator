package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.transport.SerialConfig;

import java.nio.file.Path;
import java.util.List;

record RuntimeConfig(
        long sessionSeed,
        ClockMode clockMode,
        boolean strictOptionalPorts,
        boolean allowUnsafeDceTransmit,
        Path eventLogPath,
        SerialConfig serialLine,
        List<PortBinding> ports
) {
    RuntimeConfig {
        eventLogPath = eventLogPath == null ? defaultEventLogPath(sessionSeed) : eventLogPath;
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

    private static Path defaultEventLogPath(long sessionSeed) {
        return Path.of("target", "runtime", "logs", "main-" + sessionSeed + ".jsonl");
    }
}
