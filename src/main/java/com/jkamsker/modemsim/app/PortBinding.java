package com.jkamsker.modemsim.app;

record PortBinding(
        String id,
        EndpointType type,
        PortRole role,
        String name,
        boolean enabled,
        String profile,
        String snifferFormat
) {
    boolean isModemSimulation() {
        return role == PortRole.MODEM_SIMULATION;
    }

    boolean isOptionalSidecar() {
        return role == PortRole.SNIFFER || role == PortRole.MANUAL_DCE_INJECTION;
    }
}
