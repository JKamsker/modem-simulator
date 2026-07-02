package com.alegs3.modemsim.commands;

import com.alegs3.modemsim.parser.ParsedCommand;
import com.alegs3.modemsim.profiles.Profile;
import com.alegs3.modemsim.profiles.UnknownAtCommandPolicy;
import com.alegs3.modemsim.state.ModemLifecycle;
import com.alegs3.modemsim.state.ModemState;

import java.util.List;

public final class DefaultCommandRouter {
    private final List<CommandHandler> handlers = List.of(new HayesHandler(), new CellularHandler(), new SmsHandler());

    public CommandResult route(Profile profile, ModemState state, ParsedCommand command) {
        if (state.modem().lifecycle() == ModemLifecycle.FROZEN) {
            return new CommandResult(state, List.of(), null, "FreezePolicy", true);
        }
        for (CommandHandler handler : handlers) {
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
                    state.withModem(state.modem().withLifecycle(ModemLifecycle.REBOOTING)),
                    List.of(),
                    null,
                    "UnknownPolicy",
                    true);
        };
    }
}
