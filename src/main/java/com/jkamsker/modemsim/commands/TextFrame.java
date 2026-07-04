package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.parser.RawBytes;

public record TextFrame(String line, LineEnding ending) implements ResponseFrame {
    public TextFrame(String line) {
        this(line, null);
    }

    @Override
    public RawBytes bytes(ResponseFormatter formatter) {
        return ending == null ? formatter.line(line) : formatter.line(line, ending);
    }
}
