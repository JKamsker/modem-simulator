package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.RedactionInfo;

import java.util.ArrayList;
import java.util.List;

final class SessionEventRedactions {
    private SessionEventRedactions() {
    }

    static RedactionInfo merge(
            RedactionInfo payload,
            boolean stateBeforeRedacted,
            boolean stateAfterRedacted,
            List<String> stateClasses) {
        if (!stateBeforeRedacted && !stateAfterRedacted) {
            return payload;
        }
        List<String> fields = new ArrayList<>(payload.fields());
        List<String> classes = new ArrayList<>(payload.classes());
        if (stateBeforeRedacted) {
            fields.add("stateBefore");
        }
        if (stateAfterRedacted) {
            fields.add("stateAfter");
        }
        stateClasses.stream().filter(stateClass -> !classes.contains(stateClass)).forEach(classes::add);
        return RedactionInfo.applied(fields, classes);
    }
}
