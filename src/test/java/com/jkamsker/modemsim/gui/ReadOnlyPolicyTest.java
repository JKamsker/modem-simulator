package com.jkamsker.modemsim.gui;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReadOnlyPolicyTest {
    @Test
    void readOnlyDisablesMutatingControlsButKeepsLogAndExportEnabled() {
        var controls = new ReadOnlyPolicy().apply(new GuiControlCatalog().controls(), true);

        assertThat(controls).filteredOn(control -> control.id().startsWith("state."))
                .allSatisfy(control -> assertThat(control.enabled()).isFalse());
        assertThat(controls).filteredOn(control -> control.id().startsWith("inject."))
                .allSatisfy(control -> assertThat(control.enabled()).isFalse());
        assertThat(controls).filteredOn(control -> control.id().startsWith("fault."))
                .allSatisfy(control -> assertThat(control.enabled()).isFalse());
        assertThat(controls).filteredOn(control -> control.id().equals("macro.reload")
                        || control.id().equals("macro.enable")
                        || control.id().equals("macro.disable")
                        || control.id().equals("replay.runSelected")
                        || control.id().equals("replay.driveFromCapturedInput")
                        || control.id().equals("replay.playToDte")
                        || control.id().equals("session.reconnect"))
                .allSatisfy(control -> assertThat(control.enabled()).isFalse());
        assertThat(controls).filteredOn(control -> control.id().equals("log.table")
                        || control.id().equals("export.jsonl"))
                .allSatisfy(control -> assertThat(control.enabled()).isTrue());
    }

    @Test
    void catalogContainsAutomationIdsForRequiredGuiAreas() {
        assertThat(new GuiControlCatalog().controls()).extracting(GuiControl::id)
                .contains(
                        "session.mainPort",
                        "session.snifferPort",
                        "session.initialScenario",
                        "session.seed",
                        "log.table",
                        "log.filter",
                        "log.selection",
                        "state.simState",
                        "state.lines",
                        "inject.rawDteToDce",
                        "fault.networkOutage",
                        "fault.durationMs",
                        "fault.freezeMode",
                        "macro.reload",
                        "macro.id",
                        "replay.mode",
                        "replay.driveFromCapturedInput",
                        "replay.divergenceConfirm",
                        "export.jsonl",
                        "export.replayReport");
    }
}
