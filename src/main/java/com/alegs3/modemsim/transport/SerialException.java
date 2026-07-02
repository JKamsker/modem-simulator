package com.alegs3.modemsim.transport;

import java.io.IOException;

public class SerialException extends IOException {
    public SerialException(String message) {
        super(message);
    }

    public SerialException(String message, Throwable cause) {
        super(message, cause);
    }
}
