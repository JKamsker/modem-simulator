package com.alegs3.modemsim.session;

import com.alegs3.modemsim.commands.CommandResult;
import com.alegs3.modemsim.monitor.Direction;
import com.alegs3.modemsim.monitor.EventSink;
import com.alegs3.modemsim.monitor.EventType;
import com.alegs3.modemsim.monitor.InMemoryEventSink;
import com.alegs3.modemsim.monitor.ModemEvent;
import com.alegs3.modemsim.monitor.RedactionInfo;
import com.alegs3.modemsim.parser.ParsedCommand;
import com.alegs3.modemsim.parser.RawBytes;
import com.alegs3.modemsim.scheduler.ScheduledEmission;
import com.alegs3.modemsim.scheduler.VirtualClock;
import com.alegs3.modemsim.state.ModemState;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class SessionEventPublisher {
    private final String sessionId;
    private final String profileId;
    private final VirtualClock clock;
    private final EventSink eventSink;
    private final InMemoryEventSink memorySink;
    private long sequence;

    SessionEventPublisher(String sessionId, String profileId, VirtualClock clock, EventSink eventSink) {
        this.sessionId = sessionId;
        this.profileId = profileId;
        this.clock = clock;
        this.eventSink = eventSink;
        this.memorySink = eventSink instanceof InMemoryEventSink sink ? sink : null;
    }

    void publish(
            EventType type,
            Direction direction,
            RawBytes raw,
            ParsedCommand command,
            ModemState before,
            ModemState after,
            CommandResult result) {
        eventSink.publish(new ModemEvent(
                OffsetDateTime.now(), clock.nowNanos(), ++sequence, sessionId, type, direction,
                raw.toHex(), escape(raw.ascii()), parsed(command), profileId,
                result == null ? null : result.handler(),
                result == null || result.finalResult() == null ? null : result.finalResult().name(),
                null, before, after, RedactionInfo.none()));
    }

    void publishParsed(ParsedCommand command, ModemState state) {
        publish(EventType.PARSED_COMMAND, Direction.INTERNAL, command.sourceLine(), command, null, state, null);
    }

    void publishScheduler(EventType type, ScheduledEmission emission, ModemState state) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("dueMonotonicNanos", emission.dueMonotonicNanos());
        data.put("sourceSequence", emission.sequence());
        data.put("sourcePriority", emission.sourcePriority().name().toLowerCase());
        data.put("sampledDelayMs", emission.sampledDelayMs());
        data.put("cancelled", false);
        eventSink.publish(new ModemEvent(
                OffsetDateTime.now(), clock.nowNanos(), ++sequence, sessionId, type,
                Direction.INTERNAL, "", null, null, profileId, null, null,
                data, null, state, RedactionInfo.none()));
    }

    long nextSequence() {
        return sequence + 1;
    }

    int eventCount() {
        return memorySink == null ? 0 : memorySink.events().size();
    }

    List<ModemEvent> eventsSince(int start) {
        if (memorySink == null) {
            return List.of();
        }
        List<ModemEvent> events = memorySink.events();
        return new ArrayList<>(events.subList(Math.min(start, events.size()), events.size()));
    }

    private Map<String, Object> parsed(ParsedCommand command) {
        if (command == null) {
            return null;
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", command.normalizedName());
        data.put("kind", command.kind().name());
        data.put("arguments", command.arguments());
        return data;
    }

    private String escape(String text) {
        return text.replace("\r", "\\r").replace("\n", "\\n");
    }
}
