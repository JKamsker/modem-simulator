package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.state.FreezeMode;
import com.jkamsker.modemsim.state.ModemLifecycle;
import com.jkamsker.modemsim.state.ModemState;

final class SessionFreezePolicy {
    private SessionFreezePolicy() {
    }

    static boolean ignoresRx(ModemState state) {
        return frozen(state) && state.modem().freezeMode() == FreezeMode.HOLD_RX_TX;
    }

    static boolean holdsTx(ModemState state) {
        return frozen(state) && state.modem().freezeMode() != FreezeMode.NO_RESPONSE;
    }

    static boolean releasesTx(ModemState before, ModemState after) {
        return holdsTx(before) && !holdsTx(after);
    }

    private static boolean frozen(ModemState state) {
        return state.modem().lifecycle() == ModemLifecycle.FROZEN;
    }
}
