package com.jkamsker.modemsim.commands;

public enum ResultCode {
    OK("OK", "0"),
    CONNECT("CONNECT", "1"),
    RING("RING", "2"),
    NO_CARRIER("NO CARRIER", "3"),
    ERROR("ERROR", "4"),
    ERR("ERR", "4"),
    BUSY("BUSY", "7");

    private final String verbose;
    private final String numeric;

    ResultCode(String verbose, String numeric) {
        this.verbose = verbose;
        this.numeric = numeric;
    }

    public String text(boolean verboseMode) {
        return verboseMode ? verbose : numeric;
    }
}
