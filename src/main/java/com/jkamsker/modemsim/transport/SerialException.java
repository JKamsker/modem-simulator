package com.jkamsker.modemsim.transport;

import java.io.IOException;

public class SerialException extends IOException {
    private final String diagnosticCode;

    public SerialException(String message) {
        this(message, "PORT_BUSY");
    }

    public SerialException(String message, Throwable cause) {
        super(message, cause);
        this.diagnosticCode = "PORT_BUSY";
    }

    public SerialException(String message, String diagnosticCode) {
        super(message);
        this.diagnosticCode = diagnosticCode;
    }

    public SerialException(String message, String diagnosticCode, Throwable cause) {
        super(message, cause);
        this.diagnosticCode = diagnosticCode;
    }

    public String diagnosticCode() {
        return diagnosticCode;
    }
}
