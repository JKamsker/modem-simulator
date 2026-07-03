package com.jkamsker.modemsim.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ModemSimCliTest {
    @TempDir
    Path tempDir;

    @Test
    void validationCommandsReturnSuccessForSpecExamples() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        assertThat(runner.run(new String[] {
                "validate-profile", "docs/Tasks/Initial-Spec/examples/modem-profile.sample.xml"})).isZero();
        assertThat(runner.run(new String[] {
                "validate-macros", "docs/Tasks/Initial-Spec/examples/macros.sms-error-123.xml"})).isZero();
        assertThat(runner.run(new String[] {
                "validate-scenario", "docs/Tasks/Initial-Spec/examples/scenario.no-network.xml"})).isZero();
        assertThat(runner.run(new String[] {"coverage", "verify", "--profiles", "v1-targets"})).isZero();
        assertThat(runner.run(new String[] {"test", "--suite", "acceptance", "--case", "A01"})).isZero();
        assertThat(runner.run(new String[] {"test", "--suite", "cellular", "--case", "creg"})).isZero();
        assertThat(runner.run(new String[] {"test", "--tags", "gui", "--case", "live-log"})).isZero();
        assertThat(err.toString()).isEmpty();
    }

    @Test
    void runCommandProcessesConfiguredHeadlessPort() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        int exit = runner.run(new String[] {
                "run",
                "--config", "src/test/resources/config/valid.yaml",
                "--input-ascii", "AT\\r"});

        assertThat(exit).isZero();
        assertThat(out.toString()).contains("RUN main reads=1 outputHex=0D0A4F4B0D0A");
        assertThat(err.toString()).isEmpty();
    }

    @Test
    void runCommandSupportsDirectPortProfileOptions() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        int exit = runner.run(new String[] {
                "run",
                "--endpoint", "headless",
                "--port", "HEADLESS",
                "--baud", "115200",
                "--profile", "sierra-hl6-hl8-v20",
                "--input-ascii", "AT\\r"});

        assertThat(exit).isZero();
        assertThat(out.toString()).contains("RUN main reads=1 outputHex=0D0A4F4B0D0A");
        assertThat(err.toString()).isEmpty();
    }

    @Test
    void replayCommandValidatesJsonlTranscript() throws Exception {
        Path log = Path.of("src/test/resources/replay/basic-at-events.jsonl");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        int exit = runner.run(new String[] {
                "replay", log.toString(),
                "--mode", "validate-recompute",
                "--profile", "sierra-hl6-hl8-v20"});

        assertThat(exit).isZero();
        assertThat(out.toString()).contains("REPLAY OK mode=validate-recompute steps=1");
        assertThat(err.toString()).isEmpty();
    }

    @Test
    void replayCommandSupportsDriveAndPlayModes() throws Exception {
        Path log = Path.of("src/test/resources/replay/basic-at-events.jsonl");
        Path audit = tempDir.resolve("play-audit.jsonl");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        assertThat(runner.run(new String[] {"replay", log.toString(), "--mode", "drive-from-captured-input"})).isZero();
        assertThat(runner.run(new String[] {
                "replay", log.toString(), "--mode", "play-to-dte", "--audit-log", audit.toString()})).isZero();
        assertThat(out.toString()).contains("mode=drive-from-captured-input").contains("PLAY_TO_DTE");
        assertThat(Files.readString(audit)).contains("REPLAY_MARKER").contains("play-to-dte-start");
        assertThat(err.toString()).isEmpty();
    }

    @Test
    void headlessCommandRunsGoldenTranscriptScript() throws Exception {
        Path script = tempDir.resolve("script.yaml");
        Files.writeString(script, """
                steps:
                  - inputHex: "41540D"
                    expectedOutputHex: "0D0A4F4B0D0A"
                """);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        int exit = runner.run(new String[] {
                "headless",
                "--profile", "sierra-hl6-hl8-v20",
                "--script", script.toString()});

        assertThat(exit).isZero();
        assertThat(out.toString()).contains("HEADLESS OK steps=1");
        assertThat(err.toString()).isEmpty();
    }
}
