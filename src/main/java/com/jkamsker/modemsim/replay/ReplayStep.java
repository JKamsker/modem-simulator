package com.jkamsker.modemsim.replay;

import com.jkamsker.modemsim.parser.RawBytes;

public record ReplayStep(RawBytes input, RawBytes expectedOutput, boolean drainScheduled) {
}
