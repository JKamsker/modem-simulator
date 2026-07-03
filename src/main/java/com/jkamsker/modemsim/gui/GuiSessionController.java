package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.state.ModemLines;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.NetworkRuntime;
import com.jkamsker.modemsim.state.SignalRuntime;
import com.jkamsker.modemsim.state.SimRuntime;
import com.jkamsker.modemsim.state.SimState;

import java.util.List;

final class GuiSessionController {
    private final HeadlessSession session;

    GuiSessionController(HeadlessSession session) {
        this.session = session;
    }

    SessionResponse rawDteToDce(String text) {
        return session.receive(RawBytes.ascii(unescape(text)));
    }

    SessionResponse parsedCommand(String text) {
        return session.receive(RawBytes.ascii(ensureTerminator(text)));
    }

    SessionResponse rawDceToDte(String text, boolean allowed) {
        if (!allowed) {
            throw new IllegalStateException("Unsafe DCE transmit is disabled");
        }
        return session.injectDce(RawBytes.ascii(unescape(text)), "raw-dce-to-dte");
    }

    SessionResponse urc(String text, boolean allowed) {
        return rawDceToDte(text + "\\r\\n", allowed);
    }

    SessionResponse applyState(GuiStatePatch patch) {
        return session.applyState(patch.apply(session.snapshot()), "state-change");
    }

    SessionResponse fault(String type) {
        return session.applyFault(type);
    }

    ModemState snapshot() {
        return session.snapshot();
    }

    String jsonl(List<ModemEvent> events) {
        return events.stream().map(this::json).reduce((a, b) -> a + "\n" + b).orElse("");
    }

    String transcript(List<ModemEvent> events) {
        return events.stream()
                .filter(event -> !event.rawHex().isBlank())
                .map(event -> event.direction() + " " + event.rawHex())
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");
    }

    private String json(ModemEvent event) {
        return "{\"timestamp\":\"" + event.timestamp()
                + "\",\"monotonicNanos\":" + event.monotonicNanos()
                + ",\"sequence\":" + event.sequence()
                + ",\"sessionId\":\"" + escape(event.sessionId())
                + "\",\"eventType\":\"" + event.eventType()
                + "\",\"direction\":\"" + event.direction()
                + "\",\"rawHex\":\"" + escape(event.rawHex())
                + "\",\"redaction\":{\"applied\":" + event.redaction().applied()
                + ",\"policy\":\"" + event.redaction().policy()
                + "\",\"fields\":" + quoted(event.redaction().fields())
                + ",\"classes\":" + quoted(event.redaction().classes()) + "}}";
    }

    private String quoted(List<String> values) {
        return values.stream()
                .map(value -> "\"" + escape(value) + "\"")
                .reduce((a, b) -> a + "," + b)
                .map(value -> "[" + value + "]")
                .orElse("[]");
    }

    private String ensureTerminator(String value) {
        String unescaped = unescape(value);
        return unescaped.endsWith("\r") ? unescaped : unescaped + "\r";
    }

    private String unescape(String value) {
        return value.replace("\\r", "\r").replace("\\n", "\n").replace("\\u001A", "\u001A");
    }

    private String escape(String value) {
        return String.valueOf(value).replace("\\", "\\\\").replace("\"", "\\\"");
    }

    record GuiStatePatch(
            SimState simState,
            Integer pinRetries,
            Integer pukRetries,
            Integer networkStat,
            Integer cregN,
            String lac,
            String ci,
            Integer act,
            Integer rssi,
            Integer ber,
            Boolean dcd
    ) {
        ModemState apply(ModemState state) {
            ModemState next = state;
            SimRuntime sim = next.sim();
            if (simState != null || pinRetries != null || pukRetries != null) {
                next = next.withSim(new SimRuntime(
                        simState == null ? sim.state() : simState,
                        sim.pinQueryEnabled(),
                        sim.pinRef(),
                        sim.testPin(),
                        pinRetries == null ? sim.pinRetries() : pinRetries,
                        pukRetries == null ? sim.pukRetries() : pukRetries,
                        sim.imsi(),
                        sim.iccid()));
            }
            next = applyNetwork(next);
            if (rssi != null || ber != null) {
                SignalRuntime signal = next.signal();
                next = next.withSignal(new SignalRuntime(
                        rssi == null ? signal.rssi() : rssi,
                        ber == null ? signal.ber() : ber));
            }
            if (dcd != null) {
                ModemLines lines = next.lines();
                next = next.withLines(new ModemLines(
                        lines.dtr(), lines.dsr(), dcd, lines.ri(), lines.rts(), lines.cts()));
            }
            return next;
        }

        private ModemState applyNetwork(ModemState state) {
            NetworkRuntime network = state.network();
            if (network == null || (networkStat == null && cregN == null && lac == null && ci == null && act == null)) {
                return state;
            }
            return state.withNetwork(new NetworkRuntime(
                    cregN == null ? network.cregN() : cregN,
                    networkStat == null ? network.stat() : networkStat,
                    lac == null || lac.isBlank() ? network.lac() : lac,
                    ci == null || ci.isBlank() ? network.ci() : ci,
                    act == null ? network.act() : act,
                    network.rejectCauseType(),
                    network.rejectCause(),
                    network.operator(),
                    network.smsRateLimit(),
                    network.delays()));
        }
    }
}
