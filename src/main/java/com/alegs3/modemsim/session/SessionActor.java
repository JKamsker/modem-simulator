package com.alegs3.modemsim.session;

import com.alegs3.modemsim.parser.RawBytes;
import com.alegs3.modemsim.state.ModemState;

public interface SessionActor {
    SessionResponse receive(RawBytes bytes);

    SessionResponse advanceTime(long millis);

    ModemState snapshot();
}
