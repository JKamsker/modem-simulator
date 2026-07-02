package com.alegs3.modemsim.parser;

import java.util.List;

public record ParsedCommand(
        RawBytes sourceLine,
        String rawText,
        String normalizedName,
        CommandKind kind,
        String arguments,
        List<String> tokens,
        int commandIndexInLine,
        EntryMode entryMode
) {
    public boolean isExtended(String name) {
        return normalizedName.equalsIgnoreCase(name);
    }

    public boolean isBasic(String name) {
        return normalizedName.equalsIgnoreCase(name);
    }
}
