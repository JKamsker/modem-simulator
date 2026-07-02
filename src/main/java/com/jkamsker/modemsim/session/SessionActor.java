package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemState;

public interface SessionActor {
    SessionResponse receive(RawBytes bytes);

    SessionResponse advanceTime(long millis);

    ModemState snapshot();
}
