package com.alegs3.modemsim.commands;

import com.alegs3.modemsim.state.ModemState;

import java.util.List;

public record CommandResult(
        ModemState state,
        List<ResponseFrame> frames,
        ResultCode finalResult,
        String handler,
        boolean stopLine
) {
    public static CommandResult ok(ModemState state, String handler) {
        return new CommandResult(state, List.of(), ResultCode.OK, handler, false);
    }

    public static CommandResult error(ModemState state, String handler) {
        return new CommandResult(state, List.of(), ResultCode.ERROR, handler, true);
    }

    public CommandResult withFrames(List<ResponseFrame> value) {
        return new CommandResult(state, value, finalResult, handler, stopLine);
    }
}
