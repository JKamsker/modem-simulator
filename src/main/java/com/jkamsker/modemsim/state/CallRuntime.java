package com.jkamsker.modemsim.state;

public record CallRuntime(CallMode mode, boolean carrier, String dialedNumber) {
    public static CallRuntime command() {
        return new CallRuntime(CallMode.COMMAND, false, null);
    }

    public CallRuntime withMode(CallMode value) {
        return new CallRuntime(value, carrier, dialedNumber);
    }

    public CallRuntime connected(String number) {
        return new CallRuntime(CallMode.ONLINE_DATA, true, number);
    }

    public CallRuntime disconnected() {
        return new CallRuntime(CallMode.COMMAND, false, null);
    }
}
