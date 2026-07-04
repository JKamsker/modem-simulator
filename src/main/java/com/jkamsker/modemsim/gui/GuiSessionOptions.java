package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.app.RuntimeSessionLauncher;
import com.jkamsker.modemsim.app.RuntimeTimer;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

record GuiSessionOptions(
        String profile,
        String mainPort,
        String snifferPort,
        String manualDcePort,
        Path initialScenario,
        long seed,
        SerialConfig serialLine,
        List<RuntimeTimer> macroTimers
) {
    GuiSessionOptions {
        macroTimers = macroTimers == null ? List.of() : List.copyOf(macroTimers);
    }

    static GuiSessionOptions of(
            String profile, String mainPort, String snifferPort, String manualDcePort, String scenario,
            String seed, String baudRate, String dataBits, String stopBits, String parity, String flowControl) {
        return create(profile, mainPort, snifferPort, manualDcePort, scenario, seed, baudRate,
                integer(dataBits, 8), integer(stopBits, 1),
                Parity.valueOf(blank(parity) ? "NONE" : parity.trim()), flowControl);
    }

    static GuiSessionOptions ofFormat(
            String profile, String mainPort, String snifferPort, String manualDcePort, String scenario,
            String seed, String baudRate, String dataFormat, String flowControl) {
        return create(profile, mainPort, snifferPort, manualDcePort, scenario, seed, baudRate,
                GuiSerialDataFormat.dataBits(dataFormat), GuiSerialDataFormat.stopBits(dataFormat),
                GuiSerialDataFormat.parity(dataFormat), flowControl);
    }

    private static GuiSessionOptions create(
            String profile, String mainPort, String snifferPort, String manualDcePort, String scenario,
            String seed, String baudRate, int dataBits, int stopBits, Parity parity, String flowControl) {
        return new GuiSessionOptions(
                blank(profile) ? "sierra-hl6-hl8-v20" : profile.trim(),
                clean(mainPort),
                clean(snifferPort),
                clean(manualDcePort),
                blank(scenario) ? null : Path.of(scenario.trim()),
                blank(seed) ? 12345L : Long.parseLong(seed.trim()),
                new SerialConfig(
                        integer(baudRate, 115200),
                        dataBits,
                        stopBits,
                        parity,
                        FlowControl.valueOf(blank(flowControl) ? "NONE" : flowControl.trim())),
                List.of());
    }

    static GuiSessionOptions headless() {
        return of("sierra-hl6-hl8-v20", "headless", "", "", "", "12345", "115200", "8", "1", "NONE", "NONE");
    }

    GuiSessionOptions withMacroTimers(List<RuntimeTimer> value) {
        return new GuiSessionOptions(profile, mainPort, snifferPort, manualDcePort,
                initialScenario, seed, serialLine, value);
    }

    RuntimeSessionLauncher.Options runtimeOptions(boolean allowUnsafeDceTransmit) {
        return new RuntimeSessionLauncher.Options(
                profile, mainPort, snifferPort, manualDcePort, initialScenario, serialLine, seed,
                allowUnsafeDceTransmit, macroTimers);
    }

    Set<String> macroTimerIds() {
        return macroTimers.stream().map(RuntimeTimer::id).collect(Collectors.toSet());
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
