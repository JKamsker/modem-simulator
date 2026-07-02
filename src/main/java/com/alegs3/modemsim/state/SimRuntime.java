package com.alegs3.modemsim.state;

public record SimRuntime(
        SimState state,
        boolean pinQueryEnabled,
        String pinRef,
        String testPin,
        int pinRetries,
        int pukRetries,
        String imsi,
        String iccid
) {
    public static SimRuntime ready() {
        return new SimRuntime(
                SimState.READY, true, "TEST_SIM_PIN", "1234", 3, 10,
                "262010123456789", "8949020000000000000");
    }

    public SimRuntime withState(SimState value) {
        return new SimRuntime(value, pinQueryEnabled, pinRef, testPin, pinRetries, pukRetries, imsi, iccid);
    }

    public SimRuntime withPinRetries(int value) {
        return new SimRuntime(state, pinQueryEnabled, pinRef, testPin, value, pukRetries, imsi, iccid);
    }
}
