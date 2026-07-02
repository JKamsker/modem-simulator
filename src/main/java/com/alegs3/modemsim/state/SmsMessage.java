package com.alegs3.modemsim.state;

import java.time.OffsetDateTime;

public record SmsMessage(
        int index,
        String status,
        String sender,
        String recipient,
        OffsetDateTime timestamp,
        String text,
        String pdu
) {
}
