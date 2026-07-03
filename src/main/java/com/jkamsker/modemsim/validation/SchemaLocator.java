package com.jkamsker.modemsim.validation;

import java.nio.file.Files;
import java.nio.file.Path;

public final class SchemaLocator {
    private static final Path DOC_SCHEMA_DIR = Path.of("docs", "Tasks", "Initial-Spec", "schemas");

    private SchemaLocator() {
    }

    public static Path schemaPath(String name) {
        Path path = projectPath(DOC_SCHEMA_DIR.resolve(name));
        if (!Files.isRegularFile(path)) {
            throw new ValidationException("Schema not found: " + path.toAbsolutePath());
        }
        return path;
    }

    public static Path projectPath(String first, String... more) {
        return projectPath(Path.of(first, more));
    }

    public static Path projectPath(Path relativePath) {
        if (relativePath.isAbsolute() || Files.exists(relativePath)) {
            return relativePath;
        }
        String home = System.getProperty("modemsim.home", "");
        Path homePath = home.isBlank() ? relativePath : Path.of(home).resolve(relativePath);
        return Files.exists(homePath) ? homePath : relativePath;
    }
}
