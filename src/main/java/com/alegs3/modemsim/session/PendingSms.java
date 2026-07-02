package com.alegs3.modemsim.session;

import com.alegs3.modemsim.parser.ParsedCommand;

public record PendingSms(String destination, Integer pduLength, boolean pduMode) {
    public static PendingSms from(ParsedCommand command, boolean textMode) {
        String args = command.arguments();
        if (textMode) {
            return new PendingSms(unquote(args), null, false);
        }
        return new PendingSms(null, Integer.parseInt(args), true);
    }

    private static String unquote(String value) {
        String trimmed = value == null ? "" : value.trim();
        return trimmed.length() >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")
                ? trimmed.substring(1, trimmed.length() - 1)
                : trimmed;
    }
}
