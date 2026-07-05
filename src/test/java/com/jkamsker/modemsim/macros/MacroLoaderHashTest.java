package com.jkamsker.modemsim.macros;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class MacroLoaderHashTest {
    @TempDir
    Path tempDir;

    @Test
    void macroHashIsIndependentOfXmlLineEndings() throws Exception {
        String lfXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <custom-response id="custom-cmsg" priority="90">
                    <if command="+CMSG"/>
                    <send line="+CMSG: 42"/>
                  </custom-response>
                </macros>
                """;

        MacroLoader loader = new MacroLoader();
        MacroSet lfMacros = loader.load(write("lf.xml", lfXml));
        MacroSet crlfMacros = loader.load(write("crlf.xml", lfXml.replace("\n", "\r\n")));

        assertThat(crlfMacros.hash()).isEqualTo(lfMacros.hash());
    }

    private Path write(String fileName, String content) throws Exception {
        Path path = tempDir.resolve(fileName);
        Files.writeString(path, content);
        return path;
    }
}
