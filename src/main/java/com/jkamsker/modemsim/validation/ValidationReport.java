package com.jkamsker.modemsim.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class ValidationReport {
    private static final Pattern SENSITIVE_ATTRIBUTE = Pattern.compile(
            "(?i)\\b(pin|puk|imsi|iccid|imei|msisdn|smsc|destination|destinationRegex|body|bodyRegex|rawRegex|regex)\\s*=\\s*\"[^\"]*\"");
    private static final Pattern LONG_IDENTIFIER = Pattern.compile("(?<!\\d)\\d{14,22}(?!\\d)");
    private static final Pattern MSISDN = Pattern.compile("(?<![\\w+])\\+?\\d{6,15}(?!\\w)");
    private final List<String> errors = new ArrayList<>();
    private final List<String> warnings = new ArrayList<>();

    public static ValidationReport ok() {
        return new ValidationReport();
    }

    public void error(String message) {
        errors.add(sanitize(message));
    }

    public void warning(String message) {
        warnings.add(sanitize(message));
    }

    public boolean valid() {
        return errors.isEmpty();
    }

    public List<String> errors() {
        return List.copyOf(errors);
    }

    public List<String> warnings() {
        return List.copyOf(warnings);
    }

    public void merge(ValidationReport other) {
        errors.addAll(other.errors);
        warnings.addAll(other.warnings);
    }

    public void throwIfInvalid() {
        if (!valid()) {
            throw new ValidationException(String.join("; ", errors));
        }
    }

    private String sanitize(String message) {
        if (message == null) {
            return "";
        }
        String redacted = SENSITIVE_ATTRIBUTE.matcher(message).replaceAll(match ->
                match.group(1) + "=\"<redacted>\"");
        redacted = LONG_IDENTIFIER.matcher(redacted).replaceAll("<redacted>");
        return MSISDN.matcher(redacted).replaceAll("<redacted>");
    }
}
