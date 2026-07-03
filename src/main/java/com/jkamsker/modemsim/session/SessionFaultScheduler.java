package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.state.ModemLifecycle;
import com.jkamsker.modemsim.state.ModemState;

final class SessionFaultScheduler {
    private SessionFaultScheduler() {
    }

    static boolean cancelIfModemFault(
            ModemState before, ModemState after,
            SessionSchedulerBridge scheduler, PendingMacroTransitions transitions) {
        if (!becameFaulted(before, after)) {
            return false;
        }
        scheduler.cancelAll(after);
        transitions.clear();
        return true;
    }

    private static boolean becameFaulted(ModemState before, ModemState after) {
        ModemLifecycle lifecycle = after.modem().lifecycle();
        return before.modem().lifecycle() != lifecycle
                && (lifecycle == ModemLifecycle.FROZEN || lifecycle == ModemLifecycle.REBOOTING);
    }
}
