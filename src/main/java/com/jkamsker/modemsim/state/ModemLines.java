package com.jkamsker.modemsim.state;

public record ModemLines(boolean dtr, boolean dsr, boolean dcd, boolean ri, boolean rts, boolean cts) {
    public static ModemLines ready() {
        return new ModemLines(true, true, false, false, true, true);
    }

    public ModemLines withDcd(boolean value) {
        return new ModemLines(dtr, dsr, value, ri, rts, cts);
    }

    public ModemLines withDtr(boolean value) {
        return new ModemLines(value, dsr, dcd, ri, rts, cts);
    }

    public ModemLines withDsr(boolean value) {
        return new ModemLines(dtr, value, dcd, ri, rts, cts);
    }

    public ModemLines withRts(boolean value) {
        return new ModemLines(dtr, dsr, dcd, ri, value, cts);
    }

    public ModemLines withCts(boolean value) {
        return new ModemLines(dtr, dsr, dcd, ri, rts, value);
    }

    public ModemLines withRi(boolean value) {
        return new ModemLines(dtr, dsr, dcd, value, rts, cts);
    }
}
