package com.alegs3.modemsim.replay;

import java.util.ArrayList;
import java.util.List;

public final class ReplayReport {
    private final List<String> divergences = new ArrayList<>();

    public void divergence(String message) {
        divergences.add(message);
    }

    public boolean valid() {
        return divergences.isEmpty();
    }

    public List<String> divergences() {
        return List.copyOf(divergences);
    }
}
