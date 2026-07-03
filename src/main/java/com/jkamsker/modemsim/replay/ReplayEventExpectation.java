package com.jkamsker.modemsim.replay;

import com.fasterxml.jackson.databind.JsonNode;
import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventType;

public record ReplayEventExpectation(
        EventType eventType,
        String source,
        Direction direction,
        Long sequence,
        Long monotonicNanos,
        String rawHex,
        String profileHash,
        String configHash,
        String macroHash,
        String initialStateHash,
        Long sessionSeed,
        String clockMode,
        String port,
        String portRole,
        String schedulerOperation,
        Integer sampledDelayMs,
        Long schedulerDueMonotonicNanos,
        Long schedulerSourceSequence,
        String schedulerSourcePriority,
        Boolean schedulerCancelled,
        Boolean redacted,
        Boolean rawPayloadRedacted,
        Boolean replayDivergent,
        JsonNode stateBeforeJson,
        JsonNode stateAfterJson
) {
    public ReplayEventExpectation(
            EventType eventType, Direction direction, Long sequence, String rawHex,
            String profileHash, String configHash, String macroHash, String initialStateHash,
            Long sessionSeed, String clockMode, String schedulerOperation,
            Integer sampledDelayMs, Boolean redacted) {
        this(eventType, null, direction, sequence, null, rawHex, profileHash, configHash, macroHash,
                initialStateHash, sessionSeed, clockMode, null, null, schedulerOperation, sampledDelayMs,
                null, null, null, null, redacted, false, false, null, null);
    }

    static ReplayEventExpectation from(JsonNode node) {
        JsonNode scheduler = node.path("scheduler");
        return new ReplayEventExpectation(
                enumValue(EventType.class, node.path("eventType").asText(null)),
                text(node, "source"),
                enumValue(Direction.class, node.path("direction").asText(null)),
                longValue(node, "sequence"),
                longValue(node, "monotonicNanos"),
                text(node, "rawHex"),
                text(node, "profileHash"),
                text(node, "configHash"),
                text(node, "macroHash"),
                text(node, "initialStateHash"),
                longValue(node, "sessionSeed"),
                text(node, "clockMode"),
                text(node, "port"),
                text(node, "portRole"),
                text(scheduler, "operation"),
                intValue(scheduler, "sampledDelayMs"),
                longValue(scheduler, "dueMonotonicNanos"),
                longValue(scheduler, "sourceSequence"),
                text(scheduler, "sourcePriority"),
                scheduler.has("cancelled") ? scheduler.path("cancelled").asBoolean() : null,
                node.path("redaction").has("applied") ? node.path("redaction").path("applied").asBoolean() : null,
                rawPayloadRedacted(node),
                node.has("replayDivergent") ? node.path("replayDivergent").asBoolean() : false,
                json(node, "stateBefore"),
                json(node, "stateAfter"));
    }

    private static String text(JsonNode node, String field) {
        if (node == null || !node.has(field) || node.path(field).isNull()) {
            return null;
        }
        return node.path(field).asText();
    }

    private static JsonNode json(JsonNode node, String field) {
        if (node == null || !node.has(field) || node.path(field).isNull()) {
            return null;
        }
        return node.path(field);
    }

    private static Long longValue(JsonNode node, String field) {
        return node.has(field) && node.path(field).canConvertToLong() ? node.path(field).asLong() : null;
    }

    private static Integer intValue(JsonNode node, String field) {
        return node.has(field) && node.path(field).canConvertToInt() ? node.path(field).asInt() : null;
    }

    private static Boolean rawPayloadRedacted(JsonNode node) {
        JsonNode fields = node.path("redaction").path("fields");
        if (!fields.isArray()) {
            return null;
        }
        for (JsonNode field : fields) {
            String value = field.asText("");
            if (value.equals("rawHex") || value.equals("textEscaped")) {
                return true;
            }
        }
        return false;
    }

    private static <T extends Enum<T>> T enumValue(Class<T> type, String value) {
        return value == null || value.isBlank() ? null : Enum.valueOf(type, value);
    }
}
