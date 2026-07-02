package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.InMemoryEventSink;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SimRuntime;
import org.junit.jupiter.api.Test;

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
        assertThat(event.initialStateHash()).startsWith("sha256:");
        assertThat(event.droppedEventCount()).isZero();
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
