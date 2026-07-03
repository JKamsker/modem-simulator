package com.jkamsker.modemsim.transport;

import java.io.IOException;

public final class SerialOverflowException extends IOException {
    public SerialOverflowException(String message) {
        super(message);
    }
}
