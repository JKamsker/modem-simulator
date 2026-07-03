package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.app.RuntimeTimer;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.monitor.EventType;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GuiMacroReloadTest {
    @Test
    void reloadUsesConfiguredRuntimeTimerIds(@org.junit.jupiter.api.io.TempDir Path tempDir) throws Exception {
        Path timer = tempDir.resolve("timer.xml");
        Files.writeString(timer, """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="timer-urc" phase="on-timer">
                    <match type="timer" timerId="heartbeat"/>
                    <then><emit line="+TIMER"/></then>
                  </macro>
                </macros>
                """);
        GuiSessionController controller = new GuiSessionController(
                new HeadlessSession("gui-test", BuiltinProfiles.acceptanceSierra(), 12345));

        assertThat(controller.reloadMacros(timer).errors()).contains("runtime timer definition");

        controller.start(GuiSessionOptions.headless()
                .withMacroTimers(java.util.List.of(new RuntimeTimer("heartbeat", 1000))));
        MacroSummary summary = controller.reloadMacros(timer);

        assertThat(summary.errors()).isBlank();
        assertThat(summary.hash()).startsWith("sha256:");
    }

    @Test
    void reloadCancelsOnlyOldTimerMacrosAndKeepsOtherScheduledResponses(
            @org.junit.jupiter.api.io.TempDir Path tempDir) throws Exception {
        Path timer = delayedTimer(tempDir, "timer.xml", "heartbeat", "+TIMER");
        var macros = new com.jkamsker.modemsim.macros.MacroLoader(java.util.Set.of("heartbeat"), true).load(timer);
        HeadlessSession session = new HeadlessSession("reload", BuiltinProfiles.acceptanceSierra(), 12345,
                new com.jkamsker.modemsim.monitor.InMemoryEventSink(),
                new com.jkamsker.modemsim.macros.MacroEngine(macros));

        session.receive(com.jkamsker.modemsim.parser.RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        session.receive(com.jkamsker.modemsim.parser.RawBytes.ascii("body\u001A"));
        session.fireTimer("heartbeat");
        var reload = session.replaceMacroEngine(com.jkamsker.modemsim.macros.MacroEngine.empty(), "reload");

        assertThat(reload.events()).anySatisfy(event -> {
            assertThat(event.eventType()).isEqualTo(EventType.SCHEDULER_EMIT);
            assertThat(event.scheduler()).containsEntry("operation", "macro-timer-urc")
                    .containsEntry("cancelled", true);
        });
        assertThat(session.drainScheduled().outputAscii()).contains("+CMGS").doesNotContain("+TIMER");
    }

    @Test
    void failedReloadPreservesTimerQueueAndEnabledState(@org.junit.jupiter.api.io.TempDir Path tempDir) throws Exception {
        GuiSessionController controller = new GuiSessionController(
                new HeadlessSession("gui-test", BuiltinProfiles.acceptanceSierra(), 12345));
        controller.start(GuiSessionOptions.headless().withMacroTimers(List.of(new RuntimeTimer("heartbeat", 1000))));
        controller.reloadMacros(delayedTimer(tempDir, "valid.xml", "heartbeat", "+OLD"));
        controller.fireTimer("heartbeat");
        String enabledBefore = controller.enabledMacros();

        assertThat(controller.reloadMacros(delayedTimer(tempDir, "invalid.xml", "missing", "+NEW")).errors())
                .isNotBlank();

        assertThat(controller.enabledMacros()).isEqualTo(enabledBefore);
        assertThat(controller.advanceTime(1000).outputAscii()).contains("+OLD");
    }

    private Path delayedTimer(Path tempDir, String fileName, String timerId, String line) throws Exception {
        Path timer = tempDir.resolve(fileName);
        Files.writeString(timer, """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="timer-urc" phase="on-timer">
                    <match type="timer" timerId="%s"/>
                    <then><delay ms="1000"/><emit line="%s"/></then>
                  </macro>
                </macros>
                """.formatted(timerId, line));
        return timer;
    }
}
