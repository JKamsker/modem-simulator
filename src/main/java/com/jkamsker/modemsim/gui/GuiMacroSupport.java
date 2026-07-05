package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.macros.MacroRule;
import com.jkamsker.modemsim.macros.MacroSet;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class GuiMacroSupport {
    private GuiMacroSupport() {
    }

    static MacroSet effective(MacroSet source, Map<String, Boolean> overrides) {
        List<MacroRule> rules = source.rules().stream()
                .map(rule -> overrides.containsKey(rule.id()) ? withEnabled(rule, overrides.get(rule.id())) : rule)
                .toList();
        return new MacroSet(
                MacroSet.effectiveHash(source.randomSeed(), source.lineEnding(), rules),
                source.randomSeed(), source.lineEnding(), rules);
    }

    static String customResponses(MacroSet source) {
        return source.rules().stream()
                .filter(GuiMacroSupport::isCustomResponse)
                .map(MacroRule::id)
                .collect(Collectors.joining(", "));
    }

    static String enabled(MacroSet source, Map<String, Boolean> overrides) {
        return effective(source, overrides).rules().stream()
                .filter(MacroRule::enabled)
                .map(MacroRule::id)
                .collect(Collectors.joining(", "));
    }

    private static MacroRule withEnabled(MacroRule rule, boolean enabled) {
        return new MacroRule(rule.id(), rule.priority(), rule.order(), rule.phase(), enabled,
                rule.condition(), rule.match(), rule.actions());
    }

    private static boolean isCustomResponse(MacroRule rule) {
        return rule.actions().stream().anyMatch(action -> action.type().equals("send"));
    }
}
