package com.jkamsker.modemsim.monitor;

import com.jkamsker.modemsim.state.ModemState;

import java.time.OffsetDateTime;
import java.util.Map;

public record ModemEvent(
        OffsetDateTime timestamp,
        long monotonicNanos,
        long sequence,
        String sessionId,
        EventType eventType,
        Direction direction,
        String rawHex,
        String textEscaped,
        Map<String, Object> parsedCommand,
        String profile,
        String port,
        String portRole,
        String profileHash,
        String configHash,
        String macroHash,
        String initialStateHash,
        Long sessionSeed,
        String clockMode,
        String macroId,
        Double latencyMs,
        String injectionType,
        long droppedEventCount,
        boolean replayDivergent,
        String handler,
        String result,
        Map<String, Object> scheduler,
        ModemState stateBefore,
        ModemState stateAfter,
        RedactionInfo redaction
) {
    public ModemEvent {
        parsedCommand = copy(parsedCommand);
        scheduler = copy(scheduler);
    }

    public ModemEvent withDroppedEventCount(long value) {
        return new ModemEvent(
                timestamp, monotonicNanos, sequence, sessionId, eventType, direction, rawHex, textEscaped,
                parsedCommand, profile, port, portRole, profileHash, configHash, macroHash, initialStateHash,
                sessionSeed, clockMode, macroId, latencyMs, injectionType, value, replayDivergent, handler,
                result, scheduler, stateBefore, stateAfter, redaction);
    }

    public ModemEvent droppedEventsSummary(long nextSequence, long value) {
        return new ModemEvent(
                timestamp, monotonicNanos, nextSequence, sessionId, EventType.DROPPED_EVENTS, Direction.NONE,
                "", null, null, profile, port, portRole, profileHash, configHash, macroHash, initialStateHash,
                sessionSeed, clockMode, null, null, null, value, false, null, "dropped-events",
                null, null, null, RedactionInfo.none());
    }

    private static Map<String, Object> copy(Map<String, Object> value) {
        return value == null ? null : Map.copyOf(value);
    }
}
