package com.jkamsker.modemsim.monitor;

import com.jkamsker.modemsim.parser.CommandKind;
import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.parser.RawBytes;

import java.util.ArrayList;
import java.util.List;

public final class EventRedactor {
    private static final String REDACTED = "<redacted>";

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
        return original(raw);
    }

    public String redactCommandArguments(ParsedCommand command) {
        return isSensitiveCpinSet(command) ? REDACTED : command.arguments();
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

    private String escape(String text) {
        return text.replace("\r", "\\r").replace("\n", "\\n");
    }
}
