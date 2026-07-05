package com.jkamsker.modemsim.profiles;

import java.util.List;

public record ProfileCoverage(String source, int commandsTotal, int unknown, List<ProfileCommand> commands) {
    public ProfileCoverage {
        commands = List.copyOf(commands);
    }
}
