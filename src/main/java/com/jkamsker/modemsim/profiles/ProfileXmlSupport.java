package com.jkamsker.modemsim.profiles;

import java.util.Arrays;
import java.util.List;

final class ProfileXmlSupport {
    private ProfileXmlSupport() {
    }

    static List<String> parents(String value) {
        return value == null || value.isBlank() ? List.of() : Arrays.asList(value.trim().split("\\s+"));
    }
}
