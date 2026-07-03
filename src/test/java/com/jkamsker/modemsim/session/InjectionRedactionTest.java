package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class InjectionRedactionTest {
    @Test
    void smsBodyInjectionRedactsAuditAndCompletionTelemetry() {
        HeadlessSession session = new HeadlessSession("sms-injection-redaction", BuiltinProfiles.acceptanceSierra(), 12345);
        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));

        SessionResponse response = session.injectDte(RawBytes.ascii("injected secret body\u001A"), "raw-dte-to-dce");

        assertNoEventLeak(response.events(), "injected secret body", RawBytes.ascii("injected secret body").toHex());
        assertSmsBodyRedacted(event(response.events(), EventType.INJECTION));
        assertSmsBodyRedacted(injectionTelemetry(response.events()));
    }

    @Test
    void parsedInjectionDuringSmsEntryRedactsParseFailure() {
        HeadlessSession session = new HeadlessSession("parsed-injection-redaction", BuiltinProfiles.acceptanceSierra(), 12345);
        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));

        SessionResponse response = session.injectParsedCommand(RawBytes.ascii("parsed secret body\r"), "parsed-command");

        assertNoEventLeak(response.events(), "parsed secret body", RawBytes.ascii("parsed secret body").toHex());
        assertSmsBodyRedacted(event(response.events(), EventType.INJECTION));
        assertSmsBodyRedacted(event(response.events(), EventType.PARSE_ERROR));
        assertSmsBodyRedacted(injectionTelemetry(response.events()));
    }

    @Test
    void splitPinInjectionUsesBufferedRedactionContext() {
        HeadlessSession session = new HeadlessSession("split-pin-injection", BuiltinProfiles.acceptanceSierra(), 12345);
        session.injectDte(RawBytes.ascii("AT+CP"), "raw-dte-to-dce");

        SessionResponse response = session.injectDte(RawBytes.ascii("IN=\"9876\"\r"), "raw-dte-to-dce");

        assertNoEventLeak(response.events(), "9876", "39383736");
        assertRawRedacted(event(response.events(), EventType.INJECTION), "pin");
        assertRawRedacted(event(response.events(), EventType.RX_BYTES), "pin");
        assertRawRedacted(injectionTelemetry(response.events()), "pin");
    }

    @Test
    void splitDialInjectionUsesBufferedRedactionContext() {
        HeadlessSession session = new HeadlessSession("split-dial-injection", BuiltinProfiles.acceptanceSierra(), 12345);
        session.injectDte(RawBytes.ascii("ATD+49"), "raw-dte-to-dce");

        SessionResponse response = session.injectDte(RawBytes.ascii("1701234567\r"), "raw-dte-to-dce");

        assertNoEventLeak(response.events(), "1701234567", RawBytes.ascii("1701234567").toHex());
        assertRawRedacted(event(response.events(), EventType.INJECTION), "msisdn");
        assertRawRedacted(event(response.events(), EventType.RX_BYTES), "msisdn");
        assertRawRedacted(injectionTelemetry(response.events()), "msisdn");
    }

    private void assertSmsBodyRedacted(ModemEvent event) {
        assertRawRedacted(event, "sms-body");
    }

    private void assertRawRedacted(ModemEvent event, String redactionClass) {
        assertThat(event.rawHex()).isEqualTo("<redacted>");
        assertThat(event.textEscaped()).isEqualTo("<redacted>");
        assertThat(event.redaction().classes()).contains(redactionClass);
    }

    private void assertNoEventLeak(List<ModemEvent> events, String text, String hex) {
        assertThat(events).allSatisfy(event -> assertThat(searchable(event)).doesNotContain(text).doesNotContain(hex));
    }

    private ModemEvent event(List<ModemEvent> events, EventType type) {
        return events.stream().filter(event -> event.eventType() == type).findFirst().orElseThrow();
    }

    private ModemEvent injectionTelemetry(List<ModemEvent> events) {
        return events.stream()
                .filter(event -> event.eventType() == EventType.HANDLER_RESULT)
                .filter(event -> event.handler() != null && event.handler().startsWith("Injection:"))
                .findFirst()
                .orElseThrow();
    }

    private String searchable(ModemEvent event) {
        return String.join("|", Stream.of(event.rawHex(), event.textEscaped(), String.valueOf(event.parsedCommand()),
                String.valueOf(event.scheduler()), String.valueOf(event.stateBefore()), String.valueOf(event.stateAfter()))
                .map(String::valueOf).toList());
    }
}
