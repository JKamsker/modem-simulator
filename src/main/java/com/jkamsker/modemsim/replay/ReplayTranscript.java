package com.jkamsker.modemsim.replay;

import java.util.List;
import java.util.Map;

public record ReplayTranscript(
        String name,
        String profile,
        Map<String, String> metadata,
        List<ReplayStep> steps
) {
    public ReplayTranscript {
        metadata = Map.copyOf(metadata);
        steps = List.copyOf(steps);
    }
}
