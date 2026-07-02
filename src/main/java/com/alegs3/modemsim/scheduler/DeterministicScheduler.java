package com.alegs3.modemsim.scheduler;

import com.alegs3.modemsim.parser.RawBytes;
import com.alegs3.modemsim.state.NetworkDelay;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.SplittableRandom;

public final class DeterministicScheduler {
    private final long sessionSeed;
    private final PriorityQueue<ScheduledEmission> queue = new PriorityQueue<>();

    public DeterministicScheduler(long sessionSeed) {
        this.sessionSeed = sessionSeed;
    }

    public ScheduledEmission enqueue(
            long nowNanos,
            long sequence,
            SourcePriority priority,
            RawBytes payload,
            long stateVersion,
            String operation,
            NetworkDelay delay) {
        int sampled = sampleDelay(operation, sequence, delay);
        ScheduledEmission emission = new ScheduledEmission(
                nowNanos + sampled * 1_000_000L,
                sequence,
                priority,
                payload,
                stateVersion,
                true,
                operation,
                sampled);
        queue.add(emission);
        return emission;
    }

    public List<ScheduledEmission> due(long nowNanos, long stateVersion) {
        List<ScheduledEmission> due = new ArrayList<>();
        while (!queue.isEmpty() && queue.peek().dueMonotonicNanos() <= nowNanos) {
            ScheduledEmission emission = queue.poll();
            if (!emission.cancelOnStateChange() || emission.stateVersion() == stateVersion) {
                due.add(emission);
            }
        }
        return due;
    }

    public List<ScheduledEmission> drainAll(VirtualClock clock, long stateVersion) {
        List<ScheduledEmission> drained = new ArrayList<>();
        while (!queue.isEmpty()) {
            clock.advanceTo(queue.peek().dueMonotonicNanos());
            drained.addAll(due(clock.nowNanos(), stateVersion));
        }
        return drained;
    }

    public int size() {
        return queue.size();
    }

    private int sampleDelay(String operation, long sequence, NetworkDelay delay) {
        if (delay == null || delay.maxMs() == delay.minMs()) {
            return delay == null ? 0 : delay.minMs();
        }
        byte[] bytes = operation.getBytes(StandardCharsets.UTF_8);
        long hash = 1125899906842597L;
        for (byte value : bytes) {
            hash = 31 * hash + value;
        }
        SplittableRandom random = new SplittableRandom(sessionSeed ^ hash ^ sequence);
        return random.nextInt(delay.minMs(), delay.maxMs() + 1);
    }
}
