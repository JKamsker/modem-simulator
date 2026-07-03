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
        int terminator = firstSmsTerminator(raw);
        return entryPayload(raw, terminator < 0 ? raw.length : terminator);
    }

    static String entryPayload(byte[] raw, int end) {
        return new String(raw, 0, Math.max(0, Math.min(end, raw.length)), StandardCharsets.US_ASCII);
    }

    static SmsEntry smsEntry(RawBytes pending, RawBytes bytes) {
        int previousLength = pending.length();
        RawBytes buffered = pending.append(bytes);
        byte[] raw = buffered.toByteArray();
        int terminator = firstSmsTerminator(raw);
        if (terminator < 0) {
            return new SmsEntry(buffered, bytes, RawBytes.empty(), -1, false);
        }
        int split = Math.max(0, Math.min(bytes.length(), terminator - previousLength + 1));
        return new SmsEntry(buffered, bytes.slice(0, split), bytes.slice(split, bytes.length()),
                terminator, raw[terminator] == 27);
    }

    private static int firstSmsTerminator(byte[] raw) {
        for (int i = 0; i < raw.length; i++) {
            int value = raw[i] & 0xFF;
            if (value == 26 || value == 27) {
                return i;
            }
        }
        return -1;
    }

    static boolean isEscapeSequence(RawBytes bytes, int terminator, int escapeChar) {
        String text = bytes.ascii();
        String escape = String.valueOf((char) escapeChar).repeat(3);
        return text.equals(escape);
    }

    record SmsEntry(RawBytes buffered, RawBytes rxBytes, RawBytes tailBytes, int terminator, boolean abort) {
        boolean complete() {
            return terminator >= 0;
        }
    }
}
