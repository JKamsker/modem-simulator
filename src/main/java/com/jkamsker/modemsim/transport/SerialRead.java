package com.jkamsker.modemsim.transport;

import com.jkamsker.modemsim.parser.RawBytes;

public record SerialRead(RawBytes bytes, long firstByteMonotonicNanos, long lastByteMonotonicNanos) {
}
