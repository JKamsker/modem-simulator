package com.jkamsker.modemsim.replay;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":0,"sequence":2,"sessionId":"main","eventType":"RX_BYTES","source":"rx","direction":"DTE_TO_DCE","rawHex":"41540D","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":0,"sequence":5,"sessionId":"main","eventType":"TX_BYTES","source":"tx","direction":"DCE_TO_DTE","rawHex":"0D0A4F4B0D0A","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                """);

        var steps = new ReplayStepLoader().load(log);

        assertThat(steps).hasSize(1);
        assertThat(steps.getFirst().input().ascii()).isEqualTo("AT\r");
        assertThat(steps.getFirst().expectedOutput().toHex()).isEqualTo("0D0A4F4B0D0A");
        assertThat(steps.getFirst().expectedEvents()).hasSize(2);
    }

    @Test
    void loadsGoldenYamlMetadataAndEventOracles() {
        ReplayTranscript transcript = new ReplayStepLoader().loadTranscript(Path.of("tests/golden/basic-at.yaml"));

        assertThat(transcript.name()).isEqualTo("basic-at-ok");
        assertThat(transcript.profile()).isEqualTo("acceptance-sierra-hl6-hl8-ready");
        assertThat(transcript.metadata()).containsKey("purpose");
        assertThat(transcript.steps()).singleElement().satisfies(step -> {
            assertThat(step.input().ascii()).isEqualTo("AT\r");
            assertThat(step.expectedOutput().toHex()).isEqualTo("0D0A4F4B0D0A");
            assertThat(step.expectedEvents()).extracting(ReplayEventExpectation::command).contains("AT");
            assertThat(step.expectedEvents()).extracting(ReplayEventExpectation::finalResult).contains("OK");
        });
        assertThat(new ReplayValidator().validateRecompute(
                new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345),
                transcript.steps()).valid()).isTrue();
    }

    @Test
    void rejectsRedactedRawPayloadsBecauseTheyCannotBeReplayed() throws Exception {
        Path log = tempDir.resolve("redacted.jsonl");
        Files.writeString(log, """
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":0,"sequence":2,"sessionId":"main","eventType":"RX_BYTES","source":"rx","direction":"DTE_TO_DCE","rawHex":"<redacted>","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":true,"policy":"default-v1","fields":["rawHex"],"classes":["pin"]}}
                """);

        assertThatThrownBy(() -> new ReplayStepLoader().load(log))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot replay redacted rawHex");
    }

    @Test
    void rejectsInvalidHashFields() throws Exception {
        Path log = tempDir.resolve("bad-hash.jsonl");
        Files.writeString(log, """
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":0,"sequence":1,"sessionId":"main","eventType":"RX_BYTES","source":"rx","direction":"DTE_TO_DCE","rawHex":"41540D","profileHash":"sha256:not-a-real-hash","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                """);

        assertThatThrownBy(() -> new ReplayStepLoader().load(log))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("invalid profileHash");
    }

    @Test
    void preservesSessionStartAndStopEvents() throws Exception {
        Path log = tempDir.resolve("lifecycle.jsonl");
        Files.writeString(log, """
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":0,"sequence":1,"sessionId":"main","eventType":"SESSION_START","source":"internal","direction":"INTERNAL","rawHex":"","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":1,"sequence":2,"sessionId":"main","eventType":"SESSION_STOP","source":"internal","direction":"INTERNAL","rawHex":"","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                """);

        var steps = new ReplayStepLoader().load(log);

        assertThat(steps).singleElement().satisfies(step -> assertThat(step.expectedEvents())
                .extracting(ReplayEventExpectation::eventType)
                .containsExactly(com.jkamsker.modemsim.monitor.EventType.SESSION_START,
                        com.jkamsker.modemsim.monitor.EventType.SESSION_STOP));
    }
}
