package com.jkamsker.modemsim.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class SimulatorApplication extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Modem Simulator");
        boolean unsafeDce = Boolean.getBoolean("modemsim.gui.allowUnsafeDceTransmit");
        stage.setScene(new Scene(new SimulatorView(unsafeDce).root(), 1100, 720));
        stage.show();
    }
}
