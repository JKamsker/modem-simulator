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
    void replayCommandValidatesJsonlTranscript() throws Exception {
        Path log = tempDir.resolve("session.jsonl");
        Files.writeString(log, """
                {"inputHex":"41540D","expectedOutputHex":"0D0A4F4B0D0A"}
                """);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        int exit = runner.run(new String[] {
                "replay", log.toString(),
                "--mode", "validate-recompute",
                "--profile", "sierra-hl6-hl8-v20"});

        assertThat(exit).isZero();
        assertThat(out.toString()).contains("REPLAY OK steps=1");
        assertThat(err.toString()).isEmpty();
    }
}
