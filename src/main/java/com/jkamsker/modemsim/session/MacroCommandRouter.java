package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.CommandResult;
import com.jkamsker.modemsim.commands.DefaultCommandRouter;
import com.jkamsker.modemsim.commands.RawFrame;
import com.jkamsker.modemsim.commands.ResponseFrame;
import com.jkamsker.modemsim.macros.FaultService;
import com.jkamsker.modemsim.macros.MacroDecision;
import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.macros.MacroPhase;
import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.state.ModemState;

import java.util.List;

final class MacroCommandRouter {
    private final Profile profile;
    private final DefaultCommandRouter router;
    private final MacroEngine macroEngine;
    private final FaultService faultService = new FaultService();
    private final FrameRenderer renderer;
    private final MacroScheduler scheduler;

    MacroCommandRouter(
            Profile profile,
            DefaultCommandRouter router,
            MacroEngine macroEngine,
            FrameRenderer renderer,
            MacroScheduler scheduler) {
        this.profile = profile;
        this.router = router;
        this.macroEngine = macroEngine;
        this.renderer = renderer;
        this.scheduler = scheduler;
    }

    CommandResult route(ParsedCommand command, ModemState state) {
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

    private CommandResult commandMacro(
            ParsedCommand command, ModemState state, MacroPhase phase, boolean stopLine) {
        MacroDecision decision = macroEngine.evaluateCommand(command, state, profile, phase);
        return decision.matched() ? executeMacro(decision, state, stopLine) : null;
    }

    private CommandResult executeMacro(MacroDecision decision, ModemState state, boolean stopLine) {
        ModemState next = applyFaults(state, decision);
        RawBytes output = renderer.render(decision.frames(), state);
        RawBytes effective = scheduler.schedule("macro-" + decision.macroId(), output, decision.delayMs(), next);
        return new CommandResult(next, List.of(new RawFrame(effective)), null, "Macro:" + decision.macroId(), stopLine);
    }

    ModemState applyFaults(ModemState source, MacroDecision decision) {
        ModemState next = source;
        for (var fault : decision.faults()) {
            next = faultService.apply(next, fault);
        }
        return next;
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

    interface FrameRenderer {
        RawBytes render(List<ResponseFrame> frames, ModemState state);
    }

    interface MacroScheduler {
        RawBytes schedule(String operation, RawBytes payload, int delayMs, ModemState state);
    }
}
