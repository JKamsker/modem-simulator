package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.commands.RawFrame;
import com.jkamsker.modemsim.commands.ResponseFrame;
import com.jkamsker.modemsim.commands.TextFrame;
import com.jkamsker.modemsim.commands.LineEnding;
import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.state.ModemState;

import java.util.ArrayList;
import java.util.List;

public final class MacroEngine {
    private final MacroSet macroSet;

    public MacroEngine(MacroSet macroSet) {
        this.macroSet = macroSet;
    }

    public static MacroEngine empty() {
        return new MacroEngine(MacroSet.empty());
    }

    public MacroDecision evaluateCommand(ParsedCommand command, ModemState state, Profile profile) {
        return evaluateCommand(command, state, profile, MacroPhase.REPLACE);
    }

    public MacroDecision evaluateCommand(
            ParsedCommand command, ModemState state, Profile profile, MacroPhase phase) {
        for (MacroRule rule : macroSet.rules()) {
            if (matches(rule, command, state, profile, phase)) {
                return decision(rule);
            }
        }
        return MacroDecision.none();
    }

    public MacroDecision evaluateSms(String destination, String body, ModemState state, Profile profile) {
        for (MacroRule rule : macroSet.rules()) {
            if (rule.enabled() && rule.phase() == MacroPhase.REPLACE
                    && rule.condition().matches(state, profile) && rule.match().matchesSms(destination, body)) {
                return decision(rule);
            }
        }
        return MacroDecision.none();
    }

    public MacroDecision evaluateStateChange(ModemState before, ModemState after, Profile profile) {
        for (MacroRule rule : macroSet.rules()) {
            if (rule.enabled() && rule.phase() == MacroPhase.ON_STATE_CHANGE
                    && "state-change".equals(rule.match().type())
                    && rule.condition().matches(after, profile)) {
                return decision(rule);
            }
        }
        return MacroDecision.none();
    }

    public MacroDecision evaluateTimer(String timerId, ModemState state, Profile profile) {
        for (MacroRule rule : macroSet.rules()) {
            if (rule.enabled() && rule.phase() == MacroPhase.ON_TIMER
                    && rule.match().matchesTimer(timerId) && rule.condition().matches(state, profile)) {
                return decision(rule);
            }
        }
        return MacroDecision.none();
    }

    public String hash() {
        return macroSet.hash();
    }

    private MacroDecision decision(MacroRule rule) {
        List<ResponseFrame> frames = new ArrayList<>();
        List<FaultAction> faults = new ArrayList<>();
        List<MacroStatePatch> patches = new ArrayList<>();
        List<MacroEventAction> events = new ArrayList<>();
        int minDelay = 0;
        int maxDelay = 0;
        for (MacroAction action : rule.actions()) {
            switch (action.type()) {
                case "delay" -> {
                    int base = integer(action.attr("ms"));
                    minDelay += base;
                    maxDelay += base + integer(action.attr("jitterMs"));
                }
                case "emit" -> addEmit(frames, action);
                case "send" -> addSend(frames, action);
                case "fault" -> faults.add(MacroLoader.fault(action));
                case "set" -> patches.add(MacroLoader.statePatch(action));
                case "event" -> events.add(MacroLoader.event(action));
                default -> {
                }
            }
        }
        return new MacroDecision(
                true, rule.id(), macroSet.randomSeed(), minDelay, maxDelay, frames, faults, patches, events, rule.actions());
    }

    private boolean matches(
            MacroRule rule, ParsedCommand command, ModemState state, Profile profile, MacroPhase phase) {
        return rule.enabled()
                && rule.phase() == phase
                && rule.condition().matches(state, profile)
                && rule.match().matchesCommand(command);
    }

    private void addEmit(List<ResponseFrame> frames, MacroAction action) {
        if (action.attr("line") != null) {
            frames.add(new TextFrame(action.attr("line"), lineEnding(action)));
        } else if (action.attr("rawHex") != null) {
            frames.add(new RawFrame(RawBytes.hex(action.attr("rawHex"))));
        } else if (action.attr("raw") != null) {
            frames.add(new RawFrame(RawBytes.ascii(action.attr("raw"))));
        } else if (action.text() != null && !action.text().isBlank()) {
            frames.add(new TextFrame(action.text(), lineEnding(action)));
        }
    }

    private void addSend(List<ResponseFrame> frames, MacroAction action) {
        if (action.attr("rawHex") != null) {
            frames.add(new RawFrame(RawBytes.hex(action.attr("rawHex"))));
        } else if (action.attr("line") != null) {
            frames.add(new TextFrame(action.attr("line")));
        } else if (action.attr("text") != null) {
            frames.add(new RawFrame(RawBytes.ascii(tokens(action.attr("text")))));
        }
    }

    private LineEnding lineEnding(MacroAction action) {
        return LineEnding.from(action.attr("lineEnding"));
    }

    private String tokens(String text) {
        return text.replace("<CRLF>", "\r\n")
                .replace("<CR>", "\r")
                .replace("<LF>", "\n")
                .replace("<ESC>", "\u001B")
                .replace("<CTRL-Z>", "\u001A");
    }

    private int integer(String value) {
        if (value == null) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid macro delay value: " + value, e);
        }
    }
}
