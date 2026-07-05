package com.jkamsker.modemsim.testkit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.macros.MacroLoader;
import com.jkamsker.modemsim.monitor.InMemoryEventSink;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.replay.ReplayStepLoader;
import com.jkamsker.modemsim.replay.ReplayTranscript;
import com.jkamsker.modemsim.replay.ReplayValidator;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.validation.SchemaLocator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

final class GoldenTranscriptAcceptance {
    private static final ObjectMapper YAML = new ObjectMapper(new YAMLFactory());
    private static final Set<String> REQUIRED_CASES = Set.of(
            "A01", "A02", "A03", "A04", "A07", "A08", "A09", "A11", "A12",
            "A19", "A20", "A21", "A24", "A26", "A31", "A32");

    void run() {
        List<Path> fixtures = fixtures();
        require(!fixtures.isEmpty(), "no golden transcript fixtures found");
        Set<String> coveredCases = new LinkedHashSet<>();
        for (Path fixture : fixtures) {
            JsonNode root = readYaml(fixture);
            ReplayTranscript transcript = new ReplayStepLoader().loadTranscript(fixture);
            requireGlobalFields(fixture, root);
            requireSteps(fixture, root);
            requireEvents(fixture, root);
            coveredCases.addAll(coveredCases(root));
            replay(fixture, root, transcript);
        }
        require(coveredCases.containsAll(REQUIRED_CASES),
                "golden transcript fixtures missing acceptance cases " + missingCases(coveredCases));
    }

    private List<Path> fixtures() {
        Path root = SchemaLocator.projectPath("tests/golden");
        try (var paths = Files.walk(root)) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".yaml"))
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("cannot list golden transcript fixtures", e);
        }
    }

    private void requireGlobalFields(Path path, JsonNode root) {
        require(nonBlank(text(root, "name")), path + " missing name");
        require(nonBlank(text(root, "profile")), path + " missing profile");
        require("CR".equals(text(root, "commandTerminator")), path + " missing commandTerminator CR");
        require("CRLF".equals(text(root, "responseTerminator")), path + " missing responseTerminator CRLF");
        require(!root.path("defaultEcho").asBoolean(true), path + " must default echo off");
        require(root.path("defaultVerbose").asBoolean(false), path + " must default verbose on");
        require(!root.path("defaultQuiet").asBoolean(true), path + " must default quiet off");
        require(root.path("sessionSeed").asLong() == 12345L, path + " must declare sessionSeed 12345");
        require("virtual".equals(text(root, "clockMode")), path + " must declare virtual clock");
        JsonNode serial = root.path("serialLine");
        require(serial.path("baudRate").asInt() == 115200, path + " serial baud mismatch");
        require(serial.path("dataBits").asInt() == 8, path + " serial data bits mismatch");
        require(serial.path("stopBits").asInt() == 1, path + " serial stop bits mismatch");
        require("NONE".equals(text(serial, "parity")), path + " serial parity mismatch");
        require("NONE".equals(text(serial, "flowControl")), path + " serial flow control mismatch");
        require(root.path("metadata").isObject() && root.path("metadata").size() > 0,
                path + " missing metadata");
    }

    private void requireSteps(Path path, JsonNode root) {
        JsonNode steps = root.path("steps");
        require(steps.isArray() && !steps.isEmpty(), path + " missing steps");
        for (JsonNode step : steps) {
            require(nonBlank(text(step, "inputHex")), path + " step missing inputHex");
            require(step.has("outputHex") || step.has("expectedOutputHex"), path + " step missing output hex");
        }
    }

    private void requireEvents(Path path, JsonNode root) {
        boolean rootEvents = root.path("expectEvents").isArray() && !root.path("expectEvents").isEmpty();
        boolean stepEvents = false;
        for (JsonNode step : root.path("steps")) {
            stepEvents |= step.path("expectEvents").isArray() && !step.path("expectEvents").isEmpty();
        }
        require(rootEvents || stepEvents, path + " missing event oracle");
    }

    private void replay(Path path, JsonNode root, ReplayTranscript transcript) {
        require(text(root, "profile").equals(transcript.profile()), path + " profile drift");
        var report = new ReplayValidator().validateRecompute(
                new HeadlessSession("golden", profile(transcript.profile()), 12345,
                        new InMemoryEventSink(), macroEngine(root), text(root, "clockMode"), null, null),
                transcript.steps(), true);
        require(report.valid(), path + " diverged: " + report.divergences());
    }

    private Set<String> coveredCases(JsonNode root) {
        Set<String> cases = new LinkedHashSet<>();
        JsonNode metadata = root.path("metadata");
        if (metadata.path("acceptanceCase").isTextual()) {
            cases.add(metadata.path("acceptanceCase").asText());
        }
        if (metadata.path("acceptanceCases").isArray()) {
            metadata.path("acceptanceCases").forEach(node -> cases.add(node.asText()));
        }
        return cases;
    }

    private Set<String> missingCases(Set<String> coveredCases) {
        Set<String> missing = new LinkedHashSet<>(REQUIRED_CASES);
        missing.removeAll(coveredCases);
        return missing;
    }

    private MacroEngine macroEngine(JsonNode root) {
        String path = text(root, "macros");
        if (path == null || path.isBlank()) {
            return MacroEngine.empty();
        }
        return new MacroEngine(new MacroLoader(Set.of(), true).load(SchemaLocator.projectPath(path)));
    }

    private Profile profile(String id) {
        if ("acceptance-sierra-hl6-hl8-ready".equals(id)) {
            return BuiltinProfiles.acceptanceSierra();
        }
        return BuiltinProfiles.byId(id);
    }

    private JsonNode readYaml(Path path) {
        try {
            return YAML.readTree(path.toFile());
        } catch (IOException e) {
            throw new IllegalStateException("cannot read golden transcript fixture " + path, e);
        }
    }

    private String text(JsonNode node, String field) {
        return node.has(field) && !node.path(field).isNull() ? node.path(field).asText() : null;
    }

    private boolean nonBlank(String value) {
        return value != null && !value.isBlank();
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
