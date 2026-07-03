package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.profiles.ProfileCommand;
import com.jkamsker.modemsim.profiles.UnknownAtCommandPolicy;
import com.jkamsker.modemsim.macros.FaultAction;
import com.jkamsker.modemsim.macros.FaultService;
import com.jkamsker.modemsim.state.FreezeMode;
import com.jkamsker.modemsim.state.ModemLifecycle;
import com.jkamsker.modemsim.state.ModemState;

import java.util.List;

public final class DefaultCommandRouter {
    private final List<CommandHandler> handlers = List.of(new HayesHandler(), new CellularHandler(), new SmsHandler());
    private final FaultService faults = new FaultService();

    public CommandResult route(Profile profile, ModemState state, ParsedCommand command) {
        if (state.modem().lifecycle() == ModemLifecycle.REBOOTING || noResponseFreeze(state)) {
            return new CommandResult(state, List.of(), null, "FreezePolicy", true);
        }
        if (command.normalizedName().equals("PARSE_ERROR")) {
            return new CommandResult(state, List.of(), ResultCode.ERROR, "AtCommandParser", true);
        }
        ProfileCommand declaration = declaration(profile, command);
        if (declaration == null || !executable(declaration)) {
            return unknown(profile, state);
        }
        for (CommandHandler handler : handlers) {
            if (!handlerAllowed(profile, handler) || !handlerMatches(declaration, handler)) {
                continue;
            }
            CommandResult result = handler.handle(profile, state, command);
            if (result != null) {
                return result;
            }
        }
        return unknown(profile, state);
    }

    private CommandResult unknown(Profile profile, ModemState state) {
        UnknownAtCommandPolicy policy = profile.dialect().unknownPolicy();
        return switch (policy) {
            case OK -> CommandResult.ok(state, "UnknownPolicy");
            case ERR -> new CommandResult(state, List.of(), ResultCode.ERR, "UnknownPolicy", true);
            case ERROR -> CommandResult.error(state, "UnknownPolicy");
            case RESTART -> new CommandResult(
                    faults.apply(state, new FaultAction("modem-reboot", null, null, null, null, null)),
                    List.of(),
                null,
                "UnknownPolicy",
                true);
        };
    }

    private boolean handlerAllowed(Profile profile, CommandHandler handler) {
        if (handler instanceof CellularHandler) {
            return mobileProfile(profile) || profile.id().equals("3gpp-27007-r18");
        }
        if (handler instanceof SmsHandler) {
            return mobileProfile(profile) || profile.id().equals("3gpp-27005-r16");
        }
        return true;
    }

    private boolean mobileProfile(Profile profile) {
        return switch (profile.profileKind()) {
            case "cellular", "hybrid" -> true;
            default -> false;
        };
    }

    private ProfileCommand declaration(Profile profile, ParsedCommand command) {
        if (profile.commands().isEmpty()) {
            return new ProfileCommand(command.normalizedName(), "implemented_full", null, null);
        }
        String name = command.kind() == com.jkamsker.modemsim.parser.CommandKind.S_REGISTER_READ
                || command.kind() == com.jkamsker.modemsim.parser.CommandKind.S_REGISTER_WRITE
                ? "ATS" : command.normalizedName();
        return profile.commands().stream().filter(candidate -> candidate.name().equals(name)).findFirst().orElse(null);
    }

    private boolean executable(ProfileCommand command) {
        return command.status().equals("implemented_full") || command.status().equals("implemented_stub");
    }

    private boolean handlerMatches(ProfileCommand command, CommandHandler handler) {
        return command.handler() == null || command.handler().isBlank()
                || command.handler().equals(handler.getClass().getSimpleName());
    }

    private boolean noResponseFreeze(ModemState state) {
        return state.modem().lifecycle() == ModemLifecycle.FROZEN
                && state.modem().freezeMode() != FreezeMode.HOLD_TX;
    }
}
