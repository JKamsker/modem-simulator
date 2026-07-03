package com.jkamsker.modemsim.validation;

import com.jkamsker.modemsim.macros.MacroLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FaultInvariantValidationTest {
    @TempDir
    Path tempDir;

    @Test
    void scenarioRejectsFaultRestoreThatRegistersLockedSim() throws Exception {
        Path scenario = tempDir.resolve("locked-restore.xml");
        Files.writeString(scenario, """
                <?xml version="1.0" encoding="UTF-8"?>
                <scenario id="locked-restore" version="1.0" clock="virtual">
                  <initial-state><sim state="SIM_PIN_REQUIRED"/><network cregN="2" stat="0"/></initial-state>
                  <step atMs="0"><fault type="network-restore" stat="1" rssi="18" ber="0"/></step>
                </scenario>
                """);

        assertThat(new ScenarioValidator().validate(scenario).errors())
                .anySatisfy(error -> assertThat(error).contains("locked SIM"));
    }

    @Test
    void macroRejectsFaultRestoreThatRegistersLockedSim() throws Exception {
        Path macros = tempDir.resolve("locked-restore-macro.xml");
        Files.writeString(macros, """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="bad-restore" phase="on-state-change">
                    <match type="state-change"/>
                    <when><state path="state.sim.state" equals="SIM_PIN_REQUIRED"/></when>
                    <then><fault type="network-restore" stat="1" rssi="18" ber="0"/></then>
                  </macro>
                </macros>
                """);

        assertThat(new MacroLoader().validate(macros).errors())
                .anySatisfy(error -> assertThat(error).contains("locked SIM"));
    }
}
