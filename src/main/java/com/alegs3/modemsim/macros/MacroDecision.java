package com.alegs3.modemsim.macros;

import com.alegs3.modemsim.commands.ResponseFrame;

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
