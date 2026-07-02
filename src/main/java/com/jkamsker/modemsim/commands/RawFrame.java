package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.parser.RawBytes;

public record RawFrame(RawBytes bytes) implements ResponseFrame {
    @Override
    public RawBytes bytes(ResponseFormatter formatter) {
        return bytes;
    }
}
