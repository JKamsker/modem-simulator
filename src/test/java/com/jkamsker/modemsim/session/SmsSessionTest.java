package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.state.NetworkRuntime;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SmsRateLimit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SmsSessionTest {
    @Test
    void textModeCmgsPromptsAndStoresSubmittedMessageAfterDelay() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        assertThat(session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r")).outputHex())
                .isEqualTo("0D0A3E20");
        SessionResponse submit = session.receive(RawBytes.ascii("smscommand dst\u001A"));

        assertThat(submit.outputHex()).isEmpty();
        assertThat(submit.events()).extracting(event -> event.eventType().name())
                .contains("SCHEDULER_ENQUEUE");
        assertThat(session.drainScheduled().outputAscii()).contains("+CMGS: 42").contains("OK");
        assertThat(session.snapshot().sms().messages()).hasSize(1);
    }

    @Test
    void textModeCmgsAccumulatesBodyAcrossChunksUntilCtrlZ() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        SessionResponse partial = session.receive(RawBytes.ascii("hello "));
        SessionResponse complete = session.receive(RawBytes.ascii("world\u001A"));

        assertThat(partial.outputHex()).isEmpty();
        assertThat(partial.events()).extracting(event -> event.eventType().name()).containsExactly("RX_BYTES");
        assertThat(complete.events()).extracting(event -> event.eventType().name()).contains("SCHEDULER_ENQUEUE");
        session.drainScheduled();
        assertThat(session.snapshot().sms().messages().values())
                .extracting(message -> message.text())
                .containsOnly("hello world");
    }

    @Test
    void textModeCmgsEscAbortsWithoutStoringMessage() {
        HeadlessSession session = unlimitedSmsSession();

        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        SessionResponse aborted = session.receive(RawBytes.ascii("draft\u001B"));

        assertThat(aborted.events()).extracting(event -> event.eventType().name()).contains("HANDLER_RESULT");
        assertThat(session.drainScheduled().outputAscii()).contains("OK");
        assertThat(session.snapshot().sms().messages()).isEmpty();
        assertThat(session.receive(RawBytes.ascii("AT\r")).outputAscii()).isEqualTo("\r\nOK\r\n");
    }

    @Test
    void quietModeSuppressesSmsSubmitCompletion() {
        HeadlessSession session = unlimitedSmsSession();

        session.receive(RawBytes.ascii("ATQ1\r"));
        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        session.receive(RawBytes.ascii("silent\u001A"));

        assertThat(session.drainScheduled().outputHex()).isEmpty();
        assertThat(session.snapshot().sms().messages()).hasSize(1);
    }

    @Test
    void pduModeCmgsEscAbortsWithoutStoringMessage() {
        HeadlessSession session = unlimitedSmsSession();
        session.receive(RawBytes.ascii("AT+CMGF=0\r"));

        session.receive(RawBytes.ascii("AT+CMGS=2\r"));
        session.receive(RawBytes.ascii("0011\u001B"));

        assertThat(session.drainScheduled().outputAscii()).contains("OK");
        assertThat(session.snapshot().sms().messages()).isEmpty();
        assertThat(session.receive(RawBytes.ascii("AT\r")).outputAscii()).isEqualTo("\r\nOK\r\n");
    }

    @Test
    void sixthSmsInsideSlidingWindowUsesConfiguredCmsError() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
        for (int i = 0; i < 5; i++) {
            submitAndDrain(session, "ok " + i);
        }

        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        session.receive(RawBytes.ascii("blocked\u001A"));
        assertThat(session.drainScheduled().outputAscii()).contains("+CMS ERROR: 500");

        session.advanceTime(60_001);
        submitAndDrain(session, "after window");
        assertThat(session.snapshot().sms().messages()).hasSize(6);
    }

    @Test
    void smsStorageCommandsReadListAndDeleteMessages() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
        submitAndDrain(session, "stored");

        assertThat(session.receive(RawBytes.ascii("AT+CMGR=1\r")).outputAscii())
                .contains("+CMGR").contains("stored");
        assertThat(session.receive(RawBytes.ascii("AT+CMGL=\"ALL\"\r")).outputAscii())
                .contains("+CMGL: 1,\"STO SENT\"").doesNotContain("+CMGL: 1:")
                .contains("stored");
        assertThat(session.receive(RawBytes.ascii("AT+CMGD=1\r")).outputAscii())
                .isEqualTo("\r\nOK\r\n");
        assertThat(session.snapshot().sms().messages()).isEmpty();
    }

    @Test
    void textModeCmgsRequiresQuotedPhoneDestination() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        assertThat(session.receive(RawBytes.ascii("AT+CMGS=not-a-phone\r")).outputAscii())
                .isEqualTo("\r\nERROR\r\n");
        assertThat(session.snapshot().call().mode().name()).isEqualTo("COMMAND");
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
        session.receive(RawBytes.ascii("0011\u001A"));
        session.drainScheduled();
        assertThat(session.snapshot().sms().messages().values())
                .extracting(message -> message.pdu())
                .containsOnly("0011");
        assertThat(session.receive(RawBytes.ascii("AT+CMGR=1\r")).outputAscii())
                .contains("+CMGR").contains("0011").doesNotContain("null");
    }

    @Test
    void selectedSmsStorageControlsListReadDeleteAndCapacity() {
        HeadlessSession session = unlimitedSmsSession();

        assertThat(session.receive(RawBytes.ascii("AT+CPMS=\"SM\"\r")).outputAscii()).contains("OK");
        submitAndDrain(session, "stored in sm");
        assertThat(session.receive(RawBytes.ascii("AT+CPMS?\r")).outputAscii())
                .contains("\"SM\",1,20,\"SM\",1,20,\"SM\",1,20");
        assertThat(session.receive(RawBytes.ascii("AT+CPMS=?\r")).outputAscii())
                .contains("(\"ME\",\"SM\",\"MT\"),(\"ME\",\"SM\",\"MT\"),(\"ME\",\"SM\",\"MT\")");
        assertThat(session.receive(RawBytes.ascii("AT+CMGL=\"ALL\"\r")).outputAscii()).contains("stored in sm");
        assertThat(session.receive(RawBytes.ascii("AT+CMGL=\"REC UNREAD\"\r")).outputAscii()).doesNotContain("stored in sm");
        assertThat(session.receive(RawBytes.ascii("AT+CMGL=\"BROKEN\"\r")).outputAscii()).isEqualTo("\r\nERROR\r\n");
        assertThat(session.receive(RawBytes.ascii("AT+CMGL=?\r")).outputAscii()).contains("STO SENT");

        assertThat(session.receive(RawBytes.ascii("AT+CPMS=\"ME\"\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CPMS?\r")).outputAscii()).contains("\"ME\",0,50");
        assertThat(session.receive(RawBytes.ascii("AT+CMGL=\"ALL\"\r")).outputAscii()).doesNotContain("stored in sm");
        assertThat(session.snapshot().sms().messages()).hasSize(1);
    }

    @Test
    void cpmsStoresReadWriteAndReceiveMemoriesIndependently() {
        HeadlessSession session = unlimitedSmsSession();

        assertThat(session.receive(RawBytes.ascii("AT+CPMS=\"SM\",\"ME\",\"MT\"\r")).outputAscii()).contains("OK");
        submitAndDrain(session, "stored in me");

        assertThat(session.receive(RawBytes.ascii("AT+CPMS?\r")).outputAscii())
                .contains("\"SM\",0,20,\"ME\",1,50,\"MT\",0,70");
        assertThat(session.receive(RawBytes.ascii("AT+CMGL=\"ALL\"\r")).outputAscii()).doesNotContain("stored in me");
        assertThat(session.receive(RawBytes.ascii("AT+CPMS=\"ME\",\"ME\",\"MT\"\r")).outputAscii()).contains("OK");
        assertThat(session.receive(RawBytes.ascii("AT+CMGL=\"ALL\"\r")).outputAscii()).contains("stored in me");
    }

    @Test
    void cmgsPromptsBeforeSubmitTimeNetworkAndSimFailures() {
        HeadlessSession noNetwork = smsSession(SmsRateLimit.none(), 4, SimState.READY, 2);
        assertThat(noNetwork.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r")).outputHex())
                .startsWith("0D0A3E20");
        noNetwork.receive(RawBytes.ascii("body\u001A"));
        assertThat(noNetwork.drainScheduled().outputAscii()).contains("+CMS ERROR: 500");

        HeadlessSession simFailure = smsSession(SmsRateLimit.none(), 1, SimState.SIM_FAILURE, 2);
        assertThat(simFailure.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r")).outputHex())
                .startsWith("0D0A3E20");
        simFailure.receive(RawBytes.ascii("body\u001A"));
        assertThat(simFailure.drainScheduled().outputAscii()).contains("+CME ERROR: SIM failure");
    }

    @Test
    void selectedStorageCapacityRejectsOverflowWithoutSavingMessage() {
        HeadlessSession session = unlimitedSmsSession();
        session.receive(RawBytes.ascii("AT+CPMS=\"SM\"\r"));
        for (int i = 0; i < 20; i++) {
            submitAndDrain(session, "sm " + i);
        }

        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        session.receive(RawBytes.ascii("overflow\u001A"));

        assertThat(session.drainScheduled().outputAscii()).contains("+CMS ERROR: 322");
        assertThat(session.snapshot().sms().messages()).hasSize(20);
    }

    @Test
    void recipientScopedRateLimitDoesNotBlockDifferentRecipient() {
        HeadlessSession session = smsSession(new SmsRateLimit(1, 60, "recipient", 500));

        submitAndDrain(session, "first", "+491700000001");
        session.receive(RawBytes.ascii("AT+CMGS=\"+491700000001\"\r"));
        session.receive(RawBytes.ascii("blocked\u001A"));
        assertThat(session.drainScheduled().outputAscii()).contains("+CMS ERROR: 500");

        submitAndDrain(session, "second recipient", "+491700000002");
        assertThat(session.snapshot().sms().messages()).hasSize(2);
    }

    @Test
    void cscaRequiresQuotedSmscNumberAndStoresOnlyTheAddress() {
        HeadlessSession session = unlimitedSmsSession();

        assertThat(session.receive(RawBytes.ascii("AT+CSCA=not-a-number\r")).outputAscii()).isEqualTo("\r\nERROR\r\n");
        assertThat(session.receive(RawBytes.ascii("AT+CSCA=\"+491710760001\",145\r")).outputAscii()).isEqualTo("\r\nOK\r\n");

        assertThat(session.receive(RawBytes.ascii("AT+CSCA?\r")).outputAscii())
                .contains("+CSCA: \"+491710760001\",145");
    }

    @Test
    void registrationStateChangesEmitCregUrcsWhenEnabled() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        SessionResponse response = session.applyFault("network-outage");

        assertThat(response.outputAscii()).contains("+CREG: 4");
    }

    private void submitAndDrain(HeadlessSession session, String body) {
        submitAndDrain(session, body, "+491701234567");
    }

    private void submitAndDrain(HeadlessSession session, String body, String recipient) {
        session.receive(RawBytes.ascii("AT+CMGS=\"" + recipient + "\"\r"));
        session.receive(RawBytes.ascii(body + "\u001A"));
        session.drainScheduled();
    }

    private HeadlessSession unlimitedSmsSession() {
        return smsSession(SmsRateLimit.none());
    }

    private HeadlessSession smsSession(SmsRateLimit rateLimit) {
        return smsSession(rateLimit, 1, SimState.READY, 0);
    }

    private HeadlessSession smsSession(SmsRateLimit rateLimit, int registration, SimState simState, int cmee) {
        var base = BuiltinProfiles.acceptanceSierra();
        NetworkRuntime network = base.initialState().network();
        NetworkRuntime updated = new NetworkRuntime(
                network.cregN(), registration, network.lac(), network.ci(), network.act(),
                network.rejectCauseType(), network.rejectCause(), network.operator(), rateLimit, network.delays());
        var state = base.initialState()
                .withNetwork(updated)
                .withSim(base.initialState().sim().withState(simState))
                .withSettings(base.initialState().settings().withCmee(cmee));
        return new HeadlessSession("sms", base.withInitialState(state), 12345);
    }
}
