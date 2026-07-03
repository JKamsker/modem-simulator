package com.jkamsker.modemsim.replay;

import com.jkamsker.modemsim.parser.RawBytes;

import java.util.List;

public record ReplayStep(
        RawBytes input,
        RawBytes expectedOutput,
        boolean drainScheduled,
        List<ReplayEventExpectation> expectedEvents
) {
    public ReplayStep(RawBytes input, RawBytes expectedOutput, boolean drainScheduled) {
        this(input, expectedOutput, drainScheduled, List.of());
    }
}
