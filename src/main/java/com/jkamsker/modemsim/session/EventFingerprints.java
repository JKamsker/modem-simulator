package com.jkamsker.modemsim.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.state.ModemState;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.Map;

final class EventFingerprints {
    private static final ObjectMapper CANONICAL_JSON = new ObjectMapper()
            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

    private EventFingerprints() {
    }

    static String profileHash(Profile profile) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", profile.id());
        data.put("parents", profile.parents());
        data.put("vendor", profile.vendor());
        data.put("status", profile.status());
        data.put("profileKind", profile.profileKind());
        data.put("dialect", profile.dialect());
        data.put("identity", profile.identity());
        data.put("commands", profile.commands());
        data.put("registers", profile.registers());
        data.put("coverage", profile.coverage());
        data.put("deviations", profile.deviations());
        return hash(data);
    }

    static String configHash(
            String profileId, String clockMode, String port, String portRole, Map<String, Object> extra) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("type", "headless-session");
        data.put("profile", value(profileId));
        data.put("clockMode", value(clockMode));
        data.put("port", value(port));
        data.put("portRole", value(portRole));
        if (extra != null) {
            data.putAll(extra);
        }
        return hash(data);
    }

    static String initialStateHash(ModemState state) {
        return hash(state);
    }

    private static String hash(Object value) {
        try {
            byte[] json = CANONICAL_JSON.writeValueAsBytes(value);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return "sha256:" + HexFormat.of().formatHex(digest.digest(json));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot serialize fingerprint source", e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }

    private static String value(String value) {
        return value == null ? "" : value;
    }
}
