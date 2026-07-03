package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.session.SessionResponse;

import java.util.List;

record MacroSummary(String hash, String errors, String customResponses, String enabled, SessionResponse response) {
    MacroSummary(String hash, String errors, String customResponses, String enabled) {
        this(hash, errors, customResponses, enabled, new SessionResponse(RawBytes.empty(), List.of()));
    }
}
