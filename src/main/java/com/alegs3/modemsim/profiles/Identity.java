package com.alegs3.modemsim.profiles;

public record Identity(String manufacturer, String model, String revision, String imei) {
    public static Identity sierra() {
        return new Identity("Sierra Wireless", "HL8548", "SIM-HL6HL8-v20", "359762080000001");
    }

    public static Identity westermo(String model, String revision) {
        return new Identity("Westermo", model, revision, null);
    }
}
