package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.session.SessionResponse;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

final class GuiSessionPane {
    private final GuiViewSupport ui;
    private final GuiSessionController controller;
    private final TextArea output;
    private final Consumer<SessionResponse> handler;

    GuiSessionPane(
            GuiViewSupport ui, GuiSessionController controller,
            TextArea output, Consumer<SessionResponse> handler) {
        this.ui = ui;
        this.controller = controller;
        this.output = output;
        this.handler = handler;
    }

    Node node() {
        GridPane grid = ui.grid();
        ComboBox<String> profile = ui.combo("session.profile", "sierra-hl6-hl8-v20", "westermo-td22-6177-2203");
        TextField mainPort = ui.field("session.mainPort", "headless");
        TextField snifferPort = ui.field("session.snifferPort", "");
        TextField manualDcePort = ui.field("session.manualDcePort", "");
        TextField scenario = ui.field("session.initialScenario", "");
        TextField seed = ui.field("session.seed", "12345");
        ComboBox<String> baud = ui.combo("session.baudRate", "1200", "2400", "4800", "9600",
                "19200", "38400", "57600", "115200");
        ComboBox<String> dataFormat = ui.combo("session.dataFormat", GuiSerialDataFormat.values());
        ComboBox<String> flow = ui.combo("session.flowControl", "NONE", "RTS_CTS", "XON_XOFF");
        ui.select(baud, "115200");
        ui.add(grid, 0, "Profile", profile);
        ui.add(grid, 1, "Main port", mainPort);
        ui.add(grid, 2, "Sniffer port", snifferPort);
        ui.add(grid, 3, "Manual DCE port", manualDcePort);
        ui.add(grid, 4, "Scenario", scenario);
        ui.add(grid, 5, "Seed", seed);
        ui.add(grid, 6, "Baud", baud);
        ui.add(grid, 7, "Format", dataFormat);
        ui.add(grid, 8, "Flow", flow);
        java.util.function.Supplier<GuiSessionOptions> options = () -> GuiSessionOptions.ofFormat(
                profile.getValue(), mainPort.getText(), snifferPort.getText(), manualDcePort.getText(),
                scenario.getText(), seed.getText(), baud.getValue(), dataFormat.getValue(), flow.getValue());
        Label status = ui.label("session.portLost", "Port OK");
        Button start = ui.button("session.start", "Start");
        Button stop = ui.button("session.stop", "Stop");
        Button reconnect = ui.button("session.reconnect", "Reconnect");
        start.setOnAction(event -> updateStatus(status, controller.start(options.get())));
        stop.setOnAction(event -> updateStatus(status, controller.stop()));
        reconnect.setOnAction(event -> updateStatus(status, controller.reconnect(options.get())));
        return new VBox(12, grid, new HBox(8, start, stop, reconnect, status), output);
    }

    private void updateStatus(Label status, SessionResponse response) {
        handler.accept(response);
        status.setText(controller.runtimeStatus());
    }
}
