package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.state.NetworkRuntime;
import com.jkamsker.modemsim.state.SmsRateLimit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SmsFramingMetadataTest {
    @Test
    void runtimePduCmgsParsedEventCarriesPduContext() {
        HeadlessSession session = unlimitedSmsSession();

        session.receive(RawBytes.ascii("AT+CMGF=0\r"));
        SessionResponse prompt = session.receive(RawBytes.ascii("AT+CMGS=4\r"));

        assertThat(prompt.events().stream()
                .filter(event -> event.eventType() == EventType.PARSED_COMMAND)
                .filter(event -> "+CMGS".equals(event.parsedCommand().get("name")))
                .findFirst())
                .hasValueSatisfying(event ->
                        assertThat(event.parsedCommand()).containsEntry("pduContext", true));
    }

    @Test
    void smsSubmitProcessesTailBytesAfterCtrlZAsCommandInput() {
        HeadlessSession session = unlimitedSmsSession();

        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        SessionResponse response = session.receive(RawBytes.ascii("tail body\u001AAT\r"));

        assertThat(response.outputAscii()).isEqualTo("\r\nOK\r\n");
        assertThat(session.drainScheduled().outputAscii()).contains("+CMGS:");
        assertThat(session.snapshot().sms().messages().values())
                .extracting(message -> message.text())
                .containsOnly("tail body");
    }

    @Test
    void smsAbortProcessesTailBytesAfterEscAsCommandInput() {
        HeadlessSession session = unlimitedSmsSession();

        session.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        SessionResponse response = session.receive(RawBytes.ascii("draft\u001BAT\r"));

        assertThat(response.outputAscii()).isEqualTo("\r\nOK\r\n");
        assertThat(session.drainScheduled().outputAscii()).contains("OK");
        assertThat(session.snapshot().sms().messages()).isEmpty();
    }

    private HeadlessSession unlimitedSmsSession() {
        var base = BuiltinProfiles.acceptanceSierra();
        NetworkRuntime network = base.initialState().network();
        NetworkRuntime updated = new NetworkRuntime(
                network.cregN(), network.stat(), network.lac(), network.ci(), network.act(),
                network.rejectCauseType(), network.rejectCause(), network.operator(),
                SmsRateLimit.none(), network.delays());
        return new HeadlessSession("sms", base.withInitialState(base.initialState().withNetwork(updated)), 12345);
    }
}
