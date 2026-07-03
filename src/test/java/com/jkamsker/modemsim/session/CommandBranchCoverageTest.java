package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.state.NetworkRuntime;
import com.jkamsker.modemsim.state.SignalRuntime;
import com.jkamsker.modemsim.state.SimState;
import org.junit.jupiter.api.Test;

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
        assertThat(session.receive(RawBytes.ascii("ATD555\r")).outputAscii()).contains("CONNECT");
        assertThat(session.receive(RawBytes.ascii("+++\r")).outputAscii()).contains("OK");
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
        assertThat(session.receive(RawBytes.ascii("AT+CREG=3\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CREG=4\r")).outputAscii()).contains("ERROR");
        assertThat(session.receive(RawBytes.ascii("AT+CREG\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CSQ=?\r")).outputAscii()).contains("(0-31,99)");
        assertThat(session.receive(RawBytes.ascii("AT+COPS=?\r")).outputAscii()).contains("(0,1,2,3,4)");
        assertThat(session.receive(RawBytes.ascii("AT+COPS=0\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CMEE=2\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CMEE?\r")).outputAscii()).contains("+CMEE: 2");
        assertThat(session.receive(RawBytes.ascii("AT+CMEE=?\r")).outputAscii()).contains("(0-2)");
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

        assertThat(new HeadlessSession("pstn", BuiltinProfiles.westermoTd22(), 12345)
                .receive(RawBytes.ascii("AT+COPS?\r")).outputAscii()).contains("ERROR");

        assertPinState(SimState.SIM_PUK_REQUIRED, "+CPIN: SIM PUK");
        assertPinState(SimState.SIM_NOT_INSERTED, "+CME ERROR: SIM not inserted");
        assertPinState(SimState.SIM_FAILURE, "+CME ERROR: SIM failure");
        assertPinState(SimState.SIM_BUSY, "+CME ERROR: operation not allowed");

        HeadlessSession locked = lockedSession();
        locked.receive(RawBytes.ascii("AT+CMEE=1\r"));
        assertThat(locked.receive(RawBytes.ascii("AT+CPIN=\"0000\"\r")).outputAscii())
                .contains("+CME ERROR: 16");
    }

    @Test
    void smsBranchesCoverReadTestInvalidAndStorageConfiguration() {
        HeadlessSession session = session();

        assertThat(session.receive(RawBytes.ascii("AT+CMGF?\r")).outputAscii()).contains("+CMGF: 1");
        assertThat(session.receive(RawBytes.ascii("AT+CMGF=?\r")).outputAscii()).contains("(0,1)");
        assertThat(session.receive(RawBytes.ascii("AT+CMGF=9\r")).outputAscii()).contains("ERROR");
        assertThat(session.receive(RawBytes.ascii("AT+CMGS?\r")).outputAscii()).contains("ERROR");
        assertThat(session.receive(RawBytes.ascii("AT+CNMI=1,2,0,0,0\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CPMS?\r")).outputAscii()).contains("+CPMS:");
        assertThat(session.receive(RawBytes.ascii("AT+CPMS=?\r")).outputAscii()).contains("\"ME\"");
        assertThat(session.receive(RawBytes.ascii("AT+CPMS=\"SM\"\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CSCA?\r")).outputAscii()).contains("+CSCA:");
        assertThat(session.receive(RawBytes.ascii("AT+CSCA=\"+123\"\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CSCA\r")).outputAscii()).contains("ERROR");
        assertThat(session.receive(RawBytes.ascii("AT+CMGR=abc\r")).outputAscii()).contains("+CMS ERROR: 321");
        assertThat(session.receive(RawBytes.ascii("AT+CMGD=99\r")).outputAscii()).contains("+CMS ERROR: 321");
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

    private void assertPinState(SimState simState, String expected) {
        var base = BuiltinProfiles.acceptanceSierra().initialState();
        var state = base.withSim(base.sim().withState(simState))
                .withNetwork(base.network().withRegistration(0));
        HeadlessSession session = new HeadlessSession("sim", BuiltinProfiles.acceptanceSierra().withInitialState(state), 12345);
        session.receive(RawBytes.ascii("AT+CMEE=2\r"));
        assertThat(session.receive(RawBytes.ascii("AT+CPIN?\r")).outputAscii()).contains(expected);
    }
}
