package com.alegs3.modemsim.commands;

import com.alegs3.modemsim.parser.RawBytes;

public sealed interface ResponseFrame permits TextFrame, RawFrame, PromptFrame {
    RawBytes bytes(ResponseFormatter formatter);
}
