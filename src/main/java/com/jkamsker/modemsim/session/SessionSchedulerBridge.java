package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.scheduler.DeterministicScheduler;
import com.jkamsker.modemsim.scheduler.ScheduledEmission;
import com.jkamsker.modemsim.scheduler.SourcePriority;
import com.jkamsker.modemsim.scheduler.VirtualClock;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.NetworkDelay;

import java.util.List;

final class SessionSchedulerBridge {
    private final DeterministicScheduler scheduler;
    private final VirtualClock clock;
    private final SessionEventPublisher events;
    private boolean emittedSinceLastCheck;

    SessionSchedulerBridge(long sessionSeed, VirtualClock clock, SessionEventPublisher events) {
        this.scheduler = new DeterministicScheduler(sessionSeed);
        this.clock = clock;
        this.events = events;
    }

    RawBytes advanceTime(long millis, ModemState state) {
        clock.advanceMillis(millis);
        return emitScheduled(scheduler.due(clock.nowNanos(), state.version()), state);
    }

    RawBytes advanceTo(long monotonicNanos, ModemState state) {
        clock.advanceTo(monotonicNanos);
        return emitScheduled(scheduler.due(clock.nowNanos(), state.version()), state);
    }

    RawBytes drainScheduled(ModemState state) {
        return emitScheduled(scheduler.drainAll(clock, state.version()), state);
    }

    RawBytes scheduleOrReturn(String operation, RawBytes payload, NetworkDelay delay, ModemState source) {
        if (delay == null || delay.maxMs() == 0) {
            return payload;
        }
        SourcePriority priority = operation != null && operation.startsWith("macro-")
                ? SourcePriority.MACRO
                : SourcePriority.RX;
        ScheduledEmission emission = scheduler.enqueue(
                clock.nowNanos(), events.nextSequence(), priority, payload, source.version(), operation, delay);
        events.publishScheduler(EventType.SCHEDULER_ENQUEUE, emission, source);
        return RawBytes.empty();
    }

    RawBytes scheduleImmediate(String operation, RawBytes payload, ModemState source) {
        ScheduledEmission emission = scheduler.enqueue(
                clock.nowNanos(), events.nextSequence(), SourcePriority.INTERNAL,
                payload, source.version(), operation, new NetworkDelay(operation, 0, 0));
        events.publishScheduler(EventType.SCHEDULER_ENQUEUE, emission, source);
        return emitScheduled(scheduler.due(clock.nowNanos(), source.version()), source);
    }

    void cancelAll(ModemState state) {
        for (ScheduledEmission emission : scheduler.cancelAll()) {
            events.publishScheduler(EventType.SCHEDULER_EMIT, emission, state, true);
        }
    }

    boolean consumeEmitted() {
        boolean result = emittedSinceLastCheck;
        emittedSinceLastCheck = false;
        return result;
    }

    private RawBytes emitScheduled(List<ScheduledEmission> emissions, ModemState state) {
        emittedSinceLastCheck = !emissions.isEmpty();
        RawBytes output = RawBytes.empty();
        for (ScheduledEmission emission : emissions) {
            output = output.append(emission.payload());
            events.publishScheduler(EventType.SCHEDULER_EMIT, emission, state);
        }
        publishCancelled(state);
        return output;
    }

    private void publishCancelled(ModemState state) {
        for (ScheduledEmission emission : scheduler.cancelledSinceLastCheck()) {
            events.publishScheduler(EventType.SCHEDULER_EMIT, emission, state, true);
        }
    }
}
