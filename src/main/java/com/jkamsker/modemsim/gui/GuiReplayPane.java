package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.session.SessionResponse;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.nio.file.Path;
import java.util.function.Consumer;

final class GuiReplayPane {
    private final GuiViewSupport ui;
    private final GuiSessionController controller;
    private final Consumer<SessionResponse> handler;

    GuiReplayPane(GuiViewSupport ui, GuiSessionController controller, Consumer<SessionResponse> handler) {
        this.ui = ui;
        this.controller = controller;
        this.handler = handler;
    }

    Node node() {
        Label hashStatus = ui.label("replay.hashStatus", "hashes pending");
        Label divergences = ui.label("replay.divergences", "");
        TextField logFile = ui.field("replay.logFile", "logs/session.jsonl");
        ComboBox<String> mode = ui.combo("replay.mode", "validate-recompute", "drive-from-captured-input", "play-to-dte");
        CheckBox virtualClock = ui.register(new CheckBox("Virtual clock"), "replay.virtualClock");
        CheckBox confirm = ui.register(new CheckBox("Confirm DCE transmit"), "replay.safetyConfirm");
        CheckBox divergenceConfirm = ui.register(new CheckBox("Confirm divergence"), "replay.divergenceConfirm");
        virtualClock.setSelected(true);
        GridPane grid = ui.grid();
        ui.add(grid, 0, "Replay mode", mode);
        ui.add(grid, 1, "Replay log", logFile);
        Button validate = ui.button("replay.validate", "Validate");
        Button runSelected = ui.button("replay.runSelected", "Run");
        Button drive = ui.button("replay.driveFromCapturedInput", "Drive Input");
        Button play = ui.button("replay.playToDte", "Play to DTE");
        validate.setOnAction(event -> show(controller.replay(
                Path.of(logFile.getText()), "validate-recompute", virtualClock.isSelected()), hashStatus, divergences));
        runSelected.setOnAction(event -> show(controller.replay(
                Path.of(logFile.getText()), mode.getValue(), virtualClock.isSelected(),
                confirm.isSelected(), divergenceConfirm.isSelected()), hashStatus, divergences));
        drive.setOnAction(event -> show(controller.replay(Path.of(logFile.getText()), "drive-from-captured-input",
                virtualClock.isSelected(), false, divergenceConfirm.isSelected()), hashStatus, divergences));
        play.setOnAction(event -> show(controller.replay(
                Path.of(logFile.getText()), "play-to-dte", virtualClock.isSelected(),
                confirm.isSelected(), divergenceConfirm.isSelected()), hashStatus, divergences));
        return new VBox(8, ui.explainer("Replay", GuiHelpText.forLabel("Replay mode")), grid,
                hashStatus, virtualClock, confirm, divergenceConfirm, divergences,
                new HBox(8, validate, runSelected, drive, play));
    }

    private void show(ReplaySummary result, Label hashStatus, Label divergences) {
        hashStatus.setText(result.hashStatus());
        divergences.setText(result.message());
        handler.accept(result.response());
    }
}
