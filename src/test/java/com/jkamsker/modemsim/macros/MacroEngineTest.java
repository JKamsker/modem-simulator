package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.state.ModemLifecycle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class MacroEngineTest {
    @TempDir
    Path tempDir;

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

    @Test
    void beforeAndAfterMacrosWrapNormalCommandHandling() throws Exception {
        MacroSet macros = new MacroLoader().load(write("hooks.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="before-at" priority="100" phase="before">
                    <match command="AT"/>
                    <then><emit line="+BEFORE"/></then>
                  </macro>
                  <macro id="after-at" priority="90" phase="after">
                    <match command="AT"/>
                    <then><emit line="+AFTER"/></then>
                  </macro>
                </macros>
                """));
        HeadlessSession session = new HeadlessSession(
                "main", BuiltinProfiles.acceptanceSierra(), 12345,
                new com.jkamsker.modemsim.monitor.InMemoryEventSink(), new MacroEngine(macros));

        String output = session.receive(RawBytes.ascii("AT\r")).outputAscii();

        assertThat(output).contains("+BEFORE");
        assertThat(output).contains("+AFTER");
        assertThat(output).contains("OK");
    }

    @Test
    void smsRegexPredicatesParticipateInMatching() throws Exception {
        MacroSet macros = new MacroLoader().load(write("sms-regex.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="regex-sms" priority="100" phase="replace">
                    <match type="sms-submit">
                      <destination regex="^\\+49170"/>
                      <body regex="dst$"/>
                    </match>
                    <then><emit line="+CMS ERROR: 321"/></then>
                  </macro>
                </macros>
                """));
        HeadlessSession session = new HeadlessSession(
                "main", BuiltinProfiles.acceptanceSierra(), 12345,
                new com.jkamsker.modemsim.monitor.InMemoryEventSink(), new MacroEngine(macros));

        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        String output = session.receive(RawBytes.ascii("smscommand dst\u001A")).outputAscii();

        assertThat(output).contains("+CMS ERROR: 321");
    }

    @Test
    void validatorRejectsAmbiguousCustomResponseSemantics() throws Exception {
        var report = new MacroLoader().validate(write("ambiguous-custom.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <custom-response id="bad" priority="100">
                    <if rawGlob="AT*" command="AT"/>
                    <send text="OK" rawHex="4F4B"/>
                  </custom-response>
                </macros>
                """));

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("exactly one"));
    }

    @Test
    void validatorRejectsUnknownStatePathsAndFaultAttributes() throws Exception {
        var report = new MacroLoader().validate(write("bad-actions.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="bad-actions" priority="100" phase="replace">
                    <match command="AT"/>
                    <then>
                      <set path="state.unknown.value" value="1"/>
                      <fault type="network-outage" stat="1"/>
                    </then>
                  </macro>
                </macros>
                """));

        assertThat(report.valid()).isFalse();
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("unknown state path"));
        assertThat(report.errors()).anySatisfy(error -> assertThat(error).contains("does not accept stat"));
    }

    private Path write(String name, String content) throws Exception {
        Path path = tempDir.resolve(name);
        Files.writeString(path, content);
        return path;
    }

    private static final class SessionAssertions {
        static void assertNoImmediateOutput(com.jkamsker.modemsim.session.SessionResponse response) {
            assertThat(response.outputHex()).isEmpty();
            assertThat(response.events()).extracting(event -> event.eventType().name())
                    .contains("SCHEDULER_ENQUEUE");
        }
    }
}
