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

    SessionSchedulerBridge(long sessionSeed, VirtualClock clock, SessionEventPublisher events) {
        this.scheduler = new DeterministicScheduler(sessionSeed);
        this.clock = clock;
        this.events = events;
    }

    ScheduledBatch advanceTime(long millis, ModemState state) {
        clock.advanceMillis(millis);
        return emitScheduled(scheduler.due(clock.nowNanos(), state.version()), state);
    }

    ScheduledBatch advanceTo(long monotonicNanos, ModemState state) {
        clock.advanceTo(monotonicNanos);
        return emitScheduled(scheduler.due(clock.nowNanos(), state.version()), state);
    }

    ScheduledBatch drainScheduled(ModemState state) {
        return emitScheduled(scheduler.drainAll(clock, state.version()), state);
    }

    RawBytes scheduleOrReturn(String operation, RawBytes payload, NetworkDelay delay, ModemState source) {
        return scheduleWithMetadata(operation, payload, delay, source).output();
    }

    ScheduledPayload scheduleWithMetadata(String operation, RawBytes payload, NetworkDelay delay, ModemState source) {
        return scheduleWithMetadata(operation, payload, delay, source, false);
    }

    ScheduledPayload scheduleWithMetadata(
            String operation, RawBytes payload, NetworkDelay delay, ModemState source, boolean cancelOnMacroReload) {
        if (delay == null || delay.maxMs() == 0) {
            return new ScheduledPayload(payload, null, 0);
        }
        SourcePriority priority = operation != null && operation.startsWith("macro-")
                ? SourcePriority.MACRO
                : SourcePriority.RX;
        ScheduledEmission emission = scheduler.enqueue(
                clock.nowNanos(), events.nextSequence(), priority, payload, source.version(),
                operation, delay, cancelOnMacroReload);
        events.publishScheduler(EventType.SCHEDULER_ENQUEUE, emission, source);
        return new ScheduledPayload(RawBytes.empty(), emission.sequence(), emission.sampledDelayMs());
    }

    Integer previewDelayAfterNextEvent(String operation, NetworkDelay delay) {
        return delay == null || delay.maxMs() == 0 ? null
                : scheduler.sampleDelay(operation, events.nextSequence() + 1, delay);
    }

    RawBytes scheduleImmediate(String operation, RawBytes payload, ModemState source) {
        ScheduledEmission emission = scheduler.enqueue(
                clock.nowNanos(), events.nextSequence(), SourcePriority.INTERNAL,
                payload, source.version(), operation, new NetworkDelay(operation, 0, 0));
        events.publishScheduler(EventType.SCHEDULER_ENQUEUE, emission, source);
        return emitScheduled(scheduler.due(clock.nowNanos(), source.version()), source).output();
    }

    long nowNanos() {
        return clock.nowNanos();
    }

    void cancelAll(ModemState state) {
        for (ScheduledEmission emission : scheduler.cancelAll()) {
            events.publishScheduler(EventType.SCHEDULER_EMIT, emission, state, true);
        }
    }

    List<ScheduledEmission> cancelMacroReloadable(ModemState state) {
        List<ScheduledEmission> cancelled = scheduler.cancelMacroReloadable();
        for (ScheduledEmission emission : cancelled) {
            events.publishScheduler(EventType.SCHEDULER_EMIT, emission, state, true);
        }
        return cancelled;
    }

    private ScheduledBatch emitScheduled(List<ScheduledEmission> emissions, ModemState state) {
        RawBytes output = RawBytes.empty();
        for (ScheduledEmission emission : emissions) {
            output = output.append(emission.payload());
            events.publishScheduler(EventType.SCHEDULER_EMIT, emission, state);
        }
        List<ScheduledEmission> cancelled = publishCancelled(state);
        return new ScheduledBatch(output, List.copyOf(emissions), cancelled);
    }

    private List<ScheduledEmission> publishCancelled(ModemState state) {
        List<ScheduledEmission> cancelled = scheduler.cancelledSinceLastCheck();
        for (ScheduledEmission emission : cancelled) {
            events.publishScheduler(EventType.SCHEDULER_EMIT, emission, state, true);
        }
        return cancelled;
    }
}
