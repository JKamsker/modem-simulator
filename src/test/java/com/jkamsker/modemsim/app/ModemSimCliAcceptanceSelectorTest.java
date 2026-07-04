package com.jkamsker.modemsim.app;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class ModemSimCliAcceptanceSelectorTest {
    @Test
    void rejectsUnknownSuiteEvenWhenCaseIsPresent() {
        CliRun result = run("test", "--suite", "typo", "--case", "A01");

        assertRejectedSelector(result);
    }

    @Test
    void rejectsUnknownTagsEvenWhenCaseIsPresent() {
        CliRun result = run("test", "--tags", "typo", "--case", "A01");

        assertRejectedSelector(result);
    }

    @Test
    void validatesSuiteWhenTagsArePresent() {
        CliRun result = run("test", "--suite", "typo", "--tags", "gui", "--case", "live-log");

        assertRejectedSelector(result);
    }

    private static void assertRejectedSelector(CliRun result) {
        assertThat(result.exit()).isEqualTo(1);
        assertThat(result.out()).isEmpty();
        assertThat(result.err()).contains("Unknown acceptance suite: typo");
    }

    private static CliRun run(String... args) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        return new CliRun(runner.run(args), out.toString(), err.toString());
    }

    private record CliRun(int exit, String out, String err) {
    }
}
