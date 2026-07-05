package com.jkamsker.modemsim.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jkamsker.modemsim.state.SessionSettings;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

final class NvramStore {
    private static final ObjectMapper JSON = new ObjectMapper();
    private final Path path;

    private NvramStore(Path path) {
        this.path = path;
    }

    static NvramStore forSession(String sessionId) {
        return new NvramStore(Path.of("runtime", "sessions", sessionId, "nvram.json"));
    }

    Optional<SessionSettings> load() {
        if (!Files.exists(path)) {
            return Optional.empty();
        }
        try {
            var node = JSON.readTree(path.toFile());
            return Optional.of(new SessionSettings(
                    node.path("echo").asBoolean(),
                    node.path("quiet").asBoolean(),
                    node.path("verbose").asBoolean(true),
                    node.path("cmee").asInt(),
                    node.path("s0").asInt(),
                    node.path("s2").asInt(43),
                    node.path("s3").asInt(13),
                    node.path("s4").asInt(10),
                    node.path("s5").asInt(8),
                    node.path("s6").asInt(2),
                    node.path("s7").asInt(60),
                    node.path("s8").asInt(2),
                    node.path("s12").asInt(50),
                    node.path("ampD").asInt(2),
                    node.path("ampC").asInt(1)));
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read NVRAM: " + path, e);
        }
    }

    void save(SessionSettings settings) {
        try {
            Files.createDirectories(path.getParent());
            JSON.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), json(settings));
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write NVRAM: " + path, e);
        }
    }

    private ObjectNode json(SessionSettings settings) {
        ObjectNode json = JSON.createObjectNode();
        json.put("echo", settings.echo());
        json.put("quiet", settings.quiet());
        json.put("verbose", settings.verbose());
        json.put("cmee", settings.cmee());
        json.put("s0", settings.s0());
        json.put("s2", settings.s2());
        json.put("s3", settings.s3());
        json.put("s4", settings.s4());
        json.put("s5", settings.s5());
        json.put("s6", settings.s6());
        json.put("s7", settings.s7());
        json.put("s8", settings.s8());
        json.put("s12", settings.s12());
        json.put("ampD", settings.ampD());
        json.put("ampC", settings.ampC());
        return json;
    }
}
