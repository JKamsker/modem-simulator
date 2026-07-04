package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.DropAwareEventSink;
import com.jkamsker.modemsim.monitor.ModemEvent;

import java.util.ArrayList;
import java.util.List;

final class DropOnceSink implements DropAwareEventSink {
    private final List<ModemEvent> events = new ArrayList<>();
    private boolean dropNext;

    @Override
    public void publish(ModemEvent event) {
        events.add(event);
    }

    @Override
    public boolean publishDroppable(ModemEvent event) {
        if (dropNext) {
            dropNext = false;
            return false;
        }
        events.add(event);
        return true;
    }

    void dropNextDroppable() {
        dropNext = true;
    }

    List<ModemEvent> events() {
        return List.copyOf(events);
    }

    long nextSequence() {
        return events.stream().mapToLong(ModemEvent::sequence).max().orElse(0) + 1;
    }
}
