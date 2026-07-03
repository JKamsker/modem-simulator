package com.jkamsker.modemsim.testkit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SourceSizeGateTest {
    @TempDir
    Path tempDir;

    @Test
    void missingRootsAreIgnored() {
        assertThat(SourceSizeGate.failures(List.of(tempDir.resolve("missing")))).isEmpty();
    }

    @Test
    void reportsOversizedJavaAndIgnoresGeneratedAndTarget() throws Exception {
        write(tempDir.resolve("src/main/java/Small.java"), "class Small {}\n");
        write(tempDir.resolve("src/main/java/TooLarge.java"), oversizedJava());
        write(tempDir.resolve("src/generated/Generated.java"), oversizedJava());
        write(tempDir.resolve("target/generated/Generated.java"), oversizedJava());

        assertThat(SourceSizeGate.failures(List.of(tempDir)))
                .singleElement()
                .satisfies(failure -> assertThat(failure)
                        .contains("TooLarge.java")
                        .contains("300 lines"));
    }

    @Test
    void mainFailsWhenOversizedSourceExists() throws Exception {
        write(tempDir.resolve("src/main/java/TooLarge.java"), oversizedJava());

        ByteArrayOutputStream err = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        try {
            System.setErr(new PrintStream(err));
            assertThatThrownBy(() -> SourceSizeGate.main(new String[] {tempDir.toString()}))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Java source size gate failed");
        } finally {
            System.setErr(originalErr);
        }
        assertThat(err.toString()).contains("TooLarge.java");
    }

    @Test
    void mainUsesSrcAsDefaultRoot() {
        assertThatCode(() -> SourceSizeGate.main(new String[0])).doesNotThrowAnyException();
    }

    private void write(Path path, String content) throws Exception {
        Files.createDirectories(path.getParent());
        Files.writeString(path, content);
    }

    private String oversizedJava() {
        return IntStream.range(0, 300)
                .mapToObj(index -> "// line " + index)
                .collect(Collectors.joining(System.lineSeparator(), "", System.lineSeparator()));
    }
}
