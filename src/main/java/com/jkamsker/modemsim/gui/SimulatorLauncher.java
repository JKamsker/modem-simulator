package com.jkamsker.modemsim.gui;

import javafx.application.Application;

public final class SimulatorLauncher {
    private SimulatorLauncher() {
    }

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("--headless-smoke")) {
            GuiAcceptanceHarness.main(new String[] {"live-log-filter-export"});
            return;
        }
        Application.launch(SimulatorApplication.class, args);
    }
}
