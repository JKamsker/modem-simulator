package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.monitor.ModemEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.nio.file.Path;

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
        output.setPrefRowCount(4);
        export.setEditable(false);
        export.setId("export.preview");
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
        return root;
    }

    private Node sessionPane() {
        GridPane grid = ui.grid();
        ComboBox<String> profile = ui.combo("session.profile", "sierra-hl6-hl8-v20", "westermo-td22-6177-2203");
        TextField mainPort = ui.field("session.mainPort", "headless");
        TextField snifferPort = ui.field("session.snifferPort", "");
        TextField manualDcePort = ui.field("session.manualDcePort", "");
        TextField scenario = ui.field("session.initialScenario", "");
        TextField seed = ui.field("session.seed", "12345");
        TextField baud = ui.field("session.baudRate", "115200");
        ComboBox<String> dataBits = ui.combo("session.dataBits", "8", "7", "6", "5");
        ComboBox<String> stopBits = ui.combo("session.stopBits", "1", "2");
        ComboBox<String> parity = ui.combo("session.parity", "NONE", "EVEN", "ODD", "MARK", "SPACE");
        ComboBox<String> flow = ui.combo("session.flowControl", "NONE", "RTS_CTS", "XON_XOFF");
        ui.add(grid, 0, "Profile", profile);
        ui.add(grid, 1, "Main port", mainPort);
        ui.add(grid, 2, "Sniffer port", snifferPort);
        ui.add(grid, 3, "Manual DCE port", manualDcePort);
        ui.add(grid, 4, "Scenario", scenario);
        ui.add(grid, 5, "Seed", seed);
        ui.add(grid, 6, "Baud", baud);
        ui.add(grid, 7, "Data bits", dataBits);
        ui.add(grid, 8, "Stop bits", stopBits);
        ui.add(grid, 9, "Parity", parity);
        ui.add(grid, 10, "Flow", flow);
        java.util.function.Supplier<GuiSessionOptions> options = () -> GuiSessionOptions.of(
                profile.getValue(), mainPort.getText(), snifferPort.getText(), manualDcePort.getText(),
                scenario.getText(), seed.getText(), baud.getText(), dataBits.getValue(), stopBits.getValue(),
                parity.getValue(), flow.getValue());
        Label status = ui.label("session.portLost", "Port OK");
        Button start = ui.button("session.start", "Start");
        Button stop = ui.button("session.stop", "Stop");
        Button reconnect = ui.button("session.reconnect", "Reconnect");
        start.setOnAction(event -> {
            handle(controller.start(options.get()));
            status.setText(controller.runtimeStatus());
        });
        stop.setOnAction(event -> {
            handle(controller.stop());
            status.setText(controller.runtimeStatus());
        });
        reconnect.setOnAction(event -> {
            handle(controller.reconnect(options.get()));
            status.setText(controller.runtimeStatus());
        });
        HBox buttons = new HBox(8, start, stop, reconnect, status);
        return new VBox(12, grid, buttons, output);
    }

    private Node logPane() {
        return new GuiLogPane(ui, events, export, controller).node();
    }

    private Node statePane() {
        GridPane grid = ui.grid();
        ComboBox<String> simState = ui.combo("state.simState", ui.names(SimState.values()));
        TextField pinRetries = ui.field("state.pinRetries", "3");
        TextField pukRetries = ui.field("state.pukRetries", "10");
        TextField networkStat = ui.field("state.networkStat", "1");
        TextField cregN = ui.field("state.cregN", "2");
        TextField lac = ui.field("state.lac", "00C3");
        TextField ci = ui.field("state.ci", "00001234");
        TextField act = ui.field("state.act", "7");
        TextField rejectCauseType = ui.field("state.rejectCauseType", "");
        TextField rejectCause = ui.field("state.rejectCause", "");
        TextField signal = ui.field("state.signal", "18,0");
        ComboBox<String> smsStorage = ui.combo("state.smsStorage", "ME", "SM", "MT");
        TextField callMode = ui.field("state.callMode", "COMMAND");
        TextField lifecycle = ui.field("state.modemLifecycle", "READY");
        TextField freeze = ui.field("state.freezeMode", "NONE");
        TextField lines = ui.field("state.lines", "DTR DSR RTS CTS");
        ui.add(grid, 0, "SIM", simState);
        ui.add(grid, 1, "PIN retries", pinRetries);
        ui.add(grid, 2, "PUK retries", pukRetries);
        ui.add(grid, 3, "Network stat", networkStat);
        ui.add(grid, 4, "CREG n", cregN);
        ui.add(grid, 5, "LAC", lac);
        ui.add(grid, 6, "CI", ci);
        ui.add(grid, 7, "AcT", act);
        ui.add(grid, 8, "Reject type", rejectCauseType);
        ui.add(grid, 9, "Reject cause", rejectCause);
        ui.add(grid, 10, "Signal", signal);
        ui.add(grid, 11, "SMS store", smsStorage);
        ui.add(grid, 12, "Call mode", callMode);
        ui.add(grid, 13, "Lifecycle", lifecycle);
        ui.add(grid, 14, "Freeze", freeze);
        ui.add(grid, 15, "Lines", lines);
        Button apply = ui.button("state.apply", "Apply");
        apply.setOnAction(event -> handle(controller.applyState(GuiStatePatchFactory.fromValues(
                simState.getValue(), pinRetries.getText(), pukRetries.getText(), networkStat.getText(),
                cregN.getText(), lac.getText(), ci.getText(), act.getText(),
                signal.getText(), rejectCauseType.getText(), rejectCause.getText(),
                smsStorage.getValue(), callMode.getText(), lifecycle.getText(), freeze.getText(), lines.getText()))));
        return new VBox(8, grid, apply);
    }

    private Node injectionPane() {
        TextField rawDte = ui.field("inject.rawDteToDce", "AT\\r");
        TextField rawDce = ui.field("inject.rawDceToDte", "+CREG: 4\\r\\n");
        TextField parsed = ui.field("inject.parsedCommand", "AT+CSQ");
        TextField urc = ui.field("inject.urc", "+CREG: 4");
        TextField patch = ui.field("inject.statePatch", "network.stat=4");
        CheckBox safety = ui.register(new CheckBox("Confirm DCE transmit"), "inject.safetyConfirm");
        Button sendDte = ui.button("inject.sendDte", "Send DTE");
        Button sendDce = ui.button("inject.sendDce", "Send DCE");
        Button sendParsed = ui.button("inject.sendParsed", "Send Parsed");
        Button sendUrc = ui.button("inject.sendUrc", "Send URC");
        Button applyPatch = ui.button("inject.applyPatch", "Apply Patch");
        sendDte.setOnAction(event -> handle(controller.rawDteToDce(rawDte.getText())));
        sendParsed.setOnAction(event -> handle(controller.parsedCommand(parsed.getText())));
        sendDce.setOnAction(event -> handleUnsafe(() -> controller.rawDceToDte(rawDce.getText(), safety.isSelected())));
        sendUrc.setOnAction(event -> handleUnsafe(() -> controller.urc(urc.getText(), safety.isSelected())));
        applyPatch.setOnAction(event -> handle(controller.applyState(GuiStatePatchFactory.fromText(patch.getText()))));
        return new VBox(8, rawDte, rawDce, parsed, urc, patch, safety,
                new HBox(8, sendDte, sendDce, sendParsed, sendUrc, applyPatch));
    }

    private Node faultPane() {
        return new GuiFaultPane(ui, controller, this::handle).node();
    }

    private Node macroPane() {
        TextField file = ui.field("macro.file", "docs/Tasks/Initial-Spec/examples/macros.sms-error-123.xml");
        TextField macroId = ui.field("macro.id", "smscommand-dst-error-123");
        Label hash = ui.label("macro.hash", "sha256:");
        Label errors = ui.label("macro.errors", "");
        Label customResponses = ui.label("macro.customResponses", "");
        Label enabled = ui.label("macro.enabled", "");
        Button reload = ui.button("macro.reload", "Reload");
        Button enable = ui.button("macro.enable", "Enable");
        Button disable = ui.button("macro.disable", "Disable");
        reload.setOnAction(event -> {
            MacroSummary summary = controller.reloadMacros(Path.of(file.getText()));
            hash.setText(summary.hash().isBlank() ? "sha256:" : summary.hash());
            errors.setText(summary.errors());
            customResponses.setText(summary.customResponses());
            enabled.setText(summary.enabled());
            handle(summary.response());
        });
        enable.setOnAction(event -> errors.setText(controller.macroToggleStatus("enable", macroId.getText())));
        disable.setOnAction(event -> errors.setText(controller.macroToggleStatus("disable", macroId.getText())));
        return new VBox(8,
                file, macroId, hash, errors, customResponses, enabled,
                new HBox(8, reload, enable, disable));
    }

    private Node replayPane() {
        Label hashStatus = ui.label("replay.hashStatus", "hashes pending");
        Label divergences = ui.label("replay.divergences", "");
        TextField logFile = ui.field("replay.logFile", "logs/session.jsonl");
        ComboBox<String> mode = ui.combo("replay.mode", "validate-recompute", "drive-from-captured-input", "play-to-dte");
        CheckBox virtualClock = ui.register(new CheckBox("Virtual clock"), "replay.virtualClock");
        CheckBox confirm = ui.register(new CheckBox("Confirm DCE transmit"), "replay.safetyConfirm");
        CheckBox divergenceConfirm = ui.register(new CheckBox("Confirm divergence"), "replay.divergenceConfirm");
        virtualClock.setSelected(true);
        Button validate = ui.button("replay.validate", "Validate");
        Button runSelected = ui.button("replay.runSelected", "Run");
        Button drive = ui.button("replay.driveFromCapturedInput", "Drive Input");
        Button play = ui.button("replay.playToDte", "Play to DTE");
        validate.setOnAction(event -> {
            ReplaySummary result = controller.replay(Path.of(logFile.getText()), "validate-recompute", virtualClock.isSelected());
            showReplay(result, hashStatus, divergences);
        });
        runSelected.setOnAction(event -> {
            ReplaySummary result = controller.replay(
                    Path.of(logFile.getText()), mode.getValue(), virtualClock.isSelected(),
                    confirm.isSelected(), divergenceConfirm.isSelected());
            showReplay(result, hashStatus, divergences);
        });
        drive.setOnAction(event -> {
            ReplaySummary result = controller.replay(Path.of(logFile.getText()), "drive-from-captured-input", virtualClock.isSelected());
            showReplay(result, hashStatus, divergences);
        });
        play.setOnAction(event -> {
            ReplaySummary result = controller.replay(
                    Path.of(logFile.getText()), "play-to-dte", virtualClock.isSelected(),
                    confirm.isSelected(), divergenceConfirm.isSelected());
            showReplay(result, hashStatus, divergences);
        });
        return new VBox(8,
                mode, logFile,
                hashStatus, virtualClock, confirm, divergenceConfirm, divergences,
                new HBox(8, validate, runSelected, drive, play));
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

    private void showReplay(ReplaySummary result, Label hashStatus, Label divergences) { hashStatus.setText(result.hashStatus()); divergences.setText(result.message()); handle(result.response()); }

    private void refreshState() {
        display.refreshState();
    }

    private void startPolling() {
        poller = new Timeline(new KeyFrame(Duration.millis(250), event -> handle(controller.pollEvents())));
        poller.setCycleCount(Animation.INDEFINITE);
        poller.play();
    }

    private void handleUnsafe(Action action) {
        try {
            handle(action.run());
        } catch (RuntimeException e) {
            output.appendText(e.getMessage() + "\n");
        }
    }

    @FunctionalInterface
    private interface Action {
        SessionResponse run();
    }
}
