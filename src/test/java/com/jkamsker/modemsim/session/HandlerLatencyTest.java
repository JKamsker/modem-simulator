package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.macros.MacroLoader;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.state.NetworkDelay;
import com.jkamsker.modemsim.state.NetworkRuntime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerLatencyTest {
    @TempDir
    Path tempDir;

    @Test
    void smsSubmitLatencyUsesSampledNetworkDelay() {
        HeadlessSession session = new HeadlessSession("sms-latency", profileWithDelay("sms-submit", 77), 12345);

        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        SessionResponse response = session.receive(RawBytes.ascii("latency\u001A"));

        assertThat(event(response.events(), EventType.HANDLER_RESULT).latencyMs()).isEqualTo(77.0);
    }

    @Test
    void delayedDialLatencyMatchesSchedulerSample() {
        HeadlessSession session = new HeadlessSession("dial-latency", BuiltinProfiles.acceptanceSierra(), 12345);

        SessionResponse response = session.receive(RawBytes.ascii("ATD123\r"));

        assertThat(event(response.events(), EventType.HANDLER_RESULT).latencyMs())
                .isEqualTo((double) schedulerDelay(response.events(), "dial"));
    }

    @Test
    void timerMacroLatencyUsesSampledMacroDelay() throws Exception {
        Path macros = tempDir.resolve("timer.xml");
        Files.writeString(macros, """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="timer-urc" phase="on-timer">
                    <match type="timer" timerId="heartbeat"/>
                    <then><delay ms="33"/><emit line="+TIMER"/></then>
                  </macro>
                </macros>
                """);
        HeadlessSession session = new HeadlessSession("timer-latency", BuiltinProfiles.acceptanceSierra(), 12345,
                new com.jkamsker.modemsim.monitor.InMemoryEventSink(),
                new MacroEngine(new MacroLoader(Set.of("heartbeat"), true).load(macros)));

        SessionResponse response = session.fireTimer("heartbeat");

        assertThat(event(response.events(), EventType.HANDLER_RESULT).latencyMs()).isEqualTo(33.0);
    }

    private Profile profileWithDelay(String operation, int delayMs) {
        Profile base = BuiltinProfiles.acceptanceSierra();
        NetworkRuntime network = base.initialState().network();
        var delays = new LinkedHashMap<>(network.delays());
        delays.put(operation, new NetworkDelay(operation, delayMs, delayMs));
        return base.withInitialState(base.initialState().withNetwork(new NetworkRuntime(
                network.cregN(), network.stat(), network.lac(), network.ci(), network.act(),
                network.rejectCauseType(), network.rejectCause(), network.operator(), network.smsRateLimit(), delays)));
    }

    private ModemEvent event(List<ModemEvent> events, EventType type) {
        return events.stream().filter(event -> event.eventType() == type).findFirst().orElseThrow();
    }

    private int schedulerDelay(List<ModemEvent> events, String operation) {
        return events.stream()
                .filter(event -> event.scheduler() != null && operation.equals(event.scheduler().get("operation")))
                .map(event -> ((Number) event.scheduler().get("sampledDelayMs")).intValue())
                .findFirst()
                .orElseThrow();
    }
}
