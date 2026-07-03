package com.jkamsker.modemsim.parser;

final class AtExtendedSyntax {
    private AtExtendedSyntax() {
    }

    static void rejectExtraFormSeparator(String arguments) {
        boolean quoted = false;
        for (int i = 0; i < arguments.length(); i++) {
            char ch = arguments.charAt(i);
            if (ch == '"') {
                quoted = !quoted;
            } else if (!quoted && (ch == '?' || ch == '=')) {
                throw new AtParseException("extra extended command separator");
            }
        }
    }
}
