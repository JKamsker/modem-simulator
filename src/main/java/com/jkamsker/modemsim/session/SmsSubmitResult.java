package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemState;

public record SmsSubmitResult(ModemState state, RawBytes response, String result) {
}
