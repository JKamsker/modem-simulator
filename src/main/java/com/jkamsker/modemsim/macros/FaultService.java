package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.state.FreezeMode;
import com.jkamsker.modemsim.state.ModemLifecycle;
import com.jkamsker.modemsim.state.ModemRuntimeInfo;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.NetworkRuntime;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SignalRuntime;

public final class FaultService {
    public ModemState apply(ModemState state, FaultAction action) {
        return switch (action.type()) {
            case "network-outage" -> networkOutage(state);
            case "network-restore" -> networkRestore(state, action);
            case "modem-reboot" -> state
                    .withModem(new ModemRuntimeInfo(ModemLifecycle.REBOOTING, FreezeMode.NONE, value(action.durationMs(), 0)))
                    .withLines(state.lines().withDsr(false).withDcd(false));
            case "modem-freeze" -> state.withModem(state.modem().frozen(
                    action.freezeMode() == null ? FreezeMode.NO_RESPONSE : action.freezeMode()));
            case "modem-unfreeze" -> state.withModem(state.modem().withLifecycle(ModemLifecycle.READY));
            default -> state;
        };
    }

    private ModemState networkOutage(ModemState state) {
        if (state.network() == null) {
            return state;
        }
        return state.withNetwork(state.network().withRegistration(4)).withSignal(SignalRuntime.unknown());
    }

    private ModemState networkRestore(ModemState state, FaultAction action) {
        if (state.network() == null) {
            return state;
        }
        int restoredStat = state.sim().state() == SimState.READY ? value(action.stat(), 1) : 0;
        NetworkRuntime network = state.network().withRegistration(restoredStat);
        SignalRuntime signal = new SignalRuntime(value(action.rssi(), 18), value(action.ber(), 0));
        return state.withNetwork(network).withSignal(signal);
    }

    private int value(Integer value, int fallback) {
        return value == null ? fallback : value;
    }
}
