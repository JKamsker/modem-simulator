package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.parser.CommandKind;
import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.profiles.ProfileRegister;
import com.jkamsker.modemsim.profiles.ProfileRegisterCatalog;
import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.CallRuntime;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.SessionSettings;
import com.jkamsker.modemsim.state.SimState;

import java.util.List;

public final class HayesHandler implements CommandHandler {
    @Override
    public CommandResult handle(Profile profile, ModemState state, ParsedCommand command) {
        if (command.kind() == CommandKind.SPECIAL_ESCAPE) {
            return escapeToOnlineCommandMode(state);
        }
        if (command.kind() == CommandKind.S_REGISTER_READ) {
            return readRegister(profile, state, command);
        }
        if (command.kind() == CommandKind.S_REGISTER_WRITE) {
            return writeRegister(profile, state, command);
        }
        return switch (command.normalizedName()) {
            case "AT" -> CommandResult.ok(state, "HayesHandler");
            case "ATE" -> setEcho(state, command);
            case "ATQ" -> setQuiet(state, command);
            case "ATV" -> setVerbose(state, command);
            case "ATI" -> identity(profile, state);
            case "ATZ", "AT&F" -> CommandResult.ok(resetVolatile(profile, state), "HayesHandler");
            case "AT&W" -> CommandResult.ok(state, "HayesHandler");
            case "AT&V" -> viewConfig(state);
            case "AT&D" -> setDtrPolicy(state, command.arguments());
            case "AT&C" -> setDcdPolicy(state, command.arguments());
            case "ATD" -> dial(profile, state, command.arguments());
            case "ATA" -> answer(state);
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
                new TextFrame("S0=" + settings.s0() + " S2=" + settings.s2()
                        + " S6=" + settings.s6() + " S8=" + settings.s8()),
                new TextFrame("S3=" + settings.s3() + " S4=" + settings.s4()
                        + " S5=" + settings.s5() + " S7=" + settings.s7()
                        + " &D" + settings.ampD() + " &C" + settings.ampC())),
                ResultCode.OK,
                "HayesHandler",
                false);
    }

    private CommandResult dial(Profile profile, ModemState state, String number) {
        if (number == null || number.isBlank()) {
            return CommandResult.error(state, "HayesHandler");
        }
        if (mobileProfile(profile) && (state.sim().state() != SimState.READY
                || state.network() == null || !state.network().registeredForCircuitServices())) {
            return new CommandResult(state, List.of(), ResultCode.NO_CARRIER, "HayesHandler", true);
        }
        ModemState connected = state
                .withCall(state.call().connected(number))
                .withLines(state.lines().withDcd(true));
        return new CommandResult(connected, List.of(), ResultCode.CONNECT, "HayesHandler", false);
    }

    private CommandResult answer(ModemState state) {
        if (state.call().mode() != CallMode.RINGING) {
            return new CommandResult(state, List.of(), ResultCode.NO_CARRIER, "HayesHandler", true);
        }
        ModemState connected = state
                .withCall(state.call().answerIncoming())
                .withLines(state.lines().withDcd(true).withRi(false));
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

    private CommandResult setDtrPolicy(ModemState state, String value) {
        Integer mode = value == null || value.isBlank() ? 0 : parseNumber(value);
        if (mode == null || mode < 0 || mode > 3) {
            return CommandResult.error(state, "HayesHandler");
        }
        return CommandResult.ok(state.withSettings(state.settings().withAmpD(mode)), "HayesHandler");
    }

    private CommandResult setDcdPolicy(ModemState state, String value) {
        Integer mode = value == null || value.isBlank() ? 0 : parseNumber(value);
        if (mode == null || mode < 0 || mode > 1) {
            return CommandResult.error(state, "HayesHandler");
        }
        ModemState next = state.withSettings(state.settings().withAmpC(mode));
        return CommandResult.ok(next.withLines(next.lines().withDcd(mode == 0 || next.call().carrier())), "HayesHandler");
    }

    private boolean mobileProfile(Profile profile) {
        return profile.profileKind().equals("cellular") || profile.profileKind().equals("hybrid")
                || profile.id().equals("3gpp-27007-r18") || profile.id().equals("3gpp-27005-r16");
    }

    private CommandResult escapeToOnlineCommandMode(ModemState state) {
        if (!state.call().carrier()) {
            return null;
        }
        return CommandResult.ok(state.withCall(state.call().withMode(CallMode.ONLINE_COMMAND)), "HayesHandler");
    }

    private CommandResult readRegister(Profile profile, ModemState state, ParsedCommand command) {
        int register = register(command);
        if (ProfileRegisterCatalog.find(profile, register) == null) {
            return CommandResult.invalidParameter(state, "HayesHandler");
        }
        return new CommandResult(
                state,
                List.of(new TextFrame(Integer.toString(state.settings().register(register)))),
                ResultCode.OK,
                "HayesHandler",
                false);
    }

    private CommandResult writeRegister(Profile profile, ModemState state, ParsedCommand command) {
        int register = register(command);
        ProfileRegister spec = ProfileRegisterCatalog.find(profile, register);
        Integer value = parseNumber(command.arguments());
        if (spec == null || value == null || !spec.writable()) {
            return CommandResult.invalidParameter(state, "HayesHandler");
        }
        if (!ProfileRegisterCatalog.accepts(spec, value)) {
            return CommandResult.invalidParameter(state, "HayesHandler");
        }
        return CommandResult.ok(state.withSettings(state.settings().withRegister(register, value)), "HayesHandler");
    }

    private ModemState resetVolatile(Profile profile, ModemState state) {
        SessionSettings settings = ProfileRegisterCatalog.applyDefaults(SessionSettings.defaults(), profile.registers())
                .withEcho(profile.dialect().defaultEcho())
                .withQuiet(profile.dialect().defaultQuiet())
                .withVerbose(profile.dialect().defaultVerbose())
                .withRegister(3, profile.dialect().commandTerminator())
                .withRegister(4, profile.dialect().responseTerminator());
        return state
                .withSettings(settings)
                .withCall(CallRuntime.command())
                .withLines(state.lines().withDcd(false));
    }

    private CommandResult setEcho(ModemState state, ParsedCommand command) {
        Boolean value = flag(command);
        return value == null ? CommandResult.error(state, "HayesHandler")
                : CommandResult.ok(state.withSettings(state.settings().withEcho(value)), "HayesHandler");
    }

    private CommandResult setQuiet(ModemState state, ParsedCommand command) {
        Boolean value = flag(command);
        return value == null ? CommandResult.error(state, "HayesHandler")
                : CommandResult.ok(state.withSettings(state.settings().withQuiet(value)), "HayesHandler");
    }

    private CommandResult setVerbose(ModemState state, ParsedCommand command) {
        Boolean value = flag(command);
        return value == null ? CommandResult.error(state, "HayesHandler")
                : CommandResult.ok(state.withSettings(state.settings().withVerbose(value)), "HayesHandler");
    }

    private Boolean flag(ParsedCommand command) {
        Integer value = command.arguments() == null || command.arguments().isBlank()
                ? 1 : parseNumber(command.arguments());
        if (value == null) {
            return null;
        }
        return value == 0 ? Boolean.FALSE : value == 1 ? Boolean.TRUE : null;
    }

    private int register(ParsedCommand command) {
        Integer value = parseNumber(command.normalizedName().substring(1));
        return value == null ? -1 : value;
    }

    private Integer parseNumber(String value) {
        if (value == null || !value.matches("[0-9]+")) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
