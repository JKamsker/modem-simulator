package com.jkamsker.modemsim.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class SimulatorApplication extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Modem Simulator");
        stage.setScene(new Scene(new SimulatorView().root(), 1100, 720));
        stage.show();
    }
}
