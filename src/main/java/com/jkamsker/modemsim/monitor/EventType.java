package com.jkamsker.modemsim.monitor;

public enum EventType {
    SESSION_START,
    RX_BYTES,
    TX_BYTES,
    PARSED_COMMAND,
    HANDLER_RESULT,
    SCHEDULER_ENQUEUE,
    SCHEDULER_EMIT,
    STATE_CHANGE,
    FAULT_TRIGGERED,
    INJECTION,
    AUDIT_FAILURE
}
