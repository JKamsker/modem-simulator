package com.jkamsker.modemsim.profiles;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

final class BuiltinProfileMetadata {
    private static final ObjectMapper JSON = new ObjectMapper();

    private BuiltinProfileMetadata() {
    }

    static List<ProfileCommand> commands(String id) {
        return commands(coverageJson(id));
    }

    private static List<ProfileCommand> commands(JsonNode root) {
        List<ProfileCommand> result = new ArrayList<>();
        for (JsonNode command : root.path("commands")) {
            result.add(new ProfileCommand(
                    command.path("command").asText(),
                    command.path("status").asText(),
                    textOrNull(command, "handler"),
                    textOrNull(command, "reason")));
        }
        return result;
    }

    static ProfileCoverage coverage(String id) {
        JsonNode root = coverageJson(id);
        return new ProfileCoverage(root.path("source").asText(),
                root.path("commands_total").asInt(), root.path("unknown").asInt(), commands(root));
    }

    static List<ProfileRegister> registers() {
        return List.of(
                register("S0", 0, 0, 255),
                register("S2", 43, 0, 127),
                register("S3", 13, 0, 127),
                register("S4", 10, 0, 127),
                register("S5", 8, 0, 127),
                register("S6", 2, 0, 255),
                register("S7", 60, 1, 255),
                register("S8", 2, 0, 255),
                register("S12", 50, 0, 255));
    }

    private static ProfileRegister register(String name, int defaultValue, int min, int max) {
        return new ProfileRegister(name, defaultValue, min, max, true, true);
    }

    private static JsonNode coverageJson(String id) {
        String resource = "/coverage/v1-targets/" + id + ".json";
        try (InputStream stream = BuiltinProfileMetadata.class.getResourceAsStream(resource)) {
            if (stream == null) {
                throw new IllegalArgumentException("Missing built-in coverage resource: " + resource);
            }
            return JSON.readTree(stream);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read built-in coverage resource: " + resource, e);
        }
    }

    private static String textOrNull(JsonNode node, String field) {
        return node.hasNonNull(field) ? node.path(field).asText() : null;
    }
}
