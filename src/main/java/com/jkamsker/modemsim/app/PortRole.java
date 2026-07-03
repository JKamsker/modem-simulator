package com.jkamsker.modemsim.app;

enum PortRole {
    MODEM_SIMULATION,
    SNIFFER,
    MANUAL_DCE_INJECTION;

    static PortRole fromConfig(String value) {
        return switch (value) {
            case "modem-simulation" -> MODEM_SIMULATION;
            case "sniffer" -> SNIFFER;
            case "manual-dce-injection" -> MANUAL_DCE_INJECTION;
            default -> throw new IllegalArgumentException("Unsupported port role: " + value);
        };
    }
}
