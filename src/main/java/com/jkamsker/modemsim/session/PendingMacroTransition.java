package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.state.ModemState;

record PendingMacroTransition(ModemState state, EventType eventType, String result, long scheduledSequence) {
}
