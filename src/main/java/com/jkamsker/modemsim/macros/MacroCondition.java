package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.state.ModemState;

import java.util.List;

public record MacroCondition(List<MacroStatePredicate> states, List<MacroProfilePredicate> profiles) {
    public static MacroCondition always() {
        return new MacroCondition(List.of(), List.of());
    }

    public boolean matches(ModemState state, Profile profile) {
        return states.stream().allMatch(predicate -> predicate.matches(state))
                && profiles.stream().allMatch(predicate -> predicate.matches(profile));
    }
}
