package com.jkamsker.modemsim.scheduler;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.NetworkDelay;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.SplittableRandom;

public final class DeterministicScheduler {
    private final long sessionSeed;
    private final PriorityQueue<ScheduledEmission> queue = new PriorityQueue<>();
    private final List<ScheduledEmission> cancelled = new ArrayList<>();

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
            } else {
                cancelled.add(emission);
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

    public List<ScheduledEmission> cancelAll() {
        List<ScheduledEmission> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            result.add(queue.poll());
        }
        return result;
    }

    public List<ScheduledEmission> cancelledSinceLastCheck() {
        List<ScheduledEmission> result = List.copyOf(cancelled);
        cancelled.clear();
        return result;
    }

    public int size() {
        return queue.size();
    }

    private int sampleDelay(String operation, long sequence, NetworkDelay delay) {
        if (delay == null || delay.maxMs() == delay.minMs()) {
            return delay == null ? 0 : delay.minMs();
        }
        String sampleKey = delay.operation() == null || delay.operation().isBlank() ? operation : delay.operation();
        byte[] bytes = sampleKey.getBytes(StandardCharsets.UTF_8);
        long hash = 1125899906842597L;
        for (byte value : bytes) {
            hash = 31 * hash + value;
        }
        SplittableRandom random = new SplittableRandom(sessionSeed ^ hash ^ sequence);
        return random.nextInt(delay.minMs(), delay.maxMs() + 1);
    }
}
