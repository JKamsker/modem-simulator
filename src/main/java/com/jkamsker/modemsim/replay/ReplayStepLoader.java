package com.jkamsker.modemsim.replay;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jkamsker.modemsim.parser.RawBytes;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ReplayStepLoader {
    private static final ObjectMapper JSON = new ObjectMapper();

    public List<ReplayStep> load(Path path) {
        try {
            return loadLines(Files.readAllLines(path));
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read replay log: " + path, e);
        }
    }

    private List<ReplayStep> loadLines(List<String> lines) {
        List<ReplayStep> steps = new ArrayList<>();
        RawBytes pendingInput = null;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            JsonNode node = parse(line, i + 1);
            if (node.has("inputHex")) {
                steps.add(step(node));
            } else {
                pendingInput = eventStep(steps, pendingInput, node);
            }
        }
        if (pendingInput != null) {
            steps.add(new ReplayStep(pendingInput, RawBytes.empty(), false));
        }
        return steps;
    }

    private ReplayStep step(JsonNode node) {
        return new ReplayStep(
                rawHex(node.path("inputHex").asText()),
                rawHex(node.path("expectedOutputHex").asText()),
                node.path("drainScheduled").asBoolean(false));
    }

    private RawBytes eventStep(List<ReplayStep> steps, RawBytes pendingInput, JsonNode node) {
        String eventType = node.path("eventType").asText();
        String direction = node.path("direction").asText();
        if (eventType.equals("RX_BYTES") && direction.equals("DTE_TO_DCE")) {
            return rawHex(node.path("rawHex").asText());
        }
        if (eventType.equals("TX_BYTES") && direction.equals("DCE_TO_DTE") && pendingInput != null) {
            steps.add(new ReplayStep(pendingInput, rawHex(node.path("rawHex").asText()), false));
            return null;
        }
        return pendingInput;
    }

    private JsonNode parse(String line, int lineNumber) {
        try {
            return JSON.readTree(line);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid replay JSON at line " + lineNumber, e);
        }
    }

    private RawBytes rawHex(String value) {
        if (value == null || value.isBlank()) {
            return RawBytes.empty();
        }
        if (value.equals("<redacted>")) {
            throw new IllegalArgumentException("Cannot replay redacted rawHex");
        }
        return RawBytes.hex(value);
    }
}
