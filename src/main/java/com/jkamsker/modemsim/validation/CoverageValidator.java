package com.jkamsker.modemsim.validation;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class CoverageValidator {
    private static final Set<String> HAYES = Set.of(
        "AT", "A/", "+++", "ATE", "ATQ", "ATV", "ATZ", "AT&F", "AT&W", "AT&V",
            "AT&D", "AT&C", "ATA", "ATD", "ATH", "ATO", "ATS", "ATI");
    private static final Set<String> CELLULAR = Set.of(
            "+CGMI", "+CGMM", "+CGMR", "+CGSN", "+CPIN", "+CMEE", "+CREG", "+CGREG",
            "+CEREG", "+CSQ", "+COPS", "+CCLK", "+CFUN");
    private static final Set<String> SMS = Set.of(
            "+CMGF", "+CMGS", "+CMGR", "+CMGL", "+CMGD", "+CNMI", "+CPMS", "+CSCA", "+CSCS");
    private static final Set<String> SIERRA_VENDOR = Set.of("+KCNXCFG", "+KCNXTIMER", "+WDSI", "+WDSR", "+KSIMSLOT");
    private static final Set<String> WESTERMO_VENDOR = Set.of("+WIND", "+WIOR", "+WIOW", "+STSF", "+CCED");
    private static final Set<String> SIERRA = union(HAYES, CELLULAR, SMS, SIERRA_VENDOR);
    private static final Set<String> WESTERMO_ANALOG = HAYES;
    private static final Set<String> WESTERMO_CELLULAR = union(HAYES, CELLULAR, SMS, WESTERMO_VENDOR);
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
            "generic-hayes-v250", HAYES,
            "3gpp-27007-r18", CELLULAR,
            "3gpp-27005-r16", SMS,
            "sierra-common", SIERRA,
            "sierra-hl6-hl8-v20", SIERRA,
            "westermo-common", WESTERMO_ANALOG,
            "westermo-td22-6177-2203", WESTERMO_ANALOG,
            "westermo-td36-6618-2202", WESTERMO_ANALOG,
            "westermo-gd01-6196-2220", WESTERMO_CELLULAR,
            "westermo-gdw11-6615-2220", WESTERMO_CELLULAR);

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
        validateHandlers(root, report);
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
            String profile = schemaValidator.readJson(path).path("profile").asText();
            if (!V1_TARGETS.contains(profile)) {
                report.error("Unexpected v1 coverage report for " + profile);
            }
            seen.add(profile);
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

    private void validateHandlers(JsonNode root, ValidationReport report) {
        root.path("commands").forEach(command -> {
            String status = command.path("status").asText();
            if (!status.startsWith("implemented_")) {
                return;
            }
            String name = command.path("command").asText();
            String handler = command.path("handler").asText();
            if (handler.isBlank()) {
                report.error("implemented command missing handler: " + name);
            } else if (!handler.equals(expectedHandler(name))) {
                report.error("handler mismatch for " + name + ": " + handler);
            }
        });
    }

    private String expectedHandler(String command) {
        if (SMS.contains(command)) {
            return "SmsHandler";
        }
        if (CELLULAR.contains(command)) {
            return "CellularHandler";
        }
        return "HayesHandler";
    }

    @SafeVarargs
    private static Set<String> union(Set<String>... sets) {
        Set<String> result = new HashSet<>();
        for (Set<String> set : sets) {
            result.addAll(set);
        }
        return Set.copyOf(result);
    }
}
