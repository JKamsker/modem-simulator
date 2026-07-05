package com.jkamsker.modemsim.monitor;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemState;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.HexFormat;

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
        return audit(sequence, sessionId, profile, type, direction, injectionType, null, raw, before, after, "virtual");
    }

    public static ModemEvent audit(
            long sequence,
            String sessionId,
            String profile,
            EventType type,
            Direction direction,
            String injectionType,
            String result,
            RawBytes raw,
            ModemState before,
            ModemState after) {
        return audit(sequence, sessionId, profile, type, direction, injectionType, result, raw, before, after, "virtual");
    }

    public static ModemEvent audit(
            long sequence,
            String sessionId,
            String profile,
            EventType type,
            Direction direction,
            String injectionType,
            String result,
            RawBytes raw,
            ModemState before,
            ModemState after,
            String clockMode) {
        return audit(sequence, sessionId, profile, type, direction, injectionType, result, raw, before, after,
                clockMode, null, null);
    }

    public static ModemEvent audit(
            long sequence,
            String sessionId,
            String profile,
            EventType type,
            Direction direction,
            String injectionType,
            String result,
            RawBytes raw,
            ModemState before,
            ModemState after,
            String clockMode,
            String configHash,
            Long sessionSeed) {
        EventRedactor rawRedactor = new EventRedactor();
        RedactedPayload payload = rawRedactor.redactRaw(type, direction, raw, null, false);
        EventStateRedactor stateRedactor = new EventStateRedactor();
        boolean stateBeforeRedacted = stateRedactor.containsSensitiveData(before);
        boolean stateAfterRedacted = stateRedactor.containsSensitiveData(after);
        RedactionInfo redaction = EventRedactions.merge(
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
                configHash == null ? sha256("headless-session:" + profile) : configHash,
                null,
                sha256(String.valueOf(before)),
                sessionSeed,
                clockMode,
                null,
                null,
                injectionType,
                0,
                type == EventType.RX_OVERFLOW || type == EventType.TX_OVERFLOW,
                null,
                result,
                null,
                stateBeforeRedacted ? stateRedactor.redactSensitiveData(before) : before,
                stateAfterRedacted ? stateRedactor.redactSensitiveData(after) : after,
                redaction);
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
