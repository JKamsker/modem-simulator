package com.jkamsker.modemsim.monitor;

import java.util.ArrayList;
import java.util.List;

public final class EventRedactions {
    private EventRedactions() {
    }

    public static RedactionInfo merge(
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
