package com.alegs3.modemsim.profiles;

import com.alegs3.modemsim.state.ModemState;

import java.util.List;

public record Profile(
        String id,
        List<String> parents,
        String vendor,
        String status,
        String profileKind,
        Dialect dialect,
        Identity identity,
        ModemState initialState
) {
    public Profile withInitialState(ModemState value) {
        return new Profile(id, parents, vendor, status, profileKind, dialect, identity, value);
    }

    public Profile withDialect(Dialect value) {
        return new Profile(id, parents, vendor, status, profileKind, value, identity, initialState);
    }
}
