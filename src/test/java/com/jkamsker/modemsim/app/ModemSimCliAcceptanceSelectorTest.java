package com.jkamsker.modemsim.app;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class ModemSimCliAcceptanceSelectorTest {
    @Test
    void rejectsUnknownSuiteEvenWhenCaseIsPresent() {
        CliRun result = run("test", "--suite", "typo", "--case", "A01");

        assertRejectedSelector(result, "Unknown acceptance suite: typo");
    }

    @Test
    void rejectsUnknownTagsEvenWhenCaseIsPresent() {
        CliRun result = run("test", "--tags", "typo", "--case", "A01");

        assertRejectedSelector(result, "Unknown acceptance tag: typo");
    }

    @Test
    void validatesSuiteWhenTagsArePresent() {
        CliRun result = run("test", "--suite", "typo", "--tags", "gui", "--case", "live-log");

        assertRejectedSelector(result, "Unknown acceptance suite: typo");
    }

    @Test
    void rejectsSuiteWithoutValue() {
        CliRun result = run("test", "--suite");

        assertRejectedSelector(result, "Missing option value: --suite");
    }

    @Test
    void rejectsTagsWithoutValue() {
        CliRun result = run("test", "--tags", "--case", "A01");

        assertRejectedSelector(result, "Missing option value: --tags");
    }

    @Test
    void rejectsCaseOutsideSelectedSuite() {
        CliRun result = run("test", "--suite", "sms", "--case", "creg");

        assertRejectedSelector(result, "Acceptance case creg is not in suite sms");
    }

    @Test
    void tagAllRunsOnlyTaggedCases() {
        CliRun result = run("test", "--tags", "gui", "--case", "all");

        assertThat(result.exit()).isZero();
        assertThat(result.out()).contains("A13 OK", "A14 OK", "A27 OK");
        assertThat(result.out()).doesNotContain("A01 OK", "A09 OK");
        assertThat(result.err()).isEmpty();
    }

    @Test
    void serialItTagIsKnown() {
        CliRun result = run("test", "--tags", "serial-it", "--case", "A01");

        assertThat(result.exit()).isZero();
        assertThat(result.out()).contains("A01 OK");
        assertThat(result.err()).isEmpty();
    }

    private static void assertRejectedSelector(CliRun result, String message) {
        assertThat(result.exit()).isEqualTo(1);
        assertThat(result.out()).isEmpty();
        assertThat(result.err()).contains(message);
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
