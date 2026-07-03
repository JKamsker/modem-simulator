package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.app.RuntimeDiagnosticsAcceptance;
import com.jkamsker.modemsim.gui.GuiAcceptanceHarness;
import com.jkamsker.modemsim.monitor.DropAwareEventSink;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.profiles.ProfileRegister;
import com.jkamsker.modemsim.profiles.ProfileRegisterCatalog;
import com.jkamsker.modemsim.replay.ReplayStepLoader;
import com.jkamsker.modemsim.replay.ReplayValidator;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.CallRuntime;
import com.jkamsker.modemsim.state.ModemLines;
import com.jkamsker.modemsim.validation.SchemaLocator;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class AcceptanceExtendedChecks {
    void parserAndAta() {
        requireParseError("AT+CPIN=\"1234\r");
        requireParseError("ATS=\r");
        requireParseError("AT+=1\r");
        require(session().receive(RawBytes.ascii("ATA\r")).outputAscii().contains("NO CARRIER"));
        HeadlessSession ringing = new HeadlessSession("ata", ringingProfile(), 12345);
        require(ringing.receive(RawBytes.ascii("ATA\r")).outputAscii().contains("CONNECT"));
        require(ringing.snapshot().call().mode() == CallMode.ONLINE_DATA);
        require(ringing.snapshot().lines().dcd());
    }

    void macroHotReloadTimers() {
        new GuiAcceptanceHarness().macroHotReloadTimers();
    }

    void auditBackpressure() {
        DropOnceSink sink = new DropOnceSink();
        HeadlessSession s = new HeadlessSession("acceptance-drop", BuiltinProfiles.acceptanceSierra(), 12345, sink);
        sink.dropNextDroppable();
        s.receive(RawBytes.ascii("AT\r"));
        require(sink.events().stream().anyMatch(event ->
                event.eventType() == EventType.DROPPED_EVENTS && event.droppedEventCount() > 0));
        require(sink.events().stream().anyMatch(event ->
                event.eventType() != EventType.DROPPED_EVENTS && event.droppedEventCount() > 0));
    }

    void diagnostics() {
        new RuntimeDiagnosticsAcceptance().run();
    }

    void sourceSizeGate() {
        try {
            Path dir = Files.createTempDirectory("modemsim-size-gate");
            Files.writeString(dir.resolve("TooLarge.java"), oversizedJava(), StandardCharsets.UTF_8);
            Process process = new ProcessBuilder(
                    "bash", SchemaLocator.projectPath("scripts/check-code-size.sh").toString(), dir.toString())
                    .redirectErrorStream(true)
                    .start();
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            require(process.waitFor() != 0 && output.contains("TooLarge.java"));
        } catch (java.io.IOException e) {
            throw new IllegalStateException("source-size acceptance check failed", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("source-size acceptance interrupted", e);
        }
    }

    void goldenYamlLoader() {
        var transcript = new ReplayStepLoader().loadTranscript(SchemaLocator.projectPath("tests/golden/basic-at.yaml"));
        require(transcript.metadata().containsKey("purpose"));
        require(transcript.steps().stream().anyMatch(step -> !step.expectedEvents().isEmpty()));
        require(new ReplayValidator().validateRecompute(session(), transcript.steps()).valid());
    }

    void sRegisterBounds() {
        HeadlessSession strict = new HeadlessSession("strict", withS7(60, true), 12345);
        HeadlessSession relaxed = new HeadlessSession("relaxed", withS7(120, true), 12345);
        HeadlessSession readOnly = new HeadlessSession("readonly", withS7(120, false), 12345);
        require(strict.receive(RawBytes.ascii("ATS7=90\r")).outputAscii().contains("ERROR"));
        require(relaxed.receive(RawBytes.ascii("ATS7=90\r")).outputAscii().contains("OK"));
        require(readOnly.receive(RawBytes.ascii("ATS7=61\r")).outputAscii().contains("ERROR"));
    }

    private void requireParseError(String input) {
        require(session().receive(RawBytes.ascii(input)).events().stream()
                .anyMatch(event -> event.eventType() == EventType.PARSE_ERROR));
    }

    private Profile ringingProfile() {
        var base = BuiltinProfiles.acceptanceSierra().initialState();
        return BuiltinProfiles.acceptanceSierra().withInitialState(base
                .withCall(new CallRuntime(CallMode.RINGING, false, null, "+491701234567"))
                .withLines(new ModemLines(true, true, false, true, true, true)));
    }

    private Profile withS7(int max, boolean writable) {
        Profile base = BuiltinProfiles.acceptanceSierra();
        List<ProfileRegister> registers = base.registers().stream()
                .map(register -> register.name().equals("S7")
                        ? new ProfileRegister("S7", 60, register.min(), max, writable, register.persistent())
                        : register)
                .toList();
        Profile updated = base.withRegisters(registers);
        return updated.withInitialState(updated.initialState().withSettings(
                ProfileRegisterCatalog.applyDefaults(updated.initialState().settings(), registers)));
    }

    private String oversizedJava() {
        return IntStream.range(0, 300)
                .mapToObj(index -> "// line " + index)
                .collect(Collectors.joining(System.lineSeparator(), "", System.lineSeparator()));
    }

    private HeadlessSession session() {
        return new HeadlessSession("extended", BuiltinProfiles.acceptanceSierra(), 12345);
    }

    private void require(boolean condition) {
        if (!condition) {
            throw new IllegalStateException("acceptance check failed");
        }
    }

    private static final class DropOnceSink implements DropAwareEventSink {
        private final java.util.List<ModemEvent> events = new java.util.ArrayList<>();
        private boolean dropNext;

        @Override public void publish(ModemEvent event) { events.add(event); }

        @Override
        public boolean publishDroppable(ModemEvent event) {
            if (dropNext) {
                dropNext = false;
                return false;
            }
            events.add(event);
            return true;
        }

        void dropNextDroppable() { dropNext = true; }

        java.util.List<ModemEvent> events() { return java.util.List.copyOf(events); }
    }
}
