package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.CommandResult;
import com.jkamsker.modemsim.commands.DefaultCommandRouter;
import com.jkamsker.modemsim.commands.RawFrame;
import com.jkamsker.modemsim.commands.ResponseFrame;
import com.jkamsker.modemsim.commands.TextFrame;
import com.jkamsker.modemsim.macros.MacroAction;
import com.jkamsker.modemsim.macros.FaultService;
import com.jkamsker.modemsim.macros.MacroDecision;
import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.macros.MacroLoader;
import com.jkamsker.modemsim.macros.MacroPhase;
import com.jkamsker.modemsim.macros.MacroStateMutator;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.NetworkDelay;

import java.util.List;

final class MacroCommandRouter {
    private final Profile profile;
    private final DefaultCommandRouter router;
    private final MacroEngine macroEngine;
    private final FaultService faultService = new FaultService();
    private final MacroStateMutator stateMutator = new MacroStateMutator();
    private final FrameRenderer renderer;
    private final MacroScheduler scheduler;
    private final DecisionSink decisionSink;
    private PendingMacroTransition pendingDelayedTransition;

    MacroCommandRouter(
            Profile profile,
            DefaultCommandRouter router,
            MacroEngine macroEngine,
            FrameRenderer renderer,
            MacroScheduler scheduler,
            DecisionSink decisionSink) {
        this.profile = profile;
        this.router = router;
        this.macroEngine = macroEngine;
        this.renderer = renderer;
        this.scheduler = scheduler;
        this.decisionSink = decisionSink;
    }

    CommandResult route(ParsedCommand command, ModemState state) {
        pendingDelayedTransition = null;
        CommandResult result = null;
        CommandResult before = commandMacro(command, state, MacroPhase.BEFORE, false);
        if (before != null) {
            state = before.state();
            result = before;
        }
        MacroDecision replace = macroEngine.evaluateCommand(command, state, profile, MacroPhase.REPLACE);
        CommandResult main = replace.matched() ? executeMacro(replace, state, true) : router.route(profile, state, command);
        state = main.state();
        result = result == null ? main : appendUsingExtraResult(result, main);
        if (!replace.matched()) {
            CommandResult after = commandMacro(command, state, MacroPhase.AFTER, false);
            if (after != null) {
                result = appendKeepingCurrentResult(result, after);
            }
        }
        return result;
    }

    com.jkamsker.modemsim.profiles.Profile profile() {
        return profile;
    }

    PendingMacroTransition pendingDelayedTransition(PendingMacroTransition fallback) {
        PendingMacroTransition result = pendingDelayedTransition;
        pendingDelayedTransition = null;
        return result == null ? fallback : result;
    }

    private CommandResult commandMacro(
            ParsedCommand command, ModemState state, MacroPhase phase, boolean stopLine) {
        MacroDecision decision = macroEngine.evaluateCommand(command, state, profile, phase);
        return decision.matched() ? executeMacro(decision, state, stopLine) : null;
    }

    private CommandResult executeMacro(MacroDecision decision, ModemState state, boolean stopLine) {
        decisionSink.publish(decision, state);
        OrderedMacroResult ordered = ordered(decision, state);
        ModemState next = ordered.immediateState();
        pendingDelayedTransition = ordered.pendingState() == null ? null
                : new PendingMacroTransition(ordered.pendingState(), eventType(decision), "macro-" + decision.macroId());
        RawBytes output = renderer.render(ordered.immediateFrames(), next);
        RawBytes delayed = renderer.render(ordered.delayedFrames(), pendingDelayedTransition == null ? next : pendingDelayedTransition.state());
        String operation = "macro-" + decision.macroId();
        RawBytes effective = output.append(scheduler.schedule(operation, delayed, decision.delay(operation), next));
        return new CommandResult(next, List.of(new RawFrame(effective)), null, "Macro:" + decision.macroId(), stopLine);
    }

    private OrderedMacroResult ordered(MacroDecision decision, ModemState state) {
        ModemState immediate = state;
        ModemState pending = state;
        boolean delayed = false;
        var immediateFrames = new java.util.ArrayList<ResponseFrame>();
        var delayedFrames = new java.util.ArrayList<ResponseFrame>();
        for (MacroAction action : decision.actions()) {
            delayed = delayed || action.type().equals("delay");
            if (action.type().equals("fault")) {
                pending = faultService.apply(pending, MacroLoader.fault(action));
                if (!delayed) { immediate = pending; }
            } else if (action.type().equals("set")) {
                pending = stateMutator.apply(pending, List.of(MacroLoader.statePatch(action)));
                if (!delayed) { immediate = pending; }
            } else if (action.type().equals("emit") || action.type().equals("send")) {
                (delayed ? delayedFrames : immediateFrames).add(frame(action));
            }
        }
        return new OrderedMacroResult(immediate, delayed ? pending : null, immediateFrames, delayedFrames);
    }

    private ResponseFrame frame(MacroAction action) {
        if (action.attr("rawHex") != null) { return new RawFrame(RawBytes.hex(action.attr("rawHex"))); }
        if (action.attr("raw") != null) { return new RawFrame(RawBytes.ascii(action.attr("raw"))); }
        if (action.attr("text") != null) { return new RawFrame(RawBytes.ascii(tokens(action.attr("text")))); }
        if (action.attr("line") != null) { return new TextFrame(action.attr("line")); }
        return new TextFrame(action.text());
    }

    private String tokens(String text) {
        return text.replace("<CRLF>", "\r\n").replace("<CR>", "\r")
                .replace("<LF>", "\n").replace("<ESC>", "\u001B").replace("<CTRL-Z>", "\u001A");
    }

    CommandResult executeStateChange(MacroDecision decision, ModemState state) {
        return executeMacro(decision, state, false);
    }

    CommandResult executeTimer(MacroDecision decision, ModemState state) {
        pendingDelayedTransition = null;
        return executeMacro(decision, state, false);
    }

    ModemState applyFaults(ModemState source, MacroDecision decision) {
        ModemState next = source;
        for (var fault : decision.faults()) {
            next = faultService.apply(next, fault);
        }
        return next;
    }

    ModemState applyEffects(ModemState source, MacroDecision decision) {
        return stateMutator.apply(applyFaults(source, decision), decision.statePatches());
    }

    private CommandResult appendUsingExtraResult(CommandResult current, CommandResult extra) {
        var frames = new java.util.ArrayList<ResponseFrame>(current.frames());
        frames.addAll(extra.frames());
        return new CommandResult(extra.state(), frames, extra.finalResult(),
                current.handler() + "+" + extra.handler(), extra.stopLine());
    }

    private CommandResult appendKeepingCurrentResult(CommandResult current, CommandResult extra) {
        var frames = new java.util.ArrayList<ResponseFrame>(current.frames());
        frames.addAll(extra.frames());
        return new CommandResult(extra.state(), frames, current.finalResult(),
                current.handler() + "+" + extra.handler(), current.stopLine());
    }

    private EventType eventType(MacroDecision decision) {
        return decision.faults().isEmpty() ? EventType.STATE_CHANGE : EventType.FAULT_TRIGGERED;
    }

    interface FrameRenderer {
        RawBytes render(List<ResponseFrame> frames, ModemState state);
    }

    interface MacroScheduler {
        RawBytes schedule(String operation, RawBytes payload, NetworkDelay delay, ModemState state);
    }

    interface DecisionSink {
        void publish(MacroDecision decision, ModemState state);
    }

    private record OrderedMacroResult(
            ModemState immediateState, ModemState pendingState,
            List<ResponseFrame> immediateFrames, List<ResponseFrame> delayedFrames) {
    }
}
