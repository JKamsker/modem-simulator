package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.state.ModemState;

import java.util.List;

public enum CmsError {
    UNKNOWN(500),
    INVALID_INDEX(321),
    MEMORY_FULL(322);

    private final int code;

    CmsError(int code) {
        this.code = code;
    }

    public CommandResult result(ModemState state, String handler) {
        return new CommandResult(
                state,
                List.of(new TextFrame("+CMS ERROR: " + code)),
                null,
                handler,
                true);
    }
}
