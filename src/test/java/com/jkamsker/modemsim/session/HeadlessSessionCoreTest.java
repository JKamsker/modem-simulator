package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.macros.FaultAction;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.FreezeMode;
import com.jkamsker.modemsim.state.SimRuntime;
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
    void pinUnlockCanUseConfiguredTestPinReferenceWithoutClearPin() {
        var base = BuiltinProfiles.acceptanceSierra().initialState();
        SimRuntime sim = base.sim();
        var locked = base.withSim(new SimRuntime(
                SimState.SIM_PIN_REQUIRED, sim.pinQueryEnabled(), "TEST_SIM_PIN", null,
                sim.pinRetries(), sim.pukRetries(), sim.imsi(), sim.iccid()));
        HeadlessSession session = new HeadlessSession(
                "locked", BuiltinProfiles.acceptanceSierra().withInitialState(locked), 12345);

        assertThat(session.receive(RawBytes.ascii("AT+CPIN=\"1234\"\r")).outputAscii()).isEqualTo("\r\nOK\r\n");

        assertThat(session.snapshot().sim().state()).isEqualTo(SimState.READY);
    }

    @Test
    void simBusyAndWrongMapToSpecifiedCmeErrors() {
        assertThat(simStateSession(SimState.SIM_BUSY).receive(RawBytes.ascii("AT+CPIN?\r")).outputAscii())
                .contains("+CME ERROR: 14");
        assertThat(simStateSession(SimState.SIM_WRONG).receive(RawBytes.ascii("AT+CPIN?\r")).outputAscii())
                .contains("+CME ERROR: 15");
    }

    @Test
    void dialEscapeAndHangupUpdateCarrierAndDcd() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        SessionResponse dial = session.receive(RawBytes.ascii("ATD123\r"));
        assertThat(dial.outputHex()).isEmpty();
        assertThat(dial.events()).extracting(event -> event.eventType().name()).contains("SCHEDULER_ENQUEUE");
        assertThat(session.drainScheduled().outputAscii()).isEqualTo("\r\nCONNECT\r\n");
        assertThat(session.snapshot().call().mode()).isEqualTo(CallMode.ONLINE_DATA);
        assertThat(session.snapshot().lines().dcd()).isTrue();
        session.advanceTime(1_000);
        assertThat(session.receive(RawBytes.ascii("+++")).outputHex()).isEmpty();
        assertThat(session.advanceTime(1_000).outputAscii()).isEqualTo("\r\nOK\r\n");
        assertThat(session.receive(RawBytes.ascii("ATH\r")).outputAscii()).isEqualTo("\r\nOK\r\n");
        assertThat(session.snapshot().lines().dcd()).isFalse();
    }

    @Test
    void onlineDataModeTreatsNonEscapeBytesAsTransparentData() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        session.receive(RawBytes.ascii("ATD123\r"));
        session.drainScheduled();
        SessionResponse response = session.receive(RawBytes.ascii("AT\r"));

        assertThat(response.outputHex()).isEmpty();
        assertThat(response.events()).extracting(event -> event.eventType().name())
                .containsExactly("RX_BYTES");
        assertThat(session.snapshot().call().mode()).isEqualTo(CallMode.ONLINE_DATA);
    }

    @Test
    void freezeModesDistinguishHeldTxAndIgnoredRx() {
        HeadlessSession holdTx = new HeadlessSession("hold-tx", BuiltinProfiles.acceptanceSierra(), 12345);
        holdTx.applyFault(new FaultAction("modem-freeze", null, null, null, null, FreezeMode.HOLD_TX));

        assertThat(holdTx.receive(RawBytes.ascii("AT\r")).outputHex()).isEmpty();
        assertThat(holdTx.applyFault(new FaultAction("modem-unfreeze", null, null, null, null, null)).outputAscii())
                .isEqualTo("\r\nOK\r\n");

        HeadlessSession holdRxTx = new HeadlessSession("hold-rx-tx", BuiltinProfiles.acceptanceSierra(), 12345);
        holdRxTx.applyFault(new FaultAction("modem-freeze", null, null, null, null, FreezeMode.HOLD_RX_TX));
        SessionResponse ignored = holdRxTx.receive(RawBytes.ascii("AT\r"));

        assertThat(ignored.outputHex()).isEmpty();
        assertThat(ignored.events()).extracting(event -> event.eventType().name()).containsExactly("RX_BYTES");
    }

    @Test
    void onlineDataModeRequiresGuardTimeBeforeEscapeSequence() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        session.receive(RawBytes.ascii("ATD123\r"));
        session.drainScheduled();
        session.receive(RawBytes.ascii("data"));

        assertThat(session.receive(RawBytes.ascii("+++")).outputHex()).isEmpty();
        assertThat(session.snapshot().call().mode()).isEqualTo(CallMode.ONLINE_DATA);
        session.advanceTime(1_000);
        assertThat(session.receive(RawBytes.ascii("+++")).outputHex()).isEmpty();
        assertThat(session.advanceTime(1_000).outputAscii()).isEqualTo("\r\nOK\r\n");
        assertThat(session.snapshot().call().mode()).isEqualTo(CallMode.ONLINE_COMMAND);
    }

    @Test
    void buffersSlowCommandBytesAcrossReads() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        assertThat(session.receive(RawBytes.ascii("AT")).outputHex()).isEmpty();

        assertThat(session.receive(RawBytes.ascii("\r")).outputAscii()).isEqualTo("\r\nOK\r\n");
    }

    @Test
    void parsesMultipleFramesAndSlowARepeatFromByteStream() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        assertThat(session.receive(RawBytes.ascii("AT\rAT\r")).outputAscii()).isEqualTo("\r\nOK\r\n\r\nOK\r\n");
        assertThat(session.receive(RawBytes.ascii("A")).outputHex()).isEmpty();
        assertThat(session.receive(RawBytes.ascii("/")).outputAscii()).isEqualTo("\r\nOK\r\n");
    }

    @Test
    void crlfLineEndingDoesNotPoisonNextCommand() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        assertThat(session.receive(RawBytes.ascii("AT\r\n")).outputAscii()).isEqualTo("\r\nOK\r\n");

        assertThat(session.receive(RawBytes.ascii("AT\r")).outputAscii()).isEqualTo("\r\nOK\r\n");
        assertThat(session.receive(RawBytes.ascii("AT\r\nAT\r")).outputAscii()).isEqualTo("\r\nOK\r\n\r\nOK\r\n");
    }

    @Test
    void escapeSequenceSplitAcrossGuardIntervalsIsData() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
        session.receive(RawBytes.ascii("ATD123\r"));
        session.drainScheduled();
        session.advanceTime(1_000);

        assertThat(session.receive(RawBytes.ascii("+")).outputHex()).isEmpty();
        session.advanceTime(1_000);
        assertThat(session.receive(RawBytes.ascii("+")).outputHex()).isEmpty();
        session.advanceTime(1_000);
        assertThat(session.receive(RawBytes.ascii("+")).outputHex()).isEmpty();
        assertThat(session.advanceTime(1_000).outputHex()).isEmpty();
        assertThat(session.snapshot().call().mode()).isEqualTo(CallMode.ONLINE_DATA);
    }

    @Test
    void escapeSequenceSplitWithinGuardWindowIsAccepted() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
        session.receive(RawBytes.ascii("ATD123\r"));
        session.drainScheduled();
        session.advanceTime(1_000);

        session.receive(RawBytes.ascii("+"));
        session.advanceTime(999);
        session.receive(RawBytes.ascii("+"));
        session.advanceTime(999);
        session.receive(RawBytes.ascii("+"));

        assertThat(session.advanceTime(1_000).outputAscii()).isEqualTo("\r\nOK\r\n");
        assertThat(session.snapshot().call().mode()).isEqualTo(CallMode.ONLINE_COMMAND);
    }

    @Test
    void escapeSequenceChunkWithLongByteSpanIsData() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
        session.receive(RawBytes.ascii("ATD123\r"));
        session.drainScheduled();
        session.advanceTime(1_000);

        session.receiveTimed(RawBytes.ascii("+++"), 1_000_000_000L, 2_100_000_000L);

        assertThat(session.advanceTime(1_000).outputHex()).isEmpty();
        assertThat(session.snapshot().call().mode()).isEqualTo(CallMode.ONLINE_DATA);
    }

    @Test
    void escapeSequencePostGuardStartsAtThirdByte() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
        session.receive(RawBytes.ascii("ATD123\r"));
        session.drainScheduled();
        long first = 10_000_000_000L;
        long last = first + 20_000_000L;
        session.advanceTo(first - 100_000_000L);

        session.receiveTimed(RawBytes.ascii("+++"), first, last);

        assertThat(session.advanceTo(first + 50_000_000L).outputHex()).isEmpty();
        assertThat(session.advanceTo(last + 1_000_000_000L).outputAscii()).isEqualTo("\r\nOK\r\n");
    }

    @Test
    void repeatCommandPublishesSpecialRepeatBeforeReplayingLastCommand() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
        session.receive(RawBytes.ascii("AT\r"));

        SessionResponse repeated = session.receive(RawBytes.ascii("A/"));

        assertThat(repeated.outputAscii()).isEqualTo("\r\nOK\r\n");
        assertThat(repeated.events()).anySatisfy(event -> {
            assertThat(event.eventType().name()).isEqualTo("PARSED_COMMAND");
            assertThat(event.parsedCommand()).containsEntry("kind", "SPECIAL_REPEAT");
        });
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

    private HeadlessSession simStateSession(SimState state) {
        HeadlessSession base = new HeadlessSession("base", BuiltinProfiles.acceptanceSierra(), 12345);
        var next = base.snapshot()
                .withSim(base.snapshot().sim().withState(state))
                .withSettings(base.snapshot().settings().withCmee(1));
        return new HeadlessSession("sim", BuiltinProfiles.acceptanceSierra().withInitialState(next), 12345);
    }
}
