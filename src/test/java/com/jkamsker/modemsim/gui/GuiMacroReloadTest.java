package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

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

        controller.macroTimerIds(Set.of("heartbeat"));
        MacroSummary summary = controller.reloadMacros(timer);

        assertThat(summary.errors()).isBlank();
        assertThat(summary.hash()).startsWith("sha256:");
    }
}
