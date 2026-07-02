package com.alegs3.modemsim.profiles;

import com.alegs3.modemsim.state.ModemState;

import java.util.List;

public final class BuiltinProfiles {
    private BuiltinProfiles() {
    }

    public static Profile acceptanceSierra() {
        return new Profile(
                "sierra-hl6-hl8-v20",
                List.of("sierra-common"),
                "Sierra Wireless / Semtech",
                "device-family",
                "cellular",
                Dialect.v250(),
                Identity.sierra(),
                ModemState.cellularReady());
    }

    public static Profile westermoTd22() {
        return new Profile(
                "westermo-td22-6177-2203",
                List.of("westermo-common"),
                "Westermo",
                "device-target",
                "pstn",
                Dialect.v250(),
                Identity.westermo("TD-22", "6177-2203"),
                ModemState.pstnReady());
    }
}
