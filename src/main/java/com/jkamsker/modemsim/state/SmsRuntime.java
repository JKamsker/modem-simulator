package com.jkamsker.modemsim.state;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public record SmsRuntime(
        boolean textMode,
        String smsc,
        SmsStorage storage,
        int nextMessageReference,
        Map<Integer, SmsMessage> messages
) {
    public SmsRuntime {
        messages = Map.copyOf(messages);
    }

    public static SmsRuntime defaults() {
        return new SmsRuntime(true, "+491710760000", SmsStorage.ME, 42, Map.of());
    }

    public SmsRuntime withTextMode(boolean value) {
        return new SmsRuntime(value, smsc, storage, nextMessageReference, messages);
    }

    public SmsRuntime withSmsc(String value) {
        return new SmsRuntime(textMode, value, storage, nextMessageReference, messages);
    }

    public SmsRuntime withStorage(SmsStorage value) {
        return new SmsRuntime(textMode, smsc, value, nextMessageReference, messages);
    }

    public SmsRuntime incrementReference() {
        return new SmsRuntime(textMode, smsc, storage, nextMessageReference + 1, messages);
    }

    public SmsRuntime storeOutbound(String recipient, String text, String pdu) {
        int index = messages.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
        Map<Integer, SmsMessage> next = new LinkedHashMap<>(messages);
        next.put(index, new SmsMessage(index, "STO SENT", null, recipient, OffsetDateTime.now(), text, pdu));
        return new SmsRuntime(textMode, smsc, storage, nextMessageReference + 1, next);
    }

    public SmsRuntime delete(int index) {
        Map<Integer, SmsMessage> next = new LinkedHashMap<>(messages);
        next.remove(index);
        return new SmsRuntime(textMode, smsc, storage, nextMessageReference, next);
    }
}
