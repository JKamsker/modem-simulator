package com.jkamsker.modemsim.replay;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ReplayStepLoaderTest {
    @TempDir
    Path tempDir;

    @Test
    void loadsDirectReplayStepJsonl() throws Exception {
        Path log = tempDir.resolve("steps.jsonl");
        Files.writeString(log, """
                {"inputHex":"41540D","expectedOutputHex":"0D0A4F4B0D0A","drainScheduled":false}
                """);

        var steps = new ReplayStepLoader().load(log);

        assertThat(steps).hasSize(1);
        assertThat(steps.getFirst().input().toHex()).isEqualTo("41540D");
        assertThat(steps.getFirst().expectedOutput().toHex()).isEqualTo("0D0A4F4B0D0A");
    }

    @Test
    void loadsRxTxPairsFromEventJsonl() throws Exception {
        Path log = tempDir.resolve("events.jsonl");
        Files.writeString(log, """
                {"eventType":"RX_BYTES","direction":"DTE_TO_DCE","rawHex":"41540D"}
                {"eventType":"TX_BYTES","direction":"DCE_TO_DTE","rawHex":"0D0A4F4B0D0A"}
                """);

        var steps = new ReplayStepLoader().load(log);

        assertThat(steps).hasSize(1);
        assertThat(steps.getFirst().input().ascii()).isEqualTo("AT\r");
        assertThat(steps.getFirst().expectedOutput().toHex()).isEqualTo("0D0A4F4B0D0A");
    }
}
