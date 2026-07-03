package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.InMemoryEventSink;
import com.jkamsker.modemsim.monitor.ModemEventJson;
import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.macros.FaultAction;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.state.FreezeMode;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GuiSessionControllerTest {
    @Test
    void rawDteActionExecutesCommandAndReturnsAuditableEvents() {
        GuiSessionController controller = controller();

        SessionResponse response = controller.rawDteToDce("AT\\r");

        assertThat(response.outputAscii()).isEqualTo("\r\nOK\r\n");
        assertThat(response.events()).extracting(event -> event.eventType())
                .containsExactly(
                        EventType.INJECTION,
                        EventType.RX_BYTES,
                        EventType.PARSED_COMMAND,
                        EventType.HANDLER_RESULT,
                        EventType.TX_BYTES);
        assertThat(response.events().getFirst().injectionType()).isEqualTo("raw-dte-to-dce");
    }

    @Test
    void parsedCommandInjectionDoesNotPublishRawRxBytes() {
        GuiSessionController controller = controller();

        SessionResponse response = controller.parsedCommand("AT+CSQ");

        assertThat(response.outputAscii()).contains("+CSQ");
        assertThat(response.events()).extracting(event -> event.eventType())
                .containsExactly(EventType.INJECTION, EventType.PARSED_COMMAND, EventType.HANDLER_RESULT, EventType.TX_BYTES);
        assertThat(response.events()).noneSatisfy(event -> assertThat(event.eventType()).isEqualTo(EventType.RX_BYTES));
    }

    @Test
    void rawDceActionRequiresSafetyConfirmationAndPublishesInjectionEvent() {
        GuiSessionController controller = controller(false);

        assertThatThrownBy(() -> controller.rawDceToDte("+CREG: 4\\r\\n", true))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unsafe DCE transmit");

        SessionResponse response = controller(true).rawDceToDte("+CREG: 4\\r\\n", true);

        assertThat(response.outputAscii()).isEqualTo("+CREG: 4\r\n");
        assertThat(response.events()).extracting(event -> event.eventType())
                .containsExactly(EventType.INJECTION, EventType.TX_BYTES);
        assertThat(response.events().getFirst().injectionType()).isEqualTo("raw-dce-to-dte");
        assertThat(response.events().getFirst().scheduler()).isNull();
    }

    @Test
    void statePatchAndFaultActionsProduceSchemaShapedAuditEvents() {
        GuiSessionController controller = controller();

        SessionResponse stateChange = controller.applyState(GuiStatePatchFactory.fromText("network.stat=4"));
        SessionResponse fault = controller.fault("network-restore");

        assertThat(stateChange.events()).extracting(event -> event.eventType())
                .containsExactly(EventType.STATE_CHANGE, EventType.SCHEDULER_ENQUEUE, EventType.SCHEDULER_EMIT, EventType.TX_BYTES);
        assertThat(stateChange.outputAscii()).contains("+CREG: 4");
        assertThat(stateChange.events().getFirst().stateAfter().network().stat()).isEqualTo(4);
        assertThat(fault.events()).extracting(event -> event.eventType())
                .contains(EventType.FAULT_TRIGGERED, EventType.SCHEDULER_ENQUEUE, EventType.SCHEDULER_EMIT, EventType.TX_BYTES);
        assertThat(fault.events().getFirst().result()).isEqualTo("network-restore");
        assertThat(fault.events().getFirst().injectionType()).isNull();
        assertThat(controller.snapshot().network().stat()).isEqualTo(1);
    }

    @Test
    void parameterizedFaultActionUsesGuiValues() {
        GuiSessionController controller = controller();

        controller.fault(new FaultAction("network-outage", null, null, null, null, null));
        SessionResponse restored = controller.fault(new FaultAction("network-restore", null, 5, 10, 3, null));
        SessionResponse frozen = controller.fault(new FaultAction("modem-freeze", null, null, null, null, FreezeMode.HOLD_RX_TX));

        assertThat(restored.events().getFirst().stateAfter().network().stat()).isEqualTo(5);
        assertThat(restored.events().getFirst().stateAfter().signal().rssi()).isEqualTo(10);
        assertThat(restored.events().getFirst().stateAfter().signal().ber()).isEqualTo(3);
        assertThat(frozen.events().getFirst().stateAfter().modem().freezeMode()).isEqualTo(FreezeMode.HOLD_RX_TX);
    }

    @Test
    void lifecycleStartAndExpandedStatePatchAffectActiveSession() {
        GuiSessionController controller = controller();

        SessionResponse started = controller.start(GuiSessionOptions.of(
                "westermo-td22-6177-2203", "headless", "", "", "",
                "77", "115200", "8", "1", "NONE", "NONE"));
        SessionResponse state = controller.applyState(GuiStatePatchFactory.fromValues(
                "READY", "2", "9", "1", "2", "00C3", "00001234", "7", "20,1", "",
                "15", "SM", "ONLINE_COMMAND", "FROZEN", "NO_RESPONSE", "DTR DSR DCD RI RTS CTS"));

        assertThat(started.events()).extracting(event -> event.eventType()).contains(EventType.SESSION_START);
        assertThat(state.events().getFirst().stateAfter().sms().storage().name()).isEqualTo("SM");
        assertThat(state.events().getFirst().stateAfter().call().mode().name()).isEqualTo("ONLINE_COMMAND");
        assertThat(state.events().getFirst().stateAfter().modem().lifecycle().name()).isEqualTo("FROZEN");
        assertThat(state.events().getFirst().stateAfter().lines().ri()).isTrue();
    }

    @Test
    void startOptionsApplyScenarioAndSerialFormValuesWithoutResettingHeadlessControl() {
        GuiSessionController controller = controller();

        SessionResponse started = controller.start(GuiSessionOptions.of(
                "sierra-hl6-hl8-v20", "headless", "COM8", "COM9",
                "docs/Tasks/Initial-Spec/examples/scenario.no-network.xml",
                "77", "9600", "7", "2", "EVEN", "RTS_CTS"));

        assertThat(started.events()).extracting(event -> event.eventType()).contains(EventType.SESSION_START);
        assertThat(started.events().getFirst().sessionSeed()).isEqualTo(77);
        assertThat(started.events().getFirst().port()).isEqualTo("headless");
        assertThat(controller.snapshot().network().stat()).isEqualTo(4);
        assertThat(controller.snapshot().signal().rssi()).isEqualTo(99);
    }

    @Test
    void exportsJsonlAndTranscriptFromEventModel() {
        GuiSessionController controller = controller();
        SessionResponse response = controller.rawDteToDce("AT\\r");

        String jsonl = controller.jsonl(response.events());
        String transcript = controller.transcript(response.events());

        assertThat(jsonl)
                .contains("\"timestamp\"")
                .contains("\"monotonicNanos\"")
                .contains("\"sessionId\":\"gui-test\"")
                .contains("\"redaction\":{\"applied\":true")
                .contains("\"fields\":[\"stateAfter\"]");
        assertThat(transcript).isEqualTo(String.join("\n", List.of(
                "DTE_TO_DCE 41540D",
                "DCE_TO_DTE 0D0A4F4B0D0A")));
        assertThat(controller.coverage(response.events())).contains("INJECTION=1").contains("TX_BYTES=1");
    }

    @Test
    void replayValidateReportsDivergenceOrSuccess(@org.junit.jupiter.api.io.TempDir Path tempDir) throws Exception {
        Path log = matchingReplayLog(tempDir.resolve("session.jsonl"));

        assertThat(controller().replayValidate(log)).contains("OK steps=1");
        assertThatThrownBy(() -> controller().replay(log, "play-to-dte", true, false))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unsafe DCE transmit");
        ReplaySummary play = controller(true).replay(log, "play-to-dte", true, true);
        assertThat(play.message()).contains("PLAY_TO_DTE");
        assertThat(play.response().outputAscii()).isEqualTo("\r\nOK\r\n");
    }

    @Test
    void replayPlayRequiresDivergenceConfirmation(@org.junit.jupiter.api.io.TempDir Path tempDir) throws Exception {
        Path log = matchingReplayLog(tempDir.resolve("session.jsonl"));
        Files.writeString(log, Files.readString(log).replaceFirst(
                "sha256:[0-9a-f]{64}", "sha256:" + "f".repeat(64)));

        ReplaySummary blocked = controller(true).replay(log, "play-to-dte", true, true, false);
        ReplaySummary confirmed = controller(true).replay(log, "play-to-dte", true, true, true);

        assertThat(blocked.hashStatus()).isEqualTo("hash divergence");
        assertThat(blocked.response().outputHex()).isEmpty();
        assertThat(confirmed.hashStatus()).isEqualTo("hash divergence confirmed");
        assertThat(confirmed.response().outputAscii()).isEqualTo("\r\nOK\r\n");
    }

    @Test
    void replayPlayToDteUsesCapturedTxEventTiming(@org.junit.jupiter.api.io.TempDir Path tempDir) throws Exception {
        Path log = txOnlyReplayLog(tempDir.resolve("tx-only.jsonl"));

        ReplaySummary summary = controller(true).replay(log, "play-to-dte", true, true, true);

        assertThat(summary.response().outputAscii()).isEqualTo("AB");
        assertThat(summary.response().events().stream()
                .filter(event -> event.eventType() == EventType.TX_BYTES)
                .map(event -> event.monotonicNanos()).toList())
                .containsExactly(10L, 20L);
    }

    @Test
    void macroReloadReportsHashAndValidationErrors() throws Exception {
        GuiSessionController controller = controller();

        MacroSummary summary = controller.reloadMacros(
                Path.of("docs/Tasks/Initial-Spec/examples/macros.faults-and-custom-responses.xml"));

        assertThat(summary.hash()).startsWith("sha256:");
        assertThat(summary.errors()).isBlank();
        assertThat(summary.customResponses()).contains("cmsg-error");
        assertThat(summary.response().events()).extracting(event -> event.injectionType()).contains("macro-control");
        assertThat(controller.macroToggleStatus("disable", "cmsg-error")).contains("disabled");
        assertThat(controller.macroToggleStatus("enable", "cmsg-error")).contains("enabled");
        assertThat(controller.macroToggleStatus("disable", "missing")).contains("Unknown macro ID");
    }

    @Test
    void macroReloadFailureIsAudited(@org.junit.jupiter.api.io.TempDir Path tempDir) throws Exception {
        Path invalid = tempDir.resolve("invalid.xml");
        Files.writeString(invalid, "<macros version=\"1.0\"><macro id=\"broken\"></macros>");

        MacroSummary summary = controller().reloadMacros(invalid);

        assertThat(summary.errors()).isNotBlank();
        assertThat(summary.response().events()).extracting(event -> event.eventType())
                .contains(EventType.VALIDATION_ERROR);
    }

    @Test
    void macroReloadPrunesOverrideStateForRemovedIds(@org.junit.jupiter.api.io.TempDir Path tempDir) throws Exception {
        Path first = tempDir.resolve("first.xml");
        Path second = tempDir.resolve("second.xml");
        Files.writeString(first, macroFile("reintroduced", "+ONE"));
        Files.writeString(second, macroFile("other", "+TWO"));
        GuiSessionController controller = controller();

        controller.reloadMacros(first);
        assertThat(controller.macroToggleStatus("disable", "reintroduced")).contains("disabled");
        controller.reloadMacros(second);
        MacroSummary summary = controller.reloadMacros(first);

        assertThat(summary.enabled()).contains("reintroduced");
        assertThat(controller.rawDteToDce("AT+REINTRODUCED\\r").outputAscii()).contains("+ONE");
    }

    private String macroFile(String id, String line) {
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="%s" phase="replace">
                    <match rawGlob="AT+REINTRODUCED"/>
                    <then><emit line="%s"/></then>
                  </macro>
                </macros>
                """.formatted(id, line);
    }

    private Path matchingReplayLog(Path log) throws Exception {
        HeadlessSession capture = new HeadlessSession(
                "gui-replay", BuiltinProfiles.acceptanceSierra(), 12345, new InMemoryEventSink(),
                MacroEngine.empty(), "virtual", "gui-headless", "modem-simulation");
        Files.writeString(log, ModemEventJson.toJsonLines(capture.receive(com.jkamsker.modemsim.parser.RawBytes.ascii("AT\r")).events()));
        return log;
    }

    private Path txOnlyReplayLog(Path log) throws Exception {
        Files.writeString(log, """
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":10,"sequence":1,"sessionId":"main","eventType":"TX_BYTES","source":"tx","direction":"DCE_TO_DTE","rawHex":"41","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                {"timestamp":"2026-01-01T00:00:00Z","monotonicNanos":20,"sequence":2,"sessionId":"main","eventType":"TX_BYTES","source":"tx","direction":"DCE_TO_DTE","rawHex":"42","profileHash":"sha256:1111111111111111111111111111111111111111111111111111111111111111","configHash":"sha256:2222222222222222222222222222222222222222222222222222222222222222","macroHash":"sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855","initialStateHash":"sha256:3333333333333333333333333333333333333333333333333333333333333333","sessionSeed":12345,"clockMode":"virtual","redaction":{"applied":false,"policy":"default-v1","fields":[],"classes":[]}}
                """);
        return log;
    }

    private GuiSessionController controller() {
        return controller(false);
    }

    private GuiSessionController controller(boolean allowUnsafeDceTransmit) {
        return new GuiSessionController(
                new HeadlessSession("gui-test", BuiltinProfiles.acceptanceSierra(), 12345),
                allowUnsafeDceTransmit);
    }
}
