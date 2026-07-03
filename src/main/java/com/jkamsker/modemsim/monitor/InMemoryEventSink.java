package com.jkamsker.modemsim.monitor;

import java.util.ArrayList;
import java.util.List;

public final class InMemoryEventSink implements EventSink {
    private final List<ModemEvent> events = new ArrayList<>();

    @Override
    public synchronized void publish(ModemEvent event) {
        events.add(event);
    }

    public synchronized List<ModemEvent> events() {
        return List.copyOf(events);
    }

    public synchronized List<ModemEvent> eventsSince(int start) {
        return List.copyOf(events.subList(Math.min(start, events.size()), events.size()));
    }

    public synchronized int eventCount() {
        return events.size();
    }

    public synchronized void clear() {
        events.clear();
    }
}
