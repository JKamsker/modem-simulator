package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.commands.ResponseFrame;
import com.jkamsker.modemsim.state.NetworkDelay;

import java.util.List;

public record MacroDecision(
        boolean matched,
        String macroId,
        Long randomSeed,
        int minDelayMs,
        int maxDelayMs,
        List<ResponseFrame> frames,
        List<FaultAction> faults,
        List<MacroStatePatch> statePatches,
        List<MacroEventAction> events,
        List<MacroAction> actions
) {
    public static MacroDecision none() {
        return new MacroDecision(false, null, null, 0, 0, List.of(), List.of(), List.of(), List.of(), List.of());
    }

    public int delayMs() {
        return minDelayMs;
    }

    public NetworkDelay delay(String operation) {
        String sampleKey = randomSeed == null ? operation : operation + ":seed=" + randomSeed;
        return maxDelayMs <= 0 ? null : new NetworkDelay(sampleKey, minDelayMs, maxDelayMs);
    }
}
