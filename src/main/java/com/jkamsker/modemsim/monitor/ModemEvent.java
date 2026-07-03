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
}
