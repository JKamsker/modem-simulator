package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HayesLineControlTest {
    @Test
    void hangupForcesDcdLowEvenWhenAmpC0IsConfigured() {
        HeadlessSession session = new HeadlessSession("ampc0-hangup",
                BuiltinProfiles.byId("generic-hayes-v250"), 12345);

        assertThat(session.receive(RawBytes.ascii("AT&C0\r")).outputAscii()).contains("OK");
        assertThat(session.snapshot().lines().dcd()).isTrue();
        assertThat(session.receive(RawBytes.ascii("ATD123\r")).outputAscii()).contains("CONNECT");
        session.advanceTime(1_000);
        assertThat(session.receive(RawBytes.ascii("+++")).outputAscii()).isEmpty();
        assertThat(session.advanceTime(1_000).outputAscii()).contains("OK");

        assertThat(session.receive(RawBytes.ascii("ATH\r")).outputAscii()).contains("OK");

        assertThat(session.snapshot().call().carrier()).isFalse();
        assertThat(session.snapshot().lines().dcd()).isFalse();
    }
}
