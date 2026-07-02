package com.alegs3.modemsim.session;

import com.alegs3.modemsim.commands.CommandResult;
import com.alegs3.modemsim.commands.DefaultCommandRouter;
import com.alegs3.modemsim.commands.ResponseFormatter;
import com.alegs3.modemsim.commands.ResponseFrame;
import com.alegs3.modemsim.monitor.Direction;
import com.alegs3.modemsim.monitor.EventSink;
import com.alegs3.modemsim.monitor.EventType;
import com.alegs3.modemsim.monitor.InMemoryEventSink;
import com.alegs3.modemsim.monitor.ModemEvent;
import com.alegs3.modemsim.monitor.RedactionInfo;
import com.alegs3.modemsim.parser.AtCommandParser;
import com.alegs3.modemsim.parser.EntryMode;
import com.alegs3.modemsim.parser.ParsedCommand;
import com.alegs3.modemsim.parser.RawBytes;
import com.alegs3.modemsim.profiles.Profile;
import com.alegs3.modemsim.scheduler.DeterministicScheduler;
import com.alegs3.modemsim.scheduler.ScheduledEmission;
import com.alegs3.modemsim.scheduler.VirtualClock;
import com.alegs3.modemsim.state.CallMode;
import com.alegs3.modemsim.state.ModemState;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class HeadlessSession implements SessionActor {
    private final String sessionId;
    private final Profile profile;
    private final long sessionSeed;
    private final EventSink eventSink;
    private final InMemoryEventSink memorySink;
    private final DefaultCommandRouter router = new DefaultCommandRouter();
    private final VirtualClock clock = new VirtualClock();
    private final DeterministicScheduler scheduler;

    private long eventSequence;
    private ModemState state;
    private RawBytes lastCommandLine = RawBytes.empty();

    public HeadlessSession(String sessionId, Profile profile, long sessionSeed) {
        this(sessionId, profile, sessionSeed, new InMemoryEventSink());
    }

    public HeadlessSession(String sessionId, Profile profile, long sessionSeed, EventSink eventSink) {
        this.sessionId = sessionId;
        this.profile = profile;
        this.sessionSeed = sessionSeed;
        this.eventSink = eventSink;
        this.memorySink = eventSink instanceof InMemoryEventSink sink ? sink : null;
        this.scheduler = new DeterministicScheduler(sessionSeed);
        this.state = profile.initialState();
        publish(EventType.SESSION_START, Direction.INTERNAL, RawBytes.empty(), null, null, state, null);
    }

    @Override
    public SessionResponse receive(RawBytes bytes) {
        int start = eventCount();
        RawBytes output = RawBytes.empty();
        publish(EventType.RX_BYTES, Direction.DTE_TO_DCE, bytes, null, null, state, null);
        if (state.settings().echo()) {
            output = output.append(bytes);
        }
        RawBytes effective = bytes.ascii().equals("A/") ? lastCommandLine : bytes;
        List<ParsedCommand> commands = new AtCommandParser(state.settings().s5()).parse(effective, entryMode());
        if (!bytes.ascii().equals("A/")) {
            lastCommandLine = bytes;
        }
        CommandResult finalResult = null;
        for (ParsedCommand command : commands) {
            publishParsed(command);
            ModemState before = state;
            CommandResult result = router.route(profile, state, command);
            state = result.state();
            output = output.append(renderFrames(result.frames()));
            publish(EventType.HANDLER_RESULT, Direction.INTERNAL, RawBytes.empty(), command, before, state, result);
            finalResult = result;
            if (result.stopLine()) {
                break;
            }
        }
        if (finalResult != null && finalResult.finalResult() != null && !state.settings().quiet()) {
            output = output.append(new ResponseFormatter(state).line(finalResult.finalResult().text(state.settings().verbose())));
        }
        if (!output.isEmpty()) {
            publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, output, null, null, state, null);
        }
        return response(output, start);
    }

    @Override
    public SessionResponse advanceTime(long millis) {
        int start = eventCount();
        clock.advanceMillis(millis);
        RawBytes output = RawBytes.empty();
        for (ScheduledEmission emission : scheduler.due(clock.nowNanos(), state.version())) {
            output = output.append(emission.payload());
            publishScheduler(EventType.SCHEDULER_EMIT, emission);
        }
        if (!output.isEmpty()) {
            publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, output, null, null, state, null);
        }
        return response(output, start);
    }

    public SessionResponse drainScheduled() {
        int start = eventCount();
        RawBytes output = RawBytes.empty();
        for (ScheduledEmission emission : scheduler.drainAll(clock, state.version())) {
            output = output.append(emission.payload());
            publishScheduler(EventType.SCHEDULER_EMIT, emission);
        }
        if (!output.isEmpty()) {
            publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, output, null, null, state, null);
        }
        return response(output, start);
    }

    @Override
    public ModemState snapshot() {
        return state;
    }

    private RawBytes renderFrames(List<ResponseFrame> frames) {
        RawBytes output = RawBytes.empty();
        ResponseFormatter formatter = new ResponseFormatter(state);
        for (ResponseFrame frame : frames) {
            output = output.append(frame.bytes(formatter));
        }
        return output;
    }

    private EntryMode entryMode() {
        return switch (state.call().mode()) {
            case ONLINE_DATA -> EntryMode.ONLINE_DATA;
            case ONLINE_COMMAND -> EntryMode.ONLINE_COMMAND;
            case SMS_TEXT_ENTRY -> EntryMode.SMS_TEXT_ENTRY;
            case SMS_PDU_ENTRY -> EntryMode.SMS_PDU_ENTRY;
            default -> EntryMode.COMMAND;
        };
    }

    private SessionResponse response(RawBytes output, int start) {
        return new SessionResponse(output, eventsSince(start));
    }

    private void publishParsed(ParsedCommand command) {
        publish(EventType.PARSED_COMMAND, Direction.INTERNAL, command.sourceLine(), command, null, state, null);
    }

    private void publish(
            EventType type,
            Direction direction,
            RawBytes raw,
            ParsedCommand command,
            ModemState before,
            ModemState after,
            CommandResult result) {
        eventSink.publish(new ModemEvent(
                OffsetDateTime.now(),
                clock.nowNanos(),
                ++eventSequence,
                sessionId,
                type,
                direction,
                raw.toHex(),
                escape(raw.ascii()),
                parsed(command),
                profile.id(),
                result == null ? null : result.handler(),
                result == null || result.finalResult() == null ? null : result.finalResult().name(),
                null,
                before,
                after,
                RedactionInfo.none()));
    }

    private void publishScheduler(EventType type, ScheduledEmission emission) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("dueMonotonicNanos", emission.dueMonotonicNanos());
        data.put("sourceSequence", emission.sequence());
        data.put("sourcePriority", emission.sourcePriority().name().toLowerCase());
        data.put("sampledDelayMs", emission.sampledDelayMs());
        data.put("cancelled", false);
        eventSink.publish(new ModemEvent(
                OffsetDateTime.now(), clock.nowNanos(), ++eventSequence, sessionId,
                type, Direction.INTERNAL, "", null, null, profile.id(), null,
                null, data, null, state, RedactionInfo.none()));
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

    private int eventCount() {
        return memorySink == null ? 0 : memorySink.events().size();
    }

    private List<ModemEvent> eventsSince(int start) {
        if (memorySink == null) {
            return List.of();
        }
        List<ModemEvent> events = memorySink.events();
        return new ArrayList<>(events.subList(Math.min(start, events.size()), events.size()));
    }
}
