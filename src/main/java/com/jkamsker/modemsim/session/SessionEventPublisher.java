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
    private final SessionEventDelivery delivery;
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
        this.delivery = new SessionEventDelivery(eventSink, () -> ++sequence);
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

    void publishMeasured(
            EventType type,
            Direction direction,
            RawBytes raw,
            ParsedCommand command,
            ModemState before,
            ModemState after,
            CommandResult result,
            long startedNanos) {
        publish(type, direction, raw, command, before, after, result,
                false, raw, Math.max(0, clock.nowNanos() - startedNanos) / 1_000_000.0);
    }

    void publishWithLatency(
            EventType type,
            Direction direction,
            RawBytes raw,
            ParsedCommand command,
            ModemState before,
            ModemState after,
            CommandResult result,
            double latencyMs) {
        publish(type, direction, raw, command, before, after, result, false, raw, latencyMs);
    }

    void publishRx(RawBytes raw, RawBytes redactionContext, ModemState state) {
        publish(EventType.RX_BYTES, Direction.DTE_TO_DCE, raw, null, null, state, null, false, redactionContext, null);
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
        publish(type, direction, raw, command, before, after, result, smsBodyEntry, raw, null);
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
            RawBytes redactionContext,
            Double latencyMs) {
        RedactedPayload payload = redactor.redactRaw(type, direction, raw, command, smsBodyEntry, redactionContext);
        boolean stateBeforeRedacted = stateRedactor.containsSensitiveData(before);
        boolean stateAfterRedacted = stateRedactor.containsSensitiveData(after);
        RedactionInfo redaction = SessionEventRedactions.merge(
                payload.redaction(), stateBeforeRedacted, stateAfterRedacted, stateRedactor.classes(before, after));
        delivery.publish(new ModemEvent(
                timestamp(), clock.nowNanos(), ++sequence, sessionId, type, direction,
                payload.rawHex(), payload.textEscaped(), ParsedCommandEventData.from(command, redactor), profileId,
                port, portRole, profileHash, configHash, macroHash, initialStateHash, sessionSeed, clockMode,
                SessionEventData.macroId(result), result == null ? null : latencyMs, null, 0,
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
        String macroId = SessionEventData.macroId(emission.operation());
        boolean stateRedacted = stateRedactor.containsSensitiveData(state);
        delivery.publish(new ModemEvent(
                timestamp(), clock.nowNanos(), ++sequence, sessionId, type,
                Direction.INTERNAL, "", null, null, profileId,
                port, portRole, profileHash, configHash, macroHash, initialStateHash, sessionSeed, clockMode,
                macroId, null, null, 0, false, null, cancelled ? "cancelled" : null, data, null,
                stateRedacted ? stateRedactor.redactSensitiveData(state) : state,
                SessionEventRedactions.merge(RedactionInfo.none(), false, stateRedacted, stateRedactor.classes(null, state))));
    }

    void publishAudit(
            EventType type, Direction direction, String injectionType, String result,
            RawBytes raw, ModemState before, ModemState after) {
        publishAudit(type, direction, injectionType, result, raw, before, after, port, portRole, null);
    }

    void publishAuditWithLatency(
            EventType type, Direction direction, String injectionType, String result,
            RawBytes raw, ModemState before, ModemState after, double latencyMs) {
        publishAudit(type, direction, injectionType, result, raw, before, after, port, portRole, latencyMs);
    }

    void publishAudit(
            EventType type, Direction direction, String injectionType, String result,
            RawBytes raw, ModemState before, ModemState after, String eventPort, String eventPortRole) {
        publishAudit(type, direction, injectionType, result, raw, before, after, eventPort, eventPortRole, null);
    }

    private void publishAudit(
            EventType type, Direction direction, String injectionType, String result,
            RawBytes raw, ModemState before, ModemState after, String eventPort, String eventPortRole,
            Double latencyMs) {
        RedactedPayload payload = redactor.redactRaw(type, direction, raw, null, false);
        boolean stateBeforeRedacted = stateRedactor.containsSensitiveData(before);
        boolean stateAfterRedacted = stateRedactor.containsSensitiveData(after);
        RedactionInfo redaction = SessionEventRedactions.merge(
                payload.redaction(), stateBeforeRedacted, stateAfterRedacted, stateRedactor.classes(before, after));
        delivery.publish(new ModemEvent(
                timestamp(), clock.nowNanos(), ++sequence, sessionId, type, direction,
                payload.rawHex(), payload.textEscaped(), null, profileId,
                eventPort, eventPortRole, profileHash, configHash, macroHash, initialStateHash, sessionSeed, clockMode,
                null, latencyMs, injectionType, 0, replayDivergent(type), null, result, null,
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
        data.put("events", decision.events().stream().map(SessionEventData::macroEvent).toList());
        Map<String, Object> parsed = new LinkedHashMap<>();
        parsed.put("macroDecision", data);
        boolean stateRedacted = stateRedactor.containsSensitiveData(state);
        ModemState visibleState = stateRedacted ? stateRedactor.redactSensitiveData(state) : state;
        delivery.publish(new ModemEvent(
                timestamp(), clock.nowNanos(), ++sequence, sessionId, EventType.MACRO_DECISION,
                Direction.INTERNAL, "", null, parsed, profileId,
                port, portRole, profileHash, configHash, macroHash, initialStateHash, sessionSeed, clockMode,
                decision.macroId(), null, null, 0, false, "MacroEngine", "matched", null, visibleState,
                visibleState,
                SessionEventRedactions.merge(RedactionInfo.none(), stateRedacted, stateRedacted, stateRedactor.classes(state, state))));
    }

    void publishMacroEvents(List<MacroEventAction> actions, String macroId, ModemState state) {
        for (MacroEventAction action : actions) {
            Map<String, Object> parsed = new LinkedHashMap<>();
            parsed.put("macroEvent", SessionEventData.macroEvent(action));
            boolean stateRedacted = stateRedactor.containsSensitiveData(state);
            ModemState visibleState = stateRedacted ? stateRedactor.redactSensitiveData(state) : state;
            delivery.publish(new ModemEvent(
                    timestamp(), clock.nowNanos(), ++sequence, sessionId, EventType.MACRO_EVENT,
                    Direction.INTERNAL, "", null, parsed, profileId,
                    port, portRole, profileHash, configHash, macroHash, initialStateHash, sessionSeed, clockMode,
                    macroId, null, null, 0, false, "MacroEngine", action.type(), null,
                    visibleState, visibleState,
                    SessionEventRedactions.merge(RedactionInfo.none(), stateRedacted, stateRedacted, stateRedactor.classes(state, state))));
        }
    }

    void publishEvent(ModemEvent event) {
        delivery.publish(event);
        sequence = Math.max(sequence, event.sequence());
    }

    private boolean replayDivergent(EventType type) {
        return type == EventType.RX_OVERFLOW || type == EventType.TX_OVERFLOW;
    }

    long nextSequence() { return sequence + 1; }

    String sessionId() { return sessionId; }

    void replaceMacroHash(String macroHash) { this.macroHash = macroHash; }

    int eventCount() { return memorySink == null ? 0 : memorySink.events().size(); }

    List<ModemEvent> eventsSince(int start) {
        if (memorySink == null) {
            return List.of();
        }
        List<ModemEvent> events = memorySink.events();
        return new ArrayList<>(events.subList(Math.min(start, events.size()), events.size()));
    }

    private OffsetDateTime timestamp() {
        return EPOCH.plusNanos(clock.nowNanos());
    }

}
