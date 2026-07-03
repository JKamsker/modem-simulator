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
import java.util.regex.Pattern;

public final class ReplayStepLoader {
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final Pattern SHA256 = Pattern.compile("^sha256:[0-9a-f]{64}$");

    public List<ReplayStep> load(Path path) {
        try {
            return loadLines(Files.readAllLines(path));
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read replay log: " + path, e);
        }
    }

    private List<ReplayStep> loadLines(List<String> lines) {
        if (lines.stream().filter(line -> !line.isBlank()).findFirst().orElse("").trim().startsWith("{")) {
            return loadJsonLines(lines);
        }
        return loadYamlLines(lines);
    }

    private List<ReplayStep> loadJsonLines(List<String> lines) {
        List<ReplayStep> steps = new ArrayList<>();
        boolean active = false;
        RawBytes pendingInput = RawBytes.empty();
        RawBytes pendingOutput = RawBytes.empty();
        boolean drainScheduled = false;
        List<ReplayEventExpectation> pendingEvents = new ArrayList<>();
        List<ReplayEventExpectation> preambleEvents = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            JsonNode node = parse(line, i + 1);
            if (node.has("inputHex")) {
                steps.add(step(node));
            } else {
                validateEventNode(node, i + 1);
                ReplayEventExpectation expectation = ReplayEventExpectation.from(node);
                String eventType = node.path("eventType").asText();
                String direction = node.path("direction").asText();
                if (eventType.equals("RX_BYTES") && direction.equals("DTE_TO_DCE")) {
                    if (active) {
                        steps.add(new ReplayStep(pendingInput, pendingOutput, drainScheduled, pendingEvents));
                    }
                    active = true;
                    pendingInput = rawHex(node.path("rawHex").asText());
                    pendingOutput = RawBytes.empty();
                    pendingEvents = new ArrayList<>(preambleEvents);
                    preambleEvents.clear();
                    drainScheduled = false;
                } else if (!active) {
                    preambleEvents.add(expectation);
                    continue;
                }
                pendingEvents.add(expectation);
                if (eventType.equals("TX_BYTES") && direction.equals("DCE_TO_DTE")) {
                    pendingOutput = pendingOutput.append(rawHex(node.path("rawHex").asText()));
                }
                if (eventType.equals("SCHEDULER_ENQUEUE") || eventType.equals("SCHEDULER_EMIT")) {
                    drainScheduled = true;
                }
            }
        }
        if (active) {
            steps.add(new ReplayStep(pendingInput, pendingOutput, drainScheduled, pendingEvents));
        } else if (!preambleEvents.isEmpty()) {
            steps.add(new ReplayStep(RawBytes.empty(), outputFrom(preambleEvents), false, preambleEvents));
        }
        return steps;
    }

    private RawBytes outputFrom(List<ReplayEventExpectation> events) {
        return events.stream()
                .filter(event -> event.eventType() == com.jkamsker.modemsim.monitor.EventType.TX_BYTES
                        && event.direction() == com.jkamsker.modemsim.monitor.Direction.DCE_TO_DTE)
                .map(event -> rawHex(event.rawHex()))
                .reduce(RawBytes.empty(), RawBytes::append);
    }

    private List<ReplayStep> loadYamlLines(List<String> lines) {
        List<ReplayStep> steps = new ArrayList<>();
        String input = null;
        String output = null;
        boolean drain = false;
        for (String raw : lines) {
            String line = raw.trim();
            if (line.startsWith("- inputHex:") || line.startsWith("inputHex:")) {
                if (input != null) {
                    steps.add(new ReplayStep(rawHex(input), rawHex(output), drain));
                }
                input = value(line);
                output = "";
                drain = false;
            } else if (line.startsWith("expectedOutputHex:") || line.startsWith("outputHex:")) {
                output = value(line);
            } else if (line.startsWith("drainScheduled:")) {
                drain = Boolean.parseBoolean(value(line));
            }
        }
        if (input != null) {
            steps.add(new ReplayStep(rawHex(input), rawHex(output), drain));
        }
        return steps;
    }

    private String value(String line) {
        String value = line.substring(line.indexOf(':') + 1).trim();
        return value.replace("\"", "");
    }

    private ReplayStep step(JsonNode node) {
        return new ReplayStep(
                rawHex(node.path("inputHex").asText()),
                rawHex(node.path("expectedOutputHex").asText()),
                node.path("drainScheduled").asBoolean(false),
                List.of());
    }

    private JsonNode parse(String line, int lineNumber) {
        try {
            return JSON.readTree(line);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid replay JSON at line " + lineNumber, e);
        }
    }

    private void validateEventNode(JsonNode node, int lineNumber) {
        require(node, lineNumber, "timestamp", "monotonicNanos", "sequence", "sessionId",
                "eventType", "direction", "rawHex", "profileHash", "configHash",
                "macroHash", "initialStateHash", "sessionSeed", "clockMode", "redaction");
        require(node.path("redaction"), lineNumber, "applied", "policy", "fields", "classes");
        String type = node.path("eventType").asText();
        if (type.equals("SCHEDULER_ENQUEUE") || type.equals("SCHEDULER_EMIT")) {
            JsonNode scheduler = node.path("scheduler");
            require(scheduler, lineNumber, "dueMonotonicNanos", "sourceSequence",
                    "sourcePriority", "stateVersion", "cancelOnStateChange",
                    "operation", "sampledDelayMs", "cancelled");
        }
        requireHash(node, lineNumber, "profileHash", "configHash", "macroHash", "initialStateHash");
    }

    private void require(JsonNode node, int lineNumber, String... fields) {
        for (String field : fields) {
            if (node == null || !node.has(field) || node.path(field).isNull()) {
                throw new IllegalArgumentException("Replay event at line " + lineNumber + " missing " + field);
            }
        }
    }

    private void requireHash(JsonNode node, int lineNumber, String... fields) {
        for (String field : fields) {
            String value = node.path(field).asText("");
            if (!SHA256.matcher(value).matches()) {
                throw new IllegalArgumentException("Replay event at line " + lineNumber
                        + " has invalid " + field);
            }
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
