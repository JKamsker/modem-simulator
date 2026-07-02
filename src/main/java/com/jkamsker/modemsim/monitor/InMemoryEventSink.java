package com.jkamsker.modemsim.monitor;

import java.util.ArrayList;
import java.util.List;

public final class InMemoryEventSink implements EventSink {
    private final List<ModemEvent> events = new ArrayList<>();

    @Override
    public void publish(ModemEvent event) {
        events.add(event);
    }

    public List<ModemEvent> events() {
        return List.copyOf(events);
    }

    public void clear() {
        events.clear();
    }
}
