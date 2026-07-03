package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;

@FunctionalInterface
interface ScheduledAction {
    RawBytes run();
}
