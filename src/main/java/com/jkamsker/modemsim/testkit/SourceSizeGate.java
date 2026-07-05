package com.jkamsker.modemsim.testkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SourceSizeGate {
    private static final int LIMIT_FAIL = 300;

    private SourceSizeGate() {
    }

    public static void main(String[] args) {
        List<Path> roots = args.length == 0
                ? List.of(Path.of("src"))
                : Arrays.stream(args).map(Path::of).toList();
        List<String> failures = failures(roots);
        failures.forEach(System.err::println);
        if (!failures.isEmpty()) {
            throw new IllegalStateException("Java source size gate failed");
        }
    }

    static List<String> failures(List<Path> roots) {
        List<String> failures = new ArrayList<>();
        for (Path root : roots) {
            scanRoot(root, failures);
        }
        return failures;
    }

    private static void scanRoot(Path root, List<String> failures) {
        if (!Files.exists(root)) {
            return;
        }
        try (var files = Files.walk(root)) {
            files.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".java"))
                    .filter(path -> !ignored(path))
                    .forEach(path -> check(path, failures));
        } catch (IOException e) {
            throw new IllegalStateException("cannot scan Java source sizes under " + root, e);
        }
    }

    private static boolean ignored(Path path) {
        String normalized = path.toString().replace('\\', '/');
        return normalized.contains("/generated/") || normalized.contains("/target/");
    }

    private static void check(Path path, List<String> failures) {
        try (var lines = Files.lines(path)) {
            long count = lines.count();
            if (count >= LIMIT_FAIL) {
                failures.add("ERROR: " + path + " has " + count
                        + " lines; non-generated code must stay under " + LIMIT_FAIL + " lines.");
            }
        } catch (IOException e) {
            throw new IllegalStateException("cannot count lines in " + path, e);
        }
    }
}
