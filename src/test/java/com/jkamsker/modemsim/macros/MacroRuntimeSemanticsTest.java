package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MacroRuntimeSemanticsTest {
    @TempDir
    Path tempDir;

    @Test
    void stateChangeMacrosRunAfterNormalCommandStateTransitions() throws Exception {
        MacroSet macros = new MacroLoader().load(write("state-change-command.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="quiet-state" priority="100" phase="on-state-change">
                    <match type="state-change"/>
                    <when><state path="state.sms.textMode" equals="false"/></when>
                    <then><emit line="+STATE: PDU"/></then>
                  </macro>
                </macros>
                """));
        HeadlessSession session = session(macros);

        assertThat(session.receive(RawBytes.ascii("AT+CMGF=0\r")).outputAscii()).contains("+STATE: PDU");
    }

    @Test
    void timerMacrosCanBeScopedToRuntimeTimerId() throws Exception {
        MacroSet macros = new MacroLoader(true).load(write("timer-id.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="first" phase="on-timer">
                    <match type="timer" timerId="first"/>
                    <then><emit line="+TIMER: FIRST"/></then>
                  </macro>
                  <macro id="second" phase="on-timer">
                    <match type="timer" timerId="second"/>
                    <then><emit line="+TIMER: SECOND"/></then>
                  </macro>
                </macros>
                """));

        assertThat(session(macros).fireTimer("second").outputAscii())
                .contains("+TIMER: SECOND")
                .doesNotContain("FIRST");
    }

    @Test
    void timerFaultArmsRebootCompletion() throws Exception {
        MacroSet macros = new MacroLoader(true).load(write("timer-reboot.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="reboot" phase="on-timer">
                    <match type="timer" timerId="reboot"/>
                    <then><fault type="modem-reboot" durationMs="1000"/></then>
                  </macro>
                </macros>
                """));
        HeadlessSession session = session(macros);

        session.fireTimer("reboot");
        assertThat(session.snapshot().modem().lifecycle()).isEqualTo(
                com.jkamsker.modemsim.state.ModemLifecycle.REBOOTING);

        session.advanceTime(1_000);

        assertThat(session.snapshot().modem().lifecycle()).isEqualTo(
                com.jkamsker.modemsim.state.ModemLifecycle.READY);
    }

    @Test
    void rawGlobMatchesCompleteIncomingAtLine() throws Exception {
        MacroSet macros = new MacroLoader().load(write("line-match.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="compound-line" phase="replace">
                    <match rawGlob="AT+FIRST;+SECOND"/>
                    <then><emit line="+FULL-LINE"/></then>
                  </macro>
                </macros>
                """));

        assertThat(session(macros).receive(RawBytes.ascii("AT+FIRST;+SECOND\r")).outputAscii())
                .contains("+FULL-LINE");
    }

    @Test
    void macroControlDoesNotSwitchEngineWhenAuditSinkFails() throws Exception {
        MacroSet macros = new MacroLoader().load(write("replace-at.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="replace-at" phase="replace">
                    <match command="AT"/>
                    <then><emit line="+REPLACED"/></then>
                  </macro>
                </macros>
                """));
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345, event -> {
            if ("macro-control".equals(event.injectionType())) {
                throw new com.jkamsker.modemsim.monitor.AuditLogException("boom", null);
            }
        });

        assertThatThrownBy(() -> session.replaceMacroEngine(new MacroEngine(macros), "reload"))
                .isInstanceOf(com.jkamsker.modemsim.monitor.AuditLogException.class);
        assertThat(session.receive(RawBytes.ascii("AT\r")).outputAscii()).isEqualTo("\r\nOK\r\n");
    }

    private HeadlessSession session(MacroSet macros) {
        return new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345,
                new com.jkamsker.modemsim.monitor.InMemoryEventSink(), new MacroEngine(macros));
    }

    private Path write(String name, String content) throws Exception {
        Path path = tempDir.resolve(name);
        Files.writeString(path, content);
        return path;
    }
}
