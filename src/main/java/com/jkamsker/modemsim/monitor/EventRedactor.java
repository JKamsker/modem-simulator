package com.jkamsker.modemsim.monitor;

import com.jkamsker.modemsim.parser.CommandKind;
import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.parser.RawBytes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class EventRedactor {
    private static final String REDACTED = "<redacted>";
    private static final Pattern MSISDN = Pattern.compile("\\+\\d{6,15}");
    private static final Pattern LONG_IDENTIFIER = Pattern.compile("(?<!\\d)\\d{14,22}(?!\\d)");

    public RedactedPayload redactRaw(
            EventType type,
            Direction direction,
            RawBytes raw,
            ParsedCommand command,
            boolean smsBodyEntry) {
        if (raw.isEmpty()) {
            return original(raw);
        }
        if (smsBodyEntry && type == EventType.RX_BYTES && direction == Direction.DTE_TO_DCE) {
            return fullyRedacted("rawHex", "textEscaped", "sms-body");
        }
        if (isSensitiveCpinSet(command) || rawLooksLikeCpinSet(raw)) {
            return fullyRedacted("rawHex", "textEscaped", cpinClasses(command));
        }
        List<String> identifierClasses = identifierClasses(raw.ascii());
        if (!identifierClasses.isEmpty()) {
            return fullyRedacted("rawHex", "textEscaped", identifierClasses);
        }
        return original(raw);
    }

    public String redactCommandArguments(ParsedCommand command) {
        if (isSensitiveCpinSet(command) || !identifierClasses(command.arguments()).isEmpty()) {
            return REDACTED;
        }
        return command.arguments();
    }

    private RedactedPayload original(RawBytes raw) {
        return new RedactedPayload(raw.toHex(), escape(raw.ascii()), RedactionInfo.none());
    }

    private RedactedPayload fullyRedacted(String firstField, String secondField, String redactionClass) {
        return fullyRedacted(firstField, secondField, List.of(redactionClass));
    }

    private RedactedPayload fullyRedacted(String firstField, String secondField, List<String> redactionClasses) {
        return new RedactedPayload(
                REDACTED,
                REDACTED,
                RedactionInfo.applied(List.of(firstField, secondField), redactionClasses));
    }

    private List<String> cpinClasses(ParsedCommand command) {
        List<String> classes = new ArrayList<>();
        classes.add("pin");
        if (command != null && command.arguments().contains(",")) {
            classes.add("puk");
        }
        return classes;
    }

    private boolean isSensitiveCpinSet(ParsedCommand command) {
        return command != null
                && command.kind() == CommandKind.EXTENDED_SET
                && command.isExtended("+CPIN")
                && !command.arguments().isBlank();
    }

    private boolean rawLooksLikeCpinSet(RawBytes raw) {
        return raw.ascii().toUpperCase().contains("+CPIN=");
    }

    private List<String> identifierClasses(String text) {
        List<String> classes = new ArrayList<>();
        if (text == null) {
            return classes;
        }
        if (MSISDN.matcher(text).find()) {
            classes.add("msisdn");
        }
        if (LONG_IDENTIFIER.matcher(text).find()) {
            classes.add("imei");
            classes.add("imsi");
            classes.add("iccid");
        }
        return classes;
    }

    private String escape(String text) {
        return text.replace("\r", "\\r").replace("\n", "\\n");
    }
}
