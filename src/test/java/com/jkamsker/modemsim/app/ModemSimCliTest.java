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
    void testCommandRejectsUnknownSuiteEvenWhenCaseIsPresent() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        int exit = runner.run(new String[] {"test", "--suite", "typo", "--case", "A01"});

        assertThat(exit).isEqualTo(1);
        assertThat(out.toString()).isEmpty();
        assertThat(err.toString()).contains("Unknown acceptance suite: typo");
    }

    @Test
    void validateMacrosCanUseRuntimeConfigContextForTimerAndSeed() throws Exception {
        Path macros = tempDir.resolve("timer-jitter.xml");
        Path config = tempDir.resolve("runtime.yaml");
        Files.writeString(macros, """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="heartbeat" phase="on-timer">
                    <match type="timer" timerId="heartbeat"/>
                    <then><delay ms="1000" jitterMs="10"/><emit line="+TIMER"/></then>
                  </macro>
                </macros>
                """);
        Files.writeString(config, """
                sessionSeed: 12345
                clockMode: virtual
                serialLine:
                  baudRate: 115200
                  dataBits: 8
                  stopBits: 1
                  parity: NONE
                  flowControl: NONE
                macroTimers:
                  - id: heartbeat
                    atMs: 1000
                ports:
                  - id: modem
                    type: headless
                    role: modem-simulation
                    enabled: true
                    profile: sierra-hl6-hl8-v20
                redaction:
                  enabled: true
                  maskPin: true
                  maskPuk: true
                  maskImsi: true
                  maskIccid: true
                  maskImei: true
                  maskMsisdn: true
                  maskSmsBody: true
                """);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        assertThat(runner.run(new String[] {"validate-macros", macros.toString()})).isEqualTo(1);
        assertThat(runner.run(new String[] {
                "validate-macros", macros.toString(), "--config", config.toString()})).isZero();
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

        ByteArrayOutputStream caseErr = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner caseRunner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(new ByteArrayOutputStream()), new PrintStream(caseErr));
        assertThat(caseRunner.run(new String[] {
                "replay", log.toString(), "--mode", "drive-from-captured-input",
                "--case", "playback-divergence", "--confirm-divergence"})).isEqualTo(1);
        assertThat(caseErr.toString()).contains("Expected replay divergence");
    }

    @Test
    void replayDriveAndPlayModesRequireConfirmationForDivergence() throws Exception {
        Path log = divergentReplayLog();
        Path audit = tempDir.resolve("divergent-play-audit.jsonl");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        assertThat(runner.run(new String[] {"replay", log.toString(), "--mode", "drive-from-captured-input"}))
                .isEqualTo(1);
        assertThat(runner.run(new String[] {
                "replay", log.toString(), "--mode", "drive-from-captured-input",
                "--case", "playback-divergence", "--confirm-divergence"}))
                .isZero();
        assertThat(runner.run(new String[] {
                "replay", log.toString(), "--mode", "play-to-dte", "--audit-log", audit.toString()}))
                .isEqualTo(1);
        assertThat(runner.run(new String[] {
                "replay", log.toString(), "--mode", "play-to-dte",
                "--case", "playback-divergence", "--confirm-divergence",
                "--audit-log", audit.toString()})).isZero();
        assertThat(out.toString()).contains("DIVERGENCE CONFIRMED mode=drive-from-captured-input")
                .contains("DIVERGENCE CONFIRMED mode=play-to-dte");
        assertThat(err.toString()).contains("DIVERGENCE:");
    }

    @Test
    void replayConfirmationDoesNotOverrideHardReplayDivergentEvents() throws Exception {
        Path log = hardReplayDivergentLog();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        assertThat(runner.run(new String[] {
                "replay", log.toString(), "--mode", "drive-from-captured-input", "--confirm-divergence"}))
                .isEqualTo(1);

        assertThat(out.toString()).doesNotContain("DIVERGENCE CONFIRMED");
        assertThat(err.toString()).contains("replay divergent");
    }

    @Test
    void replayPlayToDteSupportsTrmTranscripts() throws Exception {
        Path log = tempDir.resolve("capture.trm");
        Path audit = tempDir.resolve("trm-audit.jsonl");
        Files.writeString(log, """
                RX 1 11 (4858538528): AT+CREG?
                TX 1 11 (4858538535): +CREG: 0,1
                """);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        int exit = runner.run(new String[] {
                "replay", log.toString(), "--mode", "play-to-dte",
                "--endpoint", "headless", "--audit-log", audit.toString()});

        assertThat(exit).isZero();
        assertThat(out.toString()).contains("outputHex=2B435245473A20302C31");
        assertThat(Files.readString(audit)).contains("play-to-dte-start");
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

    private Path divergentReplayLog() throws Exception {
        Path log = tempDir.resolve("divergent-events.jsonl");
        Files.writeString(log, """
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":0,"sequence":1,"sessionId":"main","eventType":"RX_BYTES","source":"rx","direction":"DTE_TO_DCE","rawHex":"41540D","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":1,"sequence":2,"sessionId":"main","eventType":"TX_BYTES","source":"tx","direction":"DCE_TO_DTE","rawHex":"0D0A4552524F520D0A","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                """);
        return log;
    }

    private Path hardReplayDivergentLog() throws Exception {
        Path log = tempDir.resolve("hard-divergent-events.jsonl");
        Files.writeString(log, """
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":0,"sequence":1,"sessionId":"main","eventType":"RX_OVERFLOW","source":"transport","direction":"DTE_TO_DCE","rawHex":"","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","replayDivergent":true,"redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                """);
        return log;
    }
}
