package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.parser.ParsedCommand;

import java.util.Locale;
import java.util.regex.Pattern;

public record MatchSpec(
        String command,
        String mode,
        String rawGlob,
        Pattern rawRegex,
        String type,
        String destinationEquals,
        String destinationContains,
        Pattern destinationRegex,
        String bodyEquals,
        String bodyContains,
        Pattern bodyRegex
) {
    public boolean matchesCommand(ParsedCommand parsed) {
        String raw = parsed.rawText();
        if (rawGlob != null && glob(rawGlob).matcher(raw).matches()) {
            return true;
        }
        if (rawRegex != null && rawRegex.matcher(raw).matches()) {
            return true;
        }
        return command != null && parsed.normalizedName().equalsIgnoreCase(command);
    }

    public boolean matchesSms(String destination, String body) {
        if (!"sms-submit".equals(type)) {
            return false;
        }
        return stringMatches(destination, destinationEquals, destinationContains, destinationRegex)
                && stringMatches(body, bodyEquals, bodyContains, bodyRegex);
    }

    private Pattern glob(String value) {
        StringBuilder regex = new StringBuilder();
        for (char ch : value.toCharArray()) {
            switch (ch) {
                case '*' -> regex.append(".*");
                case '?' -> regex.append('.');
                default -> regex.append(Pattern.quote(String.valueOf(ch)));
            }
        }
        return Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    public String normalizedMode() {
        return mode == null ? null : mode.toLowerCase(Locale.ROOT);
    }

    private boolean stringMatches(String value, String equals, String contains, Pattern regex) {
        if (equals != null && !equals.equals(value)) {
            return false;
        }
        if (contains != null && !value.contains(contains)) {
            return false;
        }
        return regex == null || regex.matcher(value).find();
    }
}
