package com.jkamsker.modemsim.macros;

import java.util.List;

public record MacroRule(
        String id,
        int priority,
        int order,
        MacroPhase phase,
        boolean enabled,
        MatchSpec match,
        List<MacroAction> actions
) {
}
