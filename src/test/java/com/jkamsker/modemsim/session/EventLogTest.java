package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.InMemoryEventSink;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.monitor.ModemEventJson;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SimRuntime;
import com.jkamsker.modemsim.validation.JsonSchemaValidator;
import com.jkamsker.modemsim.validation.SchemaLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class EventLogTest {
    @Test
    void sessionStartIncludesDeterministicMetadata() {
        InMemoryEventSink sink = new InMemoryEventSink();

        new HeadlessSession("meta", BuiltinProfiles.acceptanceSierra(), 9876, sink);

        ModemEvent event = sink.events().getFirst();
        assertThat(event.eventType()).isEqualTo(EventType.SESSION_START);
        assertThat(event.sessionSeed()).isEqualTo(9876);
        assertThat(event.clockMode()).isEqualTo("virtual");
        assertThat(event.profileHash()).startsWith("sha256:");
        assertThat(event.configHash()).startsWith("sha256:");
        assertThat(event.macroHash()).isEqualTo("sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        assertThat(event.initialStateHash()).startsWith("sha256:");
        assertThat(event.droppedEventCount()).isZero();
    }

    @Test
    void auditEventsUseSessionMetadata() {
        InMemoryEventSink sink = new InMemoryEventSink();
        HeadlessSession session = new HeadlessSession("audit", BuiltinProfiles.acceptanceSierra(), 9876, sink);

        session.applyState(session.snapshot(), "state-change");

        ModemEvent event = sink.events().getLast();
        assertThat(event.eventType()).isEqualTo(EventType.STATE_CHANGE);
        assertThat(event.sessionSeed()).isEqualTo(9876);
        assertThat(event.configHash()).startsWith("sha256:");
        assertThat(event.configHash()).isNotEqualTo("sha256:gui-session:" + event.profile());
    }

    @Test
    void staleScheduledEmissionsAreLoggedAsCancelled() {
        HeadlessSession session = new HeadlessSession("cancel-session", BuiltinProfiles.acceptanceSierra(), 42);
        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        session.receive(RawBytes.ascii("payload\u001A"));

        SessionResponse fault = session.applyFault("network-outage");
        SessionResponse drained = session.drainScheduled();

        assertThat(drained.output()).isEqualTo(RawBytes.empty());
        assertThat(fault.events()).extracting(ModemEvent::eventType).contains(EventType.SCHEDULER_EMIT);
        ModemEvent cancel = fault.events().stream()
                .filter(item -> item.eventType() == EventType.SCHEDULER_EMIT)
                .filter(item -> Boolean.TRUE.equals(item.scheduler().get("cancelled")))
                .findFirst()
                .orElseThrow();
        assertThat(cancel.scheduler())
                .containsEntry("cancelled", true)
                .containsEntry("operation", "sms-submit");
    }

    @Test
    void cancellationAndPortLostEventsValidateAgainstEventSchema(@TempDir Path tempDir) throws Exception {
        HeadlessSession session = new HeadlessSession(
                "schema", BuiltinProfiles.acceptanceSierra(), 42,
                new InMemoryEventSink(), "virtual", "modem", "modem-simulation");
        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        session.receive(RawBytes.ascii("payload\u001A"));
        List<ModemEvent> events = Stream.concat(
                session.portLost("port-lost:modem:test").events().stream(),
                session.stop("normal-stop").events().stream()).toList();

        JsonSchemaValidator validator = new JsonSchemaValidator();
        for (int i = 0; i < events.size(); i++) {
            Path json = tempDir.resolve("event-" + i + ".json");
            Files.writeString(json, ModemEventJson.toJson(events.get(i)));
            assertThat(validator.validateJson(json, SchemaLocator.schemaPath("event-log.schema.json")).valid())
                    .as(ModemEventJson.toJson(events.get(i)))
                    .isTrue();
        }
    }

    @Test
    void pinEntryIsRedactedFromRxAndParsedEvents() {
        HeadlessSession session = new HeadlessSession("locked", lockedProfile(), 12345);

        SessionResponse response = session.receive(RawBytes.ascii("AT+CPIN=\"9876\"\r"));

        assertThat(response.outputAscii()).isEqualTo("\r\nOK\r\n");
        assertNoEventLeak(response.events(), "9876", "39383736");
        ModemEvent rx = event(response.events(), EventType.RX_BYTES);
        assertThat(rx.rawHex()).isEqualTo("<redacted>");
        assertThat(rx.textEscaped()).isEqualTo("<redacted>");
        assertThat(rx.redaction().classes()).contains("pin");
        ModemEvent parsed = event(response.events(), EventType.PARSED_COMMAND);
        assertThat(parsed.parsedCommand()).containsEntry("arguments", "<redacted>");
        assertThat(parsed.redaction().classes()).contains("pin");
    }

    @Test
    void smsBodyIsRedactedFromRxAndStateEvents() {
        HeadlessSession session = new HeadlessSession("sms", BuiltinProfiles.acceptanceSierra(), 12345);
        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));

        SessionResponse response = session.receive(RawBytes.ascii("top secret body\u001A"));

        assertNoEventLeak(response.events(), "top secret body", RawBytes.ascii("top secret body").toHex());
        ModemEvent rx = event(response.events(), EventType.RX_BYTES);
        assertThat(rx.rawHex()).isEqualTo("<redacted>");
        assertThat(rx.redaction().classes()).contains("sms-body");
        ModemEvent handler = event(response.events(), EventType.HANDLER_RESULT);
        assertThat(handler.redaction().fields()).contains("stateAfter");
        assertThat(handler.stateAfter().sms().messages().values())
                .extracting(message -> message.text())
                .containsOnly("<redacted>");
        assertThat(session.snapshot().sms().messages().values())
                .extracting(message -> message.text())
                .containsOnly("top secret body");
    }

    @Test
    void storedSmsReadResponsesAreRedactedInEventLog() {
        HeadlessSession session = new HeadlessSession("sms-read", BuiltinProfiles.acceptanceSierra(), 12345);
        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        session.receive(RawBytes.ascii("very secret body\u001A"));
        session.drainScheduled();

        SessionResponse response = session.receive(RawBytes.ascii("AT+CMGR=1\r"));

        assertNoEventLeak(response.events(), "very secret body", RawBytes.ascii("very secret body").toHex());
        ModemEvent tx = event(response.events(), EventType.TX_BYTES);
        assertThat(tx.rawHex()).isEqualTo("<redacted>");
        assertThat(tx.redaction().classes()).contains("sms-body");
    }

    @Test
    void identifiersAreRedactedFromRawEventsAndStateSnapshots() {
        HeadlessSession session = new HeadlessSession("ids", BuiltinProfiles.acceptanceSierra(), 12345);

        SessionResponse imei = session.receive(RawBytes.ascii("AT+CGSN\r"));
        SessionResponse smsc = session.receive(RawBytes.ascii("AT+CSCA?\r"));
        SessionResponse dial = session.receive(RawBytes.ascii("ATD+491701234567\r"));
        SessionResponse localDial = session.receive(RawBytes.ascii("ATD1234567890\r"));

        assertNoEventLeak(imei.events(), "359762080000001", RawBytes.ascii("359762080000001").toHex());
        assertNoEventLeak(smsc.events(), "+491710760000", RawBytes.ascii("+491710760000").toHex());
        assertNoEventLeak(dial.events(), "+491701234567", RawBytes.ascii("+491701234567").toHex());
        assertNoEventLeak(localDial.events(), "1234567890", RawBytes.ascii("1234567890").toHex());
        assertThat(event(imei.events(), EventType.TX_BYTES).redaction().classes())
                .contains("imei", "imsi", "iccid");
        assertThat(event(smsc.events(), EventType.TX_BYTES).redaction().classes()).contains("msisdn");
        assertThat(event(dial.events(), EventType.HANDLER_RESULT).stateAfter().call().dialedNumber())
                .isEqualTo("<redacted>");
    }

    @Test
    void cregLocationFieldsAreNotRedactedAsPhoneNumbers() {
        HeadlessSession session = new HeadlessSession("creg-redaction", BuiltinProfiles.acceptanceSierra(), 12345);

        SessionResponse response = session.receive(RawBytes.ascii("AT+CREG?\r"));

        ModemEvent tx = event(response.events(), EventType.TX_BYTES);
        assertThat(tx.rawHex()).isNotEqualTo("<redacted>");
        assertThat(tx.textEscaped()).contains("00001234");
        assertThat(tx.redaction().fields()).doesNotContain("rawHex", "textEscaped");
    }

    @Test
    void splitSensitiveRxUsesBufferedCommandContextForRedaction() {
        HeadlessSession locked = new HeadlessSession("split-pin", lockedProfile(), 12345);

        locked.receive(RawBytes.ascii("AT+CP"));
        SessionResponse pin = locked.receive(RawBytes.ascii("IN=\"9876\"\r"));

        assertNoEventLeak(pin.events(), "9876", "39383736");
        assertThat(event(pin.events(), EventType.RX_BYTES).rawHex()).isEqualTo("<redacted>");

        HeadlessSession dial = new HeadlessSession("split-dial", BuiltinProfiles.acceptanceSierra(), 12345);
        dial.receive(RawBytes.ascii("ATD+49"));
        SessionResponse number = dial.receive(RawBytes.ascii("1701234567\r"));

        assertNoEventLeak(number.events(), "+491701234567", RawBytes.ascii("+491701234567").toHex());
        assertThat(event(number.events(), EventType.RX_BYTES).redaction().classes()).contains("msisdn");
    }

    private Profile lockedProfile() {
        Profile base = BuiltinProfiles.acceptanceSierra();
        SimRuntime sim = base.initialState().sim();
        return base.withInitialState(base.initialState().withSim(new SimRuntime(
                SimState.SIM_PIN_REQUIRED,
                sim.pinQueryEnabled(),
                sim.pinRef(),
                "9876",
                sim.pinRetries(),
                sim.pukRetries(),
                sim.imsi(),
                sim.iccid())));
    }

    private void assertNoEventLeak(List<ModemEvent> events, String text, String hex) {
        assertThat(events).allSatisfy(event -> assertThat(searchable(event))
                .doesNotContain(text)
                .doesNotContain(hex));
    }

    private ModemEvent event(List<ModemEvent> events, EventType type) {
        return events.stream()
                .filter(event -> event.eventType() == type)
                .findFirst()
                .orElseThrow();
    }

    private String searchable(ModemEvent event) {
        return String.join("|", Stream.of(
                event.rawHex(),
                event.textEscaped(),
                String.valueOf(event.parsedCommand()),
                String.valueOf(event.scheduler()),
                String.valueOf(event.stateBefore()),
                String.valueOf(event.stateAfter())).map(String::valueOf).toList());
    }
}
