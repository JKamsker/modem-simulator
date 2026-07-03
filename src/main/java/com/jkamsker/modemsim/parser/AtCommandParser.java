package com.jkamsker.modemsim.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class AtCommandParser {
    private final int backspace;
    private final int terminator;

    public AtCommandParser(int backspace) {
        this(backspace, '\r');
    }

    public AtCommandParser(int backspace, int terminator) {
        this.backspace = backspace;
        this.terminator = terminator;
    }

    public List<ParsedCommand> parse(RawBytes source, EntryMode mode) {
        String edited = applyBackspace(source.toByteArray());
        if (!hasTerminator(edited)) {
            if (edited.equals("A/")) {
                return List.of(special(source, edited, "A/", CommandKind.SPECIAL_REPEAT, mode));
            }
            if (edited.equals("+++")) {
                return List.of(special(source, edited, "+++", CommandKind.SPECIAL_ESCAPE, mode));
            }
            return List.of();
        }
        List<ParsedCommand> commands = new ArrayList<>();
        int start = 0;
        int terminatorIndex;
        while ((terminatorIndex = edited.indexOf((char) terminator, start)) >= 0) {
            String line = edited.substring(start, terminatorIndex);
            if (!line.isEmpty()) {
                commands.addAll(parseLine(source, line, mode));
            }
            start = nextLineStart(edited, terminatorIndex);
        }
        return commands;
    }

    private int nextLineStart(String text, int terminatorIndex) {
        int next = terminatorIndex + 1;
        return terminator == '\r' && next < text.length() && text.charAt(next) == '\n' ? next + 1 : next;
    }

    private List<ParsedCommand> parseLine(RawBytes source, String line, EntryMode mode) {
        if (line.equals("A/")) {
            return List.of(special(source, line, "A/", CommandKind.SPECIAL_REPEAT, mode));
        }
        if (line.equals("+++")) {
            return List.of(special(source, line, "+++", CommandKind.SPECIAL_ESCAPE, mode));
        }
        if (!line.regionMatches(true, 0, "AT", 0, 2)) {
            return List.of(command(RawBytes.ascii(line), line, "PARSE_ERROR", CommandKind.BASIC, "", 0, mode));
        }
        return parseBody(source, line, line.substring(2), mode);
    }

    private static ParsedCommand special(
            RawBytes source, String raw, String name, CommandKind kind, EntryMode mode) {
        return new ParsedCommand(source, raw, name, kind, "", List.of(name), 0, mode);
    }

    private List<ParsedCommand> parseBody(RawBytes source, String rawLine, String body, EntryMode mode) {
        if (body.isEmpty()) {
            return List.of(command(source, rawLine, "AT", CommandKind.BASIC, "", 0, mode));
        }
        List<ParsedCommand> commands = new ArrayList<>();
        int index = 0;
        int position = 0;
        while (position < body.length()) {
            if (body.charAt(position) == ';') {
                position++;
                continue;
            }
            Slice slice = nextSlice(body, position);
            RawBytes commandSource = RawBytes.ascii((index == 0 ? "AT" : "") + slice.text());
            commands.add(toCommand(commandSource, rawLine, slice.text(), index++, mode));
            position = slice.next();
        }
        return commands;
    }

    private Slice nextSlice(String body, int start) {
        char first = body.charAt(start);
        if (isExtendedPrefix(first)) {
            return extendedSlice(body, start);
        }
        if (first == '&' && start + 1 < body.length()) {
            int end = start + 2;
            if (end < body.length() && Character.isDigit(body.charAt(end))) {
                end++;
            }
            return new Slice(body.substring(start, end), end);
        }
        if (first == 'S' || first == 's') {
            return sRegisterSlice(body, start);
        }
        if (first == 'D' || first == 'd') {
            return new Slice(body.substring(start), body.length());
        }
        int end = start + 1;
        if (end < body.length() && Character.isDigit(body.charAt(end))) {
            end++;
        }
        return new Slice(body.substring(start, end), end);
    }

    private Slice extendedSlice(String body, int start) {
        boolean quoted = false;
        for (int i = start; i < body.length(); i++) {
            char ch = body.charAt(i);
            if (ch == '"') {
                quoted = !quoted;
            } else if (ch == ';' && !quoted) {
                return new Slice(body.substring(start, i), i + 1);
            }
        }
        return new Slice(body.substring(start), body.length());
    }

    private Slice sRegisterSlice(String body, int start) {
        int end = start + 1;
        while (end < body.length() && Character.isDigit(body.charAt(end))) {
            end++;
        }
        if (end < body.length() && body.charAt(end) == '?') {
            end++;
        } else if (end < body.length() && body.charAt(end) == '=') {
            end++;
            while (end < body.length() && Character.isDigit(body.charAt(end))) {
                end++;
            }
        }
        return new Slice(body.substring(start, end), end);
    }

    private ParsedCommand toCommand(
            RawBytes source, String rawLine, String slice, int index, EntryMode mode) {
        if (slice.regionMatches(true, 0, "S", 0, 1)) {
            return sRegisterCommand(source, rawLine, slice, index, mode);
        }
        if (isExtendedPrefix(slice.charAt(0))) {
            return extendedCommand(source, rawLine, slice, index, mode);
        }
        return basicCommand(source, rawLine, slice, index, mode);
    }

    private ParsedCommand extendedCommand(
            RawBytes source, String rawLine, String slice, int index, EntryMode mode) {
        int separator = firstSeparator(slice);
        String name = (separator < 0 ? slice : slice.substring(0, separator)).toUpperCase(Locale.ROOT);
        String args = separator < 0 ? "" : slice.substring(separator + 1);
        CommandKind kind = switch (separator < 0 ? '\0' : slice.charAt(separator)) {
            case '?' -> CommandKind.EXTENDED_READ;
            case '=' -> args.equals("?") ? CommandKind.EXTENDED_TEST : CommandKind.EXTENDED_SET;
            default -> CommandKind.EXTENDED_EXEC;
        };
        return command(source, rawLine, name, kind, args, index, mode);
    }

    private ParsedCommand sRegisterCommand(
            RawBytes source, String rawLine, String slice, int index, EntryMode mode) {
        String upper = slice.toUpperCase(Locale.ROOT);
        int question = upper.indexOf('?');
        int equals = upper.indexOf('=');
        int end = question >= 0 ? question : equals >= 0 ? equals : upper.length();
        String name = upper.substring(0, end);
        String args = equals >= 0 ? upper.substring(equals + 1) : "";
        CommandKind kind = equals >= 0 ? CommandKind.S_REGISTER_WRITE : CommandKind.S_REGISTER_READ;
        return command(source, rawLine, name, kind, args, index, mode);
    }

    private ParsedCommand basicCommand(
            RawBytes source, String rawLine, String slice, int index, EntryMode mode) {
        String upper = slice.toUpperCase(Locale.ROOT);
        String name = switch (upper.charAt(0)) {
            case 'E' -> "ATE";
            case 'Q' -> "ATQ";
            case 'V' -> "ATV";
            case 'D' -> "ATD";
            case 'H' -> "ATH";
            case 'O' -> "ATO";
            case 'Z' -> "ATZ";
            case 'I' -> "ATI";
            default -> upper.startsWith("&") ? "AT" + upper.substring(0, Math.min(2, upper.length()))
                    : "AT" + upper.charAt(0);
        };
        String args = upper.startsWith("&") ? upper.substring(2) : upper.substring(1);
        return command(source, rawLine, name, CommandKind.BASIC, args, index, mode);
    }

    private ParsedCommand command(
            RawBytes source, String raw, String name, CommandKind kind, String args, int index, EntryMode mode) {
        return new ParsedCommand(source, raw, name, kind, args, List.of(name, args), index, mode);
    }

    private int firstSeparator(String slice) {
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

    private boolean isExtendedPrefix(char ch) {
        return ch == '+' || ch == '%' || ch == '#' || ch == '!';
    }

    private boolean hasTerminator(String text) {
        return text.indexOf((char) terminator) >= 0;
    }

    private String applyBackspace(byte[] bytes) {
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

    private record Slice(String text, int next) {
    }
}
