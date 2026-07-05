package com.jkamsker.modemsim.replay;

import java.util.ArrayList;
import java.util.List;

public final class ReplayReport {
    public enum DivergenceKind {
        METADATA(true),
        OUTPUT(false),
        SAFETY(false);

        private final boolean confirmable;

        DivergenceKind(boolean confirmable) {
            this.confirmable = confirmable;
        }
    }

    private final List<Divergence> divergences = new ArrayList<>();
    private final List<String> hardFailures = new ArrayList<>();

    public void divergence(String message) {
        divergence(DivergenceKind.METADATA, message);
    }

    public void outputDivergence(String message) {
        divergence(DivergenceKind.OUTPUT, message);
    }

    private void divergence(DivergenceKind kind, String message) {
        divergences.add(new Divergence(kind, message));
    }

    public void hardFailure(String message) {
        hardFailures.add(message);
        divergence(DivergenceKind.SAFETY, message);
    }

    public boolean valid() {
        return divergences.isEmpty();
    }

    public boolean hasHardFailures() {
        return !hardFailures.isEmpty();
    }

    public boolean canContinueAfterConfirmation() {
        return !valid() && divergences.stream().allMatch(divergence -> divergence.kind().confirmable);
    }

    public List<String> divergences() {
        return divergences.stream().map(Divergence::message).toList();
    }

    public List<String> hardFailures() {
        return List.copyOf(hardFailures);
    }

    private record Divergence(DivergenceKind kind, String message) {
    }
}
