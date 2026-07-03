package com.jkamsker.modemsim.replay;

import java.util.ArrayList;
import java.util.List;

public final class ReplayReport {
    private final List<String> divergences = new ArrayList<>();
    private final List<String> hardFailures = new ArrayList<>();

    public void divergence(String message) {
        divergences.add(message);
    }

    public void hardFailure(String message) {
        hardFailures.add(message);
        divergences.add(message);
    }

    public boolean valid() {
        return divergences.isEmpty();
    }

    public boolean hasHardFailures() {
        return !hardFailures.isEmpty();
    }

    public List<String> divergences() {
        return List.copyOf(divergences);
    }

    public List<String> hardFailures() {
        return List.copyOf(hardFailures);
    }
}
