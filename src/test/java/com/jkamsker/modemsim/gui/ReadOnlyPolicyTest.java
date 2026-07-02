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
        assertThat(controls).filteredOn(control -> control.id().equals("log.table")
                        || control.id().equals("export.jsonl"))
                .allSatisfy(control -> assertThat(control.enabled()).isTrue());
    }
}
