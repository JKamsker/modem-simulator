package com.jkamsker.modemsim.profiles;

import com.jkamsker.modemsim.state.ModemState;

import java.util.List;

public record Profile(
        String id,
        List<String> parents,
        String vendor,
        String status,
        String profileKind,
        Dialect dialect,
        Identity identity,
        ModemState initialState,
        List<ProfileCommand> commands,
        List<ProfileRegister> registers,
        ProfileCoverage coverage,
        List<ProfileDeviation> deviations
) {
    public Profile(
            String id, List<String> parents, String vendor, String status, String profileKind,
            Dialect dialect, Identity identity, ModemState initialState) {
        this(id, parents, vendor, status, profileKind, dialect, identity, initialState, List.of(), List.of(), null, List.of());
    }

    public Profile {
        parents = List.copyOf(parents);
        commands = List.copyOf(commands);
        registers = List.copyOf(registers);
        deviations = List.copyOf(deviations);
    }

    public Profile withInitialState(ModemState value) {
        return new Profile(id, parents, vendor, status, profileKind, dialect, identity, value,
                commands, registers, coverage, deviations);
    }

    public Profile withDialect(Dialect value) {
        return new Profile(id, parents, vendor, status, profileKind, value, identity, initialState,
                commands, registers, coverage, deviations);
    }
}
