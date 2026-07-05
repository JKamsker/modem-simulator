package com.jkamsker.modemsim.transport;

public record SerialConfig(
        int baudRate,
        int dataBits,
        int stopBits,
        Parity parity,
        FlowControl flowControl
) {
    public static SerialConfig defaults() {
        return new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE);
    }
}
