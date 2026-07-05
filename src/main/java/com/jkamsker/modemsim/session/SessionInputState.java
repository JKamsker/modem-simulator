package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.SessionSettings;

final class SessionInputState {
    private RawBytes lastCommandLine = RawBytes.empty();
    private RawBytes commandBuffer = RawBytes.empty();
    private int escapePlusCount;
    private boolean repeatCommand;
    private long lastDteRxNanos = Long.MIN_VALUE;

    long idleBeforeRx(long firstByteNanos) {
        return lastDteRxNanos == Long.MIN_VALUE ? Long.MAX_VALUE : firstByteNanos - lastDteRxNanos;
    }

    void markDteRx(long lastByteNanos) {
        lastDteRxNanos = lastByteNanos;
    }

    void clearPendingEscape() {
        escapePlusCount = 0;
    }

    void clearCommandBuffer() {
        commandBuffer = RawBytes.empty();
    }

    RawBytes effectiveCommandLine(RawBytes bytes) {
        repeatCommand = false;
        if (commandBuffer.isEmpty() && bytes.ascii().equals("A/")) {
            repeatCommand = true;
            return lastCommandLine;
        }
        RawBytes effective = commandBuffer.isEmpty() ? bytes : commandBuffer.append(bytes);
        if (effective.ascii().equals("A/")) {
            repeatCommand = true;
            return lastCommandLine;
        }
        return effective;
    }

    boolean repeatCommand() {
        return repeatCommand;
    }

    void rememberIncomplete(RawBytes effective) {
        commandBuffer = effective;
    }

    void rememberParsed(RawBytes effective, int terminator) {
        commandBuffer = RawBytes.ascii(remainder(effective.ascii(), terminator));
        String complete = completePrefix(effective.ascii(), terminator);
        String lastLine = lastCompleteLine(complete, terminator);
        lastLine = terminator == '\r' && lastLine.startsWith("\n") ? lastLine.substring(1) : lastLine;
        if (!lastLine.equals("A/") && !lastLine.isBlank()) {
            lastCommandLine = RawBytes.ascii(lastLine + (char) terminator);
        }
    }

    private String remainder(String text, int terminator) {
        int index = text.lastIndexOf((char) terminator);
        if (index < 0 || index == text.length() - 1) {
            return "";
        }
        String tail = text.substring(index + 1);
        return terminator == '\r' && tail.startsWith("\n") ? tail.substring(1) : tail;
    }

    private String completePrefix(String text, int terminator) {
        int index = text.lastIndexOf((char) terminator);
        return index < 0 ? text : text.substring(0, index);
    }

    private String lastCompleteLine(String text, int terminator) {
        int index = text.lastIndexOf((char) terminator);
        return index < 0 ? text : text.substring(index + 1);
    }

    boolean onlineEscapeSatisfied(
            RawBytes bytes, long idleBeforeRxNanos, long byteSpanNanos, SessionSettings settings) {
        if (SessionBytes.isEscapeSequence(bytes, settings.s2())
                && guardSatisfied(idleBeforeRxNanos, settings)
                && byteSpanNanos < guardNanos(settings)) {
            escapePlusCount = 0; return true;
        }
        String text = bytes.ascii();
        if (text.length() != 1 || text.charAt(0) != (char) settings.s2()) {
            escapePlusCount = 0; return false;
        }
        if (escapePlusCount == 0) {
            escapePlusCount = guardSatisfied(idleBeforeRxNanos, settings) ? 1 : 0;
            return false;
        }
        if (guardSatisfied(idleBeforeRxNanos, settings)) {
            escapePlusCount = 1;
            return false;
        }
        escapePlusCount++;
        if (escapePlusCount == 3) {
            escapePlusCount = 0;
            return true;
        }
        return false;
    }

    private boolean guardSatisfied(long idleBeforeRxNanos, SessionSettings settings) {
        return idleBeforeRxNanos >= guardNanos(settings);
    }

    private long guardNanos(SessionSettings settings) {
        return settings.s12() * 20_000_000L;
    }
}
