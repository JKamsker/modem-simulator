package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.gui.GuiControlCatalog;
import com.jkamsker.modemsim.gui.GuiAcceptanceHarness;
import com.jkamsker.modemsim.gui.GuiControl;
import com.jkamsker.modemsim.gui.ReadOnlyPolicy;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.state.NetworkDelay;
import com.jkamsker.modemsim.state.NetworkRuntime;

import java.util.LinkedHashMap;
import java.util.List;

final class GuiAcceptanceChecks {
    void liveLog() {
        new GuiAcceptanceHarness().liveLog();
        var controls = new GuiControlCatalog().controls();
        require(controls.stream().anyMatch(c -> c.id().equals("log.table")));
        require(controls.stream().anyMatch(c -> c.id().equals("log.export")));
        SessionResponse response = new HeadlessSession("gui", BuiltinProfiles.acceptanceSierra(), 12345)
                .receive(RawBytes.ascii("AT\r"));
        require(response.events().stream().map(ModemEvent::eventType).toList()
                .containsAll(List.of(EventType.RX_BYTES, EventType.PARSED_COMMAND, EventType.HANDLER_RESULT, EventType.TX_BYTES)));
        ModemEvent parsed = event(response, EventType.PARSED_COMMAND);
        require("AT".equals(parsed.parsedCommand().get("name")));
        ModemEvent handled = event(response, EventType.HANDLER_RESULT);
        require("HayesHandler".equals(handled.handler()) && "OK".equals(handled.result()));
        require(handled.latencyMs() != null && handled.stateBefore() != null && handled.stateAfter() != null);
        require(!event(response, EventType.RX_BYTES).rawHex().isBlank());
        HeadlessSession delayed = new HeadlessSession("gui-delay", smsDelayProfile(77), 12345);
        delayed.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        require(Double.valueOf(77).equals(event(delayed.receive(RawBytes.ascii("latency\u001A")),
                EventType.HANDLER_RESULT).latencyMs()));
    }

    void readOnly() {
        var controls = new ReadOnlyPolicy().apply(new GuiControlCatalog().controls(), true);
        require(controls.stream().filter(c -> c.id().startsWith("inject.")).noneMatch(GuiControl::enabled));
        require(controls.stream().filter(c -> c.id().startsWith("state.")).noneMatch(GuiControl::enabled));
        require(controls.stream().filter(c -> c.id().startsWith("fault.")).noneMatch(GuiControl::enabled));
    }

    void injection() {
        new GuiAcceptanceHarness().injection();
        var catalog = new GuiControlCatalog().controls();
        require(catalog.stream().filter(c -> c.id().startsWith("inject.")).anyMatch(GuiControl::enabled));
        readOnly();
        HeadlessSession s = new HeadlessSession("gui", BuiltinProfiles.acceptanceSierra(), 12345);
        require(event(s.injectDte(RawBytes.ascii("AT\r"), "raw-dte-to-dce"), EventType.INJECTION) != null);
        require(event(s.injectDce(RawBytes.ascii("+CREG: 4\r\n"), "raw-dce-to-dte"), EventType.TX_BYTES) != null);
        require(event(s.applyFault("network-outage"), EventType.FAULT_TRIGGERED) != null);
    }

    private ModemEvent event(SessionResponse response, EventType type) {
        return response.events().stream()
                .filter(item -> item.eventType() == type)
                .findFirst()
                .orElseThrow();
    }

    private Profile smsDelayProfile(int delayMs) {
        Profile base = BuiltinProfiles.acceptanceSierra();
        NetworkRuntime network = base.initialState().network();
        var delays = new LinkedHashMap<>(network.delays());
        delays.put("sms-submit", new NetworkDelay("sms-submit", delayMs, delayMs));
        return base.withInitialState(base.initialState().withNetwork(new NetworkRuntime(
                network.cregN(), network.stat(), network.lac(), network.ci(), network.act(),
                network.rejectCauseType(), network.rejectCause(), network.operator(), network.smsRateLimit(), delays)));
    }

    private void require(boolean condition) {
        if (!condition) {
            throw new IllegalStateException("acceptance check failed");
        }
    }
}
