package com.jkamsker.modemsim.replay;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.HeadlessEndpoint;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class TrmReplayParserTest {
    @TempDir
    Path tempDir;

    @Test
    void loadsTrmRxTxPairsAndPreservesPayloadSpaces() throws Exception {
        Path trm = trm();

        var steps = new ReplayStepLoader().load(trm);

        assertThat(steps).hasSize(2);
        assertThat(steps.getFirst().input().ascii()).isEqualTo("AT+CREG?");
        assertThat(steps.getFirst().expectedOutput().ascii()).isEqualTo("+CREG: 0,1");
        assertThat(steps.get(1).input().ascii()).isEqualTo("AT+CGDCONT=1,\"IP\",\"internet\"");
        assertThat(steps.get(1).expectedOutput().ascii()).isEqualTo("OK WITH SPACE");
        assertThat(steps.getFirst().expectedEvents().getFirst().monotonicNanos()).isEqualTo(4_858_538_535_000_000L);
    }

    @Test
    void findsResponseForIncomingCommandAndCanPlayTxPayload() throws Exception {
        var steps = new ReplayStepLoader().load(trm());
        ReplayPlayback playback = new ReplayPlayback();
        HeadlessEndpoint endpoint = new HeadlessEndpoint();

        RawBytes response = playback.responseFor(RawBytes.ascii("AT+CREG?\r"), steps);
        RawBytes written = playback.play(endpoint, serialConfig(), steps.subList(0, 1), false);

        assertThat(response.ascii()).isEqualTo("+CREG: 0,1");
        assertThat(playback.validate(steps).valid()).isTrue();
        assertThat(written.ascii()).isEqualTo("+CREG: 0,1");
    }

    private Path trm() throws Exception {
        Path trm = tempDir.resolve("capture.trm");
        Files.writeString(trm, """
                RX 1 11 (4858538528): AT+CREG?
                TX 1 11 (4858538535): +CREG: 0,1
                TX 1 11 (4858538536): OK
                RX 4 99 (4858540000): AT+CGDCONT=1,"IP","internet"
                TX 4 99 (4858540015): OK WITH SPACE
                """);
        return trm;
    }

    private SerialConfig serialConfig() {
        return new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE);
    }
}
