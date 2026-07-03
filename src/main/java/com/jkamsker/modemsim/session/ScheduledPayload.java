package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;

record ScheduledPayload(RawBytes output, Long scheduledSequence) {
}
