package com.alegs3.modemsim.session;

import com.alegs3.modemsim.monitor.ModemEvent;
import com.alegs3.modemsim.parser.RawBytes;

import java.util.List;

public record SessionResponse(RawBytes output, List<ModemEvent> events) {
    public String outputHex() {
        return output.toHex();
    }

    public String outputAscii() {
        return output.ascii();
    }
}
