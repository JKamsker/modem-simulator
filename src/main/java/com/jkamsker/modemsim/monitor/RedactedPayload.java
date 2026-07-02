package com.jkamsker.modemsim.monitor;

public record RedactedPayload(String rawHex, String textEscaped, RedactionInfo redaction) {
}
