package com.jkamsker.modemsim.state;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public record SmsRuntime(
        boolean textMode,
        String smsc,
        String cnmi,
        SmsStorage storage,
        SmsStorage writeStorage,
        SmsStorage receiveStorage,
        int nextMessageReference,
        Map<Integer, SmsMessage> messages
) {
    public SmsRuntime {
        Map<Integer, SmsMessage> ordered = new LinkedHashMap<>();
        messages.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> ordered.put(entry.getKey(), entry.getValue()));
        messages = Collections.unmodifiableMap(ordered);
    }

    public static SmsRuntime defaults() {
        return new SmsRuntime(true, "+491710760000", "0,0,0,0,0", SmsStorage.ME, 42, Map.of());
    }

    public SmsRuntime(
            boolean textMode, String smsc, String cnmi, SmsStorage storage,
            int nextMessageReference, Map<Integer, SmsMessage> messages) {
        this(textMode, smsc, cnmi, storage, storage, storage, nextMessageReference, messages);
    }

    public SmsRuntime withTextMode(boolean value) {
        return copy(value, smsc, cnmi, storage, writeStorage, receiveStorage, nextMessageReference, messages);
    }

    public SmsRuntime withSmsc(String value) {
        return copy(textMode, value, cnmi, storage, writeStorage, receiveStorage, nextMessageReference, messages);
    }

    public SmsRuntime withCnmi(String value) {
        return copy(textMode, smsc, value, storage, writeStorage, receiveStorage, nextMessageReference, messages);
    }

    public SmsRuntime withStorage(SmsStorage value) {
        return withStorages(value, value, value);
    }

    public SmsRuntime withStorages(SmsStorage read, SmsStorage write, SmsStorage receive) {
        return copy(textMode, smsc, cnmi, read, write, receive, nextMessageReference, messages);
    }

    public SmsRuntime incrementReference() {
        return copy(textMode, smsc, cnmi, storage, writeStorage, receiveStorage, nextMessageReference + 1, messages);
    }

    public SmsRuntime storeOutbound(String recipient, String text, String pdu) {
        return storeOutbound(recipient, text, pdu, OffsetDateTime.parse("2026-01-01T00:00:00Z"));
    }

    public SmsRuntime storeOutbound(String recipient, String text, String pdu, OffsetDateTime timestamp) {
        int index = messages.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
        Map<Integer, SmsMessage> next = new LinkedHashMap<>(messages);
        next.put(index, new SmsMessage(index, writeStorage, "STO SENT", null, recipient, timestamp, text, pdu));
        return copy(textMode, smsc, cnmi, storage, writeStorage, receiveStorage, nextMessageReference + 1, next);
    }

    public SmsRuntime delete(int index) {
        Map<Integer, SmsMessage> next = new LinkedHashMap<>(messages);
        next.remove(index);
        return copy(textMode, smsc, cnmi, storage, writeStorage, receiveStorage, nextMessageReference, next);
    }

    public Map<Integer, SmsMessage> messagesInSelectedStorage() {
        return messagesIn(storage);
    }

    public Map<Integer, SmsMessage> messagesIn(SmsStorage selected) {
        Map<Integer, SmsMessage> filtered = new LinkedHashMap<>();
        messages.forEach((index, message) -> {
            if (message.storage() == selected) {
                filtered.put(index, message);
            }
        });
        return filtered;
    }

    public int used(SmsStorage selected) {
        return messagesIn(selected).size();
    }

    public int capacity(SmsStorage selected) {
        return switch (selected) {
            case ME -> 50;
            case SM -> 20;
            case MT -> 70;
        };
    }

    public boolean selectedStorageFull() {
        return used(writeStorage) >= capacity(writeStorage);
    }

    private SmsRuntime copy(
            boolean textMode, String smsc, String cnmi, SmsStorage read, SmsStorage write,
            SmsStorage receive, int nextReference, Map<Integer, SmsMessage> messages) {
        return new SmsRuntime(textMode, smsc, cnmi, read, write, receive, nextReference, messages);
    }
}
