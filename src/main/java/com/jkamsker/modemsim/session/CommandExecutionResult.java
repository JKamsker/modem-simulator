package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemState;

import java.util.List;

record CommandExecutionResult(
        ModemState state,
        RawBytes output,
        PendingSms pendingSms,
        ModemState pendingDialConnectedState,
        List<PendingMacroTransition> pendingMacroTransitions
) {
}
