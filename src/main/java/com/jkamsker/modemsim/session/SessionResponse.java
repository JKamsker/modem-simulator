package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.parser.RawBytes;

import java.util.List;

public record SessionResponse(RawBytes output, List<ModemEvent> events) {
    public String outputHex() {
        return output.toHex();
    }

    public String outputAscii() {
        return output.ascii();
    }
}
