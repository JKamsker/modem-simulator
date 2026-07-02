package com.alegs3.modemsim.session;

import com.alegs3.modemsim.parser.RawBytes;
import com.alegs3.modemsim.profiles.BuiltinProfiles;
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
                .contains("+CMGL: 1").contains("stored");
        assertThat(session.receive(RawBytes.ascii("AT+CMGD=1\r")).outputAscii())
                .isEqualTo("\r\nOK\r\n");
        assertThat(session.snapshot().sms().messages()).isEmpty();
    }

    private void submitAndDrain(HeadlessSession session, String body) {
        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        session.receive(RawBytes.ascii(body + "\u001A"));
        session.drainScheduled();
    }
}
