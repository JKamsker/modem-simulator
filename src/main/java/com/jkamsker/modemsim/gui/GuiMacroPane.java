package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.session.SessionResponse;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.nio.file.Path;
import java.util.function.Consumer;

final class GuiMacroPane {
    private final GuiViewSupport ui;
    private final GuiSessionController controller;
    private final Consumer<SessionResponse> handler;

    GuiMacroPane(GuiViewSupport ui, GuiSessionController controller, Consumer<SessionResponse> handler) {
        this.ui = ui;
        this.controller = controller;
        this.handler = handler;
    }

    Node node() {
        TextField file = ui.field("macro.file", "docs/Tasks/Initial-Spec/examples/macros.sms-error-123.xml");
        TextField macroId = ui.field("macro.id", "smscommand-dst-error-123");
        Label hash = ui.label("macro.hash", "sha256:");
        Label errors = ui.label("macro.errors", "");
        Label customResponses = ui.label("macro.customResponses", "");
        Label enabled = ui.label("macro.enabled", "");
        Button reload = ui.button("macro.reload", "Reload");
        Button enable = ui.button("macro.enable", "Enable");
        Button disable = ui.button("macro.disable", "Disable");
        reload.setOnAction(event -> reload(file, hash, errors, customResponses, enabled));
        enable.setOnAction(event -> toggle(errors, enabled, "enable", macroId.getText()));
        disable.setOnAction(event -> toggle(errors, enabled, "disable", macroId.getText()));
        return new VBox(8,
                file, macroId, hash, errors, customResponses, enabled,
                new HBox(8, reload, enable, disable));
    }

    private void reload(TextField file, Label hash, Label errors, Label customResponses, Label enabled) {
        MacroSummary summary = controller.reloadMacros(Path.of(file.getText()));
        hash.setText(summary.hash().isBlank() ? "sha256:" : summary.hash());
        errors.setText(summary.errors());
        customResponses.setText(summary.customResponses());
        enabled.setText(summary.enabled());
        handler.accept(summary.response());
    }

    private void toggle(Label errors, Label enabled, String action, String macroId) {
        errors.setText(controller.macroToggleStatus(action, macroId));
        enabled.setText(controller.enabledMacros());
    }
}
