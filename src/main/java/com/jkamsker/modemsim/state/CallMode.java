package com.jkamsker.modemsim.state;

public enum CallMode {
    COMMAND("command"),
    RINGING("ringing"),
    DIALING("dialing"),
    ONLINE_DATA("online-data"),
    ONLINE_COMMAND("online-command"),
    SMS_TEXT_ENTRY("sms-text-entry"),
    SMS_PDU_ENTRY("sms-pdu-entry");

    private final String pathValue;

    CallMode(String pathValue) {
        this.pathValue = pathValue;
    }

    public String pathValue() {
        return pathValue;
    }
}
