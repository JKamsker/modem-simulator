package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemState;

record CommandExecutionResult(
        ModemState state,
        RawBytes output,
        PendingSms pendingSms,
        ModemState pendingDialConnectedState,
        PendingMacroTransition pendingMacroTransition
) {
}
