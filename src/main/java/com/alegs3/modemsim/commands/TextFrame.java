package com.alegs3.modemsim.commands;

import com.alegs3.modemsim.parser.RawBytes;

public record TextFrame(String line) implements ResponseFrame {
    @Override
    public RawBytes bytes(ResponseFormatter formatter) {
        return formatter.line(line);
    }
}
