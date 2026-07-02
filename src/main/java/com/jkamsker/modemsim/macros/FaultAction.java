package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.state.FreezeMode;

public record FaultAction(String type, Integer durationMs, Integer stat, Integer rssi, Integer ber, FreezeMode freezeMode) {
}
