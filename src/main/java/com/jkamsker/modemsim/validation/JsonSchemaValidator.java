package com.jkamsker.modemsim.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;

import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class JsonSchemaValidator {
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final ObjectMapper YAML = new ObjectMapper(new YAMLFactory());
    private static final ConcurrentMap<Path, JsonSchema> SCHEMAS = new ConcurrentHashMap<>();

    public ValidationReport validateJson(Path documentPath, Path schemaPath) {
        return validate(documentPath, schemaPath, JSON);
    }

    public JsonDocument validateJsonDocument(Path documentPath, Path schemaPath) {
        return validateDocument(documentPath, schemaPath, JSON);
    }

    public ValidationReport validateJson(JsonNode documentNode, Path schemaPath) {
        return validate(documentNode, schemaPath);
    }

    public ValidationReport validateYaml(Path documentPath, Path schemaPath) {
        return validate(documentPath, schemaPath, YAML);
    }

    public JsonNode readJson(Path documentPath) {
        try {
            return JSON.readTree(documentPath.toFile());
        } catch (Exception e) {
            throw new ValidationException("Cannot read JSON: " + documentPath, e);
        }
    }

    public JsonNode readYaml(Path documentPath) {
        try {
            return YAML.readTree(documentPath.toFile());
        } catch (Exception e) {
            throw new ValidationException("Cannot read YAML: " + documentPath, e);
        }
    }

    private ValidationReport validate(Path documentPath, Path schemaPath, ObjectMapper mapper) {
        return validateDocument(documentPath, schemaPath, mapper).report();
    }

    private JsonDocument validateDocument(Path documentPath, Path schemaPath, ObjectMapper mapper) {
        try {
            JsonNode documentNode = mapper.readTree(documentPath.toFile());
            return new JsonDocument(documentNode, validate(documentNode, schemaPath));
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            ValidationReport report = ValidationReport.ok();
            report.error("Cannot read document: " + documentPath + ": " + e.getOriginalMessage());
            return new JsonDocument(null, report);
        } catch (java.io.IOException e) {
            ValidationReport report = ValidationReport.ok();
            report.error("Cannot read document: " + documentPath + ": " + e.getMessage());
            return new JsonDocument(null, report);
        } catch (Exception e) {
            throw new ValidationException("JSON schema validation failed", e);
        }
    }

    private ValidationReport validate(JsonNode documentNode, Path schemaPath) {
        ValidationReport report = ValidationReport.ok();
        schema(schemaPath).validate(documentNode).forEach(error -> report.error(error.getMessage()));
        return report;
    }

    private JsonSchema schema(Path schemaPath) {
        Path key = schemaPath.toAbsolutePath().normalize();
        return SCHEMAS.computeIfAbsent(key, JsonSchemaValidator::loadSchema);
    }

    private static JsonSchema loadSchema(Path schemaPath) {
        try {
            JsonNode schemaNode = JSON.readTree(schemaPath.toFile());
            return JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012).getSchema(schemaNode);
        } catch (Exception e) {
            throw new ValidationException("Cannot read JSON schema: " + schemaPath, e);
        }
    }

    public record JsonDocument(JsonNode root, ValidationReport report) {
    }
}
