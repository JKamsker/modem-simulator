package com.alegs3.modemsim.commands;

import com.alegs3.modemsim.parser.CommandKind;
import com.alegs3.modemsim.parser.ParsedCommand;
import com.alegs3.modemsim.profiles.Profile;
import com.alegs3.modemsim.state.CallMode;
import com.alegs3.modemsim.state.CallRuntime;
import com.alegs3.modemsim.state.ModemState;
import com.alegs3.modemsim.state.SessionSettings;

import java.util.List;

public final class HayesHandler implements CommandHandler {
    @Override
    public CommandResult handle(Profile profile, ModemState state, ParsedCommand command) {
        if (command.kind() == CommandKind.SPECIAL_ESCAPE) {
            return escapeToOnlineCommandMode(state);
        }
        if (command.kind() == CommandKind.S_REGISTER_READ) {
            return readRegister(state, command);
        }
        if (command.kind() == CommandKind.S_REGISTER_WRITE) {
            return writeRegister(state, command);
        }
        return switch (command.normalizedName()) {
            case "AT" -> CommandResult.ok(state, "HayesHandler");
            case "ATE" -> CommandResult.ok(state.withSettings(state.settings().withEcho(flag(command))), "HayesHandler");
            case "ATQ" -> CommandResult.ok(state.withSettings(state.settings().withQuiet(flag(command))), "HayesHandler");
            case "ATV" -> CommandResult.ok(state.withSettings(state.settings().withVerbose(flag(command))), "HayesHandler");
            case "ATI" -> identity(profile, state);
            case "ATZ", "AT&F" -> CommandResult.ok(resetVolatile(state), "HayesHandler");
            case "AT&W" -> CommandResult.ok(state, "HayesHandler");
            case "AT&V" -> viewConfig(state);
            case "ATD" -> dial(state, command.arguments());
            case "ATH" -> hangup(state);
            case "ATO" -> online(state);
            default -> null;
        };
    }

    private CommandResult identity(Profile profile, ModemState state) {
        String revision = profile.identity().revision() == null ? "" : " " + profile.identity().revision();
        return new CommandResult(
                state,
                List.of(new TextFrame(profile.identity().manufacturer() + " " + profile.identity().model() + revision)),
                ResultCode.OK,
                "HayesHandler",
                false);
    }

    private CommandResult viewConfig(ModemState state) {
        SessionSettings settings = state.settings();
        return new CommandResult(
                state,
                List.of(
                        new TextFrame("E" + (settings.echo() ? 1 : 0)
                                + " Q" + (settings.quiet() ? 1 : 0)
                                + " V" + (settings.verbose() ? 1 : 0)),
                        new TextFrame("S3=" + settings.s3() + " S4=" + settings.s4()
                                + " S5=" + settings.s5() + " S7=" + settings.s7())),
                ResultCode.OK,
                "HayesHandler",
                false);
    }

    private CommandResult dial(ModemState state, String number) {
        ModemState connected = state
                .withCall(state.call().connected(number))
                .withLines(state.lines().withDcd(true));
        return new CommandResult(connected, List.of(), ResultCode.CONNECT, "HayesHandler", false);
    }

    private CommandResult hangup(ModemState state) {
        ModemState disconnected = state
                .withCall(state.call().disconnected())
                .withLines(state.lines().withDcd(false));
        return CommandResult.ok(disconnected, "HayesHandler");
    }

    private CommandResult online(ModemState state) {
        if (!state.call().carrier()) {
            return new CommandResult(state, List.of(), ResultCode.NO_CARRIER, "HayesHandler", true);
        }
        return new CommandResult(state.withCall(state.call().withMode(CallMode.ONLINE_DATA)),
                List.of(), ResultCode.CONNECT, "HayesHandler", false);
    }

    private CommandResult escapeToOnlineCommandMode(ModemState state) {
        if (!state.call().carrier()) {
            return null;
        }
        return CommandResult.ok(state.withCall(state.call().withMode(CallMode.ONLINE_COMMAND)), "HayesHandler");
    }

    private CommandResult readRegister(ModemState state, ParsedCommand command) {
        int register = register(command);
        return new CommandResult(
                state,
                List.of(new TextFrame(Integer.toString(state.settings().register(register)))),
                ResultCode.OK,
                "HayesHandler",
                false);
    }

    private CommandResult writeRegister(ModemState state, ParsedCommand command) {
        int register = register(command);
        int value = parseNumber(command.arguments(), 0);
        return CommandResult.ok(state.withSettings(state.settings().withRegister(register, value)), "HayesHandler");
    }

    private ModemState resetVolatile(ModemState state) {
        return state
                .withSettings(SessionSettings.defaults())
                .withCall(CallRuntime.command())
                .withLines(state.lines().withDcd(false));
    }

    private boolean flag(ParsedCommand command) {
        return parseNumber(command.arguments(), 1) != 0;
    }

    private int register(ParsedCommand command) {
        return Integer.parseInt(command.normalizedName().substring(1));
    }

    private int parseNumber(String value, int fallback) {
        return value == null || value.isBlank() ? fallback : Integer.parseInt(value);
    }
}
