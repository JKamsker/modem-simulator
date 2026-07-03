package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.monitor.InMemoryEventSink;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.replay.ReplayPlayback;
import com.jkamsker.modemsim.replay.ReplayReport;
import com.jkamsker.modemsim.replay.ReplayStep;
import com.jkamsker.modemsim.replay.ReplayStepLoader;
import com.jkamsker.modemsim.replay.ReplayValidator;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

final class GuiReplayService {
    GuiSessionController.ReplaySummary replay(
            HeadlessSession session, Path logPath, String mode, boolean virtualClock, boolean unsafeAllowed,
            boolean confirmed, Profile profile, long seed, String port, MacroEngine macros) {
        List<ReplayStep> steps = new ReplayStepLoader().load(logPath);
        return switch (mode) {
            case "drive-from-captured-input" -> drive(session, steps, virtualClock, profile, seed, port, macros);
            case "play-to-dte" -> play(session, steps, unsafeAllowed, confirmed, virtualClock, profile, seed, port, macros);
            default -> validate(steps, mode, virtualClock, profile, seed, port, macros);
        };
    }

    private GuiSessionController.ReplaySummary validate(
            List<ReplayStep> steps, String mode, boolean virtualClock, Profile profile, long seed,
            String port, MacroEngine macros) {
        ReplayReport report = validationReport(steps, virtualClock, profile, seed, port, macros);
        return summary(report, report.valid() ? mode.toUpperCase() + " OK steps=" + steps.size()
                : "DIVERGENCE: " + String.join("; ", report.divergences()), emptyResponse());
    }

    private GuiSessionController.ReplaySummary drive(
            HeadlessSession session, List<ReplayStep> steps, boolean virtualClock, Profile profile, long seed,
            String port, MacroEngine macros) {
        ReplayReport report = validationReport(steps, virtualClock, profile, seed, port, macros);
        if (!report.valid()) {
            return summary(report, "DIVERGENCE: " + String.join("; ", report.divergences()), emptyResponse());
        }
        return new GuiSessionController.ReplaySummary("hashes valid",
                "DRIVE_FROM_CAPTURED_INPUT OK steps=" + steps.size(), driveSteps(session, steps));
    }

    private GuiSessionController.ReplaySummary play(
            HeadlessSession session, List<ReplayStep> steps, boolean unsafeAllowed, boolean confirmed,
            boolean virtualClock, Profile profile, long seed, String port, MacroEngine macros) {
        if (!unsafeAllowed || !confirmed) {
            throw new IllegalStateException("Unsafe DCE transmit is disabled");
        }
        ReplayReport hashReport = validationReport(steps, virtualClock, profile, seed, port, macros);
        if (!hashReport.valid()) {
            return summary(hashReport, "DIVERGENCE: " + String.join("; ", hashReport.divergences()), emptyResponse());
        }
        ReplayPlayback playback = new ReplayPlayback();
        ReplayReport report = playback.validate(steps);
        if (!report.valid()) {
            return summary(report, "DIVERGENCE: " + String.join("; ", report.divergences()), emptyResponse());
        }
        RawBytes bytes = playback.combinedOutput(steps);
        SessionResponse response = session.injectDce(bytes, "replay");
        return new GuiSessionController.ReplaySummary("hashes valid", "PLAY_TO_DTE bytes=" + bytes.toHex(), response);
    }

    private ReplayReport validationReport(
            List<ReplayStep> steps, boolean virtualClock, Profile profile, long seed, String port, MacroEngine macros) {
        var replaySession = new HeadlessSession("gui-replay", profile, seed,
                new InMemoryEventSink(), macros, virtualClock ? "virtual" : "monotonic", port, "modem-simulation");
        return new ReplayValidator().validateRecompute(replaySession, steps, !steps.isEmpty()
                && !steps.getFirst().expectedEvents().isEmpty());
    }

    private SessionResponse driveSteps(HeadlessSession session, List<ReplayStep> steps) {
        RawBytes output = RawBytes.empty();
        var events = new ArrayList<com.jkamsker.modemsim.monitor.ModemEvent>();
        for (ReplayStep step : steps) {
            SessionResponse response = session.injectDte(step.input(), "replay");
            output = output.append(response.output());
            events.addAll(response.events());
        }
        return new SessionResponse(output, events);
    }

    private GuiSessionController.ReplaySummary summary(
            ReplayReport report, String message, SessionResponse response) {
        return new GuiSessionController.ReplaySummary(report.valid() ? "hashes valid" : "hash divergence",
                message, response);
    }

    private SessionResponse emptyResponse() {
        return new SessionResponse(RawBytes.empty(), List.of());
    }
}
