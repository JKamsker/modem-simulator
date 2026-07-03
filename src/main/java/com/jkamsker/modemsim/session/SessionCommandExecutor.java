package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.CommandResult;
import com.jkamsker.modemsim.commands.ResponseFormatter;
import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.CallRuntime;
import com.jkamsker.modemsim.state.ModemLifecycle;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.NetworkDelay;
import com.jkamsker.modemsim.state.SessionSettings;

import java.util.List;

final class SessionCommandExecutor {
    private final MacroCommandRouter commandRouter;
    private final SessionSchedulerBridge scheduler;
    private final SessionEventPublisher events;
    private final NvramStore nvramStore;
    private SessionSettings nvramSettings;

    SessionCommandExecutor(
            MacroCommandRouter commandRouter, SessionSchedulerBridge scheduler, SessionEventPublisher events) {
        this.commandRouter = commandRouter;
        this.scheduler = scheduler;
        this.events = events;
        this.nvramStore = NvramStore.forSession(events.sessionId());
    }

    CommandExecutionResult execute(List<ParsedCommand> commands, ModemState initialState) {
        ModemState state = initialState;
        if (nvramSettings == null) {
            nvramSettings = nvramStore.load().orElse(initialState.settings());
        }
        ModemState pendingDialState = null;
        java.util.ArrayList<PendingMacroTransition> pendingMacroTransitions = new java.util.ArrayList<>();
        PendingSms pendingSms = null;
        RawBytes output = RawBytes.empty();
        RawBytes lineOutput = RawBytes.empty();
        CommandResult lineResult = null;
        boolean lineDelayedDial = false;
        for (int i = 0; i < commands.size(); i++) {
            ParsedCommand command = withSessionContext(commands.get(i), state);
            if (command.commandIndexInLine() == 0 && lineResult != null) {
                output = output.append(finishCommandLine(lineOutput, lineResult, lineDelayedDial, state));
                lineOutput = RawBytes.empty();
                lineResult = null;
                lineDelayedDial = false;
            }
            events.publishParsed(command, state);
            ModemState before = state;
            long startedNanos = scheduler.nowNanos();
            CommandResult routed = commandRouter.route(command, state);
            pendingMacroTransitions.addAll(commandRouter.pendingDelayedTransitions());
            boolean delayedDial = command.normalizedName().equals("ATD")
                    && DialDelayPolicy.shouldDelay(routed, routed.state());
            CommandResult result = delayedDial ? delayedDialResult(command, before, routed) : routed;
            result = applySavedSettings(command, result);
            state = result.state();
            if (command.normalizedName().equals("AT&W")) {
                nvramSettings = state.settings();
                nvramStore.save(nvramSettings);
            }
            if (delayedDial) {
                pendingDialState = routed.state();
            }
            if (!state.settings().quiet()) {
                lineOutput = lineOutput.append(SessionFrameRenderer.render(result.frames(), state));
            }
            if (command.normalizedName().equals("+CMGS") && result.finalResult() == null) {
                pendingSms = PendingSms.from(command, state.sms().textMode());
            }
            events.publishMeasured(
                    EventType.HANDLER_RESULT, Direction.INTERNAL, RawBytes.empty(),
                    command, before, state, result, startedNanos);
            if (isUnknownRestart(result)) {
                events.publishAudit(EventType.FAULT_TRIGGERED, Direction.INTERNAL,
                        "unknown-at-command-policy", "restart", RawBytes.empty(), before, state);
            }
            lineResult = result;
            lineDelayedDial = delayedDial;
            if (result.stopLine()) {
                while (i + 1 < commands.size() && commands.get(i + 1).commandIndexInLine() > 0) {
                    i++;
                }
            }
        }
        output = output.append(finishCommandLine(lineOutput, lineResult, lineDelayedDial, state));
        return new CommandExecutionResult(state, output, pendingSms, pendingDialState, List.copyOf(pendingMacroTransitions));
    }

    private CommandResult delayedDialResult(ParsedCommand command, ModemState before, CommandResult routed) {
        ModemState dialing = before
                .withCall(new CallRuntime(CallMode.DIALING, false, command.arguments(), null))
                .withLines(before.lines().withDcd(false));
        return new CommandResult(dialing, routed.frames(), routed.finalResult(), routed.handler(), routed.stopLine());
    }

    private RawBytes finishCommandLine(
            RawBytes lineOutput, CommandResult result, boolean delayedDial, ModemState state) {
        RawBytes output = lineOutput;
        if (result != null && result.finalResult() != null && !state.settings().quiet()) {
            output = output.append(new ResponseFormatter(state)
                    .result(result.finalResult().text(state.settings().verbose()), state.settings().verbose()));
        }
        return delayedDial ? scheduleOrReturn("dial", output, state) : output;
    }

    private RawBytes scheduleOrReturn(String operation, RawBytes payload, ModemState state) {
        NetworkDelay delay = state.network() == null ? null : state.network().delays().get(operation);
        return scheduler.scheduleOrReturn(operation, payload, delay, state);
    }

    private CommandResult applySavedSettings(ParsedCommand command, CommandResult result) {
        if (!command.normalizedName().equals("ATZ")
                || commandRouter.profile().dialect().resetPolicy()
                != com.jkamsker.modemsim.profiles.ResetPolicy.NVRAM_ON_ATZ) {
            return result;
        }
        return new CommandResult(result.state().withSettings(nvramSettings),
                result.frames(), result.finalResult(), result.handler(), result.stopLine());
    }

    private boolean isUnknownRestart(CommandResult result) {
        return "UnknownPolicy".equals(result.handler())
                && result.state().modem().lifecycle() == ModemLifecycle.REBOOTING;
    }

    private ParsedCommand withSessionContext(ParsedCommand command, ModemState state) {
        if (command.normalizedName().equals("+CMGS") && state.sms() != null && !state.sms().textMode()) {
            return command.withPduContext(true);
        }
        return command;
    }
}
