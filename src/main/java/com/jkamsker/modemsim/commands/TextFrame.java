package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.parser.RawBytes;

public record TextFrame(String line) implements ResponseFrame {
    @Override
    public RawBytes bytes(ResponseFormatter formatter) {
        return formatter.line(line);
    }
}
