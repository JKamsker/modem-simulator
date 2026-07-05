package com.jkamsker.modemsim.gui;

import java.util.List;

public final class ReadOnlyPolicy {
    public List<GuiControl> apply(List<GuiControl> controls, boolean readOnly) {
        if (!readOnly) {
            return controls;
        }
        return controls.stream()
                .map(control -> control.withEnabled(!control.mutating()))
                .toList();
    }
}
