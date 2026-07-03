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
import java.util.ArrayList;
import java.util.List;

final class RuntimeConfigLoader {
    private static final ObjectMapper YAML = new ObjectMapper(new YAMLFactory());

    RuntimeConfig load(Path path) {
        new ConfigValidator().validate(path).throwIfInvalid();
        JsonNode root = read(path);
        return new RuntimeConfig(
                root.path("sessionSeed").asLong(),
                clockMode(root.path("clockMode").asText("monotonic")),
                root.path("strictOptionalPorts").asBoolean(false),
                serialLine(root.path("serialLine")),
                ports(root.path("ports")));
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

    private List<PortBinding> ports(JsonNode nodes) {
        List<PortBinding> result = new ArrayList<>();
        for (JsonNode node : nodes) {
            result.add(new PortBinding(
                    node.path("id").asText(),
                    EndpointType.fromConfig(node.path("type").asText()),
                    PortRole.fromConfig(node.path("role").asText()),
                    textOrNull(node, "name"),
                    node.path("enabled").asBoolean(false),
                    textOrNull(node, "profile"),
                    node.path("snifferFormat").asText("tagged-text")));
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
}
