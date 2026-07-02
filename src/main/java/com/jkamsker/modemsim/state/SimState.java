package com.jkamsker.modemsim.state;

public enum SimState {
    READY,
    SIM_NOT_INSERTED,
    SIM_PIN_REQUIRED,
    SIM_PUK_REQUIRED,
    SIM_FAILURE,
    SIM_BUSY,
    SIM_WRONG
}
