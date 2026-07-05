package com.jkamsker.modemsim.monitor;

public final class ModemEventCopies {
    private ModemEventCopies() {
    }

    public static ModemEvent withDroppedEventCount(ModemEvent event, long droppedEventCount) {
        return event.withDroppedEventCount(droppedEventCount);
    }

    public static ModemEvent droppedEventsSummary(ModemEvent source, long sequence, long droppedEventCount) {
        return source.droppedEventsSummary(sequence, droppedEventCount);
    }
}
