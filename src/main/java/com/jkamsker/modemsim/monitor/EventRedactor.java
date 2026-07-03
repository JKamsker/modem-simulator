package com.jkamsker.modemsim.monitor;

import com.jkamsker.modemsim.parser.CommandKind;
import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.parser.RawBytes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class EventRedactor {
    private static final String REDACTED = "<redacted>";
    private static final Pattern MSISDN = Pattern.compile("(?<!\\d)\\+?\\d{6,15}(?!\\d)");
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
        if (type == EventType.TX_BYTES && direction == Direction.DCE_TO_DTE && containsSmsStorageBody(raw)) {
            return fullyRedacted("rawHex", "textEscaped", "sms-body");
        }
        if (isSensitiveCpinSet(command) || rawLooksLikeCpinSet(raw)) {
            return fullyRedacted("rawHex", "textEscaped", cpinClasses(command, raw));
        }
        List<String> identifierClasses = identifierClasses(raw.ascii(), command);
        if (!identifierClasses.isEmpty()) {
            return fullyRedacted("rawHex", "textEscaped", identifierClasses);
        }
        return original(raw);
    }

    public String redactCommandArguments(ParsedCommand command) {
        if (isSensitiveCpinSet(command) || !identifierClasses(command.arguments(), command).isEmpty()) {
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

    private List<String> cpinClasses(ParsedCommand command, RawBytes raw) {
        List<String> classes = new ArrayList<>();
        classes.add("pin");
        if ((command != null && command.arguments().contains(","))
                || (raw != null && raw.ascii().contains(","))) {
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

    private boolean containsSmsStorageBody(RawBytes raw) {
        String text = raw.ascii().toUpperCase();
        return text.contains("+CMGR") || text.contains("+CMGL");
    }

    private List<String> identifierClasses(String text, ParsedCommand command) {
        List<String> classes = new ArrayList<>();
        if (text == null) {
            return classes;
        }
        if (containsMsisdn(text, command)) {
            classes.add("msisdn");
        }
        if (LONG_IDENTIFIER.matcher(text).find()) {
            classes.add("imei");
            classes.add("imsi");
            classes.add("iccid");
        }
        return classes;
    }

    private boolean containsMsisdn(String text, ParsedCommand command) {
        String upper = text.toUpperCase();
        if (upper.contains("+CREG:") || upper.contains("+CGREG:") || upper.contains("+CEREG:")) {
            return false;
        }
        if (upper.contains("+CSCA") || upper.contains("+CMGS") || upper.contains("ATD")) {
            return MSISDN.matcher(text).find();
        }
        return command != null && (command.isBasic("ATD") || command.isExtended("+CSCA") || command.isExtended("+CMGS"))
                && MSISDN.matcher(text).find();
    }

    private String escape(String text) {
        return text.replace("\r", "\\r").replace("\n", "\\n");
    }
}
