package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.macros.FaultAction;
import com.jkamsker.modemsim.macros.MacroStateMutator;
import com.jkamsker.modemsim.macros.MacroStatePatch;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;

import java.util.ArrayList;

record InitialScenarioAction(MacroStatePatch patch, FaultAction fault, Long advanceMs) {
    private static final MacroStateMutator MUTATOR = new MacroStateMutator();

    SessionResponse apply(HeadlessSession session) {
        if (patch != null) {
            return session.applyState(MUTATOR.apply(session.snapshot(), java.util.List.of(patch)), "scenario-step");
        }
        if (fault != null) {
            return session.applyFault(fault);
        }
        return session.advanceTime(advanceMs == null ? 0 : advanceMs);
    }

    static SessionResponse combine(java.util.List<SessionResponse> responses) {
        RawBytes output = RawBytes.empty();
        var events = new ArrayList<com.jkamsker.modemsim.monitor.ModemEvent>();
        for (SessionResponse response : responses) {
            output = output.append(response.output());
            events.addAll(response.events());
        }
        return new SessionResponse(output, events);
    }
}
