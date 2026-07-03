package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.commands.RawFrame;
import com.jkamsker.modemsim.commands.ResponseFrame;
import com.jkamsker.modemsim.commands.TextFrame;
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
            if (rule.enabled() && rule.phase() == phase && rule.match().matchesCommand(command)) {
                return decision(rule);
            }
        }
        return MacroDecision.none();
    }

    public MacroDecision evaluateSms(String destination, String body, ModemState state, Profile profile) {
        for (MacroRule rule : macroSet.rules()) {
            if (rule.enabled() && rule.phase() == MacroPhase.REPLACE && rule.match().matchesSms(destination, body)) {
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
        int delay = 0;
        for (MacroAction action : rule.actions()) {
            switch (action.type()) {
                case "delay" -> delay += integer(action.attr("ms"));
                case "emit" -> addEmit(frames, action);
                case "send" -> addSend(frames, action);
                case "fault" -> faults.add(MacroLoader.fault(action));
                default -> {
                }
            }
        }
        return new MacroDecision(true, rule.id(), delay, frames, faults);
    }

    private void addEmit(List<ResponseFrame> frames, MacroAction action) {
        if (action.attr("line") != null) {
            frames.add(new TextFrame(action.attr("line")));
        } else if (action.attr("rawHex") != null) {
            frames.add(new RawFrame(RawBytes.hex(action.attr("rawHex"))));
        } else if (action.attr("raw") != null) {
            frames.add(new RawFrame(RawBytes.ascii(action.attr("raw"))));
        } else if (action.text() != null && !action.text().isBlank()) {
            frames.add(new TextFrame(action.text()));
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

    private String tokens(String text) {
        return text.replace("<CRLF>", "\r\n")
                .replace("<CR>", "\r")
                .replace("<LF>", "\n")
                .replace("<ESC>", "\u001B")
                .replace("<CTRL-Z>", "\u001A");
    }

    private int integer(String value) {
        return value == null ? 0 : Integer.parseInt(value);
    }
}
