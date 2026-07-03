package com.jkamsker.modemsim.validation;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class CoverageValidator {
    private static final Set<String> V1_TARGETS = Set.of(
            "generic-hayes-v250",
            "3gpp-27007-r18",
            "3gpp-27005-r16",
            "sierra-common",
            "sierra-hl6-hl8-v20",
            "westermo-common",
            "westermo-td22-6177-2203",
            "westermo-td36-6618-2202",
            "westermo-gd01-6196-2220",
            "westermo-gdw11-6615-2220");
    private static final Map<String, Set<String>> REQUIRED_COMMANDS = Map.of(
            "generic-hayes-v250", Set.of(
                    "AT", "A/", "+++", "ATE", "ATQ", "ATV", "ATZ", "AT&F", "AT&W", "AT&V",
                    "AT&D", "AT&C", "ATD", "ATH", "ATO", "ATS"),
            "3gpp-27007-r18", Set.of(
                    "+CGMI", "+CGMM", "+CGMR", "+CGSN", "+CPIN", "+CMEE", "+CREG", "+CGREG",
                    "+CEREG", "+CSQ", "+COPS", "+CCLK", "+CFUN"),
            "3gpp-27005-r16", Set.of(
                    "+CMGF", "+CMGS", "+CMGR", "+CMGL", "+CMGD", "+CNMI", "+CPMS", "+CSCA", "+CSCS"));

    private final JsonSchemaValidator schemaValidator = new JsonSchemaValidator();

    public ValidationReport validate(Path coveragePath) {
        ValidationReport report = schemaValidator.validateJson(
                coveragePath, SchemaLocator.schemaPath("coverage.schema.json"));
        if (!report.valid()) {
            return report;
        }
        JsonNode root = schemaValidator.readJson(coveragePath);
        int commandsTotal = root.path("commands_total").asInt();
        int commandCount = root.path("commands").size();
        if (commandsTotal != commandCount) {
            report.error("commands_total must equal commands length");
        }
        validateStatusCounts(root, report);
        validateRequiredCommands(root, report);
        if (root.path("unknown").asInt(-1) != 0) {
            report.error("coverage unknown must be zero");
        }
        return report;
    }

    public ValidationReport verifyV1Targets(Path directory) {
        ValidationReport report = ValidationReport.ok();
        Set<String> seen = new HashSet<>();
        try (var files = Files.list(directory)) {
            files.filter(path -> path.getFileName().toString().endsWith(".json"))
                    .forEach(path -> validateCoverageFile(path, report, seen));
        } catch (IOException e) {
            report.error("Cannot read coverage directory: " + e.getMessage());
        }
        for (String target : V1_TARGETS) {
            if (!seen.contains(target)) {
                report.error("Missing v1 coverage report for " + target);
            }
        }
        return report;
    }

    public Set<String> v1Targets() {
        return V1_TARGETS;
    }

    private void validateCoverageFile(Path path, ValidationReport report, Set<String> seen) {
        ValidationReport fileReport = validate(path);
        report.merge(fileReport);
        if (fileReport.valid()) {
            seen.add(schemaValidator.readJson(path).path("profile").asText());
        }
    }

    private void validateStatusCounts(JsonNode root, ValidationReport report) {
        Map<String, Integer> counts = new java.util.LinkedHashMap<>();
        Set<String> commands = new HashSet<>();
        root.path("commands").forEach(command -> {
            String name = command.path("command").asText();
            if (!commands.add(name)) {
                report.error("duplicate command in coverage: " + name);
            }
            String status = command.path("status").asText();
            counts.put(status, counts.getOrDefault(status, 0) + 1);
        });
        for (String status : Set.of("implemented_full", "implemented_stub", "unsupported_declared", "not_applicable")) {
            if (root.has(status) && root.path(status).asInt() != counts.getOrDefault(status, 0)) {
                report.error(status + " count must equal commands with that status");
            }
        }
    }

    private void validateRequiredCommands(JsonNode root, ValidationReport report) {
        Set<String> required = REQUIRED_COMMANDS.get(root.path("profile").asText());
        if (required == null) {
            return;
        }
        Set<String> commands = new HashSet<>();
        root.path("commands").forEach(command -> commands.add(command.path("command").asText()));
        for (String command : required) {
            if (!commands.contains(command)) {
                report.error("missing required coverage command: " + command);
            }
        }
    }
}
