package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.commands.ResponseFrame;

import java.util.List;

public record MacroDecision(
        boolean matched,
        String macroId,
        int delayMs,
        List<ResponseFrame> frames,
        List<FaultAction> faults
) {
    public static MacroDecision none() {
        return new MacroDecision(false, null, 0, List.of(), List.of());
    }
}
