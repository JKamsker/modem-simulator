package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.CommandResult;
import com.jkamsker.modemsim.commands.CmeError;
import com.jkamsker.modemsim.commands.ResponseFormatter;
import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.ErrorPolicy;
import com.jkamsker.modemsim.state.ModemState;

final class SessionParseFailure {
    private SessionParseFailure() {
    }

    static SessionResponse response(
            ErrorPolicy errorPolicy, SessionEventPublisher events, SessionInputState inputState, long lastByteNanos,
            ModemState state, RawBytes input, RawBytes output, int start) {
        return response(errorPolicy, events, inputState, lastByteNanos, state, input, output, start, false);
    }

    static SessionResponse response(
            ErrorPolicy errorPolicy, SessionEventPublisher events, SessionInputState inputState, long lastByteNanos,
            ModemState state, RawBytes input, RawBytes output, int start, boolean smsBodyEntry) {
        CommandResult commandResult = parseResult(errorPolicy, state);
        RawBytes result = renderResult(commandResult, state);
        RawBytes response = output.append(result);
        events.publish(EventType.PARSE_ERROR, Direction.INTERNAL, input, null, state, state,
                commandResult, smsBodyEntry);
        inputState.clearCommandBuffer();
        if (!response.isEmpty()) {
            events.publish(EventType.TX_BYTES, Direction.DCE_TO_DTE, response, null, null, state, null);
        }
        inputState.markDteRx(lastByteNanos);
        return new SessionResponse(response, events.eventsSince(start));
    }

    private static CommandResult parseResult(ErrorPolicy errorPolicy, ModemState state) {
        return switch (errorPolicy.invalidParameter()) {
            case "CME", "CME_OR_ERROR" -> CmeError.INCORRECT_PARAMETERS.result(state, "AtCommandParser");
            default -> CommandResult.error(state, "AtCommandParser");
        };
    }

    private static RawBytes renderResult(CommandResult result, ModemState state) {
        if (state.settings().quiet()) {
            return RawBytes.empty();
        }
        RawBytes frames = SessionFrameRenderer.render(result.frames(), state);
        if (result.finalResult() == null) {
            return frames;
        }
        return frames.append(new ResponseFormatter(state)
                .result(result.finalResult().text(state.settings().verbose()), state.settings().verbose()));
    }
}
