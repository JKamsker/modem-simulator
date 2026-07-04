package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.monitor.InMemoryEventSink;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ReplayCommandEdgeTest {
    @TempDir
    Path tempDir;

    @Test
    void playToDteAcceptsTxOnlyLogsWithMatchingMetadata() throws Exception {
        Path log = txOnlyLog();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        int exit = runner.run(new String[] {
                "replay", log.toString(), "--mode", "play-to-dte",
                "--endpoint", "headless", "--audit-log", tempDir.resolve("audit.jsonl").toString()});

        assertThat(exit).isZero();
        assertThat(out.toString()).contains("outputHex=4142");
        assertThat(err.toString()).isEmpty();
    }

    private Path txOnlyLog() throws Exception {
        HeadlessSession session = new HeadlessSession("replay", BuiltinProfiles.acceptanceSierra(), 12345,
                new InMemoryEventSink(), MacroEngine.empty(), "virtual", null, null);
        ModemEvent start = session.events().getFirst();
        Path log = tempDir.resolve("tx-only.jsonl");
        Files.writeString(log, txEvent(1, 10, "41", start) + txEvent(2, 20, "42", start));
        return log;
    }

    private String txEvent(int sequence, long nanos, String rawHex, ModemEvent metadata) {
        return """
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":%d,"sequence":%d,"sessionId":"main","eventType":"TX_BYTES","source":"tx","direction":"DCE_TO_DTE","rawHex":"%s","profileHash":"%s","configHash":"%s","macroHash":"%s","initialStateHash":"%s","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                """.formatted(nanos, sequence, rawHex, metadata.profileHash(), metadata.configHash(),
                metadata.macroHash(), metadata.initialStateHash());
    }
}
