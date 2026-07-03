package com.jkamsker.modemsim.profiles;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

final class ProfileXmlResource {
    private ProfileXmlResource() {
    }

    static Profile load(ProfileXmlLoader loader, String resource, String profileId) {
        Path xmlPath = copy(resource);
        try {
            return loader.load(xmlPath, profileId);
        } finally {
            delete(xmlPath);
        }
    }

    private static Path copy(String resource) {
        try (InputStream stream = ProfileXmlResource.class.getResourceAsStream(resource)) {
            if (stream == null) {
                throw new IllegalArgumentException("Missing profile resource: " + resource);
            }
            Path xmlPath = Files.createTempFile("modem-profile-", ".xml");
            Files.copy(stream, xmlPath, StandardCopyOption.REPLACE_EXISTING);
            return xmlPath;
        } catch (IOException e) {
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
