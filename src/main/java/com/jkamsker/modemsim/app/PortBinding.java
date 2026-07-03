package com.jkamsker.modemsim.app;

import java.nio.file.Path;

record PortBinding(
        String id,
        EndpointType type,
        PortRole role,
        String name,
        boolean enabled,
        String profile,
        Path initialScenario,
        String snifferFormat
) {
    PortBinding(
            String id, EndpointType type, PortRole role, String name,
            boolean enabled, String profile, String snifferFormat) {
        this(id, type, role, name, enabled, profile, null, snifferFormat);
    }

    boolean isModemSimulation() {
        return role == PortRole.MODEM_SIMULATION;
    }

    boolean isOptionalSidecar() {
        return role == PortRole.SNIFFER || role == PortRole.MANUAL_DCE_INJECTION;
    }
}
