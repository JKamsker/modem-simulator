package com.alegs3.modemsim.macros;

import com.alegs3.modemsim.state.FreezeMode;

public record FaultAction(String type, Integer durationMs, Integer stat, Integer rssi, Integer ber, FreezeMode freezeMode) {
}
