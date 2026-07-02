package com.alegs3.modemsim.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public final class SimulatorApplication extends Application {
    @Override
    public void start(Stage stage) {
        TabPane tabs = new TabPane(
                tab("Session", "session.profile"),
                tab("Live Log", "log.table"),
                tab("State", "state.simState"),
                tab("Injection", "inject.rawDteToDce"),
                tab("Faults", "fault.networkOutage"),
                tab("Macros", "macro.reload"),
                tab("Replay", "replay.mode"),
                tab("Export", "export.jsonl"));
        stage.setTitle("Modem Simulator");
        stage.setScene(new Scene(new BorderPane(tabs), 1100, 720));
        stage.show();
    }

    private Tab tab(String title, String id) {
        BorderPane pane = new BorderPane();
        pane.setId(id);
        Tab tab = new Tab(title, pane);
        tab.setClosable(false);
        return tab;
    }
}
