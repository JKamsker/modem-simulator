package com.jkamsker.modemsim.replay;

import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.ModemEventJson;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ReplayValidatorTest {
    @TempDir
    Path tempDir;

    @Test
    void recomputeDetectsMatchingAndDivergentTranscripts() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
        ReplayStep step = new ReplayStep(RawBytes.ascii("AT\r"), RawBytes.hex("0D0A4F4B0D0A"), false);

        assertThat(new ReplayValidator().validateRecompute(session, List.of(step)).valid()).isTrue();

        HeadlessSession other = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
        ReplayReport divergent = new ReplayValidator().validateRecompute(other,
                List.of(new ReplayStep(RawBytes.ascii("AT\r"), RawBytes.hex("00"), false)));
        assertThat(divergent.valid()).isFalse();
        assertThat(divergent.divergences()).singleElement().asString().contains("expected 00");
    }

    @Test
    void validatesEventJsonlMetadataDuringRecompute() throws Exception {
        HeadlessSession capture = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
        Path log = tempDir.resolve("events.jsonl");
        Files.writeString(log, ModemEventJson.toJsonLines(capture.receive(RawBytes.ascii("AT\r")).events()));

        List<ReplayStep> steps = new ReplayStepLoader().load(log);
        ReplayReport report = new ReplayValidator().validateRecompute(
                new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345), steps);

        assertThat(steps).singleElement().satisfies(step ->
                assertThat(step.expectedEvents()).hasSizeGreaterThanOrEqualTo(4));
        assertThat(report.valid()).isTrue();
    }

    @Test
    void detectsMetadataDivergence() {
        ReplayStep step = new ReplayStep(
                RawBytes.ascii("AT\r"),
                RawBytes.hex("0D0A4F4B0D0A"),
                false,
                List.of(new ReplayEventExpectation(
                        EventType.RX_BYTES,
                        Direction.DTE_TO_DCE,
                        999L,
                        "41540D",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)));

        ReplayReport report = new ReplayValidator().validateRecompute(
                new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345), List.of(step));

        assertThat(report.valid()).isFalse();
        assertThat(report.divergences()).anySatisfy(message -> assertThat(message).contains("sequence expected 999"));
    }

    @Test
    void strictReplayRejectsExpectedEventsWithoutMetadata() {
        ReplayStep step = new ReplayStep(
                RawBytes.ascii("AT\r"),
                RawBytes.hex("0D0A4F4B0D0A"),
                false,
                List.of(new ReplayEventExpectation(
                        EventType.RX_BYTES,
                        Direction.DTE_TO_DCE,
                        null,
                        "41540D",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)));

        ReplayReport report = new ReplayValidator().validateRecompute(
                new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345), List.of(step), true);

        assertThat(report.valid()).isFalse();
        assertThat(report.divergences()).anySatisfy(message -> assertThat(message).contains("missing profileHash"));
        assertThat(report.divergences()).anySatisfy(message -> assertThat(message).contains("missing sessionSeed"));
    }
}
