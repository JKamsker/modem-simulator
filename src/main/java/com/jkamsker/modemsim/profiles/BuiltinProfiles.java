package com.jkamsker.modemsim.profiles;

import java.util.List;

public final class BuiltinProfiles {
    private static final String RESOURCE = "/profiles/builtin-v1.xml";
    private static final List<String> IDS = List.of(
            "generic-hayes-v250",
            "3gpp-27007-r18",
            "3gpp-27005-r16",
            "sierra-common",
            "sierra-hl6-hl8-v20",
            "westermo-common",
            "westermo-td22-6177-2203",
            "westermo-td36-6618-2202",
            "westermo-gd01-6196-2220",
            "westermo-gdw11-6615-2220");

    private BuiltinProfiles() {
    }

    public static Profile byId(String id) {
        if (!IDS.contains(id)) {
            throw new IllegalArgumentException("Unknown built-in profile: " + id);
        }
        return new ProfileXmlLoader().loadResource(RESOURCE, id);
    }

    public static List<String> ids() {
        return IDS;
    }

    public static Profile acceptanceSierra() {
        return byId("sierra-hl6-hl8-v20");
    }

    public static Profile westermoTd22() {
        return byId("westermo-td22-6177-2203");
    }
}
