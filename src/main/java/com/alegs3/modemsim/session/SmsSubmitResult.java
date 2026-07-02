package com.alegs3.modemsim.session;

import com.alegs3.modemsim.parser.RawBytes;
import com.alegs3.modemsim.state.ModemState;

public record SmsSubmitResult(ModemState state, RawBytes response, String result) {
}
