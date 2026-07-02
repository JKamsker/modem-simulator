package com.jkamsker.modemsim.scheduler;

import com.jkamsker.modemsim.parser.RawBytes;

public record ScheduledEmission(
        long dueMonotonicNanos,
        long sequence,
        SourcePriority sourcePriority,
        RawBytes payload,
        long stateVersion,
        boolean cancelOnStateChange,
        String operation,
        int sampledDelayMs
) implements Comparable<ScheduledEmission> {
    @Override
    public int compareTo(ScheduledEmission other) {
        int byDue = Long.compare(dueMonotonicNanos, other.dueMonotonicNanos);
        if (byDue != 0) {
            return byDue;
        }
        int bySequence = Long.compare(sequence, other.sequence);
        if (bySequence != 0) {
            return bySequence;
        }
        return Integer.compare(sourcePriority.ordinal(), other.sourcePriority.ordinal());
    }
}
