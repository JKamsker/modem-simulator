package com.jkamsker.modemsim.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class AtCommandParser {
    private final int backspace;
    private final int terminator;
    private final List<String> extendedPrefixes;

    public AtCommandParser(int backspace) {
        this(backspace, '\r');
    }

    public AtCommandParser(int backspace, int terminator) {
        this(backspace, terminator, List.of("+", "%", "#", "!"));
    }

    public AtCommandParser(int backspace, int terminator, List<String> extendedPrefixes) {
        this.backspace = backspace;
        this.terminator = terminator;
        this.extendedPrefixes = extendedPrefixes.stream()
                .sorted((left, right) -> Integer.compare(right.length(), left.length()))
                .toList();
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
            return List.of(special(RawBytes.ascii(line), line, "A/", CommandKind.SPECIAL_REPEAT, mode));
        }
        if (line.equals("+++")) {
            return List.of(special(RawBytes.ascii(line), line, "+++", CommandKind.SPECIAL_ESCAPE, mode));
        }
        if (!line.regionMatches(true, 0, "AT", 0, 2)) {
            throw new AtParseException("line does not start with AT");
        }
        return parseBody(source, line, line.substring(2), mode);
    }

    private static ParsedCommand special(
            RawBytes source, String raw, String name, CommandKind kind, EntryMode mode) {
        return new ParsedCommand(source, raw, name, kind, "", List.of(name), 0, mode);
    }

    private List<ParsedCommand> parseBody(RawBytes source, String rawLine, String body, EntryMode mode) {
        RawBytes lineSource = RawBytes.ascii(rawLine);
        if (body.isEmpty()) {
            return List.of(command(lineSource, rawLine, "AT", CommandKind.BASIC, "", 0, mode));
        }
        List<ParsedCommand> commands = new ArrayList<>();
        int index = 0;
        int position = 0;
        while (position < body.length()) {
            if (body.charAt(position) == ';') {
                throw new AtParseException("stray command separator");
            }
            Slice slice = nextSlice(body, position);
            String rawText = (index == 0 ? "AT" : "") + slice.text();
            commands.add(toCommand(lineSource, rawText, slice.text(), index++, mode,
                    2 + position, 2 + slice.end(), slice.quoted()));
            position = slice.next();
        }
        return commands;
    }

    private Slice nextSlice(String body, int start) {
        char first = body.charAt(start);
        if (startsWithExtendedPrefix(body, start)) {
            return extendedSlice(body, start);
        }
        if (first == '&' && start + 1 < body.length()) {
            int end = start + 2;
            if (end < body.length() && Character.isDigit(body.charAt(end))) {
                end++;
            }
            return new Slice(body.substring(start, end), end, end, false);
        }
        if (first == 'S' || first == 's') {
            return sRegisterSlice(body, start);
        }
        if (first == 'D' || first == 'd') {
            return new Slice(body.substring(start), body.length(), body.length(), false);
        }
        int end = start + 1;
        if (end < body.length() && Character.isDigit(body.charAt(end))) {
            end++;
        }
        return new Slice(body.substring(start, end), end, end, false);
    }

    private Slice extendedSlice(String body, int start) {
        boolean quoted = false;
        boolean sawQuote = false;
        for (int i = start; i < body.length(); i++) {
            char ch = body.charAt(i);
            if (ch == '"') {
                sawQuote = true;
                quoted = !quoted;
            } else if (ch == ';' && !quoted) {
                return new Slice(body.substring(start, i), i, i + 1, sawQuote);
            }
        }
        if (quoted) {
            throw new AtParseException("unterminated quoted argument");
        }
        return new Slice(body.substring(start), body.length(), body.length(), sawQuote);
    }

    private Slice sRegisterSlice(String body, int start) {
        int end = start + 1;
        while (end < body.length() && Character.isDigit(body.charAt(end))) {
            end++;
        }
        if (end == start + 1) {
            throw new AtParseException("missing S-register number");
        }
        if (end < body.length() && body.charAt(end) == '?') {
            end++;
        } else if (end < body.length() && body.charAt(end) == '=') {
            end++;
            int valueStart = end;
            while (end < body.length() && Character.isDigit(body.charAt(end))) {
                end++;
            }
            if (end == valueStart) {
                throw new AtParseException("missing S-register value");
            }
        }
        return new Slice(body.substring(start, end), end, end, false);
    }

    private ParsedCommand toCommand(
            RawBytes source, String rawLine, String slice, int index, EntryMode mode,
            int rawStart, int rawEnd, boolean quoted) {
        if (slice.regionMatches(true, 0, "S", 0, 1)) {
            return sRegisterCommand(source, rawLine, slice, index, mode, rawStart, rawEnd, quoted);
        }
        if (startsWithExtendedPrefix(slice, 0)) {
            return extendedCommand(source, rawLine, slice, index, mode, rawStart, rawEnd, quoted);
        }
        return basicCommand(source, rawLine, slice, index, mode, rawStart, rawEnd, quoted);
    }

    private ParsedCommand extendedCommand(
            RawBytes source, String rawLine, String slice, int index, EntryMode mode,
            int rawStart, int rawEnd, boolean quoted) {
        int separator = firstSeparator(slice);
        String name = (separator < 0 ? slice : slice.substring(0, separator)).toUpperCase(Locale.ROOT);
        if (name.length() == extendedPrefixLength(name)) {
            throw new AtParseException("missing extended command name");
        }
        String args = separator < 0 ? "" : slice.substring(separator + 1);
        if (!(separator >= 0 && slice.charAt(separator) == '=' && args.equals("?"))) {
            AtExtendedSyntax.rejectExtraFormSeparator(args);
        }
        CommandKind kind = switch (separator < 0 ? '\0' : slice.charAt(separator)) {
            case '?' -> CommandKind.EXTENDED_READ;
            case '=' -> args.equals("?") ? CommandKind.EXTENDED_TEST : CommandKind.EXTENDED_SET;
            default -> CommandKind.EXTENDED_EXEC;
        };
        return command(source, rawLine, name, kind, args, index, mode, rawStart, rawEnd, quoted);
    }

    private ParsedCommand sRegisterCommand(
            RawBytes source, String rawLine, String slice, int index, EntryMode mode,
            int rawStart, int rawEnd, boolean quoted) {
        String upper = slice.toUpperCase(Locale.ROOT);
        int question = upper.indexOf('?');
        int equals = upper.indexOf('=');
        int end = question >= 0 ? question : equals >= 0 ? equals : upper.length();
        String name = upper.substring(0, end);
        String args = equals >= 0 ? upper.substring(equals + 1) : "";
        CommandKind kind = equals >= 0 ? CommandKind.S_REGISTER_WRITE : CommandKind.S_REGISTER_READ;
        return command(source, rawLine, name, kind, args, index, mode, rawStart, rawEnd, quoted);
    }

    private ParsedCommand basicCommand(
            RawBytes source, String rawLine, String slice, int index, EntryMode mode,
            int rawStart, int rawEnd, boolean quoted) {
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
        validateBasicArguments(name, args);
        return command(source, rawLine, name, CommandKind.BASIC, args, index, mode, rawStart, rawEnd, quoted);
    }

    private void validateBasicArguments(String name, String args) {
        if (List.of("ATE", "ATQ", "ATV").contains(name) && !args.matches("[01]?")) {
            throw new AtParseException("invalid basic flag argument");
        }
        if (name.equals("AT&C") && !args.matches("[01]?")) {
            throw new AtParseException("invalid &C argument");
        }
        if (name.equals("AT&D") && !args.matches("[0-3]?")) {
            throw new AtParseException("invalid &D argument");
        }
    }

    private ParsedCommand command(
            RawBytes source, String raw, String name, CommandKind kind, String args, int index, EntryMode mode) {
        return command(source, raw, name, kind, args, index, mode, 0, source.length(), raw.indexOf('"') >= 0);
    }

    private ParsedCommand command(
            RawBytes source, String raw, String name, CommandKind kind, String args, int index, EntryMode mode,
            int rawStart, int rawEnd, boolean quoted) {
        return new ParsedCommand(source, raw, name, kind, args, List.of(name, args), index, mode,
                rawStart, rawEnd, quoted, mode == EntryMode.SMS_PDU_ENTRY);
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

    private boolean startsWithExtendedPrefix(String text, int start) {
        return extendedPrefixes.stream().anyMatch(prefix -> text.startsWith(prefix, start));
    }

    private int extendedPrefixLength(String name) {
        return extendedPrefixes.stream()
                .filter(name::startsWith)
                .mapToInt(String::length)
                .findFirst()
                .orElse(0);
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

    private record Slice(String text, int end, int next, boolean quoted) {
    }
}
