package com.jkamsker.modemsim.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;
import com.jkamsker.modemsim.validation.ConfigValidator;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

final class RuntimeConfigLoader {
    private static final ObjectMapper YAML = new ObjectMapper(new YAMLFactory());
    private static final SecureRandom SEEDS = new SecureRandom();

    RuntimeConfig load(Path path) {
        new ConfigValidator().validate(path).throwIfInvalid();
        JsonNode root = read(path);
        long sessionSeed = root.hasNonNull("sessionSeed") ? root.path("sessionSeed").asLong() : SEEDS.nextLong();
        return new RuntimeConfig(
                sessionSeed,
                clockMode(root.path("clockMode").asText("monotonic")),
                root.path("strictOptionalPorts").asBoolean(false),
                root.path("gui").path("allowUnsafeDceTransmit").asBoolean(false),
                null,
                scenarioPath(path, textOrNull(root, "initialScenario")),
                scenarioPath(path, textOrNull(root, "macros")),
                macroTimers(root.path("macroTimers")),
                serialLine(root.path("serialLine")),
                ports(path, root.path("ports")));
    }

    private JsonNode read(Path path) {
        try {
            return YAML.readTree(path.toFile());
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read config: " + path, e);
        }
    }

    private SerialConfig serialLine(JsonNode node) {
        return new SerialConfig(
                node.path("baudRate").asInt(),
                node.path("dataBits").asInt(),
                node.path("stopBits").asInt(),
                Parity.valueOf(node.path("parity").asText()),
                FlowControl.valueOf(node.path("flowControl").asText()));
    }

    private List<PortBinding> ports(Path configPath, JsonNode nodes) {
        List<PortBinding> result = new ArrayList<>();
        for (JsonNode node : nodes) {
            result.add(new PortBinding(
                    node.path("id").asText(),
                    EndpointType.fromConfig(node.path("type").asText()),
                    PortRole.fromConfig(node.path("role").asText()),
                    textOrNull(node, "name"),
                    node.path("enabled").asBoolean(false),
                    textOrNull(node, "profile"),
                    scenarioPath(configPath, textOrNull(node, "initialScenario")),
                    node.path("snifferFormat").asText("tagged-text")));
        }
        return result;
    }

    private List<RuntimeTimer> macroTimers(JsonNode nodes) {
        List<RuntimeTimer> result = new ArrayList<>();
        for (JsonNode node : nodes) {
            result.add(new RuntimeTimer(node.path("id").asText(), node.path("atMs").asLong()));
        }
        return result;
    }

    private ClockMode clockMode(String value) {
        return switch (value) {
            case "virtual" -> ClockMode.VIRTUAL;
            case "monotonic" -> ClockMode.MONOTONIC;
            default -> throw new IllegalArgumentException("Unsupported clockMode: " + value);
        };
    }

    private String textOrNull(JsonNode node, String field) {
        return node.hasNonNull(field) ? node.path(field).asText() : null;
    }

    private Path scenarioPath(Path configPath, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        Path path = Path.of(value);
        if (path.isAbsolute() || configPath.getParent() == null) {
            return path.normalize();
        }
        return configPath.getParent().resolve(path).normalize();
    }
}
