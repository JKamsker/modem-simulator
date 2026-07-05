package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.profiles.ErrorPolicy;

import java.util.List;

public record CommandResult(
        ModemState state,
        List<ResponseFrame> frames,
        ResultCode finalResult,
        String handler,
        boolean stopLine
) {
    private static final String INVALID_PARAMETER = "InvalidParameter:";

    public static CommandResult ok(ModemState state, String handler) {
        return new CommandResult(state, List.of(), ResultCode.OK, handler, false);
    }

    public static CommandResult error(ModemState state, String handler) {
        return new CommandResult(state, List.of(), ResultCode.ERROR, handler, true);
    }

    public static CommandResult invalidParameter(ModemState state, String handler) {
        return error(state, INVALID_PARAMETER + handler);
    }

    public static CommandResult invalidParameter(ErrorPolicy policy, ModemState state, String handler) {
        return switch (policy.invalidParameter()) {
            case "CME", "CME_OR_ERROR" -> CmeError.INCORRECT_PARAMETERS.result(state, INVALID_PARAMETER + handler);
            default -> error(state, INVALID_PARAMETER + handler);
        };
    }

    public boolean invalidParameter() {
        return handler != null && handler.startsWith(INVALID_PARAMETER);
    }

    public String actualHandler() {
        return invalidParameter() ? handler.substring(INVALID_PARAMETER.length()) : handler;
    }

    public CommandResult withFrames(List<ResponseFrame> value) {
        return new CommandResult(state, value, finalResult, handler, stopLine);
    }
}
