package com.jkamsker.modemsim.profiles;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class BuiltinProfiles {
    static final String RESOURCE = "/profiles/builtin-v1.xml";
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
    private static final Map<String, org.w3c.dom.Element> ELEMENTS = ProfileXmlResource.profileElements(RESOURCE);
    private static final ConcurrentMap<String, Profile> PROFILES = new ConcurrentHashMap<>();

    private BuiltinProfiles() {
    }

    public static Profile byId(String id) {
        if (!IDS.contains(id)) {
            throw new IllegalArgumentException("Unknown built-in profile: " + id);
        }
        return PROFILES.computeIfAbsent(id, key -> new ProfileXmlLoader().loadResource(RESOURCE, key));
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

    static java.util.Map<String, org.w3c.dom.Element> profileElements() {
        return ELEMENTS;
    }
}
