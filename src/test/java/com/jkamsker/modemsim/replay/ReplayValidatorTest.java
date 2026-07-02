package com.jkamsker.modemsim.replay;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReplayValidatorTest {
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
}
