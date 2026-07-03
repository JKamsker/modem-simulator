package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.scheduler.ScheduledEmission;

import java.util.List;

record ScheduledBatch(RawBytes output, List<ScheduledEmission> emitted, List<ScheduledEmission> cancelled) {
}
