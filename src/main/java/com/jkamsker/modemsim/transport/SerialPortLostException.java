package com.jkamsker.modemsim.transport;

import java.io.IOException;

public final class SerialPortLostException extends IOException {
    public SerialPortLostException(String message) {
        super(message);
    }
}
