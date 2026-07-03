package com.jkamsker.modemsim.parser;

import java.util.List;

public record ParsedCommand(
        RawBytes sourceLine,
        String rawText,
        String normalizedName,
        CommandKind kind,
        String arguments,
        List<String> tokens,
        int commandIndexInLine,
        EntryMode entryMode,
        int rawStartOffset,
        int rawEndOffset,
        boolean quoted,
        boolean pduContext
) {
    public ParsedCommand(
            RawBytes sourceLine,
            String rawText,
            String normalizedName,
            CommandKind kind,
            String arguments,
            List<String> tokens,
            int commandIndexInLine,
            EntryMode entryMode) {
        this(sourceLine, rawText, normalizedName, kind, arguments, tokens, commandIndexInLine, entryMode,
                0, sourceLine.length(), rawText.indexOf('"') >= 0, entryMode == EntryMode.SMS_PDU_ENTRY);
    }

    public boolean isExtended(String name) {
        return normalizedName.equalsIgnoreCase(name);
    }

    public boolean isBasic(String name) {
        return normalizedName.equalsIgnoreCase(name);
    }
}
