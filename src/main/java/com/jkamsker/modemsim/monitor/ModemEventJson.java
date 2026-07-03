package com.jkamsker.modemsim.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.SmsMessage;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ModemEventJson {
    private static final ObjectMapper JSON = new ObjectMapper();

    private ModemEventJson() {
    }

    public static String toJsonLines(List<ModemEvent> events) {
        return events.stream().map(ModemEventJson::toJson).reduce((a, b) -> a + "\n" + b).orElse("");
    }

    public static String toJson(ModemEvent event) {
        try {
            return JSON.writeValueAsString(toMap(event));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot serialize event", e);
        }
    }

    public static Map<String, Object> toMap(ModemEvent event) {
        Map<String, Object> json = new LinkedHashMap<>();
        put(json, "timestamp", event.timestamp().toString());
        put(json, "monotonicNanos", event.monotonicNanos());
        put(json, "sequence", event.sequence());
        put(json, "sessionId", event.sessionId());
        put(json, "eventType", event.eventType().name());
        put(json, "source", source(event));
        put(json, "diagnosticCode", diagnosticCode(event));
        put(json, "direction", event.direction().name());
        put(json, "rawHex", event.rawHex());
        put(json, "textEscaped", event.textEscaped());
        put(json, "parsedCommand", event.parsedCommand());
        put(json, "profile", event.profile());
        put(json, "port", event.port());
        put(json, "portRole", event.portRole());
        put(json, "profileHash", event.profileHash());
        put(json, "configHash", event.configHash());
        put(json, "macroHash", event.macroHash());
        put(json, "initialStateHash", event.initialStateHash());
        put(json, "sessionSeed", event.sessionSeed());
        put(json, "clockMode", event.clockMode());
        put(json, "macroId", event.macroId());
        put(json, "latencyMs", event.latencyMs());
        put(json, "injectionType", event.injectionType());
        put(json, "droppedEventCount", event.droppedEventCount());
        put(json, "replayDivergent", event.replayDivergent());
        put(json, "handler", event.handler());
        put(json, "result", event.result());
        put(json, "scheduler", event.scheduler());
        put(json, "stateBefore", state(event.stateBefore()));
        put(json, "stateAfter", state(event.stateAfter()));
        put(json, "redaction", redaction(event.redaction()));
        return json;
    }

    private static String source(ModemEvent event) {
        return switch (event.eventType()) {
            case RX_BYTES -> "rx";
            case TX_BYTES -> "tx";
            case SCHEDULER_ENQUEUE, SCHEDULER_EMIT -> "scheduler";
            case PORT_OPEN_FAILED, PORT_LOST, RX_OVERFLOW, TX_OVERFLOW -> "transport";
            case MACRO_DECISION, MACRO_EVENT -> "macro";
            case REPLAY_MARKER -> "replay";
            case INJECTION -> "replay".equals(event.injectionType()) ? "replay" : "gui";
            case POLICY_DENIED, VALIDATION_ERROR -> "internal";
            default -> "internal";
        };
    }

    private static String diagnosticCode(ModemEvent event) {
        return switch (event.eventType()) {
            case PORT_OPEN_FAILED -> openFailureDiagnostic(event.result());
            case PORT_LOST -> "PORT_LOST";
            case RX_OVERFLOW -> "RX_OVERFLOW";
            case TX_OVERFLOW -> "TX_OVERFLOW";
            case AUDIT_FAILURE -> "AUDIT_WRITE_FAILED";
            default -> null;
        };
    }

    private static String openFailureDiagnostic(String result) {
        if (result == null) {
            return "PORT_BUSY";
        }
        String normalized = result.toLowerCase(java.util.Locale.ROOT);
        if (normalized.contains("port_not_found")) {
            return "PORT_NOT_FOUND";
        }
        if (normalized.contains("unsupported_parameters")) {
            return "UNSUPPORTED_PARAMETERS";
        }
        if (normalized.contains("port_busy")) {
            return "PORT_BUSY";
        }
        if (normalized.contains("not") && normalized.contains("found")) {
            return "PORT_NOT_FOUND";
        }
        if (normalized.contains("unsupported")) {
            return "UNSUPPORTED_PARAMETERS";
        }
        return "PORT_BUSY";
    }

    private static Map<String, Object> state(ModemState state) {
        if (state == null) {
            return null;
        }
        Map<String, Object> json = new LinkedHashMap<>();
        put(json, "sim", sim(state));
        put(json, "network", network(state));
        put(json, "signal", Map.of("rssi", state.signal().rssi(), "ber", state.signal().ber()));
        put(json, "sms", sms(state));
        put(json, "call", Map.of(
                "mode", state.call().mode().name(),
                "carrier", state.call().carrier(),
                "dialedNumber", value(state.call().dialedNumber()),
                "incomingNumber", value(state.call().incomingNumber())));
        put(json, "modem", Map.of(
                "lifecycle", state.modem().lifecycle().name(),
                "freezeMode", state.modem().freezeMode().name(),
                "bootDelayMs", state.modem().bootDelayMs()));
        put(json, "modemLines", Map.of(
                "dtr", state.lines().dtr(), "dsr", state.lines().dsr(), "dcd", state.lines().dcd(),
                "ri", state.lines().ri(), "rts", state.lines().rts(), "cts", state.lines().cts()));
        put(json, "settings", settings(state));
        put(json, "version", state.version());
        return json;
    }

    private static Map<String, Object> sim(ModemState state) {
        Map<String, Object> json = new LinkedHashMap<>();
        put(json, "state", state.sim().state().name());
        put(json, "pinQueryEnabled", state.sim().pinQueryEnabled());
        put(json, "pinRef", state.sim().pinRef());
        put(json, "testPin", state.sim().testPin());
        put(json, "pukRef", state.sim().pukRef());
        put(json, "testPuk", state.sim().testPuk());
        put(json, "pinRetries", state.sim().pinRetries());
        put(json, "pukRetries", state.sim().pukRetries());
        put(json, "imsi", state.sim().imsi());
        put(json, "iccid", state.sim().iccid());
        return json;
    }

    private static Map<String, Object> network(ModemState state) {
        if (state.network() == null) {
            return null;
        }
        Map<String, Object> json = new LinkedHashMap<>();
        put(json, "cregN", state.network().cregN());
        put(json, "stat", state.network().stat());
        put(json, "lac", state.network().lac());
        put(json, "ci", state.network().ci());
        put(json, "act", state.network().act());
        put(json, "rejectCauseType", state.network().rejectCauseType());
        put(json, "rejectCause", state.network().rejectCause());
        put(json, "operator", state.network().operator());
        put(json, "smsRateLimit", state.network().smsRateLimit());
        put(json, "delays", state.network().delays());
        return json;
    }

    private static Map<String, Object> sms(ModemState state) {
        Map<String, Object> json = new LinkedHashMap<>();
        put(json, "textMode", state.sms().textMode());
        put(json, "smsc", state.sms().smsc());
        put(json, "cnmi", state.sms().cnmi());
        put(json, "storage", state.sms().storage().name());
        put(json, "writeStorage", state.sms().writeStorage().name());
        put(json, "receiveStorage", state.sms().receiveStorage().name());
        put(json, "nextMessageReference", state.sms().nextMessageReference());
        put(json, "messages", messages(state.sms().messages()));
        return json;
    }

    private static Map<String, Object> messages(Map<Integer, SmsMessage> messages) {
        Map<String, Object> json = new LinkedHashMap<>();
        for (SmsMessage message : messages.values()) {
            Map<String, Object> entry = new LinkedHashMap<>();
            put(entry, "index", message.index());
            put(entry, "storage", message.storage().name());
            put(entry, "status", message.status());
            put(entry, "sender", message.sender());
            put(entry, "recipient", message.recipient());
            put(entry, "timestamp", message.timestamp() == null ? null : message.timestamp().toString());
            put(entry, "text", message.text());
            put(entry, "pdu", message.pdu());
            json.put(String.valueOf(message.index()), entry);
        }
        return json;
    }

    private static Map<String, Object> settings(ModemState state) {
        return Map.ofEntries(
                Map.entry("echo", state.settings().echo()),
                Map.entry("quiet", state.settings().quiet()),
                Map.entry("verbose", state.settings().verbose()),
                Map.entry("cmee", state.settings().cmee()),
                Map.entry("s3", state.settings().s3()),
                Map.entry("s4", state.settings().s4()),
                Map.entry("s5", state.settings().s5()),
                Map.entry("s7", state.settings().s7()),
                Map.entry("s12", state.settings().s12()),
                Map.entry("ampD", state.settings().ampD()),
                Map.entry("ampC", state.settings().ampC()));
    }

    private static Map<String, Object> redaction(RedactionInfo redaction) {
        Map<String, Object> json = new LinkedHashMap<>();
        put(json, "applied", redaction.applied());
        put(json, "policy", redaction.policy());
        put(json, "fields", redaction.fields());
        put(json, "classes", redaction.classes());
        return json;
    }

    private static Object value(Object value) {
        return value == null ? "" : value;
    }

    private static void put(Map<String, Object> map, String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
    }
}
