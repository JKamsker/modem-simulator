package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.CommandResult;
import com.jkamsker.modemsim.commands.ResponseFormatter;
import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemState;

final class SessionParseFailure {
    private SessionParseFailure() {
    }

    static SessionResponse response(
            SessionEventPublisher events, SessionInputState inputState, long lastByteNanos,
            ModemState state, RawBytes output, int start) {
        RawBytes result = state.settings().quiet() ? RawBytes.empty()
                : new ResponseFormatter(state).result("ERROR", state.settings().verbose());
        RawBytes response = output.append(result);
        events.publish(EventType.HANDLER_RESULT, Direction.INTERNAL, RawBytes.empty(), null, state, state,
                CommandResult.error(state, "AtCommandParser"));
        inputState.clearCommandBuffer();
        if (!response.isEmpty()) {
            events.publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, response, null, null, state, null);
        }
        inputState.markDteRx(lastByteNanos);
        return new SessionResponse(response, events.eventsSince(start));
    }
}
