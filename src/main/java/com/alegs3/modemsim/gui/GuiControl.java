package com.alegs3.modemsim.gui;

public record GuiControl(String id, boolean mutating, boolean enabled) {
    public GuiControl withEnabled(boolean value) {
        return new GuiControl(id, mutating, value);
    }
}
