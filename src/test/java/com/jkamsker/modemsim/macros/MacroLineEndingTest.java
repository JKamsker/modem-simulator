package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class MacroLineEndingTest {
    @TempDir
    Path tempDir;

    @Test
    void emitLineDefaultsToCrLineEnding() throws Exception {
        HeadlessSession session = session("""
                <macros version="1.0">
                  <macro id="ping" phase="replace">
                    <match command="AT"/>
                    <then><emit line="+PING"/></then>
                  </macro>
                </macros>
                """);

        assertThat(session.receive(RawBytes.ascii("AT\r")).outputAscii()).isEqualTo("+PING\r");
    }

    @Test
    void emitLineCanUseGlobalOrActionLineEnding() throws Exception {
        HeadlessSession global = session("""
                <macros version="1.0" lineEnding="CRLF">
                  <macro id="ping" phase="replace">
                    <match command="AT"/>
                    <then><emit line="+PING"/></then>
                  </macro>
                </macros>
                """);
        HeadlessSession local = session("""
                <macros version="1.0" lineEnding="CRLF">
                  <macro id="ping" phase="replace">
                    <match command="AT"/>
                    <then><emit line="+PING" lineEnding="LF"/></then>
                  </macro>
                </macros>
                """);

        assertThat(global.receive(RawBytes.ascii("AT\r")).outputAscii()).isEqualTo("+PING\r\n");
        assertThat(local.receive(RawBytes.ascii("AT\r")).outputAscii()).isEqualTo("+PING\n");
    }

    @Test
    void lineEndingValuesAreCaseInsensitive() throws Exception {
        HeadlessSession session = session("""
                <macros version="1.0" lineEnding="crlf">
                  <macro id="ping" phase="replace">
                    <match command="AT"/>
                    <then><emit line="+PING" lineEnding="lf"/></then>
                  </macro>
                </macros>
                """);

        assertThat(session.receive(RawBytes.ascii("AT\r")).outputAscii()).isEqualTo("+PING\n");
    }

    @Test
    void macroHashIsStableAcrossTextFileLineEndings() throws Exception {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <custom-response id="custom" priority="10">
                    <if command="+CMSG123"/>
                    <send line="ERR"/>
                  </custom-response>
                </macros>
                """;
        Path lf = tempDir.resolve("lf.xml");
        Path crlf = tempDir.resolve("crlf.xml");
        Files.writeString(lf, xml);
        Files.writeString(crlf, xml.replace("\n", "\r\n"));

        MacroLoader loader = new MacroLoader();

        assertThat(loader.load(crlf).hash()).isEqualTo(loader.load(lf).hash());
    }

    private HeadlessSession session(String content) throws Exception {
        MacroSet macros = new MacroLoader().load(write(content));
        return new HeadlessSession("line-ending", BuiltinProfiles.acceptanceSierra(), 12345,
                new com.jkamsker.modemsim.monitor.InMemoryEventSink(), new MacroEngine(macros));
    }

    private Path write(String content) throws Exception {
        Path path = tempDir.resolve("macros.xml");
        Files.writeString(path, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + content);
        return path;
    }
}
