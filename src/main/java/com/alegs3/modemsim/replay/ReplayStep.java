package com.alegs3.modemsim.replay;

import com.alegs3.modemsim.parser.RawBytes;

public record ReplayStep(RawBytes input, RawBytes expectedOutput, boolean drainScheduled) {
}
