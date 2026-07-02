package com.alegs3.modemsim.macros;

import java.util.Comparator;
import java.util.List;

public record MacroSet(String hash, List<MacroRule> rules) {
    public static MacroSet empty() {
        return new MacroSet("none", List.of());
    }

    public MacroSet {
        rules = rules.stream()
                .sorted(Comparator.comparingInt(MacroRule::priority).reversed()
                        .thenComparingInt(MacroRule::order))
                .toList();
    }
}
