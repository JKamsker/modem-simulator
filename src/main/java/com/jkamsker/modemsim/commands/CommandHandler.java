package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.state.ModemState;

public interface CommandHandler {
    CommandResult handle(Profile profile, ModemState state, ParsedCommand command);
}
