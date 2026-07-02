package com.alegs3.modemsim.state;

public record SignalRuntime(int rssi, int ber) {
    public static SignalRuntime medium() {
        return new SignalRuntime(18, 0);
    }

    public static SignalRuntime unknown() {
        return new SignalRuntime(99, 99);
    }
}
