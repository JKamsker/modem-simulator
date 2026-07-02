package com.alegs3.modemsim.validation;

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
        validatePorts(root.path("ports"), report);
        return report;
    }

    private void validatePorts(JsonNode ports, ValidationReport report) {
        int modemPorts = 0;
        Set<String> optionalRoles = new HashSet<>();
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
}
