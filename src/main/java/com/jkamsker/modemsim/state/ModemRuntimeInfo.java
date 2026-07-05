package com.jkamsker.modemsim.state;

public record ModemRuntimeInfo(ModemLifecycle lifecycle, FreezeMode freezeMode, int bootDelayMs) {
    public static ModemRuntimeInfo ready() {
        return new ModemRuntimeInfo(ModemLifecycle.READY, FreezeMode.NONE, 0);
    }

    public ModemRuntimeInfo withLifecycle(ModemLifecycle value) {
        FreezeMode nextFreeze = value == ModemLifecycle.FROZEN ? freezeMode : FreezeMode.NONE;
        return new ModemRuntimeInfo(value, nextFreeze, bootDelayMs);
    }

    public ModemRuntimeInfo frozen(FreezeMode mode) {
        return new ModemRuntimeInfo(ModemLifecycle.FROZEN, mode, bootDelayMs);
    }
}
