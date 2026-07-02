package com.alegs3.modemsim.validation;

import java.nio.file.Files;
import java.nio.file.Path;

public final class SchemaLocator {
    private static final Path DOC_SCHEMA_DIR = Path.of("docs", "Tasks", "Initial-Spec", "schemas");

    private SchemaLocator() {
    }

    public static Path schemaPath(String name) {
        Path path = DOC_SCHEMA_DIR.resolve(name);
        if (!Files.isRegularFile(path)) {
            throw new ValidationException("Schema not found: " + path.toAbsolutePath());
        }
        return path;
    }
}
