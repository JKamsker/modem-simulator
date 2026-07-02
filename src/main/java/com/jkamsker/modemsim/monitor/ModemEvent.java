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
        String handler,
        String result,
        Map<String, Object> scheduler,
        ModemState stateBefore,
        ModemState stateAfter,
        RedactionInfo redaction
) {
}
