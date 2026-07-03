package com.jkamsker.modemsim.app;

enum EndpointType {
    SERIAL,
    HEADLESS;

    static EndpointType fromConfig(String value) {
        return switch (value) {
            case "serial" -> SERIAL;
            case "headless" -> HEADLESS;
            default -> throw new IllegalArgumentException("Unsupported endpoint type: " + value);
        };
    }
}
