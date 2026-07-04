package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.monitor.ModemEventJson;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.replay.ReplayStepLoader;
import com.jkamsker.modemsim.replay.ReplayValidator;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.validation.SchemaLocator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public final class ReplayAcceptance {
    public void run() {
        try {
            Path canonical = SchemaLocator.projectPath("src/test/resources/replay/basic-at-events.jsonl");
            require(runReplay(canonical, "--mode", "validate-recompute").exit() == 0,
                    "validate-recompute should accept canonical replay log");
            require(runReplay(canonical, "--mode", "drive-from-captured-input").exit() == 0,
                    "drive-from-captured-input should accept canonical replay log");
            require(runReplay(canonical, "--mode", "drive-from-captured-input",
                    "--case", "playback-divergence", "--confirm-divergence").exit() != 0,
                    "confirm-divergence should not bypass a non-divergent replay");
            Path audit = tempLog("modemsim-acceptance-play");
            CliRun play = runReplay(canonical, "--mode", "play-to-dte", "--audit-log", audit.toString());
            require(play.exit() == 0 && Files.readString(audit).contains("play-to-dte-start"),
                    "play-to-dte should succeed and write an audit start marker");
            require(schedulerReplayValid(), "scheduler replay should recompute as valid");
            replayDivergenceConfirmation();
            hardFailureInAllModes(redactedLog(), "redacted");
            hardFailureInAllModes(missingBytesLog(), "missing rawHex");
            hardFailureInAllModes(replayDivergentLog(), "replay divergent");
        } catch (java.io.IOException e) {
            throw new IllegalStateException("replay acceptance failed", e);
        }
    }

    private void replayDivergenceConfirmation() throws java.io.IOException {
        Path log = divergentLog();
        require(runReplay(log, "--mode", "drive-from-captured-input").exit() != 0,
                "drive-from-captured-input should reject divergent replay without confirmation");
        CliRun drive = runReplay(log, "--mode", "drive-from-captured-input",
                "--case", "playback-divergence", "--confirm-divergence");
        require(drive.exit() == 0 && drive.out().contains("DIVERGENCE CONFIRMED"),
                "drive-from-captured-input should accept confirmed divergence");
        Path audit = tempLog("modemsim-acceptance-divergent-play");
        require(runReplay(log, "--mode", "play-to-dte", "--audit-log", audit.toString()).exit() != 0,
                "play-to-dte should reject divergent replay without confirmation");
        CliRun play = runReplay(log, "--mode", "play-to-dte",
                "--case", "playback-divergence", "--confirm-divergence",
                "--audit-log", audit.toString());
        require(play.exit() == 0 && play.out().contains("DIVERGENCE CONFIRMED"),
                "play-to-dte should accept confirmed divergence");
    }

    private void hardFailureInAllModes(Path log, String expectedError) throws java.io.IOException {
        require(failsWith(log, expectedError, "--mode", "validate-recompute"),
                "validate-recompute should reject " + expectedError + " fixture");
        require(failsWith(log, expectedError, "--mode", "drive-from-captured-input", "--confirm-divergence"),
                "drive-from-captured-input should reject " + expectedError + " fixture");
        Path audit = tempLog("modemsim-acceptance-hard-play");
        require(failsWith(log, expectedError, "--mode", "play-to-dte", "--confirm-divergence",
                "--audit-log", audit.toString()), "play-to-dte should reject " + expectedError + " fixture");
    }

    private boolean failsWith(Path log, String expectedError, String... args) {
        CliRun run = runReplay(log, args);
        String output = (run.out() + run.err()).toLowerCase(java.util.Locale.ROOT);
        return run.exit() != 0 && output.contains(expectedError.toLowerCase(java.util.Locale.ROOT));
    }

    private boolean schedulerReplayValid() throws java.io.IOException {
        HeadlessSession capture = new HeadlessSession("replay", new ProfileResolver().resolve("sierra-hl6-hl8-v20"), 12345);
        var events = new ArrayList<>(capture.receive(RawBytes.ascii("ATD123\r")).events());
        events.addAll(capture.drainScheduled().events());
        Path log = tempLog("modemsim-scheduler-replay");
        Files.writeString(log, ModemEventJson.toJsonLines(events), StandardCharsets.UTF_8);
        var steps = new ReplayStepLoader().load(log);
        var report = new ReplayValidator().validateRecompute(
                new HeadlessSession("main", new ProfileResolver().resolve("sierra-hl6-hl8-v20"), 12345), steps, true);
        return report.valid();
    }

    private CliRun runReplay(Path log, String... args) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        String[] command = new String[args.length + 2];
        command[0] = "replay";
        command[1] = log.toString();
        System.arraycopy(args, 0, command, 2, args.length);
        int exit = new ModemSimCli.ModemSimCliRunner(new PrintStream(out), new PrintStream(err)).run(command);
        return new CliRun(exit, out.toString(StandardCharsets.UTF_8), err.toString(StandardCharsets.UTF_8));
    }

    private Path divergentLog() throws java.io.IOException {
        return writeLog("modemsim-divergent-replay", """
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":0,"sequence":1,"sessionId":"main","eventType":"RX_BYTES","source":"rx","direction":"DTE_TO_DCE","rawHex":"41540D","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":1,"sequence":2,"sessionId":"main","eventType":"TX_BYTES","source":"tx","direction":"DCE_TO_DTE","rawHex":"0D0A4552524F520D0A","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                """);
    }

    private Path replayDivergentLog() throws java.io.IOException {
        return writeLog("modemsim-replay-divergent", """
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":0,"sequence":1,"sessionId":"main","eventType":"RX_OVERFLOW","source":"transport","direction":"DTE_TO_DCE","rawHex":"","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","replayDivergent":true,"redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                """);
    }

    private Path redactedLog() throws java.io.IOException {
        return writeLog("modemsim-redacted-replay", """
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":0,"sequence":1,"sessionId":"main","eventType":"RX_BYTES","source":"rx","direction":"DTE_TO_DCE","rawHex":"<redacted>","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":true,"policy":"default-v1","fields":["rawHex"],"classes":["pin"]}}
                """);
    }

    private Path missingBytesLog() throws java.io.IOException {
        return writeLog("modemsim-missing-bytes-replay", """
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":0,"sequence":1,"sessionId":"main","eventType":"RX_BYTES","source":"rx","direction":"DTE_TO_DCE","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                """);
    }

    private Path writeLog(String prefix, String body) throws java.io.IOException {
        Path log = tempLog(prefix);
        Files.writeString(log, body, StandardCharsets.UTF_8);
        return log;
    }

    private Path tempLog(String prefix) throws java.io.IOException {
        Path log = Files.createTempFile(prefix, ".jsonl");
        log.toFile().deleteOnExit();
        return log;
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException("replay acceptance check failed: " + message);
        }
    }

    private record CliRun(int exit, String out, String err) {
    }
}
