package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.validation.SchemaLocator;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
        require(pom.contains("failonerror=\"true\""), "source-size hook must fail Maven");
        require(pom.contains("dir=\"${project.basedir}\""), "source-size hook must use basedir");
        require(pom.contains("<arg value=\"scripts/check-code-size.sh\"/>"), "source-size hook must use relative script");
    }

    private void directScriptRejectsOversizedSource() throws java.io.IOException {
        Path dir = Files.createTempDirectory("modemsim-size-gate-direct");
        Files.writeString(dir.resolve("TooLarge.java"), oversizedJava(), StandardCharsets.UTF_8);
        ProcessResult result = runProcess("bash",
                SchemaLocator.projectPath("scripts/check-code-size.sh").toString(), dir.toString());
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
        ProcessResult result = runProcess(SchemaLocator.projectPath("mvnw").toAbsolutePath().toString(),
                "-f", project.resolve("pom.xml").toString(), "verify");
        require(result.exitCode() != 0 && result.output().contains("TooLarge.java"),
                "maven verify did not reject oversized source: " + result.output());
    }

    private ProcessResult runProcess(String... command) throws java.io.IOException {
        try {
            Process process = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .start();
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return new ProcessResult(process.waitFor(), output);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("source-size acceptance interrupted", e);
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
                  <properties>
                    <maven.compiler.release>24</maven.compiler.release>
                    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                  </properties>
                  <build>
                    <plugins>
                      <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-antrun-plugin</artifactId>
                        <version>3.1.0</version>
                        <executions>
                          <execution>
                            <id>check-java-source-size</id>
                            <phase>verify</phase>
                            <goals><goal>run</goal></goals>
                            <configuration>
                              <target>
                                <exec executable="bash" dir="${project.basedir}" failonerror="true">
                                  <arg value="scripts/check-code-size.sh"/>
                                </exec>
                              </target>
                            </configuration>
                          </execution>
                        </executions>
                      </plugin>
                    </plugins>
                  </build>
                </project>
                """;
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
