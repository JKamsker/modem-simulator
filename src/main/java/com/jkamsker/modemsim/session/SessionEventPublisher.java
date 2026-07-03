package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.CommandResult;
import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventRedactor;
import com.jkamsker.modemsim.monitor.EventSink;
import com.jkamsker.modemsim.monitor.EventStateRedactor;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.InMemoryEventSink;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.monitor.RedactedPayload;
import com.jkamsker.modemsim.monitor.RedactionInfo;
import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.scheduler.ScheduledEmission;
import com.jkamsker.modemsim.scheduler.VirtualClock;
import com.jkamsker.modemsim.state.ModemState;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class SessionEventPublisher {
    private final String sessionId;
    private final String profileId;
    private final long sessionSeed;
    private final String profileHash;
    private final String configHash;
    private final String initialStateHash;
    private final VirtualClock clock;
    private final EventSink eventSink;
    private final InMemoryEventSink memorySink;
    private final EventRedactor redactor = new EventRedactor();
    private final EventStateRedactor stateRedactor = new EventStateRedactor();
    private long sequence;

    SessionEventPublisher(
            String sessionId,
            String profileId,
            long sessionSeed,
            ModemState initialState,
            VirtualClock clock,
            EventSink eventSink) {
        this.sessionId = sessionId;
        this.profileId = profileId;
        this.sessionSeed = sessionSeed;
        this.profileHash = sha256(profileId);
        this.configHash = sha256("headless-session:" + profileId);
        this.initialStateHash = sha256(initialState.toString());
        this.clock = clock;
        this.eventSink = eventSink;
        this.memorySink = eventSink instanceof InMemoryEventSink sink ? sink : null;
    }

    void publish(
            EventType type,
            Direction direction,
            RawBytes raw,
            ParsedCommand command,
            ModemState before,
            ModemState after,
            CommandResult result) {
        publish(type, direction, raw, command, before, after, result, false);
    }

    void publish(
            EventType type,
            Direction direction,
            RawBytes raw,
            ParsedCommand command,
            ModemState before,
            ModemState after,
            CommandResult result,
            boolean smsBodyEntry) {
        RedactedPayload payload = redactor.redactRaw(type, direction, raw, command, smsBodyEntry);
        boolean stateBeforeRedacted = stateRedactor.containsSensitiveData(before);
        boolean stateAfterRedacted = stateRedactor.containsSensitiveData(after);
        RedactionInfo redaction = mergeRedaction(
                payload.redaction(), stateBeforeRedacted, stateAfterRedacted, stateRedactor.classes(before, after));
        eventSink.publish(new ModemEvent(
                OffsetDateTime.now(), clock.nowNanos(), ++sequence, sessionId, type, direction,
                payload.rawHex(), payload.textEscaped(), parsed(command), profileId,
                null, null, profileHash, configHash, null, initialStateHash, sessionSeed, "virtual",
                null, null, null, 0,
                result == null ? null : result.handler(),
                result == null || result.finalResult() == null ? null : result.finalResult().name(),
                null,
                stateBeforeRedacted ? stateRedactor.redactSensitiveData(before) : before,
                stateAfterRedacted ? stateRedactor.redactSensitiveData(after) : after,
                redaction));
    }

    void publishParsed(ParsedCommand command, ModemState state) {
        publish(EventType.PARSED_COMMAND, Direction.INTERNAL, command.sourceLine(), command, null, state, null);
    }

    void publishScheduler(EventType type, ScheduledEmission emission, ModemState state) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("dueMonotonicNanos", emission.dueMonotonicNanos());
        data.put("sourceSequence", emission.sequence());
        data.put("sourcePriority", emission.sourcePriority().name().toLowerCase());
        data.put("sampledDelayMs", emission.sampledDelayMs());
        data.put("cancelled", false);
        boolean stateRedacted = stateRedactor.containsSensitiveData(state);
        eventSink.publish(new ModemEvent(
                OffsetDateTime.now(), clock.nowNanos(), ++sequence, sessionId, type,
                Direction.INTERNAL, "", null, null, profileId,
                null, null, profileHash, configHash, null, initialStateHash, sessionSeed, "virtual",
                null, null, null, 0, null, null, data, null,
                stateRedacted ? stateRedactor.redactSensitiveData(state) : state,
                mergeRedaction(RedactionInfo.none(), false, stateRedacted, stateRedactor.classes(null, state))));
    }

    void publishEvent(ModemEvent event) {
        eventSink.publish(event);
        sequence = Math.max(sequence, event.sequence());
    }

    long nextSequence() {
        return sequence + 1;
    }

    int eventCount() {
        return memorySink == null ? 0 : memorySink.events().size();
    }

    List<ModemEvent> eventsSince(int start) {
        if (memorySink == null) {
            return List.of();
        }
        List<ModemEvent> events = memorySink.events();
        return new ArrayList<>(events.subList(Math.min(start, events.size()), events.size()));
    }

    private Map<String, Object> parsed(ParsedCommand command) {
        if (command == null) {
            return null;
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", command.normalizedName());
        data.put("kind", command.kind().name());
        data.put("arguments", redactor.redactCommandArguments(command));
        return data;
    }

    private RedactionInfo mergeRedaction(
            RedactionInfo payload,
            boolean stateBeforeRedacted,
            boolean stateAfterRedacted,
            List<String> stateClasses) {
        if (!stateBeforeRedacted && !stateAfterRedacted) {
            return payload;
        }
        List<String> fields = new ArrayList<>(payload.fields());
        List<String> classes = new ArrayList<>(payload.classes());
        if (stateBeforeRedacted) {
            fields.add("stateBefore");
        }
        if (stateAfterRedacted) {
            fields.add("stateAfter");
        }
        for (String stateClass : stateClasses) {
            if (!classes.contains(stateClass)) {
                classes.add(stateClass);
            }
        }
        return RedactionInfo.applied(fields, classes);
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return "sha256:" + HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
