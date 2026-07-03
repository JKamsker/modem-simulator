package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.CommandResult;
import com.jkamsker.modemsim.macros.MacroDecision;
import com.jkamsker.modemsim.macros.MacroEventAction;
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
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.scheduler.ScheduledEmission;
import com.jkamsker.modemsim.scheduler.VirtualClock;
import com.jkamsker.modemsim.state.ModemState;

import java.time.OffsetDateTime;
import java.util.ArrayList;
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
    private String macroHash;
    private final String port;
    private final String portRole;
    private final String clockMode;
    private final VirtualClock clock;
    private final EventSink eventSink;
    private final InMemoryEventSink memorySink;
    private final EventRedactor redactor = new EventRedactor();
    private final EventStateRedactor stateRedactor = new EventStateRedactor();
    private static final OffsetDateTime EPOCH = OffsetDateTime.parse("2026-01-01T00:00:00Z");
    private long sequence;

    SessionEventPublisher(
            String sessionId,
            Profile profile,
            long sessionSeed,
            ModemState initialState,
            VirtualClock clock,
            EventSink eventSink,
            String clockMode,
            String macroHash,
            String port,
            String portRole,
            Map<String, Object> configMetadata) {
        this.sessionId = sessionId;
        this.profileId = profile.id();
        this.sessionSeed = sessionSeed;
        this.profileHash = EventFingerprints.profileHash(profile);
        this.configHash = EventFingerprints.configHash(profile.id(), clockMode, port, portRole, configMetadata);
        this.initialStateHash = EventFingerprints.initialStateHash(initialState);
        this.macroHash = macroHash;
        this.port = port;
        this.portRole = portRole;
        this.clockMode = clockMode;
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

    void publishRx(RawBytes raw, RawBytes redactionContext, ModemState state) {
        publish(EventType.RX_BYTES, Direction.DTE_TO_DCE, raw, null, null, state, null, false, redactionContext);
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
        publish(type, direction, raw, command, before, after, result, smsBodyEntry, raw);
    }

    private void publish(
            EventType type,
            Direction direction,
            RawBytes raw,
            ParsedCommand command,
            ModemState before,
            ModemState after,
            CommandResult result,
            boolean smsBodyEntry,
            RawBytes redactionContext) {
        RedactedPayload payload = redactor.redactRaw(type, direction, raw, command, smsBodyEntry, redactionContext);
        boolean stateBeforeRedacted = stateRedactor.containsSensitiveData(before);
        boolean stateAfterRedacted = stateRedactor.containsSensitiveData(after);
        RedactionInfo redaction = mergeRedaction(
                payload.redaction(), stateBeforeRedacted, stateAfterRedacted, stateRedactor.classes(before, after));
        eventSink.publish(new ModemEvent(
                timestamp(), clock.nowNanos(), ++sequence, sessionId, type, direction,
                payload.rawHex(), payload.textEscaped(), ParsedCommandEventData.from(command, redactor), profileId,
                port, portRole, profileHash, configHash, macroHash, initialStateHash, sessionSeed, clockMode,
                macroId(result), result == null ? null : 0.0, null, 0,
                replayDivergent(type),
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
        publishScheduler(type, emission, state, false);
    }

    void publishScheduler(EventType type, ScheduledEmission emission, ModemState state, boolean cancelled) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("dueMonotonicNanos", emission.dueMonotonicNanos());
        data.put("sourceSequence", emission.sequence());
        data.put("sourcePriority", emission.sourcePriority().name().toLowerCase());
        data.put("stateVersion", emission.stateVersion());
        data.put("cancelOnStateChange", emission.cancelOnStateChange());
        data.put("operation", emission.operation());
        data.put("sampledDelayMs", emission.sampledDelayMs());
        data.put("cancelled", cancelled);
        String macroId = macroId(emission.operation());
        boolean stateRedacted = stateRedactor.containsSensitiveData(state);
        eventSink.publish(new ModemEvent(
                timestamp(), clock.nowNanos(), ++sequence, sessionId, type,
                Direction.INTERNAL, "", null, null, profileId,
                port, portRole, profileHash, configHash, macroHash, initialStateHash, sessionSeed, clockMode,
                macroId, null, null, 0, false, null, cancelled ? "cancelled" : null, data, null,
                stateRedacted ? stateRedactor.redactSensitiveData(state) : state,
                mergeRedaction(RedactionInfo.none(), false, stateRedacted, stateRedactor.classes(null, state))));
    }

    void publishAudit(
            EventType type, Direction direction, String injectionType, String result,
            RawBytes raw, ModemState before, ModemState after) {
        RedactedPayload payload = redactor.redactRaw(type, direction, raw, null, false);
        boolean stateBeforeRedacted = stateRedactor.containsSensitiveData(before);
        boolean stateAfterRedacted = stateRedactor.containsSensitiveData(after);
        RedactionInfo redaction = mergeRedaction(
                payload.redaction(), stateBeforeRedacted, stateAfterRedacted, stateRedactor.classes(before, after));
        eventSink.publish(new ModemEvent(
                timestamp(), clock.nowNanos(), ++sequence, sessionId, type, direction,
                payload.rawHex(), payload.textEscaped(), null, profileId,
                port, portRole, profileHash, configHash, macroHash, initialStateHash, sessionSeed, clockMode,
                null, null, injectionType, 0, replayDivergent(type), null, result, null,
                stateBeforeRedacted ? stateRedactor.redactSensitiveData(before) : before,
                stateAfterRedacted ? stateRedactor.redactSensitiveData(after) : after,
                redaction));
    }

    void publishMacroDecision(MacroDecision decision, ModemState state) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("minDelayMs", decision.minDelayMs());
        data.put("maxDelayMs", decision.maxDelayMs());
        data.put("frameCount", decision.frames().size());
        data.put("faultCount", decision.faults().size());
        data.put("statePatchCount", decision.statePatches().size());
        data.put("events", decision.events().stream().map(this::eventData).toList());
        Map<String, Object> parsed = new LinkedHashMap<>();
        parsed.put("macroDecision", data);
        boolean stateRedacted = stateRedactor.containsSensitiveData(state);
        ModemState visibleState = stateRedacted ? stateRedactor.redactSensitiveData(state) : state;
        eventSink.publish(new ModemEvent(
                timestamp(), clock.nowNanos(), ++sequence, sessionId, EventType.MACRO_DECISION,
                Direction.INTERNAL, "", null, parsed, profileId,
                port, portRole, profileHash, configHash, macroHash, initialStateHash, sessionSeed, clockMode,
                decision.macroId(), null, null, 0, false, "MacroEngine", "matched", null, visibleState,
                visibleState,
                mergeRedaction(RedactionInfo.none(), stateRedacted, stateRedacted, stateRedactor.classes(state, state))));
    }

    void publishMacroEvents(List<MacroEventAction> actions, String macroId, ModemState state) {
        for (MacroEventAction action : actions) {
            Map<String, Object> parsed = new LinkedHashMap<>();
            parsed.put("macroEvent", eventData(action));
            boolean stateRedacted = stateRedactor.containsSensitiveData(state);
            ModemState visibleState = stateRedacted ? stateRedactor.redactSensitiveData(state) : state;
            eventSink.publish(new ModemEvent(
                    timestamp(), clock.nowNanos(), ++sequence, sessionId, EventType.MACRO_EVENT,
                    Direction.INTERNAL, "", null, parsed, profileId,
                    port, portRole, profileHash, configHash, macroHash, initialStateHash, sessionSeed, clockMode,
                    macroId, null, null, 0, false, "MacroEngine", action.type(), null,
                    visibleState, visibleState,
                    mergeRedaction(RedactionInfo.none(), stateRedacted, stateRedacted, stateRedactor.classes(state, state))));
        }
    }

    void publishEvent(ModemEvent event) {
        eventSink.publish(event);
        sequence = Math.max(sequence, event.sequence());
    }

    private boolean replayDivergent(EventType type) {
        return type == EventType.RX_OVERFLOW || type == EventType.TX_OVERFLOW;
    }

    long nextSequence() {
        return sequence + 1;
    }

    String sessionId() {
        return sessionId;
    }

    void replaceMacroHash(String macroHash) {
        this.macroHash = macroHash;
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

    private Map<String, Object> eventData(MacroEventAction event) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("type", event.type());
        data.put("message", event.message());
        return data;
    }

    private String macroId(CommandResult result) {
        if (result == null || result.handler() == null) {
            return null;
        }
        for (String part : result.handler().split("\\+")) {
            if (part.startsWith("Macro:")) {
                return part.substring("Macro:".length());
            }
        }
        return null;
    }

    private String macroId(String operation) {
        return operation != null && operation.startsWith("macro-") ? operation.substring("macro-".length()) : null;
    }

    private OffsetDateTime timestamp() {
        return EPOCH.plusNanos(clock.nowNanos());
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

}
