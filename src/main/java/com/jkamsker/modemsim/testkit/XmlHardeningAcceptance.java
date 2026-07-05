package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.validation.ValidationException;
import com.jkamsker.modemsim.validation.XmlSecurity;

import java.nio.file.Files;
import java.nio.file.Path;

final class XmlHardeningAcceptance {
    void run() {
        reject("doctype-entity", """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE root [<!ENTITY xxe SYSTEM "file:///etc/passwd">]>
                <root>&xxe;</root>
                """, "DOCTYPE");
        reject("entity-expansion", """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE root [<!ENTITY a "aaaaaaaaaa">]>
                <root>&a;</root>
                """, "DOCTYPE");
        reject("xinclude", """
                <?xml version="1.0" encoding="UTF-8"?>
                <root xmlns:xi="http://www.w3.org/2001/XInclude">
                  <xi:include href="file:///etc/passwd"/>
                </root>
                """, "XInclude");
        reject("external-schema", """
                <?xml version="1.0" encoding="UTF-8"?>
                <root xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xsi:noNamespaceSchemaLocation="https://example.invalid/schema.xsd"/>
                """, "external schema");
        reject("deep-tree", deepTree(), "depth");
    }

    private void reject(String name, String xml, String expected) {
        try {
            Path path = Files.createTempFile("modemsim-xml-" + name, ".xml");
            path.toFile().deleteOnExit();
            Files.writeString(path, xml);
            XmlSecurity.parse(path);
            throw new IllegalStateException(name + " XML was accepted");
        } catch (ValidationException expectedFailure) {
            if (!expectedFailure.getMessage().contains(expected)) {
                throw new IllegalStateException(name + " failed for wrong reason: "
                        + expectedFailure.getMessage(), expectedFailure);
            }
        } catch (java.io.IOException e) {
            throw new IllegalStateException("cannot create XML hardening fixture " + name, e);
        }
    }

    private String deepTree() {
        StringBuilder xml = new StringBuilder("<root>");
        for (int i = 0; i < 70; i++) {
            xml.append("<n>");
        }
        for (int i = 0; i < 70; i++) {
            xml.append("</n>");
        }
        return xml.append("</root>").toString();
    }
}
