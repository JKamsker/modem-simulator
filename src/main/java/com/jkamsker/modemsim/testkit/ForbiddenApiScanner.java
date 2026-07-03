package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.validation.SchemaLocator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
        scanCurrentProcessListeners(hits);
        return hits;
    }

    List<String> scanCurrentProcessListeners() {
        var hits = new ArrayList<String>();
        scanCurrentProcessListeners(hits);
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

    private void scanCurrentProcessListeners(List<String> hits) {
        if (System.getProperty("os.name", "").toLowerCase().contains("win")) {
            scanWindowsListeners(hits);
            return;
        }
        if (System.getProperty("os.name", "").toLowerCase().contains("mac")) {
            scanMacListeners(hits);
            return;
        }
        Path proc = Path.of("/proc", Long.toString(ProcessHandle.current().pid()));
        if (Files.isDirectory(proc)) {
            scanLinuxListeners(proc, hits);
        }
    }

    private void scanLinuxListeners(Path proc, List<String> hits) {
        Set<String> sockets = socketInodes(proc.resolve("fd"));
        if (sockets.isEmpty()) {
            return;
        }
        scanLinuxTcpTable(Path.of("/proc/net/tcp"), sockets, hits, "tcp4");
        scanLinuxTcpTable(Path.of("/proc/net/tcp6"), sockets, hits, "tcp6");
    }

    private Set<String> socketInodes(Path fdDir) {
        Set<String> result = new HashSet<>();
        try (var fds = Files.list(fdDir)) {
            fds.forEach(fd -> {
                try {
                    String target = Files.readSymbolicLink(fd).toString();
                    if (target.startsWith("socket:[") && target.endsWith("]")) {
                        result.add(target.substring(8, target.length() - 1));
                    }
                } catch (IOException ignored) {
                    // File descriptors can disappear while we inspect them.
                }
            });
        } catch (IOException e) {
            throw new IllegalStateException("cannot inspect process file descriptors", e);
        }
        return result;
    }

    private void scanLinuxTcpTable(Path table, Set<String> sockets, List<String> hits, String protocol) {
        if (!Files.isRegularFile(table)) {
            return;
        }
        try {
            for (String line : Files.readAllLines(table).stream().skip(1).toList()) {
                String[] columns = line.trim().split("\\s+");
                if (columns.length > 9 && columns[3].equals("0A") && sockets.contains(columns[9])) {
                    hits.add("process-listener:" + protocol + ":" + columns[1]);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("cannot inspect TCP listeners", e);
        }
    }

    private void scanWindowsListeners(List<String> hits) {
        String output = processOutput("netstat", "-ano", "-p", "tcp");
        String pid = Long.toString(ProcessHandle.current().pid());
        for (String line : output.lines().toList()) {
            if (line.contains("LISTENING") && line.trim().endsWith(" " + pid)) {
                hits.add("process-listener:tcp:" + line.trim());
            }
        }
    }

    private void scanMacListeners(List<String> hits) {
        String output = processOutput("lsof", "-nP", "-a", "-p",
                Long.toString(ProcessHandle.current().pid()), "-iTCP", "-sTCP:LISTEN");
        for (String line : output.lines().skip(1).toList()) {
            if (line.contains("LISTEN")) {
                hits.add("process-listener:tcp:" + line.trim());
            }
        }
    }

    private String processOutput(String... command) {
        try {
            Process process = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .start();
            if (!process.waitFor(5, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                return "";
            }
            return new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Missing OS tools cannot prove a listener, so keep the source/dependency scan authoritative.
            return "";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("interrupted while inspecting TCP listeners", e);
        }
    }
}
