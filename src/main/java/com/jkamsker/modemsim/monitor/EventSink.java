package com.jkamsker.modemsim.monitor;

public interface EventSink {
    void publish(ModemEvent event);
}
