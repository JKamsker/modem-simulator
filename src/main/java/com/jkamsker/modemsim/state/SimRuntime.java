package com.jkamsker.modemsim.state;

public record SimRuntime(
        SimState state,
        boolean pinQueryEnabled,
        String pinRef,
        String testPin,
        String pukRef,
        String testPuk,
        int pinRetries,
        int pukRetries,
        String imsi,
        String iccid
) {
    public SimRuntime(
            SimState state, boolean pinQueryEnabled, String pinRef, String testPin,
            int pinRetries, int pukRetries, String imsi, String iccid) {
        this(state, pinQueryEnabled, pinRef, testPin, null, null, pinRetries, pukRetries, imsi, iccid);
    }

    public static SimRuntime ready() {
        return new SimRuntime(
                SimState.READY, true, "TEST_SIM_PIN", null, "TEST_SIM_PUK", null, 3, 10,
                "262010123456789", "8949020000000000000");
    }

    public SimRuntime withState(SimState value) {
        return new SimRuntime(value, pinQueryEnabled, pinRef, testPin, pukRef, testPuk,
                pinRetries, pukRetries, imsi, iccid);
    }

    public SimRuntime withPinRetries(int value) {
        return new SimRuntime(state, pinQueryEnabled, pinRef, testPin, pukRef, testPuk,
                value, pukRetries, imsi, iccid);
    }

    public SimRuntime withPukRetries(int value) {
        return new SimRuntime(state, pinQueryEnabled, pinRef, testPin, pukRef, testPuk,
                pinRetries, value, imsi, iccid);
    }

    public SimRuntime withPin(String value) {
        return new SimRuntime(state, pinQueryEnabled, pinRef, value, pukRef, testPuk,
                pinRetries, pukRetries, imsi, iccid);
    }
}
