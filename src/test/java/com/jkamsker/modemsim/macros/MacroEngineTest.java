package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.state.ModemLifecycle;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class MacroEngineTest {
    @Test
    void smsSubmitMacroReplacesNormalSubmitWithDelayedCmsError() {
        MacroSet macros = new MacroLoader().load(Path.of("docs/Tasks/Initial-Spec/examples/macros.sms-error-123.xml"));
        HeadlessSession session = new HeadlessSession(
                "main", BuiltinProfiles.acceptanceSierra(), 12345,
                new com.jkamsker.modemsim.monitor.InMemoryEventSink(), new MacroEngine(macros));

        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        SessionAssertions.assertNoImmediateOutput(session.receive(RawBytes.ascii("smscommand dst\u001A")));

        assertThat(session.drainScheduled().outputAscii()).isEqualTo("\r\n+CMS ERROR: 123\r\n");
        assertThat(session.snapshot().sms().messages()).isEmpty();
    }

    @Test
    void customResponseSendsTokenExpandedBytes() {
        MacroSet macros = new MacroLoader().load(
                Path.of("docs/Tasks/Initial-Spec/examples/macros.faults-and-custom-responses.xml"));
        HeadlessSession session = new HeadlessSession(
                "main", BuiltinProfiles.acceptanceSierra(), 12345,
                new com.jkamsker.modemsim.monitor.InMemoryEventSink(), new MacroEngine(macros));

        assertThat(session.receive(RawBytes.ascii("AT+CMSG123\r")).outputHex()).isEqualTo("0D4552520D");
    }

    @Test
    void faultMacroAppliesFreezeWithoutBlockingSessionThread() {
        MacroSet macros = new MacroLoader().load(
                Path.of("docs/Tasks/Initial-Spec/examples/macros.faults-and-custom-responses.xml"));
        HeadlessSession session = new HeadlessSession(
                "main", BuiltinProfiles.acceptanceSierra(), 12345,
                new com.jkamsker.modemsim.monitor.InMemoryEventSink(), new MacroEngine(macros));

        assertThat(session.receive(RawBytes.ascii("AT+TESTFREEZE\r")).outputHex()).isEmpty();
        assertThat(session.snapshot().modem().lifecycle()).isEqualTo(ModemLifecycle.FROZEN);
        assertThat(session.receive(RawBytes.ascii("AT\r")).outputHex()).isEmpty();
    }

    private static final class SessionAssertions {
        static void assertNoImmediateOutput(com.jkamsker.modemsim.session.SessionResponse response) {
            assertThat(response.outputHex()).isEmpty();
            assertThat(response.events()).extracting(event -> event.eventType().name())
                    .contains("SCHEDULER_ENQUEUE");
        }
    }
}
