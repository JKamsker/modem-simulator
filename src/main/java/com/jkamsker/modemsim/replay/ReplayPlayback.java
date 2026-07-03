package com.jkamsker.modemsim.replay;

import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.transport.SerialConfig;
import com.jkamsker.modemsim.transport.SerialEndpoint;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public final class ReplayPlayback {
    public ReplayReport validate(List<ReplayStep> steps) {
        ReplayReport report = new ReplayReport();
        for (int i = 0; i < steps.size(); i++) {
            validateStep(i + 1, steps.get(i), report);
        }
        return report;
    }

    public RawBytes combinedOutput(List<ReplayStep> steps) {
        return steps.stream()
                .map(ReplayStep::expectedOutput)
                .reduce(RawBytes.empty(), RawBytes::append);
    }

    public RawBytes play(
            SerialEndpoint endpoint, SerialConfig config, List<ReplayStep> steps, boolean recordedTiming)
            throws IOException, InterruptedException {
        endpoint.open(config);
        RawBytes written = RawBytes.empty();
        Long previousNanos = null;
        for (ReplayStep step : steps) {
            List<ReplayEventExpectation> events = txEvents(step);
            if (events.isEmpty()) {
                write(endpoint, step.expectedOutput());
                written = written.append(step.expectedOutput());
                continue;
            }
            for (ReplayEventExpectation event : events) {
                Long nextNanos = event.monotonicNanos();
                if (recordedTiming && previousNanos != null && nextNanos != null && nextNanos > previousNanos) {
                    sleepNanos(nextNanos - previousNanos);
                }
                RawBytes bytes = RawBytes.hex(event.rawHex());
                write(endpoint, bytes);
                written = written.append(bytes);
                previousNanos = nextNanos == null ? previousNanos : nextNanos;
            }
        }
        return written;
    }

    private void write(SerialEndpoint endpoint, RawBytes bytes) throws IOException {
        byte[] raw = bytes.toByteArray();
        endpoint.write(raw, 0, raw.length);
    }

    private void validateStep(int stepNumber, ReplayStep step, ReplayReport report) {
        if (step.expectedEvents().isEmpty()) {
            report.divergence("step " + stepNumber + " missing event metadata");
        }
        for (ReplayEventExpectation event : step.expectedEvents()) {
            validateEvent(stepNumber, event, report);
        }
        RawBytes eventOutput = txEvents(step).stream()
                .map(event -> event.rawHex() == null ? RawBytes.empty() : RawBytes.hex(event.rawHex()))
                .reduce(RawBytes.empty(), RawBytes::append);
        if (!step.expectedOutput().toHex().equals(eventOutput.toHex())) {
            report.divergence("step " + stepNumber + " TX metadata does not match expected output");
        }
    }

    private void validateEvent(int stepNumber, ReplayEventExpectation event, ReplayReport report) {
        if (Boolean.TRUE.equals(event.rawPayloadRedacted())) {
            report.divergence("step " + stepNumber + " contains redacted replay bytes " + event.eventType());
        }
        requireHash(stepNumber, "profileHash", event.profileHash(), report);
        requireHash(stepNumber, "configHash", event.configHash(), report);
        requireHash(stepNumber, "initialStateHash", event.initialStateHash(), report);
        requireHash(stepNumber, "macroHash", event.macroHash(), report);
        requirePresent(stepNumber, "sessionSeed", event.sessionSeed(), report);
        requirePresent(stepNumber, "clockMode", event.clockMode(), report);
        if (event.eventType() == EventType.TX_BYTES && event.direction() == Direction.DCE_TO_DTE) {
            requirePresent(stepNumber, "monotonicNanos", event.monotonicNanos(), report);
            requirePresent(stepNumber, "rawHex", event.rawHex(), report);
        }
    }

    private List<ReplayEventExpectation> txEvents(ReplayStep step) {
        return step.expectedEvents().stream()
                .filter(event -> event.eventType() == EventType.TX_BYTES && event.direction() == Direction.DCE_TO_DTE)
                .sorted(Comparator.comparing(event -> event.monotonicNanos() == null ? Long.MAX_VALUE : event.monotonicNanos()))
                .toList();
    }

    private void requireHash(int stepNumber, String name, String value, ReplayReport report) {
        if (value == null || !value.startsWith("sha256:")) {
            report.divergence("step " + stepNumber + " missing " + name);
        }
    }

    private void requirePresent(int stepNumber, String name, Object value, ReplayReport report) {
        if (value == null || (value instanceof String text && text.isBlank())) {
            report.divergence("step " + stepNumber + " missing " + name);
        }
    }

    private void sleepNanos(long nanos) throws InterruptedException {
        long millis = nanos / 1_000_000L;
        int remainder = (int) (nanos % 1_000_000L);
        Thread.sleep(millis, remainder);
    }
}
