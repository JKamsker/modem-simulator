package com.jkamsker.modemsim.replay;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.monitor.ModemEventJson;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class ReplayValidator {
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final Pattern SHA256 = Pattern.compile("^sha256:[0-9a-f]{64}$");
    public ReplayReport validateRecompute(HeadlessSession session, List<ReplayStep> steps) {
        return validateRecompute(session, steps, false);
    }

    public ReplayReport validateRecompute(HeadlessSession session, List<ReplayStep> steps, boolean requireEventMetadata) {
        ReplayReport report = new ReplayReport();
        long lastSequence = 0;
        List<ModemEvent> initialEvents = session.events();
        for (int i = 0; i < steps.size(); i++) {
            ReplayStep step = steps.get(i);
            List<ModemEvent> events = new ArrayList<>();
            if (i == 0) {
                events.addAll(initialEvents);
            }
            if (requireEventMetadata && step.expectedEvents().isEmpty()) {
                report.divergence("step " + (i + 1) + " missing event metadata");
            }
            if (requireEventMetadata) {
                validateExpectedMetadata(i + 1, step.expectedEvents(), report);
            }
            RawBytes output = RawBytes.empty();
            if (!step.input().isEmpty() || expectsDteRx(step)) {
                SessionResponse response = session.receive(step.input());
                output = response.output();
                events.addAll(response.events());
            }
            if (step.drainScheduled()) {
                SessionResponse drained = session.drainScheduled();
                output = output.append(drained.output());
                events.addAll(drained.events());
            }
            if (expectsEvent(step, com.jkamsker.modemsim.monitor.EventType.SESSION_STOP)) {
                events.addAll(session.stop("replay-stop").events());
            }
            if (!output.toHex().equals(step.expectedOutput().toHex())) {
                report.divergence("step " + (i + 1) + " expected "
                        + step.expectedOutput().toHex() + " but got " + output.toHex());
                return report;
            }
            lastSequence = validateMetadata(i + 1, events, lastSequence, report);
            validateExpectedEvents(i + 1, events, step.expectedEvents(), report);
            if (!report.valid()) {
                return report;
            }
        }
        return report;
    }

    private long validateMetadata(int stepNumber, List<ModemEvent> events, long lastSequence, ReplayReport report) {
        long previous = lastSequence;
        for (ModemEvent event : events) {
            if (event.sequence() <= previous) {
                report.divergence("step " + stepNumber + " non-monotonic sequence " + event.sequence());
            }
            previous = Math.max(previous, event.sequence());
            requireHash(stepNumber, event, "profileHash", event.profileHash(), report);
            requireHash(stepNumber, event, "configHash", event.configHash(), report);
            requireHash(stepNumber, event, "initialStateHash", event.initialStateHash(), report);
            requireHash(stepNumber, event, "macroHash", event.macroHash(), report);
            if (event.replayDivergent()) {
                report.hardFailure("step " + stepNumber + " replay divergent event " + event.eventType());
            }
            if (event.sessionSeed() == null) {
                report.divergence("step " + stepNumber + " missing sessionSeed on " + event.eventType());
            }
            if (event.clockMode() == null || event.clockMode().isBlank()) {
                report.divergence("step " + stepNumber + " missing clockMode on " + event.eventType());
            }
        }
        return previous;
    }

    private void validateExpectedEvents(
            int stepNumber,
            List<ModemEvent> events,
            List<ReplayEventExpectation> expectations,
            ReplayReport report) {
        int cursor = 0;
        for (ReplayEventExpectation expectation : expectations) {
            int matched = findMatch(events, expectation, cursor);
            if (matched < 0) {
                report.divergence("step " + stepNumber + " missing event " + expectation.eventType()
                        + "/" + expectation.direction());
                continue;
            }
            compareExpectation(stepNumber, events.get(matched), expectation, report);
            cursor = matched + 1;
        }
    }

    private void validateExpectedMetadata(
            int stepNumber, List<ReplayEventExpectation> expectations, ReplayReport report) {
        for (ReplayEventExpectation expectation : expectations) {
            requireExpected(stepNumber, "eventType", expectation.eventType(), report);
            requireExpected(stepNumber, "direction", expectation.direction(), report);
            requireExpected(stepNumber, "sequence", expectation.sequence(), report);
            requireExpected(stepNumber, "monotonicNanos", expectation.monotonicNanos(), report);
            requireExpected(stepNumber, "rawHex", expectation.rawHex(), report);
            requireExpected(stepNumber, "profileHash", expectation.profileHash(), report);
            requireExpected(stepNumber, "configHash", expectation.configHash(), report);
            requireExpected(stepNumber, "macroHash", expectation.macroHash(), report);
            requireExpected(stepNumber, "initialStateHash", expectation.initialStateHash(), report);
            requireExpected(stepNumber, "sessionSeed", expectation.sessionSeed(), report);
            requireExpected(stepNumber, "clockMode", expectation.clockMode(), report);
            requireExpected(stepNumber, "redaction.applied", expectation.redacted(), report);
            if (Boolean.TRUE.equals(expectation.replayDivergent())) {
                report.hardFailure("step " + stepNumber + " expected replay divergent event "
                        + expectation.eventType());
            }
            if (expectation.eventType() == com.jkamsker.modemsim.monitor.EventType.SCHEDULER_ENQUEUE
                    || expectation.eventType() == com.jkamsker.modemsim.monitor.EventType.SCHEDULER_EMIT) {
                requireExpected(stepNumber, "scheduler.dueMonotonicNanos", expectation.schedulerDueMonotonicNanos(), report);
                requireExpected(stepNumber, "scheduler.sourceSequence", expectation.schedulerSourceSequence(), report);
                requireExpected(stepNumber, "scheduler.sourcePriority", expectation.schedulerSourcePriority(), report);
                requireExpected(stepNumber, "scheduler.operation", expectation.schedulerOperation(), report);
                requireExpected(stepNumber, "scheduler.sampledDelayMs", expectation.sampledDelayMs(), report);
                requireExpected(stepNumber, "scheduler.cancelled", expectation.schedulerCancelled(), report);
            }
        }
    }

    private int findMatch(List<ModemEvent> events, ReplayEventExpectation expectation, int cursor) {
        for (int i = cursor; i < events.size(); i++) {
            ModemEvent event = events.get(i);
            if ((expectation.eventType() == null || expectation.eventType() == event.eventType())
                    && (expectation.direction() == null || expectation.direction() == event.direction())) {
                return i;
            }
        }
        return -1;
    }

    private void compareExpectation(
            int stepNumber,
            ModemEvent event,
            ReplayEventExpectation expectation,
            ReplayReport report) {
        compare(stepNumber, "sequence", expectation.sequence(), event.sequence(), report);
        compare(stepNumber, "monotonicNanos", expectation.monotonicNanos(), event.monotonicNanos(), report);
        compare(stepNumber, "source", expectation.source(), ModemEventJson.toMap(event).get("source"), report);
        compare(stepNumber, "command", expectation.command(),
                event.parsedCommand() == null ? null : event.parsedCommand().get("name"), report);
        compare(stepNumber, "finalResult", expectation.finalResult(), event.result(), report);
        compare(stepNumber, "rawHex", expectation.rawHex(), event.rawHex(), report);
        compare(stepNumber, "profileHash", expectation.profileHash(), event.profileHash(), report);
        compare(stepNumber, "configHash", expectation.configHash(), event.configHash(), report);
        compare(stepNumber, "macroHash", expectation.macroHash(), event.macroHash(), report);
        compare(stepNumber, "initialStateHash", expectation.initialStateHash(), event.initialStateHash(), report);
        compare(stepNumber, "sessionSeed", expectation.sessionSeed(), event.sessionSeed(), report);
        compare(stepNumber, "clockMode", expectation.clockMode(), event.clockMode(), report);
        compare(stepNumber, "port", expectation.port(), event.port(), report);
        compare(stepNumber, "portRole", expectation.portRole(), event.portRole(), report);
        compare(stepNumber, "redaction", expectation.redacted(),
                event.redaction() == null ? null : event.redaction().applied(), report);
        compare(stepNumber, "replayDivergent", expectation.replayDivergent(), event.replayDivergent(), report);
        compare(stepNumber, "stateBefore", expectation.stateBeforeJson(), stateJson(event, "stateBefore"), report);
        compare(stepNumber, "stateAfter", expectation.stateAfterJson(), stateJson(event, "stateAfter"), report);
        compareScheduler(stepNumber, event, expectation, report);
    }

    private boolean expectsDteRx(ReplayStep step) {
        return step.expectedEvents().stream().anyMatch(event ->
                event.eventType() == com.jkamsker.modemsim.monitor.EventType.RX_BYTES
                        && event.direction() == com.jkamsker.modemsim.monitor.Direction.DTE_TO_DCE);
    }

    private boolean expectsEvent(ReplayStep step, com.jkamsker.modemsim.monitor.EventType eventType) {
        return step.expectedEvents().stream().anyMatch(event -> event.eventType() == eventType);
    }

    private com.fasterxml.jackson.databind.JsonNode stateJson(ModemEvent event, String field) {
        Object value = ModemEventJson.toMap(event).get(field);
        return value == null ? null : JSON.valueToTree(value);
    }

    private void compareScheduler(
            int stepNumber, ModemEvent event, ReplayEventExpectation expectation, ReplayReport report) {
        compare(stepNumber, "scheduler.operation", expectation.schedulerOperation(), scheduler(event, "operation"), report);
        compare(stepNumber, "scheduler.sampledDelayMs", expectation.sampledDelayMs(), scheduler(event, "sampledDelayMs"), report);
        compare(stepNumber, "scheduler.dueMonotonicNanos",
                expectation.schedulerDueMonotonicNanos(), scheduler(event, "dueMonotonicNanos"), report);
        compare(stepNumber, "scheduler.sourceSequence",
                expectation.schedulerSourceSequence(), scheduler(event, "sourceSequence"), report);
        compare(stepNumber, "scheduler.sourcePriority",
                expectation.schedulerSourcePriority(), scheduler(event, "sourcePriority"), report);
        compare(stepNumber, "scheduler.cancelled", expectation.schedulerCancelled(), scheduler(event, "cancelled"), report);
    }

    private Object scheduler(ModemEvent event, String key) {
        return event.scheduler() == null ? null : event.scheduler().get(key);
    }

    private void requireHash(
            int stepNumber, ModemEvent event, String name, String value, ReplayReport report) {
        if (value == null || !SHA256.matcher(value).matches()) {
            report.divergence("step " + stepNumber + " missing " + name + " on " + event.eventType());
        }
    }

    private void requireExpected(int stepNumber, String field, Object value, ReplayReport report) {
        if (value == null) {
            report.divergence("step " + stepNumber + " expected event missing " + field);
        }
    }

    private void compare(int stepNumber, String field, Object expected, Object actual, ReplayReport report) {
        if (expected instanceof com.fasterxml.jackson.databind.JsonNode expectedJson
                && actual instanceof com.fasterxml.jackson.databind.JsonNode actualJson) {
            if (jsonDiffers(expectedJson, actualJson)) {
                report.divergence("step " + stepNumber + " " + field
                        + " expected " + expected + " but got " + actual);
            }
            return;
        }
        if (expected != null && !expected.equals(actual)) {
            report.divergence("step " + stepNumber + " " + field
                    + " expected " + expected + " but got " + actual);
        }
    }

    private boolean jsonDiffers(
            com.fasterxml.jackson.databind.JsonNode expected,
            com.fasterxml.jackson.databind.JsonNode actual) {
        if (expected.isNumber() && actual.isNumber()) {
            return expected.decimalValue().compareTo(actual.decimalValue()) != 0;
        }
        if (expected.isObject() && actual.isObject()) {
            var names = expected.fieldNames();
            while (names.hasNext()) {
                String name = names.next();
                if (!actual.has(name) || jsonDiffers(expected.get(name), actual.get(name))) {
                    return true;
                }
            }
            return expected.size() != actual.size();
        }
        if (expected.isArray() && actual.isArray()) {
            if (expected.size() != actual.size()) {
                return true;
            }
            for (int i = 0; i < expected.size(); i++) {
                if (jsonDiffers(expected.get(i), actual.get(i))) {
                    return true;
                }
            }
            return false;
        }
        return !expected.equals(actual);
    }
}
