package com.alegs3.modemsim.gui;

import java.util.List;

public final class GuiControlCatalog {
    public List<GuiControl> controls() {
        return List.of(
                control("session.profile", false),
                control("session.mainPort", false),
                control("session.start", true),
                control("session.stop", true),
                control("session.reconnect", true),
                control("log.table", false),
                control("log.export", false),
                control("state.simState", true),
                control("state.networkStat", true),
                control("state.signal", true),
                control("inject.rawDteToDce", true),
                control("inject.rawDceToDte", true),
                control("inject.statePatch", true),
                control("fault.networkOutage", true),
                control("fault.modemFreeze", true),
                control("macro.reload", true),
                control("macro.enable", true),
                control("macro.disable", true),
                control("replay.mode", false),
                control("replay.playToDte", true),
                control("export.jsonl", false));
    }

    private GuiControl control(String id, boolean mutating) {
        return new GuiControl(id, mutating, true);
    }
}
