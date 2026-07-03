package com.jkamsker.modemsim.validation;

import com.jkamsker.modemsim.state.ModemState;

import java.util.Map;
import java.util.Set;

public final class StateInvariantValidator {
    private static final Set<String> ACTIVE_REGISTRATION = Set.of("1", "5");

    private StateInvariantValidator() {
    }

    public static String lockedRegistration(String id, Map<String, String> knownState) {
        return lockedRegistration(id, knownState.get("state.sim.state"), knownState.get("state.network.stat"));
    }

    public static String lockedRegistration(String id, ModemState state) {
        if (state == null || state.network() == null) {
            return null;
        }
        return lockedRegistration(id, state.sim().state().name(), Integer.toString(state.network().stat()));
    }

    private static String lockedRegistration(String id, String sim, String stat) {
        if (sim != null && stat != null && !"READY".equals(sim) && ACTIVE_REGISTRATION.contains(stat)) {
            return id + ": locked SIM cannot be combined with active network registration";
        }
        return null;
    }
}
