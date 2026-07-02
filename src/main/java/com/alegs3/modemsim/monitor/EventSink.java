package com.alegs3.modemsim.monitor;

public interface EventSink {
    void publish(ModemEvent event);
}
