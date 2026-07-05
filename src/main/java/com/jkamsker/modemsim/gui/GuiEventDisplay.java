package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.state.ModemState;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

final class GuiEventDisplay {
    private final ObservableList<ModemEvent> events;
    private final TextArea output;
    private final Label stateSummary;
    private final GuiSessionController controller;

    GuiEventDisplay(
            ObservableList<ModemEvent> events, TextArea output,
            Label stateSummary, GuiSessionController controller) {
        this.events = events;
        this.output = output;
        this.stateSummary = stateSummary;
        this.controller = controller;
    }

    void handle(SessionResponse response) {
        events.addAll(response.events());
        response.events().stream()
                .filter(event -> event.eventType() == EventType.TX_BYTES)
                .filter(event -> event.direction() == Direction.DCE_TO_DTE)
                .forEach(event -> output.appendText(displayText(event)));
        refreshState();
    }

    void refreshState() {
        ModemState state = controller.snapshot();
        stateSummary.setText("SIM " + state.sim().state() + "  NET "
                + (state.network() == null ? "n/a" : state.network().stat())
                + "  CALL " + state.call().mode() + "  " + controller.runtimeStatus());
    }

    private String displayText(ModemEvent event) {
        if (event.redaction().applied() && "<redacted>".equals(event.rawHex())) {
            return "<redacted>\n";
        }
        return event.textEscaped().replace("\\r", "\\r").replace("\\n", "\\n\n");
    }
}
