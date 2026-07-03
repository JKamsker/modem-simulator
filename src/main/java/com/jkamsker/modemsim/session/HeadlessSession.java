package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.CommandResult;
import com.jkamsker.modemsim.commands.DefaultCommandRouter;
import com.jkamsker.modemsim.commands.ResponseFormatter;
import com.jkamsker.modemsim.commands.ResponseFrame;
import com.jkamsker.modemsim.macros.FaultService;
import com.jkamsker.modemsim.macros.MacroDecision;
import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventSink;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.InMemoryEventSink;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.monitor.ModemEvents;
import com.jkamsker.modemsim.parser.AtCommandParser;
import com.jkamsker.modemsim.parser.EntryMode;
import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.scheduler.DeterministicScheduler;
import com.jkamsker.modemsim.scheduler.ScheduledEmission;
import com.jkamsker.modemsim.scheduler.SourcePriority;
import com.jkamsker.modemsim.scheduler.VirtualClock;
import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.CallRuntime;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.NetworkDelay;

import java.util.List;

public final class HeadlessSession implements SessionActor {
    private final String sessionId;
    private final Profile profile;
    private final DefaultCommandRouter router = new DefaultCommandRouter();
    private final MacroEngine macroEngine;
    private final FaultService faultService = new FaultService();
    private final VirtualClock clock = new VirtualClock();
    private final DeterministicScheduler scheduler;
    private final SmsSubmitProcessor smsSubmitProcessor = new SmsSubmitProcessor();
    private final SessionEventPublisher events;

    private ModemState state;
    private RawBytes lastCommandLine = RawBytes.empty();
    private RawBytes pendingSmsBytes = RawBytes.empty();
    private PendingSms pendingSms;

    public HeadlessSession(String sessionId, Profile profile, long sessionSeed) {
        this(sessionId, profile, sessionSeed, new InMemoryEventSink(), MacroEngine.empty());
    }

    public HeadlessSession(String sessionId, Profile profile, long sessionSeed, EventSink eventSink) {
        this(sessionId, profile, sessionSeed, eventSink, MacroEngine.empty());
    }

    public HeadlessSession(
            String sessionId, Profile profile, long sessionSeed, EventSink eventSink, MacroEngine macroEngine) {
        this.sessionId = sessionId;
        this.profile = profile;
        this.macroEngine = macroEngine;
        this.scheduler = new DeterministicScheduler(sessionSeed);
        this.state = profile.initialState();
        this.events = new SessionEventPublisher(sessionId, profile.id(), sessionSeed, state, clock, eventSink);
        events.publish(EventType.SESSION_START, Direction.INTERNAL, RawBytes.empty(), null, null, state, null);
    }

    @Override
    public SessionResponse receive(RawBytes bytes) {
        if (pendingSms != null) {
            return receiveSmsEntry(bytes);
        }
        int start = eventCount();
        RawBytes output = RawBytes.empty();
        events.publish(EventType.RX_BYTES, Direction.DTE_TO_DCE, bytes, null, null, state, null);
        if (state.call().mode() == CallMode.ONLINE_DATA
                && !SessionBytes.isEscapeSequence(bytes, state.settings().s3())) {
            return response(RawBytes.empty(), start);
        }
        if (state.settings().echo()) {
            output = output.append(bytes);
        }
        RawBytes effective = bytes.ascii().equals("A/") ? lastCommandLine : bytes;
        List<ParsedCommand> commands = new AtCommandParser(state.settings().s5(), state.settings().s3())
                .parse(effective, entryMode());
        if (!bytes.ascii().equals("A/")) {
            lastCommandLine = bytes;
        }
        CommandResult finalResult = null;
        for (ParsedCommand command : commands) {
            events.publishParsed(command, state);
            ModemState before = state;
            MacroDecision decision = macroEngine.evaluateCommand(command, state, profile);
            CommandResult result = decision.matched() ? executeMacro(decision) : router.route(profile, state, command);
            state = result.state();
            output = output.append(renderFrames(result.frames()));
            if (command.normalizedName().equals("+CMGS") && result.finalResult() == null) {
                pendingSms = PendingSms.from(command, state.sms().textMode());
                pendingSmsBytes = RawBytes.empty();
            }
            events.publish(EventType.HANDLER_RESULT, Direction.INTERNAL, RawBytes.empty(), command, before, state, result);
            finalResult = result;
            if (result.stopLine()) {
                break;
            }
        }
        if (finalResult != null && finalResult.finalResult() != null && !state.settings().quiet()) {
            output = output.append(new ResponseFormatter(state).line(finalResult.finalResult().text(state.settings().verbose())));
        }
        if (!output.isEmpty()) {
            events.publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, output, null, null, state, null);
        }
        return response(output, start);
    }

    private SessionResponse receiveSmsEntry(RawBytes bytes) {
        int start = eventCount();
        events.publish(EventType.RX_BYTES, Direction.DTE_TO_DCE, bytes, null, null, state, null, true);
        pendingSmsBytes = pendingSmsBytes.append(bytes);
        byte[] raw = pendingSmsBytes.toByteArray();
        SmsSubmitResult result;
        Integer macroDelayMs = null;
        String macroOperation = null;
        String body = SessionBytes.entryPayload(raw);
        if (SessionBytes.contains(raw, 27)) {
            result = smsSubmitProcessor.abort(state);
        } else if (SessionBytes.contains(raw, 26)) {
            MacroDecision decision = macroEngine.evaluateSms(pendingSms.destination(), body, state, profile);
            if (decision.matched()) {
                ModemState macroState = applyFaults(state.withCall(CallRuntime.command()), decision);
                RawBytes macroOutput = renderFrames(decision.frames());
                macroDelayMs = decision.delayMs();
                macroOperation = "macro-" + decision.macroId();
                result = new SmsSubmitResult(macroState, macroOutput, "MACRO");
            } else {
                result = smsSubmitProcessor.submit(pendingSms, body, clock.nowNanos(), state);
            }
        } else {
            return response(RawBytes.empty(), start);
        }
        pendingSmsBytes = RawBytes.empty();
        ModemState before = state;
        state = result.state();
        pendingSms = null;
        RawBytes output = macroDelayMs == null
                ? scheduleOrReturn("sms-submit", result.response())
                : scheduleOrReturn(macroOperation, result.response(), macroDelayMs);
        events.publish(EventType.HANDLER_RESULT, Direction.INTERNAL, RawBytes.empty(), null, before, state,
                new CommandResult(state, List.of(), null, "SmsSubmitProcessor", false), true);
        if (!output.isEmpty()) {
            events.publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, output, null, null, state, null);
        }
        return response(output, start);
    }

    @Override
    public SessionResponse advanceTime(long millis) {
        int start = eventCount();
        clock.advanceMillis(millis);
        RawBytes output = RawBytes.empty();
        for (ScheduledEmission emission : scheduler.due(clock.nowNanos(), state.version())) {
            output = output.append(emission.payload());
            events.publishScheduler(EventType.SCHEDULER_EMIT, emission, state);
        }
        if (!output.isEmpty()) {
            events.publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, output, null, null, state, null);
        }
        return response(output, start);
    }

    public SessionResponse drainScheduled() {
        int start = eventCount();
        RawBytes output = RawBytes.empty();
        for (ScheduledEmission emission : scheduler.drainAll(clock, state.version())) {
            output = output.append(emission.payload());
            events.publishScheduler(EventType.SCHEDULER_EMIT, emission, state);
        }
        if (!output.isEmpty()) {
            events.publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, output, null, null, state, null);
        }
        return response(output, start);
    }

    @Override
    public ModemState snapshot() {
        return state;
    }

    public SessionResponse injectDce(RawBytes bytes, String injectionType) {
        int start = eventCount();
        events.publishEvent(ModemEvents.audit(
                events.nextSequence(), sessionId, profile.id(), EventType.INJECTION, Direction.DCE_TO_DTE,
                injectionType, bytes, state, state));
        events.publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, bytes, null, null, state, null);
        return response(bytes, start);
    }

    public SessionResponse applyState(ModemState next, String injectionType) {
        return applyState(next, injectionType, EventType.STATE_CHANGE);
    }

    public SessionResponse applyFault(String type) {
        return applyState(
                faultService.apply(state, new com.jkamsker.modemsim.macros.FaultAction(type, null, null, null, null, null)),
                null, type, EventType.FAULT_TRIGGERED);
    }

    private SessionResponse applyState(ModemState next, String injectionType, EventType eventType) {
        return applyState(next, injectionType, null, eventType);
    }

    public void diagnostic(EventType eventType, String result) {
        events.publishEvent(ModemEvents.audit(
                events.nextSequence(), sessionId, profile.id(), eventType, Direction.INTERNAL,
                null, result, RawBytes.empty(), state, state));
    }

    private SessionResponse applyState(ModemState next, String injectionType, String result, EventType eventType) {
        int start = eventCount();
        ModemState before = state;
        events.publishEvent(ModemEvents.audit(
                events.nextSequence(), sessionId, profile.id(), eventType, Direction.INTERNAL,
                injectionType, result, RawBytes.empty(), before, next));
        state = next;
        return response(RawBytes.empty(), start);
    }

    private RawBytes renderFrames(List<ResponseFrame> frames) {
        RawBytes output = RawBytes.empty();
        ResponseFormatter formatter = new ResponseFormatter(state);
        for (ResponseFrame frame : frames) {
            output = output.append(frame.bytes(formatter));
        }
        return output;
    }

    private RawBytes scheduleOrReturn(String operation, RawBytes payload) {
        NetworkDelay delay = state.network() == null ? null : state.network().delays().get(operation);
        return scheduleOrReturn(operation, payload, delay);
    }

    private RawBytes scheduleOrReturn(String operation, RawBytes payload, int delayMs) {
        NetworkDelay delay = delayMs <= 0 ? null : new NetworkDelay(operation, delayMs, delayMs);
        return scheduleOrReturn(operation, payload, delay);
    }

    private RawBytes scheduleOrReturn(String operation, RawBytes payload, NetworkDelay delay) {
        if (delay == null || delay.maxMs() == 0) {
            return payload;
        }
        ScheduledEmission emission = scheduler.enqueue(
                clock.nowNanos(), events.nextSequence(), SourcePriority.RX, payload, state.version(), operation, delay);
        events.publishScheduler(EventType.SCHEDULER_ENQUEUE, emission, state);
        return RawBytes.empty();
    }

    private CommandResult executeMacro(MacroDecision decision) {
        ModemState next = applyFaults(state, decision);
        RawBytes output = renderFrames(decision.frames());
        RawBytes effective = scheduleOrReturn("macro-" + decision.macroId(), output, decision.delayMs());
        return new CommandResult(next, List.of(new com.jkamsker.modemsim.commands.RawFrame(effective)),
                null, "Macro:" + decision.macroId(), true);
    }

    private ModemState applyFaults(ModemState source, MacroDecision decision) {
        ModemState next = source;
        for (var fault : decision.faults()) {
            next = faultService.apply(next, fault);
        }
        return next;
    }

    private EntryMode entryMode() {
        return switch (state.call().mode()) {
            case ONLINE_DATA -> EntryMode.ONLINE_DATA;
            case ONLINE_COMMAND -> EntryMode.ONLINE_COMMAND;
            case SMS_TEXT_ENTRY -> EntryMode.SMS_TEXT_ENTRY;
            case SMS_PDU_ENTRY -> EntryMode.SMS_PDU_ENTRY;
            default -> EntryMode.COMMAND;
        };
    }

    private SessionResponse response(RawBytes output, int start) { return new SessionResponse(output, events.eventsSince(start)); }

    private int eventCount() { return events.eventCount(); }
}
