package com.jkamsker.modemsim.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;

import java.nio.file.Path;

public final class JsonSchemaValidator {
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final ObjectMapper YAML = new ObjectMapper(new YAMLFactory());

    public ValidationReport validateJson(Path documentPath, Path schemaPath) {
        return validate(documentPath, schemaPath, JSON);
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
        ValidationReport report = ValidationReport.ok();
        try {
            JsonNode schemaNode = JSON.readTree(schemaPath.toFile());
            JsonNode documentNode = mapper.readTree(documentPath.toFile());
            var factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
            var schema = factory.getSchema(schemaNode);
            schema.validate(documentNode).forEach(error -> report.error(error.getMessage()));
            return report;
        } catch (Exception e) {
            throw new ValidationException("JSON schema validation failed", e);
        }
    }
}
