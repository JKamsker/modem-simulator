package com.jkamsker.modemsim.parser;

final class AtParserText {
    private AtParserText() {
    }

    static String applyBackspace(byte[] bytes, int backspace) {
        StringBuilder builder = new StringBuilder(bytes.length);
        for (byte value : bytes) {
            int unsigned = value & 0xFF;
            if (unsigned == backspace && !builder.isEmpty()) {
                builder.deleteCharAt(builder.length() - 1);
            } else if (unsigned != backspace) {
                builder.append((char) unsigned);
            }
        }
        return builder.toString();
    }

    static int firstSeparator(String slice) {
        int question = slice.indexOf('?');
        int equals = slice.indexOf('=');
        if (question < 0) {
            return equals;
        }
        if (equals < 0) {
            return question;
        }
        return Math.min(question, equals);
    }

    static boolean hasTerminator(String text, int terminator) {
        return text.indexOf((char) terminator) >= 0;
    }

    static int nextLineStart(String text, int terminator, int terminatorIndex) {
        int next = terminatorIndex + 1;
        return terminator == '\r' && next < text.length() && text.charAt(next) == '\n' ? next + 1 : next;
    }

    static boolean hasRemainingLine(String text, int terminator, int terminatorIndex) {
        int next = nextLineStart(text, terminator, terminatorIndex);
        while (next < text.length()) {
            char ch = text.charAt(next++);
            if (ch != '\r' && ch != '\n') {
                return true;
            }
        }
        return false;
    }
}
