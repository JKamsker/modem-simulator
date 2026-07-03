package com.jkamsker.modemsim.gui;

import java.util.List;

public final class GuiControlCatalog {
    public List<GuiControl> controls() {
        return List.of(
                control("session.profile", false),
                control("session.mainPort", false),
                control("session.snifferPort", false),
                control("session.manualDcePort", false),
                control("session.baudRate", true),
                control("session.dataBits", true),
                control("session.stopBits", true),
                control("session.parity", true),
                control("session.flowControl", true),
                control("session.start", true),
                control("session.stop", true),
                control("session.reconnect", true),
                control("session.portLost", false),
                control("log.table", false),
                control("log.export", false),
                control("state.simState", true),
                control("state.pinRetries", true),
                control("state.pukRetries", true),
                control("state.networkStat", true),
                control("state.cregN", true),
                control("state.lac", true),
                control("state.ci", true),
                control("state.act", true),
                control("state.rejectCause", true),
                control("state.signal", true),
                control("state.smsStorage", true),
                control("state.callMode", true),
                control("state.modemLifecycle", true),
                control("state.freezeMode", true),
                control("state.lines", true),
                control("inject.rawDteToDce", true),
                control("inject.rawDceToDte", true),
                control("inject.parsedCommand", true),
                control("inject.urc", true),
                control("inject.statePatch", true),
                control("inject.safetyConfirm", true),
                control("inject.send", true),
                control("fault.networkOutage", true),
                control("fault.networkRestore", true),
                control("fault.modemReboot", true),
                control("fault.modemFreeze", true),
                control("fault.modemUnfreeze", true),
                control("macro.reload", true),
                control("macro.enable", true),
                control("macro.disable", true),
                control("macro.file", false),
                control("macro.hash", false),
                control("macro.errors", false),
                control("macro.customResponses", false),
                control("replay.mode", false),
                control("replay.logFile", false),
                control("replay.hashStatus", false),
                control("replay.validate", false),
                control("replay.playToDte", true),
                control("export.jsonl", false),
                control("export.transcript", false),
                control("export.coverage", false));
    }

    private GuiControl control(String id, boolean mutating) {
        return new GuiControl(id, mutating, true);
    }
}
