package com.jkamsker.modemsim.state;

public record CallRuntime(CallMode mode, boolean carrier, String dialedNumber, String incomingNumber) {
    public CallRuntime(CallMode mode, boolean carrier, String dialedNumber) {
        this(mode, carrier, dialedNumber, null);
    }

    public static CallRuntime command() {
        return new CallRuntime(CallMode.COMMAND, false, null, null);
    }

    public CallRuntime withMode(CallMode value) {
        return new CallRuntime(value, carrier, dialedNumber, incomingNumber);
    }

    public CallRuntime connected(String number) {
        return new CallRuntime(CallMode.ONLINE_DATA, true, number, null);
    }

    public CallRuntime answerIncoming() {
        return new CallRuntime(CallMode.ONLINE_DATA, true, incomingNumber, null);
    }

    public CallRuntime disconnected() {
        return new CallRuntime(CallMode.COMMAND, false, null, null);
    }
}
