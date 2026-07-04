package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.validation.SchemaLocator;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class SourceSizeGateAcceptance {
    void run() {
        try {
            requireProjectPomWiring();
            directScriptRejectsOversizedSource();
            mavenVerifyRejectsOversizedSource();
        } catch (java.io.IOException e) {
            throw new IllegalStateException("source-size acceptance check failed: " + e.getMessage(), e);
        }
    }

    private void requireProjectPomWiring() throws java.io.IOException {
        String pom = Files.readString(SchemaLocator.projectPath("pom.xml"), StandardCharsets.UTF_8);
        require(pom.contains("<id>check-java-source-size</id>"), "missing source-size execution");
        require(pom.contains("<phase>process-classes</phase>"), "source-size hook must run during Maven builds");
        require(pom.contains("<goal>exec</goal>"), "source-size hook must fail Maven via exec");
        require(pom.contains("com.jkamsker.modemsim.testkit.SourceSizeGate"), "source-size hook must run Java gate");
        require(pom.contains("<argument>${project.basedir}/src</argument>"), "source-size hook must scan project src");
    }

    private void directScriptRejectsOversizedSource() throws java.io.IOException {
        Path dir = Files.createTempDirectory("modemsim-size-gate-direct");
        Files.createDirectories(dir.resolve("scripts"));
        Files.copy(SchemaLocator.projectPath("scripts/check-code-size.sh"), dir.resolve("scripts/check-code-size.sh"));
        Files.writeString(dir.resolve("TooLarge.java"), oversizedJava(), StandardCharsets.UTF_8);
        ProcessResult result = runProcess(dir, bashCommand(), "scripts/check-code-size.sh", ".");
        require(result.exitCode() != 0 && result.output().contains("TooLarge.java"),
                "direct size script did not reject oversized source: " + result.output());
    }

    private void mavenVerifyRejectsOversizedSource() throws java.io.IOException {
        Path project = Files.createTempDirectory("modemsim-size-gate-maven");
        Files.createDirectories(project.resolve("scripts"));
        Files.createDirectories(project.resolve("src/main/java"));
        Files.copy(SchemaLocator.projectPath("scripts/check-code-size.sh"), project.resolve("scripts/check-code-size.sh"));
        Files.writeString(project.resolve("src/main/java/TooLarge.java"), oversizedJava(), StandardCharsets.UTF_8);
        Files.writeString(project.resolve("pom.xml"), fixturePom(), StandardCharsets.UTF_8);
        ProcessResult result = runProcess(SchemaLocator.projectPath("."),
                SchemaLocator.projectPath(wrapperName()).toAbsolutePath().toString(),
                "-f", project.resolve("pom.xml").toString(), "verify");
        require(result.exitCode() != 0 && result.output().contains("TooLarge.java"),
                "maven verify did not reject oversized source, exit=" + result.exitCode()
                        + ": " + result.output());
    }

    private ProcessResult runProcess(Path workingDirectory, String... command) throws java.io.IOException {
        Path outputFile = Files.createTempFile("modemsim-size-gate-process", ".log");
        try {
            Process process = new ProcessBuilder(command)
                    .directory(workingDirectory.toAbsolutePath().normalize().toFile())
                    .redirectErrorStream(true)
                    .redirectOutput(outputFile.toFile())
                    .start();
            if (!process.waitFor(60, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                return new ProcessResult(124, Files.readString(outputFile, StandardCharsets.UTF_8));
            }
            return new ProcessResult(process.exitValue(), Files.readString(outputFile, StandardCharsets.UTF_8));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("source-size acceptance interrupted", e);
        } finally {
            Files.deleteIfExists(outputFile);
        }
    }

    private String oversizedJava() {
        return IntStream.range(0, 300)
                .mapToObj(index -> "// line " + index)
                .collect(Collectors.joining(System.lineSeparator(), "", System.lineSeparator()));
    }

    private String fixturePom() {
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                  <modelVersion>4.0.0</modelVersion>
                  <groupId>com.jkamsker</groupId>
                  <artifactId>source-size-fixture</artifactId>
                  <version>1.0.0-SNAPSHOT</version>
                  <packaging>pom</packaging>
                  <properties>
                    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                  </properties>
                  <build>
                    <plugins>
                      <plugin>
                        <groupId>org.codehaus.mojo</groupId>
                        <artifactId>exec-maven-plugin</artifactId>
                        <version>3.5.1</version>
                        <executions>
                          <execution>
                            <id>check-java-source-size</id>
                            <phase>verify</phase>
                            <goals><goal>exec</goal></goals>
                            <configuration>
                              <executable>${java.home}/bin/java</executable>
                              <arguments>
                                <argument>-cp</argument>
                                <argument>%s</argument>
                                <argument>com.jkamsker.modemsim.testkit.SourceSizeGate</argument>
                                <argument>${project.basedir}/src</argument>
                              </arguments>
                            </configuration>
                          </execution>
                        </executions>
                      </plugin>
                    </plugins>
                  </build>
                </project>
                """.formatted(xml(gateClasspath()));
    }

    private String gateClasspath() {
        try {
            return Path.of(SourceSizeGate.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI()).toString();
        } catch (java.net.URISyntaxException e) {
            throw new IllegalStateException("cannot resolve source-size gate classpath", e);
        }
    }

    private String wrapperName() {
        return windows() ? "mvnw.cmd" : "mvnw";
    }

    private String bashCommand() {
        if (!windows()) {
            return "bash";
        }
        String env = System.getenv("BASH");
        if (env != null && Files.isExecutable(Path.of(env))) {
            return env;
        }
        for (String candidate : List.of(
                "C:\\Program Files\\Git\\bin\\bash.exe",
                "C:\\Program Files\\Git\\usr\\bin\\bash.exe")) {
            if (Files.isExecutable(Path.of(candidate))) {
                return candidate;
            }
        }
        return "bash";
    }

    private boolean windows() {
        return System.getProperty("os.name", "").toLowerCase().contains("win");
    }

    private String xml(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private void require(boolean condition) {
        require(condition, "source-size acceptance check failed");
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private record ProcessResult(int exitCode, String output) {
    }
}
