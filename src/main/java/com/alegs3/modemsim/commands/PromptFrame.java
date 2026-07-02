package com.alegs3.modemsim.commands;

import com.alegs3.modemsim.parser.RawBytes;

public record PromptFrame(RawBytes bytes) implements ResponseFrame {
    @Override
    public RawBytes bytes(ResponseFormatter formatter) {
        return bytes;
    }
}
