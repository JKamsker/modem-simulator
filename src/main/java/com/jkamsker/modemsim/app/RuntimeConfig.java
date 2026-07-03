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
        Path initialScenario,
        Path macros,
        List<RuntimeTimer> macroTimers,
        SerialConfig serialLine,
        List<PortBinding> ports
) {
    RuntimeConfig(
            long sessionSeed,
            ClockMode clockMode,
            boolean strictOptionalPorts,
            boolean allowUnsafeDceTransmit,
            Path eventLogPath,
            SerialConfig serialLine,
            List<PortBinding> ports) {
        this(sessionSeed, clockMode, strictOptionalPorts, allowUnsafeDceTransmit, eventLogPath,
                null, null, List.of(), serialLine, ports);
    }

    RuntimeConfig(
            long sessionSeed, ClockMode clockMode, boolean strictOptionalPorts, boolean allowUnsafeDceTransmit,
            Path eventLogPath, Path initialScenario, SerialConfig serialLine, List<PortBinding> ports) {
        this(sessionSeed, clockMode, strictOptionalPorts, allowUnsafeDceTransmit, eventLogPath,
                initialScenario, null, List.of(), serialLine, ports);
    }

    RuntimeConfig {
        eventLogPath = eventLogPath == null ? defaultEventLogPath(sessionSeed) : eventLogPath;
        macroTimers = macroTimers == null ? List.of()
                : macroTimers.stream().sorted(java.util.Comparator.comparingLong(RuntimeTimer::atMs)).toList();
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
