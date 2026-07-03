package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.app.RuntimeSessionLauncher;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;

import java.nio.file.Path;

record GuiSessionOptions(
        String profile,
        String mainPort,
        String snifferPort,
        String manualDcePort,
        Path initialScenario,
        long seed,
        SerialConfig serialLine
) {
    static GuiSessionOptions of(
            String profile, String mainPort, String snifferPort, String manualDcePort, String scenario,
            String seed, String baudRate, String dataBits, String stopBits, String parity, String flowControl) {
        return new GuiSessionOptions(
                blank(profile) ? "sierra-hl6-hl8-v20" : profile.trim(),
                clean(mainPort),
                clean(snifferPort),
                clean(manualDcePort),
                blank(scenario) ? null : Path.of(scenario.trim()),
                blank(seed) ? 12345L : Long.parseLong(seed.trim()),
                new SerialConfig(
                        integer(baudRate, 115200),
                        integer(dataBits, 8),
                        integer(stopBits, 1),
                        Parity.valueOf(blank(parity) ? "NONE" : parity.trim()),
                        FlowControl.valueOf(blank(flowControl) ? "NONE" : flowControl.trim())));
    }

    static GuiSessionOptions headless() {
        return of("sierra-hl6-hl8-v20", "headless", "", "", "", "12345", "115200", "8", "1", "NONE", "NONE");
    }

    RuntimeSessionLauncher.Options runtimeOptions(boolean allowUnsafeDceTransmit) {
        return new RuntimeSessionLauncher.Options(
                profile, mainPort, snifferPort, manualDcePort, initialScenario, serialLine, seed, allowUnsafeDceTransmit);
    }

    boolean startsRuntime() {
        return !blank(mainPort) && !mainPort.equalsIgnoreCase("headless");
    }

    String eventPort() {
        return blank(mainPort) ? "gui-headless" : mainPort;
    }

    private static int integer(String value, int fallback) {
        return blank(value) ? fallback : Integer.parseInt(value.trim());
    }

    private static String clean(String value) {
        return blank(value) ? "" : value.trim();
    }

    private static boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
