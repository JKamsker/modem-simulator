package com.jkamsker.modemsim.validation;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SchemaLocator {
    private static final Path DOC_SCHEMA_DIR = Path.of("docs", "Tasks", "Initial-Spec", "schemas");
    private static final String SCHEMA_RESOURCE_DIR = "schemas/";
    private static final Map<String, Path> RESOURCE_CACHE = new ConcurrentHashMap<>();

    private SchemaLocator() {
    }

    public static Path schemaPath(String name) {
        Path path = projectPath(DOC_SCHEMA_DIR.resolve(name));
        if (Files.isRegularFile(path)) {
            return path;
        }
        return resourceSchemaPath(name);
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

    private static Path resourceSchemaPath(String name) {
        return RESOURCE_CACHE.computeIfAbsent(name, SchemaLocator::extractResourceSchema);
    }

    private static Path extractResourceSchema(String name) {
        var loader = SchemaLocator.class.getClassLoader();
        var resource = loader.getResource(SCHEMA_RESOURCE_DIR + name);
        if (resource == null) {
            throw new ValidationException("Schema not found: " + name);
        }
        try {
            if (resource.getProtocol().equals("file")) {
                return Path.of(resource.toURI());
            }
            Path temp = Files.createTempFile("modemsim-schema-", "-" + name);
            try (var in = resource.openStream()) {
                Files.copy(in, temp, StandardCopyOption.REPLACE_EXISTING);
            }
            temp.toFile().deleteOnExit();
            return temp;
        } catch (Exception e) {
            throw new ValidationException("Schema not found: " + name, e);
        }
    }
}
