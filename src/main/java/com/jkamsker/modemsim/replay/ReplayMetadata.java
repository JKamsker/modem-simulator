package com.jkamsker.modemsim.replay;

import com.jkamsker.modemsim.monitor.ModemEvent;

import java.util.List;

public final class ReplayMetadata {
    private ReplayMetadata() {
    }

    public static ReplayReport report(ModemEvent sessionStart, List<ReplayStep> steps) {
        ReplayReport report = new ReplayReport();
        for (ReplayStep step : steps) {
            for (ReplayEventExpectation event : step.expectedEvents()) {
                compare(report, "profileHash", event.profileHash(), sessionStart.profileHash());
                compare(report, "configHash", event.configHash(), sessionStart.configHash());
                compare(report, "macroHash", event.macroHash(), sessionStart.macroHash());
                compare(report, "initialStateHash", event.initialStateHash(), sessionStart.initialStateHash());
                compare(report, "sessionSeed", event.sessionSeed(), sessionStart.sessionSeed());
                compare(report, "clockMode", event.clockMode(), sessionStart.clockMode());
            }
        }
        return report;
    }

    public static boolean hasStrictMetadata(List<ReplayStep> steps) {
        return steps.stream().flatMap(step -> step.expectedEvents().stream())
                .anyMatch(event -> event.profileHash() != null || event.configHash() != null
                        || event.macroHash() != null || event.initialStateHash() != null
                        || event.sessionSeed() != null || event.clockMode() != null);
    }

    private static void compare(ReplayReport report, String name, Object expected, Object actual) {
        if (expected != null && !expected.equals(actual)) {
            report.divergence(name + " expected " + expected + " but got " + actual);
        }
    }
}
