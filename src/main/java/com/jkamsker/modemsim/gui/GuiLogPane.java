package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.monitor.ModemEvent;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

final class GuiLogPane {
    private final GuiViewSupport ui;
    private final ObservableList<ModemEvent> events;
    private final TextArea export;
    private final GuiSessionController controller;

    GuiLogPane(GuiViewSupport ui, ObservableList<ModemEvent> events, TextArea export, GuiSessionController controller) {
        this.ui = ui;
        this.events = events;
        this.export = export;
        this.controller = controller;
    }

    Node node() {
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
        TextField filter = ui.field("log.filter", "");
        TextArea selection = ui.register(new TextArea(), "log.selection");
        selection.setEditable(false);
        table.getSelectionModel().selectedItemProperty().addListener((ignored, old, event) ->
                selection.setText(event == null ? "" : controller.jsonl(java.util.List.of(event))));
        Button exportButton = ui.button("log.export", "Export");
        exportButton.setOnAction(event -> export.setText(controller.jsonl(events)));
        VBox.setVgrow(table, Priority.ALWAYS);
        return new VBox(8, new HBox(8, filter, exportButton), table, selection);
    }
}
