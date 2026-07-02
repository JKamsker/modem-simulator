package com.alegs3.modemsim.state;

public record ModemLines(boolean dtr, boolean dsr, boolean dcd, boolean ri, boolean rts, boolean cts) {
    public static ModemLines ready() {
        return new ModemLines(true, true, false, false, true, true);
    }

    public ModemLines withDcd(boolean value) {
        return new ModemLines(dtr, dsr, value, ri, rts, cts);
    }

    public ModemLines withDsr(boolean value) {
        return new ModemLines(dtr, value, dcd, ri, rts, cts);
    }
}
