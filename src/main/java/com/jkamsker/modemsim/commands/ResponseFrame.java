package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.parser.RawBytes;

public sealed interface ResponseFrame permits TextFrame, RawFrame, PromptFrame {
    RawBytes bytes(ResponseFormatter formatter);
}
