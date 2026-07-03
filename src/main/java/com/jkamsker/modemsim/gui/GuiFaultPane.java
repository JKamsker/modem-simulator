package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.macros.FaultAction;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.state.FreezeMode;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

final class GuiFaultPane {
    private final GuiViewSupport ui;
    private final GuiSessionController controller;
    private final java.util.function.Consumer<SessionResponse> handler;

    GuiFaultPane(GuiViewSupport ui, GuiSessionController controller,
                 java.util.function.Consumer<SessionResponse> handler) {
        this.ui = ui;
        this.controller = controller;
        this.handler = handler;
    }

    Node node() {
        TextField duration = ui.field("fault.durationMs", "3000");
        TextField stat = ui.field("fault.stat", "1");
        TextField rssi = ui.field("fault.rssi", "18");
        TextField ber = ui.field("fault.ber", "0");
        ComboBox<String> freeze = ui.combo("fault.freezeMode", ui.names(FreezeMode.values()));
        GridPane grid = ui.grid();
        ui.add(grid, 0, "Duration ms", duration);
        ui.add(grid, 1, "Network stat", stat);
        ui.add(grid, 2, "RSSI", rssi);
        ui.add(grid, 3, "BER", ber);
        ui.add(grid, 4, "Freeze mode", freeze);
        HBox buttons = new HBox(8,
                faultButton("fault.networkOutage", "Network outage", "network-outage", duration, stat, rssi, ber, freeze),
                faultButton("fault.networkRestore", "Network restore", "network-restore", duration, stat, rssi, ber, freeze),
                faultButton("fault.modemReboot", "Reboot", "modem-reboot", duration, stat, rssi, ber, freeze),
                faultButton("fault.modemFreeze", "Freeze", "modem-freeze", duration, stat, rssi, ber, freeze),
                faultButton("fault.modemUnfreeze", "Unfreeze", "modem-unfreeze", duration, stat, rssi, ber, freeze));
        return new VBox(8, grid, buttons);
    }

    private Button faultButton(String id, String text, String type, TextField duration, TextField stat,
                               TextField rssi, TextField ber, ComboBox<String> freeze) {
        Button button = ui.button(id, text);
        button.setOnAction(event -> handler.accept(controller.fault(new FaultAction(
                type, integer(duration), integer(stat), integer(rssi), integer(ber),
                FreezeMode.valueOf(freeze.getValue())))));
        return button;
    }

    private Integer integer(TextField field) {
        return field.getText() == null || field.getText().isBlank() ? null : Integer.parseInt(field.getText());
    }
}
