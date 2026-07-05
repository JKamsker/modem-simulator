package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.CommandResult;
import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemState;

final class SessionResultEvents {
    private SessionResultEvents() {
    }

    static void publish(SessionEventPublisher events, EventType type, ParsedCommand command, ModemState before,
            ModemState after, CommandResult result, long startedNanos, Integer latencyMs) {
        if (latencyMs == null) {
            events.publishMeasured(type, Direction.INTERNAL, RawBytes.empty(), command, before, after, result, startedNanos);
        } else {
            events.publishWithLatency(type, Direction.INTERNAL, RawBytes.empty(), command, before, after, result, latencyMs);
        }
    }
}
