package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.SimState;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

final class SimulatorView {
    private final HeadlessSession session = new HeadlessSession("gui", BuiltinProfiles.acceptanceSierra(), 12345);
    private final ObservableList<ModemEvent> events = FXCollections.observableArrayList();
    private final Map<String, Node> controls = new HashMap<>();
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
                tab("Session", sessionPane()),
                tab("Live Log", logPane()),
                tab("State", statePane()),
                tab("Injection", injectionPane()),
                tab("Faults", faultPane()),
                tab("Macros", macroPane()),
                tab("Replay", replayPane()),
                tab("Export", exportPane()));
        CheckBox readOnly = new CheckBox("Read-only");
        readOnly.setId("session.readOnly");
        readOnly.selectedProperty().addListener((ignored, oldValue, newValue) -> applyReadOnly(newValue));
        BorderPane root = new BorderPane(tabs);
        root.setTop(new HBox(12, readOnly, stateSummary));
        return root;
    }

    private Node sessionPane() {
        GridPane grid = grid();
        add(grid, 0, "Profile", combo("session.profile", "sierra-hl6-hl8-v20", "westermo-td22-6177-2203"));
        add(grid, 1, "Main port", field("session.mainPort", "COM7"));
        add(grid, 2, "Sniffer port", field("session.snifferPort", ""));
        add(grid, 3, "Manual DCE port", field("session.manualDcePort", ""));
        add(grid, 4, "Baud", field("session.baudRate", "115200"));
        add(grid, 5, "Data bits", combo("session.dataBits", "8", "7", "6", "5"));
        add(grid, 6, "Stop bits", combo("session.stopBits", "1", "2"));
        add(grid, 7, "Parity", combo("session.parity", "NONE", "EVEN", "ODD", "MARK", "SPACE"));
        add(grid, 8, "Flow", combo("session.flowControl", "NONE", "RTS_CTS", "XON_XOFF"));
        HBox buttons = new HBox(8,
                button("session.start", "Start"),
                button("session.stop", "Stop"),
                button("session.reconnect", "Reconnect"),
                label("session.portLost", "Port OK"));
        return new VBox(12, grid, buttons, output);
    }

    private Node logPane() {
        TableView<ModemEvent> table = register(new TableView<>(events), "log.table");
        table.getColumns().add(column("Seq", event -> Long.toString(event.sequence())));
        table.getColumns().add(column("Type", event -> event.eventType().name()));
        table.getColumns().add(column("Dir", event -> event.direction().name()));
        table.getColumns().add(column("Raw", ModemEvent::rawHex));
        table.getColumns().add(column("Text", event -> value(event.textEscaped())));
        table.getColumns().add(column("Parsed", event -> value(event.parsedCommand())));
        table.getColumns().add(column("Result", event -> value(event.result())));
        table.getColumns().add(column("Redaction", event -> Boolean.toString(event.redaction().applied())));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        Button exportButton = button("log.export", "Export");
        exportButton.setOnAction(event -> export.setText(jsonl()));
        VBox.setVgrow(table, Priority.ALWAYS);
        return new VBox(8, table, exportButton);
    }

    private Node statePane() {
        GridPane grid = grid();
        add(grid, 0, "SIM", combo("state.simState", names(SimState.values())));
        add(grid, 1, "PIN retries", field("state.pinRetries", "3"));
        add(grid, 2, "PUK retries", field("state.pukRetries", "10"));
        add(grid, 3, "Network stat", field("state.networkStat", "1"));
        add(grid, 4, "CREG n", field("state.cregN", "2"));
        add(grid, 5, "LAC", field("state.lac", "00C3"));
        add(grid, 6, "CI", field("state.ci", "00001234"));
        add(grid, 7, "AcT", field("state.act", "7"));
        add(grid, 8, "Reject cause", field("state.rejectCause", ""));
        add(grid, 9, "Signal", field("state.signal", "18,0"));
        add(grid, 10, "SMS store", combo("state.smsStorage", "ME", "SM", "MT"));
        add(grid, 11, "Call mode", field("state.callMode", "COMMAND"));
        add(grid, 12, "Lifecycle", field("state.modemLifecycle", "READY"));
        add(grid, 13, "Freeze", field("state.freezeMode", "NONE"));
        add(grid, 14, "Lines", field("state.lines", "DTR DSR RTS CTS"));
        return grid;
    }

    private Node injectionPane() {
        TextField rawDte = field("inject.rawDteToDce", "AT\\r");
        TextField rawDce = field("inject.rawDceToDte", "+CREG: 4\\r\\n");
        TextField parsed = field("inject.parsedCommand", "AT+CSQ");
        TextField urc = field("inject.urc", "+CREG: 4");
        TextField patch = field("inject.statePatch", "network.stat=4");
        CheckBox safety = register(new CheckBox("Confirm DCE transmit"), "inject.safetyConfirm");
        Button send = button("inject.send", "Send DTE");
        send.setOnAction(event -> inject(rawDte.getText()));
        return new VBox(8, rawDte, rawDce, parsed, urc, patch, safety, send);
    }

    private Node faultPane() {
        return new HBox(8,
                button("fault.networkOutage", "Network outage"),
                button("fault.networkRestore", "Network restore"),
                button("fault.modemReboot", "Reboot"),
                button("fault.modemFreeze", "Freeze"),
                button("fault.modemUnfreeze", "Unfreeze"));
    }

    private Node macroPane() {
        return new VBox(8,
                field("macro.file", "macros.xml"),
                label("macro.hash", "sha256:"),
                label("macro.errors", ""),
                label("macro.customResponses", ""),
                new HBox(8, button("macro.reload", "Reload"), button("macro.enable", "Enable"),
                        button("macro.disable", "Disable")));
    }

    private Node replayPane() {
        return new VBox(8,
                combo("replay.mode", "validate-recompute", "drive-from-captured-input", "play-to-dte"),
                field("replay.logFile", "logs/session.jsonl"),
                label("replay.hashStatus", "hashes pending"),
                new HBox(8, button("replay.validate", "Validate"), button("replay.playToDte", "Play to DTE")));
    }

    private Node exportPane() {
        Button jsonl = button("export.jsonl", "JSONL");
        jsonl.setOnAction(event -> export.setText(jsonl()));
        HBox buttons = new HBox(8, jsonl, button("export.transcript", "Transcript"), button("export.coverage", "Coverage"));
        return new VBox(8, buttons, export);
    }

    private void inject(String text) {
        SessionResponse response = session.receive(RawBytes.ascii(unescape(text)));
        events.addAll(response.events());
        if (!response.output().isEmpty()) {
            output.appendText(response.outputAscii().replace("\r", "\\r").replace("\n", "\\n\n"));
        }
        refreshState();
    }

    private void refreshState() {
        ModemState state = session.snapshot();
        stateSummary.setText("SIM " + state.sim().state() + "  NET "
                + (state.network() == null ? "n/a" : state.network().stat())
                + "  CALL " + state.call().mode());
    }

    private String jsonl() {
        return events.stream()
                .map(event -> "{\"sequence\":" + event.sequence()
                        + ",\"eventType\":\"" + event.eventType()
                        + "\",\"direction\":\"" + event.direction()
                        + "\",\"rawHex\":\"" + escapeJson(event.rawHex())
                        + "\",\"textEscaped\":\"" + escapeJson(value(event.textEscaped()))
                        + "\",\"redactionApplied\":" + event.redaction().applied() + "}")
                .collect(Collectors.joining("\n"));
    }

    private void applyReadOnly(boolean readOnly) {
        Map<String, Boolean> enabled = new ReadOnlyPolicy().apply(new GuiControlCatalog().controls(), readOnly)
                .stream()
                .collect(Collectors.toMap(GuiControl::id, GuiControl::enabled));
        controls.forEach((id, node) -> {
            if (enabled.containsKey(id)) {
                node.setDisable(!enabled.get(id));
            }
        });
    }

    private Tab tab(String title, Node content) {
        Tab tab = new Tab(title, content);
        tab.setClosable(false);
        return tab;
    }

    private GridPane grid() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        return grid;
    }

    private void add(GridPane grid, int row, String label, Node control) {
        grid.add(new Label(label), 0, row);
        grid.add(control, 1, row);
    }

    private TextField field(String id, String value) {
        TextField field = register(new TextField(value), id);
        field.setPrefColumnCount(24);
        return field;
    }

    private ComboBox<String> combo(String id, String... values) {
        ComboBox<String> combo = register(new ComboBox<>(FXCollections.observableArrayList(values)), id);
        if (values.length > 0) {
            combo.getSelectionModel().selectFirst();
        }
        return combo;
    }

    private Label label(String id, String text) {
        return register(new Label(text), id);
    }

    private Button button(String id, String text) {
        return register(new Button(text), id);
    }

    private <T extends Node> T register(T node, String id) {
        node.setId(id);
        controls.put(id, node);
        return node;
    }

    private TableColumn<ModemEvent, String> column(String name, Function<ModemEvent, String> extractor) {
        TableColumn<ModemEvent, String> column = new TableColumn<>(name);
        column.setCellValueFactory(cell -> new ReadOnlyStringWrapper(extractor.apply(cell.getValue())));
        return column;
    }

    private String[] names(Object[] values) {
        String[] names = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            names[i] = values[i].toString();
        }
        return names;
    }

    private String unescape(String value) {
        return value.replace("\\r", "\r").replace("\\n", "\n").replace("\\u001A", "\u001A");
    }

    private String value(Object value) {
        return value == null ? "" : value.toString();
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
