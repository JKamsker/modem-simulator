package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;

import java.nio.charset.StandardCharsets;

final class SessionBytes {
    private SessionBytes() {
    }

    static boolean contains(byte[] bytes, int expected) {
        for (byte value : bytes) {
            if ((value & 0xFF) == expected) {
                return true;
            }
        }
        return false;
    }

    static String entryPayload(byte[] raw) {
        int end = 0;
        while (end < raw.length && raw[end] != 26) {
            end++;
        }
        return new String(raw, 0, end, StandardCharsets.US_ASCII);
    }

    static boolean isEscapeSequence(RawBytes bytes, int terminator) {
        String text = bytes.ascii();
        if (text.equals("+++")) {
            return true;
        }
        int end = text.indexOf((char) terminator);
        return end >= 0 && text.substring(0, end).equals("+++");
    }
}
