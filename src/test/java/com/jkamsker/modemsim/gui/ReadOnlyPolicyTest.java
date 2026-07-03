package com.jkamsker.modemsim.gui;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReadOnlyPolicyTest {
    @Test
    void readOnlyDisablesMutatingControlsButKeepsLogAndExportEnabled() {
        var controls = new ReadOnlyPolicy().apply(new GuiControlCatalog().controls(), true);

        assertThat(controls).filteredOn(GuiControl::mutating)
                .allSatisfy(control -> assertThat(control.enabled()).isFalse());
        assertThat(controls).filteredOn(control -> !control.mutating())
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
