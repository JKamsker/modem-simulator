package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.state.ModemLifecycle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class MacroSchedulerSemanticsTest {
    @TempDir
    Path tempDir;

    @Test
    void unrelatedScheduledOutputDoesNotCommitDelayedMacroState() throws Exception {
        HeadlessSession session = session("""
                <macro id="delayed-state" priority="100" phase="replace">
                  <match rawGlob="AT+DELAYSTATE"/>
                  <then><delay ms="10000"/><set path="state.network.stat" value="4"/></then>
                </macro>
                <macro id="delayed-output" priority="100" phase="replace">
                  <match rawGlob="AT+PING"/>
                  <then><delay ms="1000"/><emit line="+PING"/></then>
                </macro>
                """);

        session.receive(RawBytes.ascii("AT+DELAYSTATE\r"));
        session.receive(RawBytes.ascii("AT+PING\r"));
        SessionResponse pingResponse = session.advanceTime(1_000);

        assertThat(pingResponse.outputAscii()).contains("+PING");
        assertThat(session.snapshot().network().stat()).isEqualTo(1);

        session.advanceTime(9_000);

        assertThat(session.snapshot().network().stat()).isEqualTo(4);
    }

    @Test
    void stateChangeCancelsDelayedMacroState() throws Exception {
        HeadlessSession session = session("""
                <macro id="delayed-state" priority="100" phase="replace">
                  <match rawGlob="AT+DELAYSTATE"/>
                  <then><delay ms="1000"/><set path="state.network.stat" value="4"/></then>
                </macro>
                """);

        session.receive(RawBytes.ascii("AT+DELAYSTATE\r"));
        SessionResponse fault = session.applyFault("modem-freeze");
        SessionResponse drained = session.drainScheduled();

        assertThat(drained.outputHex()).isEmpty();
        assertThat(session.snapshot().network().stat()).isEqualTo(1);
        assertThat(session.snapshot().modem().lifecycle()).isEqualTo(ModemLifecycle.FROZEN);
        assertThat(fault.events()).anySatisfy(event -> {
            assertThat(event.eventType()).isEqualTo(EventType.SCHEDULER_EMIT);
            assertThat(event.scheduler()).containsEntry("operation", "macro-delayed-state");
            assertThat(event.scheduler()).containsEntry("cancelled", true);
        });
    }

    @Test
    void macroFaultCancelsPendingSchedulerOutputImmediately() throws Exception {
        HeadlessSession session = session("""
                <macro id="delayed-output" priority="100" phase="replace">
                  <match rawGlob="AT+PING"/>
                  <then><delay ms="1000"/><emit line="+PING"/></then>
                </macro>
                <macro id="freeze" priority="100" phase="replace">
                  <match rawGlob="AT+FREEZE"/>
                  <then><fault type="modem-freeze" freezeMode="NO_RESPONSE"/></then>
                </macro>
                """);

        session.receive(RawBytes.ascii("AT+PING\r"));
        SessionResponse fault = session.receive(RawBytes.ascii("AT+FREEZE\r"));
        SessionResponse drained = session.drainScheduled();

        assertThat(drained.outputHex()).isEmpty();
        assertThat(fault.events()).anySatisfy(event -> {
            assertThat(event.eventType()).isEqualTo(EventType.SCHEDULER_EMIT);
            assertThat(event.scheduler()).containsEntry("operation", "macro-delayed-output");
            assertThat(event.scheduler()).containsEntry("cancelled", true);
        });
    }

    @Test
    void sameDueDelayedMacroStatesCommitInSchedulerOrder() throws Exception {
        HeadlessSession session = session("""
                <macro id="delayed-state-four" priority="100" phase="replace">
                  <match rawGlob="AT+DELAY4"/>
                  <then><delay ms="1000"/><set path="state.network.stat" value="4"/></then>
                </macro>
                <macro id="delayed-state-five" priority="100" phase="replace">
                  <match rawGlob="AT+DELAY5"/>
                  <then><delay ms="1000"/><set path="state.network.stat" value="5"/></then>
                </macro>
                """);

        session.receive(RawBytes.ascii("AT+DELAY4\r"));
        session.receive(RawBytes.ascii("AT+DELAY5\r"));
        SessionResponse due = session.advanceTime(1_000);

        assertThat(session.snapshot().network().stat()).isEqualTo(5);
        assertThat(due.events().stream()
                .filter(event -> event.eventType() == EventType.STATE_CHANGE)
                .filter(event -> "macro-control".equals(event.injectionType())))
                .hasSize(2);
    }

    @Test
    void sameDueDelayedMacroStatesApplyAsPatchesToCurrentState() throws Exception {
        HeadlessSession session = session("""
                <macro id="delayed-registration" priority="100" phase="replace">
                  <match rawGlob="AT+DELAYREG"/>
                  <then><delay ms="1000"/><set path="state.network.stat" value="4"/></then>
                </macro>
                <macro id="delayed-signal" priority="100" phase="replace">
                  <match rawGlob="AT+DELAYSIGNAL"/>
                  <then><delay ms="1000"/><set path="state.signal.rssi" value="12"/></then>
                </macro>
                """);

        session.receive(RawBytes.ascii("AT+DELAYREG\r"));
        session.receive(RawBytes.ascii("AT+DELAYSIGNAL\r"));
        session.advanceTime(1_000);

        assertThat(session.snapshot().network().stat()).isEqualTo(4);
        assertThat(session.snapshot().signal().rssi()).isEqualTo(12);
    }

    @Test
    void eventActionsEmitDistinctMacroEvents() throws Exception {
        HeadlessSession session = session("""
                <macro id="event-marker" priority="100" phase="replace">
                  <match rawGlob="AT+MARK"/>
                  <then><event type="TEST_MARKER" message="conditional macro"/></then>
                </macro>
                """);

        SessionResponse response = session.receive(RawBytes.ascii("AT+MARK\r"));

        assertThat(response.events()).anySatisfy(event -> {
            assertThat(event.eventType()).isEqualTo(EventType.MACRO_EVENT);
            assertThat(event.macroId()).isEqualTo("event-marker");
            assertThat(event.result()).isEqualTo("TEST_MARKER");
        });
    }

    private HeadlessSession session(String macroBody) throws Exception {
        MacroSet macros = new MacroLoader().load(write("""
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                %s
                </macros>
                """.formatted(macroBody)));
        return new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345,
                new com.jkamsker.modemsim.monitor.InMemoryEventSink(), new MacroEngine(macros));
    }

    private Path write(String content) throws Exception {
        Path path = tempDir.resolve("macros.xml");
        Files.writeString(path, content);
        return path;
    }
}
