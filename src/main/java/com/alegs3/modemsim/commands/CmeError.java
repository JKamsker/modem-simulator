package com.alegs3.modemsim.commands;

import com.alegs3.modemsim.state.ModemState;

import java.util.List;

public enum CmeError {
    SIM_NOT_INSERTED(10, "SIM not inserted"),
    SIM_PIN_REQUIRED(11, "SIM PIN required"),
    SIM_PUK_REQUIRED(12, "SIM PUK required"),
    SIM_FAILURE(13, "SIM failure"),
    INCORRECT_PASSWORD(16, "incorrect password"),
    OPERATION_NOT_ALLOWED(3, "operation not allowed");

    private final int code;
    private final String text;

    CmeError(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public CommandResult result(ModemState state, String handler) {
        if (state.settings().cmee() == 0) {
            return CommandResult.error(state, handler);
        }
        String value = state.settings().cmee() == 1 ? Integer.toString(code) : text;
        return new CommandResult(
                state,
                List.of(new TextFrame("+CME ERROR: " + value)),
                null,
                handler,
                true);
    }
}
