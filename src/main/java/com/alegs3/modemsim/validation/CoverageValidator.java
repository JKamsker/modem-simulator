package com.alegs3.modemsim.validation;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
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
}
