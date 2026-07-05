package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.session.SessionResponse;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;
import java.util.function.Supplier;

final class GuiInjectionPane {
    private final GuiViewSupport ui;
    private final GuiSessionController controller;
    private final Consumer<SessionResponse> handler;
    private final Consumer<String> errors;

    GuiInjectionPane(
            GuiViewSupport ui, GuiSessionController controller,
            Consumer<SessionResponse> handler, Consumer<String> errors) {
        this.ui = ui;
        this.controller = controller;
        this.handler = handler;
        this.errors = errors;
    }

    Node node() {
        TextField rawDte = ui.field("inject.rawDteToDce", "AT\\r");
        TextField rawDce = ui.field("inject.rawDceToDte", "+CREG: 4\\r\\n");
        TextField parsed = ui.field("inject.parsedCommand", "AT+CSQ");
        TextField urc = ui.field("inject.urc", "+CREG: 4");
        TextField patch = ui.field("inject.statePatch", "network.stat=4");
        CheckBox safety = ui.register(new CheckBox("Confirm DCE transmit"), "inject.safetyConfirm");
        GridPane grid = ui.grid();
        ui.add(grid, 0, "Raw DTE", rawDte);
        ui.add(grid, 1, "Raw DCE", rawDce);
        ui.add(grid, 2, "Parsed command", parsed);
        ui.add(grid, 3, "URC", urc);
        ui.add(grid, 4, "State patch", patch);
        Button sendDte = ui.button("inject.sendDte", "Send DTE");
        Button sendDce = ui.button("inject.sendDce", "Send DCE");
        Button sendParsed = ui.button("inject.sendParsed", "Send Parsed");
        Button sendUrc = ui.button("inject.sendUrc", "Send URC");
        Button applyPatch = ui.button("inject.applyPatch", "Apply Patch");
        sendDte.setOnAction(event -> unsafe(() -> controller.rawDteToDce(rawDte.getText())));
        sendParsed.setOnAction(event -> unsafe(() -> controller.parsedCommand(parsed.getText())));
        sendDce.setOnAction(event -> unsafe(() -> controller.rawDceToDte(rawDce.getText(), safety.isSelected())));
        sendUrc.setOnAction(event -> unsafe(() -> controller.urc(urc.getText(), safety.isSelected())));
        applyPatch.setOnAction(event -> unsafe(() -> controller.applyState(GuiStatePatchFactory.fromText(patch.getText()))));
        return new VBox(8, ui.explainer("Injection", GuiHelpText.forLabel("Raw DCE")),
                grid, safety, new HBox(8, sendDte, sendDce, sendParsed, sendUrc, applyPatch));
    }

    private void unsafe(Supplier<SessionResponse> action) {
        try {
            handler.accept(action.get());
        } catch (RuntimeException e) {
            errors.accept(e.getMessage());
        }
    }
}
