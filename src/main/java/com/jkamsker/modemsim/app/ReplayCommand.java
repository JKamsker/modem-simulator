package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.monitor.InMemoryEventSink;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.replay.ReplayEventExpectation;
import com.jkamsker.modemsim.replay.ReplayPlayback;
import com.jkamsker.modemsim.replay.ReplayReport;
import com.jkamsker.modemsim.replay.ReplayStep;
import com.jkamsker.modemsim.replay.ReplayStepLoader;
import com.jkamsker.modemsim.replay.ReplayValidator;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;
import com.jkamsker.modemsim.transport.SerialEndpoint;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

final class ReplayCommand {
    private final PrintStream out;
    private final PrintStream err;

    ReplayCommand(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    int run(String[] args) {
        Path logPath = pathArg(args, 1);
        String mode = option(args, "--mode", "validate-recompute");
        List<ReplayStep> steps = new ReplayStepLoader().load(logPath);
        return switch (mode) {
            case "play-to-dte" -> playToDte(args, steps);
            case "validate-recompute", "drive-from-captured-input" -> validate(args, mode, steps);
            default -> unsupported(mode);
        };
    }

    private int validate(String[] args, String mode, List<ReplayStep> steps) {
        String profile = option(args, "--profile", "sierra-hl6-hl8-v20");
        long seed = longOption(args, "--seed", 12345L);
        String clock = firstExpected(steps, ReplayEventExpectation::clockMode, "virtual");
        String port = firstExpected(steps, ReplayEventExpectation::port, null);
        String role = firstExpected(steps, ReplayEventExpectation::portRole, null);
        HeadlessSession session = new HeadlessSession("replay", new ProfileResolver().resolve(profile), seed,
                new InMemoryEventSink(), MacroEngine.empty(), clock, port, role);
        ReplayReport report = new ReplayValidator().validateRecompute(session, steps, true);
        if (report.valid()) {
            out.println("REPLAY OK mode=" + mode + " steps=" + steps.size());
            return 0;
        }
        report.divergences().forEach(divergence -> err.println("DIVERGENCE: " + divergence));
        if (mode.equals("drive-from-captured-input") && hasFlag(args, "--confirm-divergence")) {
            out.println("REPLAY DIVERGENCE CONFIRMED mode=" + mode + " steps=" + steps.size());
            return 0;
        }
        return 1;
    }

    private int playToDte(String[] args, List<ReplayStep> steps) {
        ReplayReport hashReport = validateReport(args, "validate-recompute", steps);
        if (!hashReport.valid()) {
            hashReport.divergences().forEach(divergence -> err.println("DIVERGENCE: " + divergence));
            if (!hasFlag(args, "--confirm-divergence")) {
                return 1;
            }
            out.println("REPLAY DIVERGENCE CONFIRMED mode=play-to-dte steps=" + steps.size());
        }
        ReplayPlayback playback = new ReplayPlayback();
        ReplayReport report = playback.validate(steps);
        if (!report.valid()) {
            report.divergences().forEach(divergence -> err.println("DIVERGENCE: " + divergence));
            return 1;
        }
        String port = option(args, "--port", null);
        EndpointType endpointType = EndpointType.fromConfig(option(args, "--endpoint", port == null ? "headless" : "serial"));
        if (endpointType != EndpointType.HEADLESS && port == null) {
            err.println("Missing option: --port");
            return 2;
        }
        if (endpointType != EndpointType.HEADLESS && !hasFlag(args, "--confirm-unsafe")) {
            err.println("Missing confirmation: --confirm-unsafe is required for serial play-to-dte");
            return 2;
        }
        PortBinding binding = new PortBinding("replay-dte", endpointType, PortRole.MANUAL_DCE_INJECTION,
                port, true, null, "tagged-text");
        boolean timing = option(args, "--timing", "none").equals("recorded");
        Path auditLog = Path.of(option(args, "--audit-log", "runtime/replay/play-to-dte.jsonl"));
        try (RuntimeEventLog eventLog = RuntimeEventLog.open(auditLog, null);
             SerialEndpoint endpoint = DefaultEndpointFactory.create(binding)) {
            HeadlessSession audit = auditSession(args, steps, eventLog);
            audit.diagnostic(com.jkamsker.modemsim.monitor.EventType.REPLAY_MARKER, "play-to-dte-start");
            RawBytes written = playback.play(endpoint, serialConfig(args), steps, timing);
            audit.diagnostic(com.jkamsker.modemsim.monitor.EventType.REPLAY_MARKER, "play-to-dte-stop");
            out.println("REPLAY PLAY_TO_DTE steps=" + steps.size() + " outputHex=" + written.toHex() + " auditLog=" + auditLog);
            return 0;
        } catch (IOException e) {
            err.println("Replay write failed: " + e.getMessage());
            return 1;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            err.println("Replay interrupted");
            return 1;
        }
    }

    private int unsupported(String mode) {
        err.println("Unsupported replay mode for CLI: " + mode);
        return 2;
    }

    private ReplayReport validateReport(String[] args, String mode, List<ReplayStep> steps) {
        String profile = option(args, "--profile", "sierra-hl6-hl8-v20");
        long seed = longOption(args, "--seed", 12345L);
        String clock = firstExpected(steps, ReplayEventExpectation::clockMode, "virtual");
        String port = firstExpected(steps, ReplayEventExpectation::port, null);
        String role = firstExpected(steps, ReplayEventExpectation::portRole, null);
        HeadlessSession session = new HeadlessSession("replay", new ProfileResolver().resolve(profile), seed,
                new InMemoryEventSink(), MacroEngine.empty(), clock, port, role);
        return new ReplayValidator().validateRecompute(session, steps, true);
    }

    private HeadlessSession auditSession(String[] args, List<ReplayStep> steps, RuntimeEventLog eventLog) {
        String profile = option(args, "--profile", "sierra-hl6-hl8-v20");
        long seed = longOption(args, "--seed", 12345L);
        String clock = firstExpected(steps, ReplayEventExpectation::clockMode, "virtual");
        return new HeadlessSession("replay-play", new ProfileResolver().resolve(profile), seed,
                eventLog.sink(), MacroEngine.empty(), clock, "replay-dte", "manual-dce-injection");
    }

    private SerialConfig serialConfig(String[] args) {
        return new SerialConfig(intOption(args, "--baud", 115200), 8, 1, Parity.NONE, FlowControl.NONE);
    }

    private Path pathArg(String[] args, int index) {
        if (args.length <= index) {
            throw new IllegalArgumentException("Missing path argument");
        }
        return Path.of(args[index]);
    }

    private String firstExpected(
            List<ReplayStep> steps, Function<ReplayEventExpectation, String> mapper, String fallback) {
        return steps.stream().flatMap(step -> step.expectedEvents().stream())
                .map(mapper).filter(value -> value != null && !value.isBlank())
                .findFirst().orElse(fallback);
    }

    private int intOption(String[] args, String name, int fallback) {
        String value = option(args, name, null);
        return value == null ? fallback : Integer.parseInt(value);
    }

    private long longOption(String[] args, String name, long fallback) {
        String value = option(args, name, null);
        return value == null ? fallback : Long.parseLong(value);
    }

    private String option(String[] args, String name, String fallback) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(name)) {
                return args[i + 1];
            }
        }
        return fallback;
    }

    private boolean hasFlag(String[] args, String name) {
        return java.util.Arrays.asList(args).contains(name);
    }
}
