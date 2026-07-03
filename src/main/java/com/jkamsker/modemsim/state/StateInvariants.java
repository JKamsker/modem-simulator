package com.jkamsker.modemsim.state;

public final class StateInvariants {
    private StateInvariants() {
    }

    public static ModemState normalize(ModemState state) {
        if (state == null || state.network() == null || state.sim().state() == SimState.READY
                || !state.network().registeredForCircuitServices()) {
            return state;
        }
        return state.withNetwork(state.network().withRegistration(0));
    }
}
