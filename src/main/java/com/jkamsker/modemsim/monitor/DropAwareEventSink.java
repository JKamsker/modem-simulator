package com.jkamsker.modemsim.monitor;

public interface DropAwareEventSink extends EventSink {
    boolean publishDroppable(ModemEvent event);
}
