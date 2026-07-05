package com.jkamsker.modemsim.profiles;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;

final class ProfileXmlResource {
    private ProfileXmlResource() {
    }

    static Profile load(ProfileXmlLoader loader, String resource, String profileId) {
        Path xmlPath = copy(resource);
        try {
            return loader.loadBuiltinResource(xmlPath, profileId);
        } finally {
            delete(xmlPath);
        }
    }

    static Map<String, org.w3c.dom.Element> profileElements(String resource) {
        Path xmlPath = copy(resource);
        try {
            Map<String, org.w3c.dom.Element> profiles = new LinkedHashMap<>();
            org.w3c.dom.Element root = com.jkamsker.modemsim.validation.XmlSecurity.parse(xmlPath).getDocumentElement();
            for (org.w3c.dom.Element profile : com.jkamsker.modemsim.validation.Dom.children(root, "profile")) {
                profiles.put(profile.getAttribute("id"), profile);
            }
            return Map.copyOf(profiles);
        } finally {
            delete(xmlPath);
        }
    }

    private static Path copy(String resource) {
        Path xmlPath = null;
        try (InputStream stream = ProfileXmlResource.class.getResourceAsStream(resource)) {
            if (stream == null) {
                throw new IllegalArgumentException("Missing profile resource: " + resource);
            }
            xmlPath = Files.createTempFile("modem-profile-", ".xml");
            Files.copy(stream, xmlPath, StandardCopyOption.REPLACE_EXISTING);
            return xmlPath;
        } catch (IOException e) {
            if (xmlPath != null) {
                delete(xmlPath);
            }
            throw new UncheckedIOException(e);
        }
    }

    private static void delete(Path xmlPath) {
        try {
            Files.deleteIfExists(xmlPath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
