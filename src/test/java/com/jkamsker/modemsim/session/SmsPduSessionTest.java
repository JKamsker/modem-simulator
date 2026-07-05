package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.state.NetworkRuntime;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SmsRateLimit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SmsPduSessionTest {
    @Test
    void pduModeCmgsEscAbortsWithoutStoringMessage() {
        HeadlessSession session = unlimitedSmsSession();
        session.receive(RawBytes.ascii("AT+CMGF=0\r"));

        session.receive(RawBytes.ascii("AT+CMGS=2\r"));
        session.receive(RawBytes.ascii("00AABB\u001B"));

        assertThat(session.drainScheduled().outputAscii()).contains("OK");
        assertThat(session.snapshot().sms().messages()).isEmpty();
        assertThat(session.receive(RawBytes.ascii("AT\r")).outputAscii()).isEqualTo("\r\nOK\r\n");
    }

    @Test
    void pduModeCmgsRejectsQuotedLengthWithoutEnteringPduMode() {
        HeadlessSession session = unlimitedSmsSession();
        session.receive(RawBytes.ascii("AT+CMGF=0\r"));

        SessionResponse response = session.receive(RawBytes.ascii("AT+CMGS=\"2\"\r"));

        assertThat(response.outputAscii()).isEqualTo("\r\nERROR\r\n");
        assertThat(response.events()).extracting(event -> event.eventType()).contains(EventType.PARSE_ERROR);
        assertThat(session.receive(RawBytes.ascii("AT\r")).outputAscii()).isEqualTo("\r\nOK\r\n");
    }

    @Test
    void pduModeValidatesDeclaredLengthAndStoresOpaquePdu() {
        HeadlessSession session = unlimitedSmsSession();

        session.receive(RawBytes.ascii("AT+CMGF=0\r"));
        assertThat(session.receive(RawBytes.ascii("AT+CMGS=2\r")).outputHex()).isEqualTo("0D0A3E20");
        session.receive(RawBytes.ascii("001\u001A"));
        assertThat(session.drainScheduled().outputAscii()).contains("+CMS ERROR: 304");
        assertThat(session.snapshot().sms().messages()).isEmpty();

        session.receive(RawBytes.ascii("AT+CMGS=2\r"));
        session.receive(RawBytes.ascii("00AABB\u001A"));
        session.drainScheduled();
        assertThat(session.snapshot().sms().messages().values())
                .extracting(message -> message.pdu())
                .containsOnly("00AABB");
        assertThat(session.receive(RawBytes.ascii("AT+CMGR=1\r")).outputAscii())
                .contains("+CMGR").contains("00AABB").doesNotContain("null");
    }

    @Test
    void pduModeNormalizesWhitespaceBeforeLengthValidationAndStorage() {
        HeadlessSession session = unlimitedSmsSession();

        session.receive(RawBytes.ascii("AT+CMGF=0\r"));
        session.receive(RawBytes.ascii("AT+CMGS=2\r"));
        session.receive(RawBytes.ascii("00 AA BB\u001A"));
        assertThat(session.drainScheduled().outputAscii()).contains("+CMGS:").contains("OK");

        assertThat(session.snapshot().sms().messages().values())
                .extracting(message -> message.pdu())
                .containsOnly("00AABB");
    }

    private HeadlessSession unlimitedSmsSession() {
        var base = BuiltinProfiles.acceptanceSierra();
        NetworkRuntime network = base.initialState().network();
        NetworkRuntime updated = new NetworkRuntime(
                network.cregN(), 1, network.lac(), network.ci(), network.act(),
                network.rejectCauseType(), network.rejectCause(), network.operator(),
                SmsRateLimit.none(), network.delays());
        var state = base.initialState()
                .withNetwork(updated)
                .withSim(base.initialState().sim().withState(SimState.READY));
        return new HeadlessSession("sms-pdu", base.withInitialState(state), 12345);
    }
}
