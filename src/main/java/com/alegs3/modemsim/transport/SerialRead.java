package com.alegs3.modemsim.transport;

import com.alegs3.modemsim.parser.RawBytes;

public record SerialRead(RawBytes bytes, long firstByteMonotonicNanos, long lastByteMonotonicNanos) {
}
