package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.parser.AtCommandParser;
import com.jkamsker.modemsim.parser.EntryMode;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class MacroRandomSeedTest {
    @TempDir
    Path tempDir;

    @Test
    void macroRandomSeedParticipatesInDelaySamplingKey() throws Exception {
        MacroSet macros = new MacroLoader().load(write("""
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0" randomSeed="42">
                  <macro id="seeded" priority="100" phase="replace">
                    <match command="+SEEDED"/>
                    <then>
                      <delay ms="10" jitterMs="5"/>
                      <emit line="+SEEDED"/>
                    </then>
                  </macro>
                </macros>
                """));

        MacroDecision decision = new MacroEngine(macros).evaluateCommand(
                new AtCommandParser(8).parse(RawBytes.ascii("AT+SEEDED\r"), EntryMode.COMMAND).getFirst(),
                BuiltinProfiles.acceptanceSierra().initialState(),
                BuiltinProfiles.acceptanceSierra());

        assertThat(macros.randomSeed()).isEqualTo(42L);
        assertThat(decision.delay("macro-seeded").operation()).isEqualTo("macro-seeded:seed=42");
    }

    private Path write(String content) throws Exception {
        Path path = tempDir.resolve("macros.xml");
        Files.writeString(path, content);
        return path;
    }
}
