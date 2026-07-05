package com.jkamsker.modemsim.profiles;

import java.util.List;

record ProfileXmlMetadata(
        List<ProfileCommand> commands,
        List<ProfileRegister> registers,
        ProfileCoverage coverage,
        List<ProfileDeviation> deviations
) {
    ProfileXmlMetadata {
        commands = List.copyOf(commands);
        registers = List.copyOf(registers);
        deviations = List.copyOf(deviations);
    }
}
