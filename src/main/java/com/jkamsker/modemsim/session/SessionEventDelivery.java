package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.DropAwareEventSink;
import com.jkamsker.modemsim.monitor.EventSink;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.monitor.ModemEventCopies;

import java.util.EnumSet;
import java.util.function.LongSupplier;

final class SessionEventDelivery {
    private static final EnumSet<EventType> REQUIRED = EnumSet.of(
            EventType.SESSION_START,
            EventType.SESSION_STOP,
            EventType.STATE_CHANGE,
            EventType.FAULT_TRIGGERED,
            EventType.INJECTION,
            EventType.POLICY_DENIED,
            EventType.VALIDATION_ERROR,
            EventType.AUDIT_FAILURE,
            EventType.REPLAY_MARKER,
            EventType.PORT_OPEN_FAILED,
            EventType.PORT_LOST,
            EventType.RX_OVERFLOW,
            EventType.TX_OVERFLOW,
            EventType.DROPPED_EVENTS);

    private final EventSink sink;
    private final LongSupplier nextSequence;
    private long pendingDropped;

    SessionEventDelivery(EventSink sink, LongSupplier nextSequence) {
        this.sink = sink;
        this.nextSequence = nextSequence;
    }

    void publish(ModemEvent event) {
        if (event.eventType() == EventType.DROPPED_EVENTS) {
            sink.publish(event);
            return;
        }
        ModemEvent candidate = carryPendingDrops(event);
        if (REQUIRED.contains(event.eventType()) || !(sink instanceof DropAwareEventSink dropAware)) {
            sink.publish(candidate);
            pendingDropped = 0;
            return;
        }
        if (dropAware.publishDroppable(candidate)) {
            pendingDropped = 0;
            return;
        }
        pendingDropped++;
        sink.publish(ModemEventCopies.droppedEventsSummary(event, nextSequence.getAsLong(), pendingDropped));
    }

    private ModemEvent carryPendingDrops(ModemEvent event) {
        return pendingDropped == 0 ? event : ModemEventCopies.withDroppedEventCount(event, pendingDropped);
    }
}
