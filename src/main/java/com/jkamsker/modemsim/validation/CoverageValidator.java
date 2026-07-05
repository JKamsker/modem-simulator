package com.jkamsker.modemsim.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;

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
            "3gpp-27007-r18", union(HAYES, CELLULAR),
            "3gpp-27005-r16", union(HAYES, SMS),
            "sierra-common", SIERRA,
            "sierra-hl6-hl8-v20", SIERRA,
            "westermo-common", WESTERMO_ANALOG,
            "westermo-td22-6177-2203", WESTERMO_ANALOG,
            "westermo-td36-6618-2202", WESTERMO_ANALOG,
            "westermo-gd01-6196-2220", WESTERMO_CELLULAR,
            "westermo-gdw11-6615-2220", WESTERMO_CELLULAR);

    private final JsonSchemaValidator schemaValidator = new JsonSchemaValidator();

    public ValidationReport validate(Path coveragePath) {
        return validateDocument(coveragePath).report();
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
        CoverageDocument document;
        try {
            document = validateDocument(path);
        } catch (ValidationException e) {
            report.error(path.getFileName() + ": " + e.getMessage());
            return;
        }
        ValidationReport fileReport = document.report();
        report.merge(fileReport);
        if (fileReport.valid()) {
            String profile = document.root().path("profile").asText();
            if (!V1_TARGETS.contains(profile)) {
                report.error("Unexpected v1 coverage report for " + profile);
            }
            validateBuiltinCoverage(document.root(), report);
            seen.add(profile);
        }
    }

    private CoverageDocument validateDocument(Path path) {
        CoverageDocument document = readAndValidate(path);
        ValidationReport report = document.report();
        if (report.valid()) {
            int commandsTotal = document.root().path("commands_total").asInt();
            if (commandsTotal != document.root().path("commands").size()) {
                report.error("commands_total must equal commands length");
            }
            validateStatusCounts(document.root(), report);
            validateRequiredCommands(document.root(), report);
            validateHandlers(document.root(), report);
            if (document.root().path("unknown").asInt(-1) != 0) {
                report.error("coverage unknown must be zero");
            }
        }
        return document;
    }

    private CoverageDocument readAndValidate(Path path) {
        JsonSchemaValidator.JsonDocument document = schemaValidator.validateJsonDocument(
                path, SchemaLocator.schemaPath("coverage.schema.json"));
        return new CoverageDocument(document.root(), document.report());
    }

    private record CoverageDocument(JsonNode root, ValidationReport report) {
    }

    private void validateBuiltinCoverage(JsonNode root, ValidationReport report) {
        String profile = root.path("profile").asText();
        if (!V1_TARGETS.contains(profile)) {
            return;
        }
        var coverage = BuiltinProfiles.byId(profile).coverage();
        if (coverage == null) {
            report.error(profile + ": missing builtin coverage metadata");
            return;
        }
        if (root.path("commands_total").asInt() != coverage.commandsTotal()) {
            report.error(profile + ": commands_total must match builtin coverage commandsTotal");
        }
        Set<String> manifest = new HashSet<>();
        root.path("commands").forEach(command -> manifest.add(command.path("command").asText()));
        Set<String> builtin = new HashSet<>();
        coverage.commands().forEach(command -> builtin.add(command.name()));
        if (!manifest.equals(builtin)) {
            report.error(profile + ": commands must match builtin coverage command list");
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
        for (String status : Set.of("implemented_full", "implemented_stub",
                "unsupported_declared", "not_applicable", "unknown")) {
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
