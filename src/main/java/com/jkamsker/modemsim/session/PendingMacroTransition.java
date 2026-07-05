package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.macros.MacroAction;

import java.util.List;

record PendingMacroTransition(List<MacroAction> actions, EventType eventType, String result, long scheduledSequence) {
}
