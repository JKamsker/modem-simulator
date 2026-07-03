package com.jkamsker.modemsim.app;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

final class RuntimeFingerprints {
    private RuntimeFingerprints() {
    }

    static Map<String, Object> config(RuntimeConfig config) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("strictOptionalPorts", config.strictOptionalPorts());
        data.put("allowUnsafeDceTransmit", config.allowUnsafeDceTransmit());
        data.put("eventLogPath", pathValue(config.eventLogPath()));
        data.put("initialScenario", pathValue(config.initialScenario()));
        data.put("macros", pathValue(config.macros()));
        data.put("macroTimers", config.macroTimers().stream().map(RuntimeFingerprints::timerData).toList());
        data.put("serialLine", serialData(config.serialLine()));
        data.put("ports", config.ports().stream().map(RuntimeFingerprints::portData).toList());
        return data;
    }

    private static Map<String, Object> timerData(RuntimeTimer timer) {
        return Map.of("id", timer.id(), "atMs", timer.atMs());
    }

    private static Map<String, Object> serialData(com.jkamsker.modemsim.transport.SerialConfig serial) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("baudRate", serial.baudRate());
        data.put("dataBits", serial.dataBits());
        data.put("stopBits", serial.stopBits());
        data.put("parity", serial.parity().name());
        data.put("flowControl", serial.flowControl().name());
        return data;
    }

    private static Map<String, Object> portData(PortBinding port) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", port.id());
        data.put("type", port.type().configName());
        data.put("role", port.role().configName());
        data.put("name", port.name());
        data.put("enabled", port.enabled());
        data.put("profile", port.profile());
        data.put("initialScenario", pathValue(port.initialScenario()));
        data.put("snifferFormat", port.snifferFormat());
        return data;
    }

    private static String pathValue(Path path) {
        return path == null ? null : path.toString();
    }
}
