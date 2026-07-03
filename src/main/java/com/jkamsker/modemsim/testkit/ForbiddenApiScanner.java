package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.validation.SchemaLocator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

final class ForbiddenApiScanner {
    private static final List<String> NEEDLES = List.of(
            "Http" + "Server",
            "Server" + "Socket",
            "Web" + "Socket",
            "com.sun.net." + "httpserver",
            "springframework" + ".web",
            "jetty-" + "server",
            "under" + "tow",
            "netty" + "-all",
            "spring-boot-starter-" + "web");

    List<String> scan() {
        var hits = new ArrayList<String>();
        scanForNeedles(SchemaLocator.projectPath("src/main/java"), hits);
        scanForNeedles(SchemaLocator.projectPath("pom.xml"), hits);
        scanLibraryNames(SchemaLocator.projectPath("lib"), hits);
        return hits;
    }

    private void scanForNeedles(Path root, List<String> hits) {
        try {
            if (!Files.exists(root)) {
                return;
            }
            if (Files.isRegularFile(root)) {
                scanFile(root, hits);
                return;
            }
            try (var files = Files.walk(root)) {
                files.filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().endsWith(".java"))
                        .forEach(path -> scanFile(path, hits));
            }
        } catch (IOException e) {
            throw new IllegalStateException("cannot scan for control APIs", e);
        }
    }

    private void scanLibraryNames(Path lib, List<String> hits) {
        if (!Files.isDirectory(lib)) {
            return;
        }
        try (var files = Files.list(lib)) {
            files.map(path -> path.getFileName().toString()).forEach(name -> {
                for (String needle : NEEDLES) {
                    if (name.contains(needle)) {
                        hits.add("lib/" + name + ":" + needle);
                    }
                }
            });
        } catch (IOException e) {
            throw new IllegalStateException("cannot scan packaged libraries", e);
        }
    }

    private void scanFile(Path path, List<String> hits) {
        try {
            String content = Files.readString(path);
            for (String needle : NEEDLES) {
                if (content.contains(needle)) {
                    hits.add(path + ":" + needle);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("cannot scan " + path, e);
        }
    }
}
