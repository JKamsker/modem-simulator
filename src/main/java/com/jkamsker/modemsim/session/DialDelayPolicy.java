package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.CommandResult;
import com.jkamsker.modemsim.commands.ResultCode;
import com.jkamsker.modemsim.state.ModemState;

final class DialDelayPolicy {
    private DialDelayPolicy() {
    }

    static boolean shouldDelay(CommandResult result, ModemState state) {
        return result != null
                && result.finalResult() == ResultCode.CONNECT
                && result.handler().contains("HayesHandler")
                && state.network() != null
                && state.network().delays().containsKey("dial");
    }
}
