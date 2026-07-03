package com.jkamsker.modemsim.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class XmlSecurityTest {
    @TempDir
    Path tempDir;

    @Test
    void rejectsDoctypeAndExternalEntityDeclarations() throws Exception {
        Path xml = write("doctype.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE root [<!ENTITY xxe SYSTEM "file:///etc/passwd">]>
                <root>&xxe;</root>
                """);

        assertThatThrownBy(() -> XmlSecurity.parse(xml))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("DOCTYPE");
    }

    @Test
    void rejectsXIncludeElements() throws Exception {
        Path xml = write("xinclude.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <root xmlns:xi="http://www.w3.org/2001/XInclude">
                  <xi:include href="file:///etc/passwd"/>
                </root>
                """);

        assertThatThrownBy(() -> XmlSecurity.parse(xml))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("XInclude");
    }

    @Test
    void rejectsExternalSchemaLocations() throws Exception {
        Path xml = write("schema-location.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <root xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xsi:noNamespaceSchemaLocation="https://example.invalid/schema.xsd"/>
                """);

        assertThatThrownBy(() -> XmlSecurity.parse(xml))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("external schema");
    }

    @Test
    void rejectsExcessiveDepth() throws Exception {
        StringBuilder xml = new StringBuilder("<root>");
        for (int i = 0; i < 70; i++) {
            xml.append("<n>");
        }
        for (int i = 0; i < 70; i++) {
            xml.append("</n>");
        }
        xml.append("</root>");
        Path path = write("deep.xml", xml.toString());

        assertThatThrownBy(() -> XmlSecurity.parse(path))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("depth");
    }

    @Test
    void rejectsExcessiveText() throws Exception {
        Path xml = write("text.xml", "<root>" + "a".repeat(50_001) + "</root>");

        assertThatThrownBy(() -> XmlSecurity.parse(xml))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("text");
    }

    @Test
    void rejectsOversizedFiles() throws Exception {
        Path xml = write("oversized.xml", "<root>" + "a".repeat(1_000_001) + "</root>");

        assertThatThrownBy(() -> XmlSecurity.parse(xml))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("maximum size");
    }

    private Path write(String name, String content) throws Exception {
        Path path = tempDir.resolve(name);
        Files.writeString(path, content);
        return path;
    }
}
