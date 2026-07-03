package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MacroTimerTest {
    @TempDir
    Path tempDir;

    @Test
    void onTimerMacroLoadsAndEvaluatesTimerMatch() throws Exception {
        Path path = tempDir.resolve("timer.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="timer-urc" phase="on-timer">
                    <match type="timer"/>
                    <then><emit line="+TEST: TIMER"/></then>
                  </macro>
                </macros>
                """);

        assertThat(new MacroLoader().validate(path).errors())
                .contains("timer-urc: on-timer macro requires a runtime timer definition");

        MacroSet set = new MacroLoader(true).load(path);
        MacroDecision decision = new MacroEngine(set)
                .evaluateTimer("timer-urc",
                        BuiltinProfiles.acceptanceSierra().initialState(), BuiltinProfiles.acceptanceSierra());

        assertThat(decision.matched()).isTrue();
        assertThat(decision.macroId()).isEqualTo("timer-urc");
    }

    @Test
    void onTimerMacroRejectsUnknownRuntimeTimerId() throws Exception {
        Path path = tempDir.resolve("unknown-timer.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="timer-urc" phase="on-timer">
                    <match type="timer" timerId="missing"/>
                    <then><emit line="+TEST: TIMER"/></then>
                  </macro>
                </macros>
                """);

        assertThat(new MacroLoader(Set.of("heartbeat")).validate(path).errors())
                .contains("timer-urc: on-timer macro references unknown timerId missing");
    }
}
