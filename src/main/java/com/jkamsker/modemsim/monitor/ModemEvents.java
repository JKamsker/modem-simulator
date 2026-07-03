package com.jkamsker.modemsim.monitor;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemState;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

public final class ModemEvents {
    private ModemEvents() {
    }

    public static ModemEvent audit(
            long sequence,
            String sessionId,
            String profile,
            EventType type,
            Direction direction,
            String injectionType,
            RawBytes raw,
            ModemState before,
            ModemState after) {
        EventRedactor rawRedactor = new EventRedactor();
        RedactedPayload payload = rawRedactor.redactRaw(type, direction, raw, null, false);
        EventStateRedactor stateRedactor = new EventStateRedactor();
        boolean stateBeforeRedacted = stateRedactor.containsSensitiveData(before);
        boolean stateAfterRedacted = stateRedactor.containsSensitiveData(after);
        RedactionInfo redaction = mergeRedaction(
                payload.redaction(), stateBeforeRedacted, stateAfterRedacted, stateRedactor.classes(before, after));
        return new ModemEvent(
                OffsetDateTime.now(),
                System.nanoTime(),
                sequence,
                sessionId,
                type,
                direction,
                payload.rawHex(),
                payload.textEscaped(),
                null,
                profile,
                null,
                null,
                sha256(profile),
                sha256("gui-session:" + profile),
                null,
                sha256(String.valueOf(before)),
                12345L,
                "virtual",
                null,
                null,
                injectionType,
                0,
                null,
                null,
                null,
                stateBeforeRedacted ? stateRedactor.redactSensitiveData(before) : before,
                stateAfterRedacted ? stateRedactor.redactSensitiveData(after) : after,
                redaction);
    }

    private static RedactionInfo mergeRedaction(
            RedactionInfo payload,
            boolean stateBeforeRedacted,
            boolean stateAfterRedacted,
            List<String> stateClasses) {
        if (!stateBeforeRedacted && !stateAfterRedacted) {
            return payload;
        }
        List<String> fields = new ArrayList<>(payload.fields());
        List<String> classes = new ArrayList<>(payload.classes());
        if (stateBeforeRedacted) {
            fields.add("stateBefore");
        }
        if (stateAfterRedacted) {
            fields.add("stateAfter");
        }
        for (String stateClass : stateClasses) {
            if (!classes.contains(stateClass)) {
                classes.add(stateClass);
            }
        }
        return RedactionInfo.applied(fields, classes);
    }

    private static String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return "sha256:" + HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
