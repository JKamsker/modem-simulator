package com.jkamsker.modemsim.state;

import java.time.OffsetDateTime;

public record SmsMessage(
        int index,
        SmsStorage storage,
        String status,
        String sender,
        String recipient,
        OffsetDateTime timestamp,
        String text,
        String pdu
) {
}
