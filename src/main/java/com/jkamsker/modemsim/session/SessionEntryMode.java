package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.EntryMode;
import com.jkamsker.modemsim.state.CallMode;

final class SessionEntryMode {
    private SessionEntryMode() {
    }

    static EntryMode from(CallMode mode) {
        return switch (mode) {
            case ONLINE_DATA -> EntryMode.ONLINE_DATA;
            case ONLINE_COMMAND -> EntryMode.ONLINE_COMMAND;
            case SMS_TEXT_ENTRY -> EntryMode.SMS_TEXT_ENTRY;
            case SMS_PDU_ENTRY -> EntryMode.SMS_PDU_ENTRY;
            default -> EntryMode.COMMAND;
        };
    }
}
