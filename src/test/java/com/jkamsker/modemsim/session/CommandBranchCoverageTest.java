package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.state.NetworkRuntime;
import com.jkamsker.modemsim.state.SignalRuntime;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SmsMessage;
import com.jkamsker.modemsim.state.SmsRuntime;
import com.jkamsker.modemsim.state.SmsStorage;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CommandBranchCoverageTest {
    @Test
    void hayesBranchesCoverRegistersIdentityRepeatAndOnlineTransitions() {
        HeadlessSession session = session();

        assertThat(session.receive(RawBytes.ascii("ATI\r")).outputAscii()).contains("Sierra Wireless");
        assertThat(session.receive(RawBytes.ascii("ATS7=55\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("ATS7?\r")).outputAscii()).contains("55");
        assertThat(session.receive(RawBytes.ascii("AT&V\r")).outputAscii()).contains("S3=");
        assertThat(session.receive(RawBytes.ascii("ATZ\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT&F\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT&W\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("A/")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("ATO\r")).outputAscii()).contains("NO CARRIER");
        assertThat(session.receive(RawBytes.ascii("ATD\r")).outputAscii()).contains("ERROR");
        assertThat(session.receive(RawBytes.ascii("ATD555\r")).outputHex()).isEmpty();
        assertThat(session.drainScheduled().outputAscii()).contains("CONNECT");
        session.advanceTime(1_000);
        assertThat(session.receive(RawBytes.ascii("+++")).outputHex()).isEmpty();
        assertThat(session.advanceTime(1_000).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("ATO\r")).outputAscii()).contains("CONNECT");
    }

    @Test
    void cellularBranchesCoverRegistrationIdentityCopsCmeeAndPinFailures() {
        HeadlessSession session = session();

        assertThat(session.receive(RawBytes.ascii("AT+CGMI\r")).outputAscii()).contains("Sierra Wireless");
        assertThat(session.receive(RawBytes.ascii("AT+CGMM\r")).outputAscii()).contains("HL8548");
        assertThat(session.receive(RawBytes.ascii("AT+CGMR\r")).outputAscii()).contains("SIM-HL6HL8-v20");
        assertThat(session.receive(RawBytes.ascii("AT+CGSN\r")).outputAscii()).contains("359762080000001");
        assertThat(session.receive(RawBytes.ascii("AT+CREG=?\r")).outputAscii()).contains("(0-3)");
        assertThat(session.receive(RawBytes.ascii("AT+CREG=1\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CREG?\r")).outputAscii())
                .contains("+CREG: 1,1")
                .doesNotContain("00C3");
        assertThat(session.receive(RawBytes.ascii("AT+CREG=2\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CREG?\r")).outputAscii()).contains("+CREG: 2,1,\"00C3\"");
        assertThat(session.receive(RawBytes.ascii("AT+CREG=abc\r")).outputAscii()).contains("ERROR");
        assertThat(session.receive(RawBytes.ascii("AT+CREG=3\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CREG=4\r")).outputAscii()).contains("ERROR");
        assertThat(session.receive(RawBytes.ascii("AT+CREG\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CGREG=1\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CREG?\r")).outputAscii()).contains("+CREG: 3,1");
        var base = session.snapshot();
        assertThat(session.applyState(base.withNetwork(base.network().withCregN(2).withRegistration(5)), "state-change")
                .outputAscii()).contains("+CREG: 5,\"00C3\",\"00001234\",7");
        assertThat(session.receive(RawBytes.ascii("AT+CSQ=?\r")).outputAscii()).contains("(0-31,99)");
        assertThat(session.receive(RawBytes.ascii("AT+COPS=?\r")).outputAscii()).contains("(0,1,2,3,4)");
        assertThat(session.receive(RawBytes.ascii("AT+COPS=0\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CMEE=2\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CMEE?\r")).outputAscii()).contains("+CMEE: 2");
        assertThat(session.receive(RawBytes.ascii("AT+CMEE=?\r")).outputAscii()).contains("(0-2)");
        assertThat(session.receive(RawBytes.ascii("AT+CMEE=9\r")).outputAscii()).contains("ERROR");
        assertThat(session.receive(RawBytes.ascii("AT+CMEE=abc\r")).outputAscii()).contains("ERROR");
        assertThat(session.receive(RawBytes.ascii("AT+CCLK?\r")).outputAscii()).contains("+CCLK:");
        assertThat(session.receive(RawBytes.ascii("AT+CCLK=?\r")).outputAscii()).contains("yy/MM/dd");
        assertThat(session.receive(RawBytes.ascii("AT+CFUN?\r")).outputAscii()).contains("+CFUN: 1");
        assertThat(session.receive(RawBytes.ascii("AT+CFUN=?\r")).outputAscii()).contains("(0,1)");
    }

    @Test
    void cellularErrorBranchesCoverDeniedRegistrationAndCmeModes() {
        NetworkRuntime denied = new NetworkRuntime(
                3, 3, null, null, null, null, null,
                BuiltinProfiles.acceptanceSierra().initialState().network().operator(),
                BuiltinProfiles.acceptanceSierra().initialState().network().smsRateLimit(),
                BuiltinProfiles.acceptanceSierra().initialState().network().delays());
        HeadlessSession deniedSession = new HeadlessSession("denied",
                BuiltinProfiles.acceptanceSierra().withInitialState(
                        BuiltinProfiles.acceptanceSierra().initialState().withNetwork(denied)), 12345);
        assertThat(deniedSession.receive(RawBytes.ascii("AT+CREG?\r")).outputAscii())
                .contains("+CREG: 3,3,0,11");

        HeadlessSession pstn = new HeadlessSession("pstn", BuiltinProfiles.westermoTd22(), 12345);
        assertThat(pstn.receive(RawBytes.ascii("AT+COPS?\r")).outputAscii()).contains("ERROR");
        assertThat(pstn.receive(RawBytes.ascii("AT+CPIN?\r")).outputAscii()).contains("ERROR");
        assertThat(pstn.receive(RawBytes.ascii("AT+CMGF?\r")).outputAscii()).contains("ERROR");

        assertPinState(SimState.SIM_PUK_REQUIRED, "+CPIN: SIM PUK");
        assertPinState(SimState.SIM_NOT_INSERTED, "+CME ERROR: SIM not inserted");
        assertPinState(SimState.SIM_FAILURE, "+CME ERROR: SIM failure");
        assertPinState(SimState.SIM_BUSY, "+CME ERROR: SIM busy");

        HeadlessSession locked = lockedSession();
        locked.receive(RawBytes.ascii("AT+CMEE=2\r"));
        assertThat(locked.receive(RawBytes.ascii("AT+CSQ\r")).outputAscii())
                .contains("+CME ERROR: SIM PIN required");
        locked = lockedSession();
        locked.receive(RawBytes.ascii("AT+CMEE=1\r"));
        assertThat(locked.receive(RawBytes.ascii("AT+CPIN=\"0000\"\r")).outputAscii())
                .contains("+CME ERROR: 16");
    }

    @Test
    void cpinPinExhaustionRequiresPukAndPukCanSetReplacementPin() {
        var base = BuiltinProfiles.acceptanceSierra().initialState();
        var locked = base.withSim(base.sim().withState(SimState.SIM_PIN_REQUIRED).withPinRetries(1));
        HeadlessSession session = new HeadlessSession(
                "puk", BuiltinProfiles.acceptanceSierra().withInitialState(locked), 12345);
        session.receive(RawBytes.ascii("AT+CMEE=1\r"));

        assertThat(session.receive(RawBytes.ascii("AT+CPIN=\"0000\"\r")).outputAscii())
                .contains("+CME ERROR: 16");
        assertThat(session.snapshot().sim().state()).isEqualTo(SimState.SIM_PUK_REQUIRED);
        assertThat(session.snapshot().sim().pinRetries()).isZero();
        assertThat(session.receive(RawBytes.ascii("AT+CPIN?\r")).outputAscii()).contains("SIM PUK");
        assertThat(session.receive(RawBytes.ascii("AT+CPIN=\"00000000\",\"4321\"\r")).outputAscii())
                .contains("+CME ERROR: 16");
        assertThat(session.snapshot().sim().pukRetries()).isEqualTo(9);
        assertThat(session.receive(RawBytes.ascii("AT+CPIN=\"87654321\",\"abcd\"\r")).outputAscii())
                .contains("+CME ERROR: 16");
        assertThat(session.snapshot().sim().state()).isEqualTo(SimState.SIM_PUK_REQUIRED);
        assertThat(session.receive(RawBytes.ascii("AT+CPIN=\"87654321\",\"4321\"\r")).outputAscii())
                .isEqualTo("\r\nOK\r\n");
        assertThat(session.snapshot().sim().state()).isEqualTo(SimState.READY);
        assertThat(session.snapshot().sim().testPin()).isEqualTo("4321");

        var pukLocked = base.withSim(base.sim().withState(SimState.SIM_PUK_REQUIRED).withPukRetries(1));
        HeadlessSession exhausted = new HeadlessSession(
                "puk-exhausted", BuiltinProfiles.acceptanceSierra().withInitialState(pukLocked), 12345);
        exhausted.receive(RawBytes.ascii("AT+CMEE=2\r"));
        assertThat(exhausted.receive(RawBytes.ascii("AT+CPIN=\"00000000\",\"4321\"\r")).outputAscii())
                .contains("+CME ERROR: incorrect password");
        assertThat(exhausted.snapshot().sim().state()).isEqualTo(SimState.SIM_FAILURE);
        assertThat(exhausted.snapshot().sim().pukRetries()).isZero();
    }

    @Test
    void smsBranchesCoverReadTestInvalidAndStorageConfiguration() {
        HeadlessSession session = session();

        assertThat(session.receive(RawBytes.ascii("AT+CMGF?\r")).outputAscii()).contains("+CMGF: 1");
        assertThat(session.receive(RawBytes.ascii("AT+CMGF=?\r")).outputAscii()).contains("(0,1)");
        assertThat(session.receive(RawBytes.ascii("AT+CMGF=9\r")).outputAscii()).contains("ERROR");
        assertThat(session.receive(RawBytes.ascii("AT+CMGS?\r")).outputAscii()).contains("ERROR");
        assertThat(session.receive(RawBytes.ascii("AT+CNMI=1,2,0,0,0\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CNMI?\r")).outputAscii()).contains("+CNMI: 1,2,0,0,0");
        assertThat(session.receive(RawBytes.ascii("AT+CNMI=?\r")).outputAscii()).contains("(0-3)");
        assertThat(session.receive(RawBytes.ascii("AT+CNMI=1,2,3,0,0\r")).outputAscii()).contains("ERROR");
        assertThat(session.receive(RawBytes.ascii("AT+CPMS?\r")).outputAscii()).contains("+CPMS:");
        assertThat(session.receive(RawBytes.ascii("AT+CPMS=?\r")).outputAscii()).contains("\"ME\"");
        assertThat(session.receive(RawBytes.ascii("AT+CPMS=\"SM\"\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CPMS=\"XX\"\r")).outputAscii()).contains("ERROR");
        assertThat(session.receive(RawBytes.ascii("AT+CSCA?\r")).outputAscii()).contains("+CSCA:");
        assertThat(session.receive(RawBytes.ascii("AT+CSCA=\"+123\"\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CSCA\r")).outputAscii()).contains("ERROR");
        assertThat(session.receive(RawBytes.ascii("AT+CSCS?\r")).outputAscii()).contains("+CSCS: \"GSM\"");
        assertThat(session.receive(RawBytes.ascii("AT+CSCS=?\r")).outputAscii()).contains("\"UCS2\"");
        SessionResponse malformedIndex = session.receive(RawBytes.ascii("AT+CMGR=abc\r"));
        assertThat(malformedIndex.outputAscii()).contains("ERROR");
        assertThat(malformedIndex.events()).extracting(event -> event.eventType()).contains(EventType.PARSE_ERROR);
        assertThat(session.receive(RawBytes.ascii("AT+CMGD=99\r")).outputAscii()).contains("+CMS ERROR: 321");
        assertThat(inboundSmsSession().receive(RawBytes.ascii("AT+CMGR=1\r")).outputAscii())
                .contains("+CMGR: \"REC UNREAD\",\"+491709999999\"")
                .contains("incoming");
    }

    @Test
    void normative3gppProfilesExposeOnlyTheirCommandFamily() {
        HeadlessSession at27007 = new HeadlessSession("27007", BuiltinProfiles.byId("3gpp-27007-r18"), 12345);
        assertThat(at27007.receive(RawBytes.ascii("AT\r")).outputAscii()).contains("OK");
        assertThat(at27007.receive(RawBytes.ascii("AT+CREG?\r")).outputAscii()).contains("+CREG");
        assertThat(at27007.receive(RawBytes.ascii("AT+CMGF?\r")).outputAscii()).contains("ERROR");

        HeadlessSession at27005 = new HeadlessSession("27005", BuiltinProfiles.byId("3gpp-27005-r16"), 12345);
        assertThat(at27005.receive(RawBytes.ascii("AT\r")).outputAscii()).contains("OK");
        assertThat(at27005.receive(RawBytes.ascii("AT+CMGF?\r")).outputAscii()).contains("+CMGF");
        assertThat(at27005.receive(RawBytes.ascii("AT+CREG?\r")).outputAscii()).contains("ERROR");
    }

    private HeadlessSession session() {
        return new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
    }

    private HeadlessSession lockedSession() {
        var base = BuiltinProfiles.acceptanceSierra().initialState();
        var state = base.withSim(base.sim().withState(SimState.SIM_PIN_REQUIRED))
                .withNetwork(base.network().withRegistration(0))
                .withSignal(SignalRuntime.unknown());
        return new HeadlessSession("locked", BuiltinProfiles.acceptanceSierra().withInitialState(state), 12345);
    }

    private HeadlessSession inboundSmsSession() {
        var base = BuiltinProfiles.acceptanceSierra().initialState();
        SmsMessage message = new SmsMessage(1, SmsStorage.ME, "REC UNREAD", "+491709999999", null, null,
                "incoming", null);
        SmsRuntime sms = base.sms().withStorage(SmsStorage.ME);
        sms = new SmsRuntime(sms.textMode(), sms.smsc(), sms.cnmi(), sms.storage(),
                sms.writeStorage(), sms.receiveStorage(), sms.nextMessageReference(), Map.of(1, message));
        return new HeadlessSession("sms-inbound",
                BuiltinProfiles.acceptanceSierra().withInitialState(base.withSms(sms)), 12345);
    }

    private void assertPinState(SimState simState, String expected) {
        var base = BuiltinProfiles.acceptanceSierra().initialState();
        var state = base.withSim(base.sim().withState(simState))
                .withNetwork(base.network().withRegistration(0));
        HeadlessSession session = new HeadlessSession("sim", BuiltinProfiles.acceptanceSierra().withInitialState(state), 12345);
        session.receive(RawBytes.ascii("AT+CMEE=2\r"));
        assertThat(session.receive(RawBytes.ascii("AT+CPIN?\r")).outputAscii()).contains(expected);
    }
}
