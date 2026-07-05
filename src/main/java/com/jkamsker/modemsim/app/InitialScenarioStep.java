package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;

import java.util.ArrayList;
import java.util.List;

record InitialScenarioStep(long atMs, List<InitialScenarioAction> actions) {
    InitialScenarioStep {
        actions = List.copyOf(actions);
    }

    SessionResponse apply(HeadlessSession session) {
        List<SessionResponse> responses = new ArrayList<>();
        for (InitialScenarioAction action : actions) {
            responses.add(action.apply(session));
        }
        return InitialScenarioAction.combine(responses);
    }
}
