package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

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
                        EventType.RX_BYTES,
                        EventType.PARSED_COMMAND,
                        EventType.HANDLER_RESULT,
                        EventType.TX_BYTES);
    }

    @Test
    void rawDceActionRequiresSafetyConfirmationAndPublishesInjectionEvent() {
        GuiSessionController controller = controller();

        assertThatThrownBy(() -> controller.rawDceToDte("+CREG: 4\\r\\n", false))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unsafe DCE transmit");

        SessionResponse response = controller.rawDceToDte("+CREG: 4\\r\\n", true);

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
                .containsExactly(EventType.STATE_CHANGE);
        assertThat(stateChange.events().getFirst().stateAfter().network().stat()).isEqualTo(4);
        assertThat(fault.events()).extracting(event -> event.eventType())
                .containsExactly(EventType.FAULT_TRIGGERED);
        assertThat(fault.events().getFirst().injectionType()).isEqualTo("fault-network-restore");
        assertThat(controller.snapshot().network().stat()).isEqualTo(1);
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
                "INTERNAL 41540D",
                "DCE_TO_DTE 0D0A4F4B0D0A")));
    }

    private GuiSessionController controller() {
        return new GuiSessionController(new HeadlessSession("gui-test", BuiltinProfiles.acceptanceSierra(), 12345));
    }
}
