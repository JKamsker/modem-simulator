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
        boolean pduContext,
        CommandSpan span,
        List<AtToken> typedTokens
) {
    public ParsedCommand(
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
            boolean pduContext) {
        this(sourceLine, rawText, normalizedName, kind, arguments, tokens, commandIndexInLine, entryMode,
                rawStartOffset, rawEndOffset, quoted, pduContext, new CommandSpan(rawStartOffset, rawEndOffset),
                defaultTokens(rawText, arguments, rawStartOffset, rawEndOffset, quoted));
    }

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

    public ParsedCommand withPduContext(boolean value) {
        return new ParsedCommand(sourceLine, rawText, normalizedName, kind, arguments, tokens, commandIndexInLine,
                entryMode, rawStartOffset, rawEndOffset, quoted, value, span, typedTokens);
    }

    public boolean isExtended(String name) {
        return extendedKind() && normalizedName.equalsIgnoreCase(name);
    }

    public boolean isBasic(String name) {
        return kind == CommandKind.BASIC && normalizedName.equalsIgnoreCase(name);
    }

    private boolean extendedKind() {
        return switch (kind) {
            case EXTENDED_EXEC, EXTENDED_SET, EXTENDED_READ, EXTENDED_TEST -> true;
            default -> false;
        };
    }

    private static List<AtToken> defaultTokens(
            String rawText, String arguments, int rawStartOffset, int rawEndOffset, boolean quoted) {
        if (arguments == null || arguments.isBlank()) {
            return List.of(new AtToken("command", rawText, new CommandSpan(rawStartOffset, rawEndOffset), quoted));
        }
        int argumentStart = Math.max(rawStartOffset, rawEndOffset - arguments.length());
        return List.of(
                new AtToken("command", rawText.substring(0, rawText.length() - arguments.length()),
                        new CommandSpan(rawStartOffset, argumentStart), quoted),
                new AtToken("arguments", arguments, new CommandSpan(argumentStart, rawEndOffset), quoted));
    }
}
