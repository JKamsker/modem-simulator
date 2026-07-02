package com.alegs3.modemsim.commands;

import com.alegs3.modemsim.parser.ParsedCommand;
import com.alegs3.modemsim.profiles.Profile;
import com.alegs3.modemsim.state.ModemState;

public interface CommandHandler {
    CommandResult handle(Profile profile, ModemState state, ParsedCommand command);
}
