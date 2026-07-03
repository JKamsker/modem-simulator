package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.SimState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

final class SimulatorView {
    private final GuiViewSupport ui = new GuiViewSupport();
    private final GuiSessionController controller = new GuiSessionController(
            new HeadlessSession("gui", BuiltinProfiles.acceptanceSierra(), 12345));
    private final ObservableList<ModemEvent> events = FXCollections.observableArrayList();
    private final TextArea output = new TextArea();
    private final TextArea export = new TextArea();
    private final Label stateSummary = new Label();

    Parent root() {
        output.setEditable(false);
        output.setPrefRowCount(4);
        export.setEditable(false);
        export.setId("export.preview");
        refreshState();

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
        ui.add(grid, 0, "Profile", ui.combo("session.profile", "sierra-hl6-hl8-v20", "westermo-td22-6177-2203"));
        ui.add(grid, 1, "Main port", ui.field("session.mainPort", "COM7"));
        ui.add(grid, 2, "Sniffer port", ui.field("session.snifferPort", ""));
        ui.add(grid, 3, "Manual DCE port", ui.field("session.manualDcePort", ""));
        ui.add(grid, 4, "Baud", ui.field("session.baudRate", "115200"));
        ui.add(grid, 5, "Data bits", ui.combo("session.dataBits", "8", "7", "6", "5"));
        ui.add(grid, 6, "Stop bits", ui.combo("session.stopBits", "1", "2"));
        ui.add(grid, 7, "Parity", ui.combo("session.parity", "NONE", "EVEN", "ODD", "MARK", "SPACE"));
        ui.add(grid, 8, "Flow", ui.combo("session.flowControl", "NONE", "RTS_CTS", "XON_XOFF"));
        Label status = ui.label("session.portLost", "Port OK");
        Button start = ui.button("session.start", "Start");
        Button stop = ui.button("session.stop", "Stop");
        Button reconnect = ui.button("session.reconnect", "Reconnect");
        start.setOnAction(event -> status.setText("Session running"));
        stop.setOnAction(event -> status.setText("Session stopped"));
        reconnect.setOnAction(event -> status.setText("Reconnect requested"));
        HBox buttons = new HBox(8, start, stop, reconnect, status);
        return new VBox(12, grid, buttons, output);
    }

    private Node logPane() {
        TableView<ModemEvent> table = ui.register(new TableView<>(events), "log.table");
        table.getColumns().add(ui.column("Seq", event -> Long.toString(event.sequence())));
        table.getColumns().add(ui.column("Time", event -> event.timestamp().toString()));
        table.getColumns().add(ui.column("Type", event -> event.eventType().name()));
        table.getColumns().add(ui.column("Dir", event -> event.direction().name()));
        table.getColumns().add(ui.column("Port", event -> ui.value(event.port())));
        table.getColumns().add(ui.column("Raw", ModemEvent::rawHex));
        table.getColumns().add(ui.column("Text", event -> ui.value(event.textEscaped())));
        table.getColumns().add(ui.column("Parsed", event -> ui.value(event.parsedCommand())));
        table.getColumns().add(ui.column("Handler", event -> ui.value(event.handler())));
        table.getColumns().add(ui.column("Macro", event -> ui.value(event.macroId())));
        table.getColumns().add(ui.column("Result", event -> ui.value(event.result())));
        table.getColumns().add(ui.column("Latency", event -> ui.value(event.latencyMs())));
        table.getColumns().add(ui.column("Before", event -> ui.value(event.stateBefore())));
        table.getColumns().add(ui.column("After", event -> ui.value(event.stateAfter())));
        table.getColumns().add(ui.column("Injection", event -> ui.value(event.injectionType())));
        table.getColumns().add(ui.column("Redaction", event -> Boolean.toString(event.redaction().applied())));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        Button exportButton = ui.button("log.export", "Export");
        exportButton.setOnAction(event -> export.setText(controller.jsonl(events)));
        VBox.setVgrow(table, Priority.ALWAYS);
        return new VBox(8, table, exportButton);
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
        TextField signal = ui.field("state.signal", "18,0");
        TextField lines = ui.field("state.lines", "DTR DSR RTS CTS");
        ui.add(grid, 0, "SIM", simState);
        ui.add(grid, 1, "PIN retries", pinRetries);
        ui.add(grid, 2, "PUK retries", pukRetries);
        ui.add(grid, 3, "Network stat", networkStat);
        ui.add(grid, 4, "CREG n", cregN);
        ui.add(grid, 5, "LAC", lac);
        ui.add(grid, 6, "CI", ci);
        ui.add(grid, 7, "AcT", act);
        ui.add(grid, 8, "Reject cause", ui.field("state.rejectCause", ""));
        ui.add(grid, 9, "Signal", signal);
        ui.add(grid, 10, "SMS store", ui.combo("state.smsStorage", "ME", "SM", "MT"));
        ui.add(grid, 11, "Call mode", ui.field("state.callMode", "COMMAND"));
        ui.add(grid, 12, "Lifecycle", ui.field("state.modemLifecycle", "READY"));
        ui.add(grid, 13, "Freeze", ui.field("state.freezeMode", "NONE"));
        ui.add(grid, 14, "Lines", lines);
        Button apply = ui.button("state.apply", "Apply");
        apply.setOnAction(event -> handle(controller.applyState(GuiStatePatchFactory.fromValues(
                simState.getValue(), pinRetries.getText(), pukRetries.getText(), networkStat.getText(),
                cregN.getText(), lac.getText(), ci.getText(), act.getText(), signal.getText(), lines.getText()))));
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
        return new HBox(8,
                faultButton("fault.networkOutage", "Network outage", "network-outage"),
                faultButton("fault.networkRestore", "Network restore", "network-restore"),
                faultButton("fault.modemReboot", "Reboot", "modem-reboot"),
                faultButton("fault.modemFreeze", "Freeze", "modem-freeze"),
                faultButton("fault.modemUnfreeze", "Unfreeze", "modem-unfreeze"));
    }

    private Node macroPane() {
        return new VBox(8,
                ui.field("macro.file", "macros.xml"),
                ui.label("macro.hash", "sha256:"),
                ui.label("macro.errors", ""),
                ui.label("macro.customResponses", ""),
                new HBox(8, ui.button("macro.reload", "Reload"), ui.button("macro.enable", "Enable"),
                        ui.button("macro.disable", "Disable")));
    }

    private Node replayPane() {
        Label hashStatus = ui.label("replay.hashStatus", "hashes pending");
        Label divergences = ui.label("replay.divergences", "");
        CheckBox virtualClock = ui.register(new CheckBox("Virtual clock"), "replay.virtualClock");
        virtualClock.setSelected(true);
        Button validate = ui.button("replay.validate", "Validate");
        Button play = ui.button("replay.playToDte", "Play to DTE");
        validate.setOnAction(event -> {
            hashStatus.setText("hash comparison pending");
            divergences.setText("No replay file loaded");
        });
        play.setOnAction(event -> divergences.setText("Replay-to-DTE requires connected DTE"));
        return new VBox(8,
                ui.combo("replay.mode", "validate-recompute", "drive-from-captured-input", "play-to-dte"),
                ui.field("replay.logFile", "logs/session.jsonl"),
                hashStatus, virtualClock, divergences,
                new HBox(8, validate, play));
    }

    private Node exportPane() {
        Button jsonl = ui.button("export.jsonl", "JSONL");
        Button transcript = ui.button("export.transcript", "Transcript");
        Button coverage = ui.button("export.coverage", "Coverage");
        jsonl.setOnAction(event -> export.setText(controller.jsonl(events)));
        transcript.setOnAction(event -> export.setText(controller.transcript(events)));
        coverage.setOnAction(event -> export.setText("events=" + events.size()));
        HBox buttons = new HBox(8, jsonl, transcript, coverage);
        return new VBox(8, buttons, export);
    }

    private void handle(SessionResponse response) {
        events.addAll(response.events());
        if (!response.output().isEmpty()) {
            output.appendText(response.outputAscii().replace("\r", "\\r").replace("\n", "\\n\n"));
        }
        refreshState();
    }

    private void refreshState() {
        ModemState state = controller.snapshot();
        stateSummary.setText("SIM " + state.sim().state() + "  NET "
                + (state.network() == null ? "n/a" : state.network().stat())
                + "  CALL " + state.call().mode());
    }

    private void handleUnsafe(Action action) {
        try {
            handle(action.run());
        } catch (RuntimeException e) {
            output.appendText(e.getMessage() + "\n");
        }
    }

    private Button faultButton(String id, String text, String type) {
        Button button = ui.button(id, text);
        button.setOnAction(event -> handle(controller.fault(type)));
        return button;
    }

    @FunctionalInterface
    private interface Action {
        SessionResponse run();
    }
}
