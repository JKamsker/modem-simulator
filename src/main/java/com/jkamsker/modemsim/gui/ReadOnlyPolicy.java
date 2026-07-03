package com.jkamsker.modemsim.gui;

import java.util.List;
import java.util.Set;

public final class ReadOnlyPolicy {
    private static final Set<String> EXACT_DISABLED = Set.of(
            "macro.reload",
            "macro.enable",
            "macro.disable",
            "replay.driveFromCapturedInput",
            "replay.playToDte",
            "session.reconnect");

    public List<GuiControl> apply(List<GuiControl> controls, boolean readOnly) {
        if (!readOnly) {
            return controls;
        }
        return controls.stream()
                .map(control -> control.withEnabled(!shouldDisable(control)))
                .toList();
    }

    private boolean shouldDisable(GuiControl control) {
        return control.id().startsWith("state.")
                || control.id().startsWith("fault.")
                || control.id().startsWith("inject.")
                || EXACT_DISABLED.contains(control.id());
    }
}
