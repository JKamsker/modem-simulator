package com.jkamsker.modemsim.app;

enum PortRole {
    MODEM_SIMULATION,
    SNIFFER,
    MANUAL_DCE_INJECTION;

    String configName() {
        return switch (this) {
            case MODEM_SIMULATION -> "modem-simulation";
            case SNIFFER -> "sniffer";
            case MANUAL_DCE_INJECTION -> "manual-dce-injection";
        };
    }

    static PortRole fromConfig(String value) {
        return switch (value) {
            case "modem-simulation" -> MODEM_SIMULATION;
            case "sniffer" -> SNIFFER;
            case "manual-dce-injection" -> MANUAL_DCE_INJECTION;
            default -> throw new IllegalArgumentException("Unsupported port role: " + value);
        };
    }
}
