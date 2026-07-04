package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.app.RuntimeDiagnosticsAcceptance;
import com.jkamsker.modemsim.gui.GuiAcceptanceHarness;
import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.macros.MacroLoader;
import com.jkamsker.modemsim.monitor.AuditLogException;
import com.jkamsker.modemsim.monitor.DropAwareEventSink;
import com.jkamsker.modemsim.monitor.EventSink;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.profiles.ProfileRegister;
import com.jkamsker.modemsim.profiles.ProfileRegisterCatalog;
import com.jkamsker.modemsim.replay.ReplayPlayback;
import com.jkamsker.modemsim.replay.ReplayStep;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.CallRuntime;
import com.jkamsker.modemsim.state.ModemLines;
import com.jkamsker.modemsim.validation.SchemaLocator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

final class AcceptanceExtendedChecks {
    void parserAndAta() {
        requireParseError("AT+CPIN=\"1234\r");
        requireParseError("ATS=\r");
        requireParseError("AT+=1\r");
        requireParseError("AT+CPIN??\r");
        requireParseError("AT+CMEE?=2\r");
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
        int positiveCarry = firstPositiveCarryIndex(sink.events());
        s.receive(RawBytes.ascii("AT\r"));
        require(sink.events().stream().skip(positiveCarry + 1L)
                .anyMatch(event -> event.eventType() != EventType.DROPPED_EVENTS
                        && event.droppedEventCount() == 0));
        auditHardStopsMutations();
        new RuntimeDiagnosticsAcceptance().auditHardStop();
    }

    private int firstPositiveCarryIndex(List<ModemEvent> events) {
        for (int i = 0; i < events.size(); i++) {
            ModemEvent event = events.get(i);
            if (event.eventType() != EventType.DROPPED_EVENTS && event.droppedEventCount() > 0) {
                return i;
            }
        }
        return -1;
    }

    void diagnostics() {
        new RuntimeDiagnosticsAcceptance().run();
    }

    void sourceSizeGate() {
        new SourceSizeGateAcceptance().run();
    }

    void goldenYamlLoader() {
        new GoldenTranscriptAcceptance().run();
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

    private HeadlessSession session() {
        return new HeadlessSession("extended", BuiltinProfiles.acceptanceSierra(), 12345);
    }

    private void auditHardStopsMutations() {
        FailingSink injectionSink = new FailingSink(Set.of(EventType.INJECTION));
        HeadlessSession injection = new HeadlessSession("audit-injection", BuiltinProfiles.acceptanceSierra(), 12345, injectionSink);
        expectAuditFailure(() -> injection.injectDte(RawBytes.ascii("AT\r"), "raw-dte-to-dce"));
        require(injectionSink.events().stream().noneMatch(event -> event.eventType() == EventType.RX_BYTES));
        expectAuditFailure(() -> injection.injectDce(RawBytes.ascii("+CREG: 4\r\n"), "raw-dce-to-dte"));
        require(injectionSink.events().stream().noneMatch(event -> event.eventType() == EventType.TX_BYTES));

        FailingSink stateSink = new FailingSink(Set.of(EventType.STATE_CHANGE));
        HeadlessSession state = new HeadlessSession("audit-state", BuiltinProfiles.acceptanceSierra(), 12345, stateSink);
        expectAuditFailure(() -> state.applyState(
                state.snapshot().withNetwork(state.snapshot().network().withRegistration(4)), "state-change"));
        require(state.snapshot().network().stat() == 1);

        FailingSink macroSink = new FailingSink(Set.of(EventType.INJECTION));
        HeadlessSession macro = new HeadlessSession("audit-macro", BuiltinProfiles.acceptanceSierra(), 12345, macroSink);
        expectAuditFailure(() -> macro.replaceMacroEngine(replacementMacro(), "reload"));
        require(!macro.receive(RawBytes.ascii("AT\r")).outputAscii().contains("+REPLACED"));

        FailingSink replaySink = new FailingSink(Set.of(EventType.REPLAY_MARKER));
        HeadlessSession replay = new HeadlessSession("audit-replay", BuiltinProfiles.acceptanceSierra(), 12345, replaySink);
        expectAuditFailure(() -> new ReplayPlayback().playToSession(replay,
                List.of(new ReplayStep(RawBytes.empty(), RawBytes.ascii("X"), false, List.of())), false));
        require(replaySink.events().stream().noneMatch(event -> event.eventType() == EventType.TX_BYTES));
    }

    private MacroEngine replacementMacro() {
        try {
            Path path = Files.createTempFile("modemsim-audit-macro", ".xml");
            Files.writeString(path, """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <macros version="1.0">
                      <macro id="replace-at" phase="replace">
                        <match command="AT"/>
                        <then><emit line="+REPLACED"/></then>
                      </macro>
                    </macros>
                    """);
            return new MacroEngine(new MacroLoader().load(path));
        } catch (java.io.IOException e) {
            throw new IllegalStateException("macro fixture failed", e);
        }
    }

    private void expectAuditFailure(Runnable action) {
        try {
            action.run();
            require(false);
        } catch (AuditLogException expected) {
            require(true);
        }
    }

    private void require(boolean condition) {
        if (!condition) {
            throw new IllegalStateException("acceptance check failed");
        }
    }

    private static final class FailingSink implements EventSink {
        private final Set<EventType> failTypes;
        private final java.util.List<ModemEvent> events = new java.util.ArrayList<>();

        FailingSink(Set<EventType> failTypes) {
            this.failTypes = Set.copyOf(failTypes);
        }

        @Override
        public void publish(ModemEvent event) {
            if (failTypes.contains(event.eventType())) {
                throw new AuditLogException("cannot write " + event.eventType(), null);
            }
            events.add(event);
        }

        java.util.List<ModemEvent> events() {
            return java.util.List.copyOf(events);
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
