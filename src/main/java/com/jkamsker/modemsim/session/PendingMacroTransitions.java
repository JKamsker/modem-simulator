package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.scheduler.ScheduledEmission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class PendingMacroTransitions {
    private final List<PendingMacroTransition> transitions = new ArrayList<>();

    void addAll(List<PendingMacroTransition> value) {
        transitions.addAll(value);
    }

    void clear() {
        transitions.clear();
    }

    void removeCancelled(List<ScheduledEmission> cancelled) {
        if (cancelled.isEmpty() || transitions.isEmpty()) {
            return;
        }
        Set<Long> sequences = new HashSet<>();
        cancelled.forEach(emission -> sequences.add(emission.sequence()));
        transitions.removeIf(transition -> sequences.contains(transition.scheduledSequence()));
    }

    RawBytes commitFor(List<ScheduledEmission> emitted, Committer committer) {
        RawBytes output = RawBytes.empty();
        for (ScheduledEmission emission : emitted) {
            output = output.append(commit(emission.sequence(), committer));
        }
        return output;
    }

    private RawBytes commit(long scheduledSequence, Committer committer) {
        for (int i = 0; i < transitions.size(); i++) {
            PendingMacroTransition transition = transitions.get(i);
            if (transition.scheduledSequence() == scheduledSequence) {
                transitions.remove(i);
                return committer.commit(transition);
            }
        }
        return RawBytes.empty();
    }

    interface Committer {
        RawBytes commit(PendingMacroTransition transition);
    }
}
