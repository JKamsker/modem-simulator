package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.ResponseFormatter;
import com.jkamsker.modemsim.commands.ResponseFrame;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemState;

import java.util.List;

final class SessionFrameRenderer {
    private SessionFrameRenderer() {
    }

    static RawBytes render(List<ResponseFrame> frames, ModemState state) {
        RawBytes output = RawBytes.empty();
        ResponseFormatter formatter = new ResponseFormatter(state);
        for (ResponseFrame frame : frames) {
            output = output.append(frame.bytes(formatter));
        }
        return output;
    }
}
