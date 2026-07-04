package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.monitor.ModemEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

final class SimulatorView {
    private final GuiViewSupport ui = new GuiViewSupport();
    private final GuiSessionController controller;
    private final ObservableList<ModemEvent> events = FXCollections.observableArrayList();
    private final TextArea output = new TextArea();
    private final TextArea export = new TextArea();
    private final Label stateSummary = new Label();
    private final GuiEventDisplay display;
    private Timeline poller;

    SimulatorView() { this(false); }

    SimulatorView(boolean allowUnsafeDceTransmit) {
        this.controller = new GuiSessionController(
                new HeadlessSession("gui", BuiltinProfiles.acceptanceSierra(), 12345), allowUnsafeDceTransmit);
        this.display = new GuiEventDisplay(events, output, stateSummary, controller);
    }

    Parent root() {
        output.setEditable(false);
        output.setId("session.output");
        output.setPrefRowCount(4);
        output.setWrapText(true);
        export.setEditable(false);
        export.setId("export.preview");
        export.setWrapText(true);
        refreshState();
        startPolling();

        TabPane tabs = new TabPane(
                ui.tab("Session", sessionPane()),
                ui.tab("Live Log", logPane()),
                ui.tab("State", statePane()),
                ui.tab("Injection", injectionPane()),
                ui.tab("Faults", faultPane()),
                ui.tab("Macros", macroPane()),
                ui.tab("Replay", replayPane()),
                ui.tab("Export", exportPane()));
        CheckBox readOnly = new CheckBox("Read-only");
        readOnly.setId("session.readOnly");
        readOnly.selectedProperty().addListener((ignored, oldValue, newValue) -> ui.applyReadOnly(newValue));
        BorderPane root = new BorderPane(tabs);
        root.setTop(new HBox(12, readOnly, stateSummary));
        root.setPadding(new Insets(0, 0, 0, 10));
        return root;
    }

    private Node sessionPane() {
        return new GuiSessionPane(ui, controller, output, this::handle).node();
    }

    private Node logPane() {
        return new GuiLogPane(ui, events, export, controller).node();
    }

    private Node statePane() {
        return new GuiStatePane(ui, controller, this::handle).node();
    }

    private Node injectionPane() {
        return new GuiInjectionPane(ui, controller, this::handle, this::appendError).node();
    }

    private Node faultPane() {
        return new GuiFaultPane(ui, controller, this::handle).node();
    }

    private Node macroPane() {
        return new GuiMacroPane(ui, controller, this::handle).node();
    }

    private Node replayPane() {
        return new GuiReplayPane(ui, controller, this::handle).node();
    }

    private Node exportPane() {
        Button jsonl = ui.button("export.jsonl", "JSONL");
        Button transcript = ui.button("export.transcript", "Transcript");
        Button coverage = ui.button("export.coverage", "Coverage");
        Button replay = ui.button("export.replayReport", "Replay Report");
        jsonl.setOnAction(event -> export.setText(controller.jsonl(events)));
        transcript.setOnAction(event -> export.setText(controller.transcript(events)));
        coverage.setOnAction(event -> export.setText(controller.coverage(events)));
        replay.setOnAction(event -> export.setText(controller.replayReport(events)));
        HBox buttons = new HBox(8, jsonl, transcript, coverage, replay);
        return new VBox(8, buttons, export);
    }

    private void handle(SessionResponse response) {
        display.handle(response);
    }

    private void refreshState() {
        display.refreshState();
    }

    private void startPolling() {
        poller = new Timeline(new KeyFrame(Duration.millis(250), event -> handle(controller.pollEvents())));
        poller.setCycleCount(Animation.INDEFINITE);
        poller.play();
    }

    private void appendError(String message) {
        output.appendText(message + "\n");
    }
}
