package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.parser.RawBytes;

public record PromptFrame(RawBytes bytes) implements ResponseFrame {
    @Override
    public RawBytes bytes(ResponseFormatter formatter) {
        return bytes;
    }
}
