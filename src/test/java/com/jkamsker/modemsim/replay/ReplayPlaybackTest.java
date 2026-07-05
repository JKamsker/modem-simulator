package com.jkamsker.modemsim.replay;

import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.HeadlessEndpoint;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ReplayPlaybackTest {
    @TempDir
    Path tempDir;

    @Test
    void validatesMetadataAndWritesCapturedDceBytes() throws Exception {
        var steps = new ReplayStepLoader().load(validLog("events.jsonl"));
        HeadlessEndpoint endpoint = new HeadlessEndpoint();

        ReplayPlayback playback = new ReplayPlayback();
        ReplayReport report = playback.validate(steps);
        var written = playback.play(endpoint, serialConfig(), steps, false);

        assertThat(report.valid()).isTrue();
        assertThat(written.toHex()).isEqualTo("0D0A4F4B0D0A");
        assertThat(endpoint.written().ascii()).isEqualTo("\r\nOK\r\n");
        assertThat(steps.getFirst().expectedEvents().getFirst().monotonicNanos()).isZero();
    }

    @Test
    void acceptsStateRedactionButRejectsRawPayloadRedaction() throws Exception {
        var stateRedacted = new ReplayStepLoader().load(validLog("state-redacted.jsonl"));
        var rawRedacted = new ReplayStepLoader().load(log("raw-redacted.jsonl", """
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":0,"sequence":1,"sessionId":"main","eventType":"RX_BYTES","source":"rx","direction":"DTE_TO_DCE","rawHex":"41540D","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":true,"policy":"default-v1","fields":["rawHex"],"classes":["pin"]}}
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":1,"sequence":2,"sessionId":"main","eventType":"TX_BYTES","source":"tx","direction":"DCE_TO_DTE","rawHex":"0D0A4F4B0D0A","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                """));

        ReplayPlayback playback = new ReplayPlayback();

        assertThat(playback.validate(stateRedacted).valid()).isTrue();
        assertThat(playback.validate(rawRedacted).divergences())
                .anySatisfy(message -> assertThat(message).contains("redacted replay bytes"));
    }

    private Path validLog(String name) throws Exception {
        return log(name, """
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":0,"sequence":1,"sessionId":"main","eventType":"RX_BYTES","source":"rx","direction":"DTE_TO_DCE","rawHex":"41540D","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":true,"policy":"default-v1","fields":["stateAfter"],"classes":["pin"]}}
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":1,"sequence":2,"sessionId":"main","eventType":"TX_BYTES","source":"tx","direction":"DCE_TO_DTE","rawHex":"0D0A4F4B0D0A","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":true,"policy":"default-v1","fields":["stateAfter"],"classes":["pin"]}}
                """);
    }

    private Path log(String name, String content) throws Exception {
        Path log = tempDir.resolve(name);
        Files.writeString(log, content);
        return log;
    }

    private SerialConfig serialConfig() {
        return new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE);
    }
}
