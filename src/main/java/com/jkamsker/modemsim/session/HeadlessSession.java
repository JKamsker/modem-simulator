package com.jkamsker.modemsim.session;
import com.jkamsker.modemsim.commands.CommandResult;
import com.jkamsker.modemsim.commands.DefaultCommandRouter;
import com.jkamsker.modemsim.commands.ResponseFormatter;
import com.jkamsker.modemsim.macros.FaultService;
import com.jkamsker.modemsim.macros.MacroDecision;
import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventSink;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.InMemoryEventSink;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.parser.AtCommandParser;
import com.jkamsker.modemsim.parser.CommandKind;
import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.scheduler.VirtualClock;
import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.CallRuntime;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.NetworkDelay;
import com.jkamsker.modemsim.state.StateInvariants;
import java.util.List;
import java.util.Map;
public final class HeadlessSession implements SessionActor {
    private final Profile profile;
    private MacroEngine macroEngine;
    private MacroCommandRouter commandRouter;
    private final FaultService faultService = new FaultService();
    private final VirtualClock clock = new VirtualClock();
    private final SessionSchedulerBridge scheduler;
    private SessionCommandExecutor commandExecutor;
    private final SmsSubmitProcessor smsSubmitProcessor = new SmsSubmitProcessor();
    private final SessionEventPublisher events;
    private final SessionInputState inputState = new SessionInputState();
    private final SessionRebootTimer rebootTimer;
    private ModemState state;
    private RawBytes pendingSmsBytes = RawBytes.empty();
    private PendingSms pendingSms;
    private ModemState pendingDialConnectedState;
    private final PendingMacroTransitions pendingMacroTransitions = new PendingMacroTransitions();
    private ModemState pendingEscapeCommandState;
    private RawBytes heldTx = RawBytes.empty();
    public HeadlessSession(String sessionId, Profile profile, long sessionSeed) { this(sessionId, profile, sessionSeed, new InMemoryEventSink(), MacroEngine.empty()); }
    public HeadlessSession(String sessionId, Profile profile, long sessionSeed, EventSink eventSink) { this(sessionId, profile, sessionSeed, eventSink, MacroEngine.empty()); }
    public HeadlessSession(String sessionId, Profile profile, long sessionSeed, EventSink eventSink, MacroEngine macroEngine) { this(sessionId, profile, sessionSeed, eventSink, macroEngine, "virtual", null, null); }
    public HeadlessSession(String sessionId, Profile profile, long sessionSeed, EventSink eventSink, String clockMode) { this(sessionId, profile, sessionSeed, eventSink, MacroEngine.empty(), clockMode, null, null); }
    public HeadlessSession(String sessionId, Profile profile, long sessionSeed, EventSink eventSink, String clockMode, String port, String portRole) { this(sessionId, profile, sessionSeed, eventSink, MacroEngine.empty(), clockMode, port, portRole); }
    public HeadlessSession(String sessionId, Profile profile, long sessionSeed, EventSink eventSink, MacroEngine macroEngine, String clockMode, String port, String portRole) { this(sessionId, profile, sessionSeed, eventSink, macroEngine, clockMode, port, portRole, null); }
    public HeadlessSession(String sessionId, Profile profile, long sessionSeed, EventSink eventSink, MacroEngine macroEngine, String clockMode, String port, String portRole, Map<String, Object> configMetadata) {
        this.profile = profile;
        this.macroEngine = macroEngine;
        this.commandRouter = commandRouter(macroEngine);
        this.state = profile.initialState();
        this.events = new SessionEventPublisher(
                sessionId, profile, sessionSeed, state, clock, eventSink, clockMode,
                macroEngine.hash(), port, portRole, configMetadata);
        this.rebootTimer = new SessionRebootTimer(clock, events);
        this.scheduler = new SessionSchedulerBridge(sessionSeed, clock, events);
        this.commandExecutor = commandExecutor(commandRouter);
        events.publish(EventType.SESSION_START, Direction.INTERNAL, RawBytes.empty(), null, null, state, null);
    }
    @Override public synchronized SessionResponse submit(SessionCommand command) { return command.execute(this); }
    @Override public synchronized SessionResponse receive(RawBytes bytes) { return receiveTimed(bytes, clock.nowNanos(), clock.nowNanos()); }
    public synchronized SessionResponse receiveTimed(RawBytes bytes, long firstByteMonotonicNanos, long lastByteMonotonicNanos) {
        long byteSpanNanos = Math.max(0, lastByteMonotonicNanos - firstByteMonotonicNanos);
        long firstByteNanos = Math.max(clock.nowNanos(), firstByteMonotonicNanos);
        long lastByteNanos = firstByteNanos + byteSpanNanos;
        clock.advanceTo(firstByteNanos);
        if (pendingSms != null) { return receiveSmsEntry(bytes); }
        int start = eventCount();
        RawBytes output = RawBytes.empty();
        RawBytes effective = inputState.effectiveCommandLine(bytes);
        events.publishRx(bytes, effective, state);
        if (SessionFreezePolicy.ignoresRx(state)) { inputState.markDteRx(lastByteNanos); return response(RawBytes.empty(), start); }
        long idleBeforeRxNanos = inputState.idleBeforeRx(firstByteNanos);
        if (state.call().mode() == CallMode.ONLINE_DATA && pendingEscapeCommandState != null && !bytes.isEmpty()) {
            scheduler.cancelAll(state); pendingEscapeCommandState = null; inputState.clearPendingEscape();
        }
        if (state.call().mode() == CallMode.ONLINE_DATA
                && !inputState.onlineEscapeSatisfied(bytes, idleBeforeRxNanos, byteSpanNanos, state.settings())) {
            inputState.markDteRx(lastByteNanos);
            return response(RawBytes.empty(), start);
        }
        if (state.call().mode() == CallMode.ONLINE_DATA) {
            clock.advanceTo(lastByteNanos);
            inputState.clearPendingEscape();
            ModemState commandState = state.withCall(state.call().withMode(CallMode.ONLINE_COMMAND));
            RawBytes ok = commandState.settings().quiet() ? RawBytes.empty()
                    : new ResponseFormatter(commandState).result(commandState.settings().verbose() ? "OK" : "0", commandState.settings().verbose());
            RawBytes delayedOk = scheduleOrReturn("escape", ok, commandState.settings().s12());
            if (delayedOk.isEmpty() && commandState.settings().s12() > 0) {
                pendingEscapeCommandState = commandState;
            } else {
                state = commandState;
            }
            if (!delayedOk.isEmpty()) {
                events.publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, delayedOk, null, null, state, null);
            }
            inputState.markDteRx(lastByteNanos);
            return response(delayedOk, start);
        }
        if (state.settings().echo()) { output = output.append(bytes); }
        List<ParsedCommand> commands;
        try {
            commands = parseCommands(effective);
        } catch (com.jkamsker.modemsim.parser.AtParseException e) { return SessionParseFailure.response(profile.errorPolicy(), events, inputState, lastByteNanos, state, effective, output, start); }
        if (commands.isEmpty()) {
            inputState.rememberIncomplete(effective);
            inputState.markDteRx(lastByteNanos);
            return response(output, start);
        }
        if (inputState.repeatCommand()) {
            events.publishParsed(new ParsedCommand(bytes, "A/", "A/", CommandKind.SPECIAL_REPEAT, "", List.of("A/"), 0, SessionEntryMode.from(state.call().mode())), state);
        }
        inputState.rememberParsed(effective, state.settings().s3());
        return executeParsedCommands(commands, output, lastByteNanos, true, start);
    }
    private SessionResponse receiveSmsEntry(RawBytes bytes) {
        int start = eventCount();
        SessionBytes.SmsEntry entry = SessionBytes.smsEntry(pendingSmsBytes, bytes);
        if (!entry.rxBytes().isEmpty()) { events.publish(EventType.RX_BYTES, Direction.DTE_TO_DCE, entry.rxBytes(), null, null, state, null, true); }
        pendingSmsBytes = entry.buffered();
        if (!entry.complete()) { return response(RawBytes.empty(), start); }
        byte[] raw = pendingSmsBytes.toByteArray();
        SmsSubmitResult result;
        NetworkDelay macroDelay = null;
        String macroOperation = null;
        String body = SessionBytes.entryPayload(raw, entry.terminator());
        if (entry.abort()) {
            result = smsSubmitProcessor.abort(state);
        } else {
            MacroDecision decision = macroEngine.evaluateSms(pendingSms.destination(), body, state, profile);
            if (decision.matched()) {
                ModemState macroState = commandRouter.applyEffects(state.withCall(CallRuntime.command()), decision);
                publishMacroDecision(decision, state, macroState);
                RawBytes macroOutput = SessionFrameRenderer.render(decision.frames(), state);
                macroOperation = "macro-" + decision.macroId();
                macroDelay = decision.delay(macroOperation);
                result = new SmsSubmitResult(macroState, macroOutput, "MACRO");
            } else {
                result = smsSubmitProcessor.submit(pendingSms, body, clock.nowNanos(), state);
            }
        }
        pendingSmsBytes = RawBytes.empty();
        ModemState before = state;
        state = StateInvariants.normalize(result.state()); SessionFaultScheduler.cancelIfModemFault(before, state, scheduler, pendingMacroTransitions);
        pendingSms = null;
        RawBytes submitResponse = state.settings().quiet() ? RawBytes.empty() : result.response();
        RawBytes output = macroOperation == null
                ? scheduleOrReturn("sms-submit", submitResponse)
                : scheduleOrReturn(macroOperation, submitResponse, macroDelay);
        String handler = macroOperation == null ? "SmsSubmitProcessor" : "Macro:" + macroOperation.substring("macro-".length());
        events.publish(EventType.HANDLER_RESULT, Direction.INTERNAL, RawBytes.empty(), null, before, state,
                new CommandResult(state, List.of(), null, handler, false), true);
        if (!output.isEmpty()) { events.publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, output, null, null, state, null); }
        if (!entry.tailBytes().isEmpty()) { output = output.append(receive(entry.tailBytes()).output()); }
        return response(output, start);
    }
    @Override
    public synchronized SessionResponse advanceTime(long millis) { return scheduledResponse(() -> scheduler.advanceTime(millis, state)); }
    public synchronized SessionResponse advanceTo(long monotonicNanos) { return scheduledResponse(() -> scheduler.advanceTo(monotonicNanos, state)); }
    public synchronized SessionResponse drainScheduled() { return scheduledResponse(() -> scheduler.drainScheduled(state)); }
    @Override
    public synchronized ModemState snapshot() { return state; }
    public synchronized List<ModemEvent> events() { return events.eventsSince(0); }
    public synchronized SessionResponse injectDce(RawBytes bytes, String injectionType) {
        int start = eventCount();
        events.publishAudit(EventType.INJECTION, Direction.DCE_TO_DTE, injectionType, null, bytes, state, state);
        events.publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, bytes, null, null, state, null);
        return response(bytes, start);
    }
    public synchronized SessionResponse injectDte(RawBytes bytes, String injectionType) {
        int start = eventCount();
        events.publishAudit(EventType.INJECTION, Direction.DTE_TO_DCE, injectionType, null, bytes, state, state);
        SessionResponse processed = receive(bytes);
        return response(processed.output(), start);
    }
    public synchronized SessionResponse injectParsedCommand(RawBytes bytes, String injectionType) {
        int start = eventCount();
        events.publishAudit(EventType.INJECTION, Direction.INTERNAL, injectionType, null, bytes, state, state);
        List<ParsedCommand> commands;
        try {
            commands = parseCommands(bytes);
        } catch (com.jkamsker.modemsim.parser.AtParseException e) { return SessionParseFailure.response(profile.errorPolicy(), events, inputState, clock.nowNanos(), state, bytes, RawBytes.empty(), start); }
        return commands.isEmpty() ? response(RawBytes.empty(), start)
                : executeParsedCommands(commands, RawBytes.empty(), clock.nowNanos(), false, start);
    }
    public synchronized SessionResponse applyState(ModemState next, String injectionType) {
        return applyState(next, injectionType, EventType.STATE_CHANGE);
    }
    public synchronized SessionResponse applyFault(String type) { return applyState(faultService.apply(state, new com.jkamsker.modemsim.macros.FaultAction(type, type.equals("reboot") || type.equals("modem-reboot") ? 3000 : null, null, null, null, null)), null, type, EventType.FAULT_TRIGGERED); }
    public synchronized SessionResponse applyFault(com.jkamsker.modemsim.macros.FaultAction action) { return applyState(faultService.apply(state, action), null, action.type(), EventType.FAULT_TRIGGERED); }
    public synchronized SessionResponse fireTimer(String timerId) {
        int start = eventCount(); MacroDecision decision = macroEngine.evaluateTimer(timerId, state, profile);
        if (!decision.matched()) { return response(RawBytes.empty(), start); }
        ModemState before = state; CommandResult result = commandRouter.executeTimer(decision, state); pendingMacroTransitions.addAll(commandRouter.pendingDelayedTransitions()); state = StateInvariants.normalize(result.state());
        var stateMacro = SessionStateChangeMacros.run(macroEngine, commandRouter, profile, before, state); state = StateInvariants.normalize(stateMacro.state()); rebootTimer.arm(state); SessionFaultScheduler.cancelIfModemFault(before, state, scheduler, pendingMacroTransitions);
        RawBytes output = SessionFrameRenderer.render(result.frames(), state).append(stateChangeOutput(before, state)).append(stateMacro.output()); events.publish(EventType.HANDLER_RESULT, Direction.INTERNAL, RawBytes.empty(), null, before, state, result);
        if (!output.isEmpty()) { events.publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, output, null, null, state, null); }
        return response(output, start);
    }
    public synchronized SessionResponse portLost(String result) {
        int start = eventCount();
        ModemState before = state, next = state.withLines(state.lines().withDsr(false).withDcd(false));
        events.publishAudit(EventType.PORT_LOST, Direction.INTERNAL, null, result, RawBytes.empty(), before, next);
        state = next;
        scheduler.cancelAll(state);
        pendingMacroTransitions.clear();
        return response(RawBytes.empty(), start);
    }
    public synchronized SessionResponse stop(String result) {
        int start = eventCount();
        scheduler.cancelAll(state);
        pendingMacroTransitions.clear();
        diagnostic(EventType.SESSION_STOP, result);
        return response(RawBytes.empty(), start);
    }
    private SessionResponse applyState(ModemState next, String injectionType, EventType eventType) { return applyState(next, injectionType, null, eventType); }
    public synchronized SessionResponse diagnostic(EventType eventType, String result) { int start = eventCount(); events.publishAudit(eventType, Direction.INTERNAL, null, result, RawBytes.empty(), state, state); return response(RawBytes.empty(), start); }
    public synchronized SessionResponse diagnostic(EventType eventType, String result, String port, String portRole) { int start = eventCount(); events.publishAudit(eventType, Direction.INTERNAL, null, result, RawBytes.empty(), state, state, port, portRole); return response(RawBytes.empty(), start); }
    public synchronized SessionResponse replaceMacroEngine(MacroEngine next, String result) {
        int start = eventCount();
        events.publishAudit(EventType.INJECTION, Direction.INTERNAL, "macro-control", result, RawBytes.empty(), state, state);
        scheduler.cancelAll(state);
        pendingMacroTransitions.clear();
        events.replaceMacroHash(next.hash());
        macroEngine = next; commandRouter = commandRouter(next); commandExecutor = commandExecutor(commandRouter);
        return response(RawBytes.empty(), start);
    }
    private SessionResponse applyState(ModemState next, String injectionType, String result, EventType eventType) {
        int start = eventCount();
        ModemState before = state;
        next = StateInvariants.normalize(next);
        events.publishAudit(eventType, Direction.INTERNAL, injectionType, result, RawBytes.empty(), before, next);
        scheduler.cancelAll(next);
        pendingMacroTransitions.clear();
        pendingDialConnectedState = null;
        pendingEscapeCommandState = null;
        var macro = SessionStateChangeMacros.run(macroEngine, commandRouter, profile, before, next);
        state = StateInvariants.normalize(macro.state());
        rebootTimer.arm(state);
        RawBytes output = releaseHeldTx(before, state).append(stateChangeOutput(before, state)).append(macro.output());
        if (!output.isEmpty()) { events.publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, output, null, null, state, null); }
        return response(output, start);
    }
    private RawBytes scheduleOrReturn(String operation, RawBytes payload) { return SessionDelayScheduler.schedule(scheduler, operation, payload, state); }
    private RawBytes scheduleOrReturn(String operation, RawBytes payload, int delayMs) { return SessionDelayScheduler.schedule(scheduler, operation, payload, delayMs, state); }
    private RawBytes scheduleOrReturn(String operation, RawBytes payload, NetworkDelay delay) { return SessionDelayScheduler.schedule(scheduler, operation, payload, delay, state); }
    private ScheduledPayload scheduleMacro(String operation, RawBytes payload, NetworkDelay delay, ModemState source) { return SessionDelayScheduler.scheduleWithMetadata(scheduler, operation, payload, delay, source); }
    private RawBytes stateChangeOutput(ModemState before, ModemState after) { RawBytes urc = SessionStateChangeMacros.registrationUrc(before, after); return urc.isEmpty() ? RawBytes.empty() : scheduler.scheduleImmediate("state-urc", urc, after); }
    private RawBytes holdTxIfNeeded(RawBytes output) { if (!output.isEmpty() && SessionFreezePolicy.holdsTx(state)) { heldTx = heldTx.append(output); return RawBytes.empty(); } return output; }
    private RawBytes releaseHeldTx(ModemState before, ModemState after) { if (SessionFreezePolicy.releasesTx(before, after)) { RawBytes released = heldTx; heldTx = RawBytes.empty(); return released; } return RawBytes.empty(); }
    private void publishMacroDecision(MacroDecision decision, ModemState source, ModemState after) { events.publishMacroDecision(decision, source); events.publishMacroEvents(decision.events(), decision.macroId(), source); publishMacroAudit(decision, source, after); }
    private void publishMacroAudit(MacroDecision decision, ModemState source, ModemState after) {
        if ((decision.faults().isEmpty() && decision.statePatches().isEmpty()) || source.equals(after)) { return; }
        events.publishAudit(decision.faults().isEmpty() ? EventType.STATE_CHANGE : EventType.FAULT_TRIGGERED,
                Direction.INTERNAL, "macro-control", "macro-" + decision.macroId(), RawBytes.empty(), source, after);
    }
    private MacroCommandRouter commandRouter(MacroEngine engine) {
        return new MacroCommandRouter(profile, new DefaultCommandRouter(), engine, SessionFrameRenderer::render,
                this::scheduleMacro, this::publishMacroDecision);
    }
    private SessionCommandExecutor commandExecutor(MacroCommandRouter router) { return new SessionCommandExecutor(router, scheduler, events); }
    private List<ParsedCommand> parseCommands(RawBytes bytes) { return new AtCommandParser(state.settings().s5(), state.settings().s3(), profile.dialect().extendedPrefixes()).parse(bytes, SessionEntryMode.from(state.call().mode())); }
    private SessionResponse response(RawBytes output, int start) { return new SessionResponse(output, events.eventsSince(start)); }
    private int eventCount() { return events.eventCount(); }
    private SessionResponse executeParsedCommands(List<ParsedCommand> commands, RawBytes output, long lastByteNanos, boolean markRx, int start) {
        ModemState before = state; CommandExecutionResult execution = commandExecutor.execute(commands, state);
        state = StateInvariants.normalize(execution.state());
        var macro = SessionStateChangeMacros.run(macroEngine, commandRouter, profile, before, state); state = StateInvariants.normalize(macro.state());
        rebootTimer.arm(state); pendingSms = execution.pendingSms(); if (pendingSms != null) { pendingSmsBytes = RawBytes.empty(); }
        pendingDialConnectedState = execution.pendingDialConnectedState(); pendingMacroTransitions.addAll(execution.pendingMacroTransitions()); if (SessionFaultScheduler.cancelIfModemFault(before, state, scheduler, pendingMacroTransitions)) { pendingDialConnectedState = null; }
        output = holdTxIfNeeded(output.append(execution.output()).append(stateChangeOutput(before, state)).append(macro.output()));
        if (!output.isEmpty()) { events.publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, output, null, null, state, null); }
        if (markRx) { inputState.markDteRx(lastByteNanos); }
        return response(output, start);
    }
    private SessionResponse scheduledResponse(ScheduledAction action) {
        int start = eventCount();
        ScheduledBatch batch = action.run();
        RawBytes output = completePendingTransitions(batch.output(), batch);
        state = rebootTimer.completeIfDue(state);
        output = holdTxIfNeeded(output);
        if (!output.isEmpty()) { events.publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, output, null, null, state, null); }
        return response(output, start);
    }
    private RawBytes completePendingTransitions(RawBytes output, ScheduledBatch batch) {
        boolean scheduledEmission = !batch.emitted().isEmpty();
        if ((scheduledEmission || !output.isEmpty()) && pendingDialConnectedState != null) { state = pendingDialConnectedState; pendingDialConnectedState = null; }
        pendingMacroTransitions.removeCancelled(batch.cancelled());
        output = output.append(pendingMacroTransitions.commitFor(batch.emitted(), this::commitPendingMacroTransition));
        if ((scheduledEmission || !output.isEmpty()) && pendingEscapeCommandState != null) { state = pendingEscapeCommandState; pendingEscapeCommandState = null; }
        return output;
    }
    private RawBytes commitPendingMacroTransition(PendingMacroTransition transition) { ModemState before = state; ModemState next = StateInvariants.normalize(commandRouter.applyDelayedActions(state, transition)); events.publishAudit(transition.eventType(), Direction.INTERNAL, "macro-control", transition.result(), RawBytes.empty(), before, next); state = next; var macro = SessionStateChangeMacros.run(macroEngine, commandRouter, profile, before, state); state = StateInvariants.normalize(macro.state()); rebootTimer.arm(state); SessionFaultScheduler.cancelIfModemFault(before, state, scheduler, pendingMacroTransitions); return stateChangeOutput(before, state).append(macro.output()); }
}
