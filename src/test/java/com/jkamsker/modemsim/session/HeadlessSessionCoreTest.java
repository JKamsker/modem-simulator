package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.SimState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HeadlessSessionCoreTest {
    @Test
    void atReturnsDefaultVerboseOkBytes() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        SessionResponse response = session.receive(RawBytes.ascii("AT\r"));

        assertThat(response.outputHex()).isEqualTo("0D0A4F4B0D0A");
        assertThat(response.events()).extracting(event -> event.eventType().name())
                .contains("RX_BYTES", "PARSED_COMMAND", "HANDLER_RESULT", "TX_BYTES");
    }

    @Test
    void quietModeSuppressesFinalResultInSameCommandLine() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        SessionResponse response = session.receive(RawBytes.ascii("ATQ1\r"));

        assertThat(response.outputHex()).isEmpty();
        assertThat(session.snapshot().settings().quiet()).isTrue();
    }

    @Test
    void quietModeSuppressesLaterHandlerOutputInSameCommandLine() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        SessionResponse response = session.receive(RawBytes.ascii("ATQ1+CSQ\r"));

        assertThat(response.outputHex()).isEmpty();
        assertThat(session.snapshot().settings().quiet()).isTrue();
    }

    @Test
    void readsRegistrationAndSignalFromState() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        assertThat(session.receive(RawBytes.ascii("AT+CREG?\r")).outputAscii())
                .isEqualTo("\r\n+CREG: 2,1,\"00C3\",\"00001234\",7\r\n\r\nOK\r\n");
        assertThat(session.receive(RawBytes.ascii("AT+CSQ\r")).outputAscii())
                .isEqualTo("\r\n+CSQ: 18,0\r\n\r\nOK\r\n");
    }

    @Test
    void pinUnlockMovesOnlySimStateToReady() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
        var locked = session.snapshot()
                .withSim(session.snapshot().sim().withState(SimState.SIM_PIN_REQUIRED))
                .withNetwork(session.snapshot().network().withRegistration(0));
        session = new HeadlessSession("locked", BuiltinProfiles.acceptanceSierra().withInitialState(locked), 12345);

        assertThat(session.receive(RawBytes.ascii("AT+CPIN?\r")).outputAscii())
                .contains("+CPIN: SIM PIN");
        assertThat(session.receive(RawBytes.ascii("AT+CPIN=\"1234\"\r")).outputAscii())
                .isEqualTo("\r\nOK\r\n");
        assertThat(session.snapshot().sim().state()).isEqualTo(SimState.READY);
        assertThat(session.snapshot().network().stat()).isZero();
    }

    @Test
    void dialEscapeAndHangupUpdateCarrierAndDcd() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        assertThat(session.receive(RawBytes.ascii("ATD123\r")).outputAscii()).isEqualTo("\r\nCONNECT\r\n");
        assertThat(session.snapshot().call().mode()).isEqualTo(CallMode.ONLINE_DATA);
        assertThat(session.snapshot().lines().dcd()).isTrue();
        session.advanceTime(1_000);
        assertThat(session.receive(RawBytes.ascii("+++\r")).outputAscii()).isEqualTo("\r\nOK\r\n");
        assertThat(session.receive(RawBytes.ascii("ATH\r")).outputAscii()).isEqualTo("\r\nOK\r\n");
        assertThat(session.snapshot().lines().dcd()).isFalse();
    }

    @Test
    void onlineDataModeTreatsNonEscapeBytesAsTransparentData() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        session.receive(RawBytes.ascii("ATD123\r"));
        SessionResponse response = session.receive(RawBytes.ascii("AT\r"));

        assertThat(response.outputHex()).isEmpty();
        assertThat(response.events()).extracting(event -> event.eventType().name())
                .containsExactly("RX_BYTES");
        assertThat(session.snapshot().call().mode()).isEqualTo(CallMode.ONLINE_DATA);
    }

    @Test
    void onlineDataModeRequiresGuardTimeBeforeEscapeSequence() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        session.receive(RawBytes.ascii("ATD123\r"));

        assertThat(session.receive(RawBytes.ascii("+++\r")).outputHex()).isEmpty();
        assertThat(session.snapshot().call().mode()).isEqualTo(CallMode.ONLINE_DATA);
        session.advanceTime(1_000);
        assertThat(session.receive(RawBytes.ascii("+++\r")).outputAscii()).isEqualTo("\r\nOK\r\n");
        assertThat(session.snapshot().call().mode()).isEqualTo(CallMode.ONLINE_COMMAND);
    }

    @Test
    void parserUsesActiveS3CommandTerminatorFromSessionState() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        session.receive(RawBytes.ascii("ATS3=59\r"));

        assertThat(session.receive(RawBytes.ascii("AT;")).outputAscii()).isEqualTo(";\nOK;\n");
        assertThat(session.receive(RawBytes.ascii("AT\r")).outputHex()).isEmpty();
    }

    @Test
    void ampersandLineSettingsAreParsedAndApplied() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        assertThat(session.receive(RawBytes.ascii("AT&D2&C0\r")).outputAscii()).isEqualTo("\r\nOK\r\n");

        assertThat(session.snapshot().settings().ampD()).isEqualTo(2);
        assertThat(session.snapshot().settings().ampC()).isZero();
        assertThat(session.snapshot().lines().dcd()).isTrue();
    }
}
