package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.state.SimState;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

final class GuiStatePane {
    private final GuiViewSupport ui;
    private final GuiSessionController controller;
    private final Consumer<SessionResponse> handler;

    GuiStatePane(GuiViewSupport ui, GuiSessionController controller, Consumer<SessionResponse> handler) {
        this.ui = ui;
        this.controller = controller;
        this.handler = handler;
    }

    Node node() {
        GridPane grid = ui.grid();
        ComboBox<String> simState = ui.combo("state.simState", ui.names(SimState.values()));
        TextField pinRetries = ui.field("state.pinRetries", "3");
        TextField pukRetries = ui.field("state.pukRetries", "10");
        ComboBox<String> networkStat = ui.combo("state.networkStat", networkStates());
        TextField cregN = ui.field("state.cregN", "2");
        TextField lac = ui.field("state.lac", "00C3");
        TextField ci = ui.field("state.ci", "00001234");
        TextField act = ui.field("state.act", "7");
        TextField rejectCauseType = ui.field("state.rejectCauseType", "");
        TextField rejectCause = ui.field("state.rejectCause", "");
        TextField signal = ui.field("state.signal", "18,0");
        Slider rssi = ui.slider("state.signalRssi", 0, 31, 18);
        bindSignal(signal, rssi);
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
        ui.add(grid, 11, "RSSI", rssi);
        ui.add(grid, 12, "SMS store", smsStorage);
        ui.add(grid, 13, "Call mode", callMode);
        ui.add(grid, 14, "Lifecycle", lifecycle);
        ui.add(grid, 15, "Freeze", freeze);
        ui.add(grid, 16, "Lines", lines);
        Button apply = ui.button("state.apply", "Apply");
        apply.setOnAction(event -> handler.accept(controller.applyState(GuiStatePatchFactory.fromValues(
                simState.getValue(), pinRetries.getText(), pukRetries.getText(), networkStat.getValue(),
                cregN.getText(), lac.getText(), ci.getText(), act.getText(),
                signal.getText(), rejectCauseType.getText(), rejectCause.getText(),
                smsStorage.getValue(), callMode.getText(), lifecycle.getText(), freeze.getText(), lines.getText()))));
        return new VBox(8, grid, apply);
    }

    private String[] networkStates() {
        return new String[] {
                "0 - not registered",
                "1 - registered home",
                "2 - searching",
                "3 - registration denied",
                "4 - unknown",
                "5 - registered roaming",
                "10 - emergency only",
                "11 - registered SMS only"
        };
    }

    private void bindSignal(TextField signal, Slider rssi) {
        rssi.setMajorTickUnit(5);
        rssi.setMinorTickCount(0);
        rssi.setShowTickMarks(true);
        rssi.valueProperty().addListener((ignored, old, value) -> {
            String ber = signal.getText().contains(",") ? signal.getText().split(",", 2)[1].trim() : "0";
            signal.setText(Math.round(value.doubleValue()) + "," + ber);
        });
        signal.textProperty().addListener((ignored, old, value) -> rssi.setValue(rssi(value, rssi.getValue())));
    }

    private double rssi(String value, double fallback) {
        try {
            return value == null || value.isBlank() ? fallback : Integer.parseInt(value.split(",", 2)[0].trim());
        } catch (RuntimeException e) {
            return fallback;
        }
    }
}
