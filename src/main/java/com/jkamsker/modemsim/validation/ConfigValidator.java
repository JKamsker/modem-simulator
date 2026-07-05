package com.jkamsker.modemsim.validation;

import com.fasterxml.jackson.databind.JsonNode;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public final class ConfigValidator {
    private final JsonSchemaValidator schemaValidator = new JsonSchemaValidator();

    public ValidationReport validate(Path configPath) {
        ValidationReport report = schemaValidator.validateYaml(
                configPath, SchemaLocator.schemaPath("config.schema.json"));
        if (!report.valid()) {
            return report;
        }
        JsonNode root = schemaValidator.readYaml(configPath);
        validatePorts(root, report);
        validateClockMode(root, report);
        return report;
    }

    private void validatePorts(JsonNode root, ValidationReport report) {
        JsonNode ports = root.path("ports");
        int modemPorts = 0;
        Set<String> optionalRoles = new HashSet<>();
        boolean allowUnsafeDce = root.path("gui").path("allowUnsafeDceTransmit").asBoolean(false);
        for (JsonNode port : ports) {
            String role = port.path("role").asText();
            if (role.equals("modem-simulation")) {
                modemPorts++;
                if (!port.path("enabled").asBoolean(false)) {
                    report.error("modem-simulation port must be enabled");
                }
            } else if (!optionalRoles.add(role)) {
                report.error("optional port role appears more than once: " + role);
            }
            if (role.equals("manual-dce-injection") && port.path("enabled").asBoolean(false) && !allowUnsafeDce) {
                report.error("manual-dce-injection requires gui.allowUnsafeDceTransmit=true");
            }
            rejectPortSerialOverride(port, report);
        }
        if (modemPorts != 1) {
            report.error("exactly one modem-simulation port is required");
        }
    }

    private void rejectPortSerialOverride(JsonNode port, ValidationReport report) {
        for (String field : new String[] {"baudRate", "parity", "dataBits", "stopBits", "flowControl"}) {
            if (port.has(field)) {
                report.error("serialLine field must not be configured per port: " + field);
            }
        }
    }

    private void validateClockMode(JsonNode root, ValidationReport report) {
        if (!root.path("clockMode").asText("monotonic").equals("virtual")) {
            return;
        }
        for (JsonNode port : root.path("ports")) {
            if (port.path("enabled").asBoolean(false) && port.path("type").asText().equals("serial")) {
                report.error("clockMode virtual is only valid for headless/replay ports; serial ports require monotonic");
            }
        }
    }
}
