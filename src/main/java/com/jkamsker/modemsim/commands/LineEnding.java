package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.parser.RawBytes;

public enum LineEnding {
    CR("\r"),
    CRLF("\r\n"),
    LF("\n");

    private final RawBytes bytes;

    LineEnding(String value) {
        this.bytes = RawBytes.ascii(value);
    }

    public RawBytes bytes() {
        return bytes;
    }

    public static LineEnding from(String value) {
        return value == null || value.isBlank() ? CR : LineEnding.valueOf(value.trim());
    }
}
