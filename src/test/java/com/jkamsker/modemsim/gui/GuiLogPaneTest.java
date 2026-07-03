package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.macros.MacroLoader;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class GuiLogPaneTest {
    @Test
    void filterMatchesMacroIdColumn() {
        var macros = new MacroLoader().load(
                Path.of("docs/Tasks/Initial-Spec/examples/macros.sms-error-123.xml"));
        HeadlessSession session = new HeadlessSession("gui-log", BuiltinProfiles.acceptanceSierra(), 12345,
                new com.jkamsker.modemsim.monitor.InMemoryEventSink(), new MacroEngine(macros));
        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        var event = session.receive(RawBytes.ascii("smscommand dst\u001A")).events().stream()
                .filter(item -> item.eventType() == EventType.HANDLER_RESULT)
                .findFirst()
                .orElseThrow();

        assertThat(event.macroId()).isEqualTo("smscommand-dst-error-123");
        assertThat(GuiLogPane.matches(event, "smscommand-dst-error-123")).isTrue();
    }
}
