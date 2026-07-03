package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.macros.MacroLoader;
import com.jkamsker.modemsim.monitor.EventSink;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.transport.HeadlessEndpoint;
import com.jkamsker.modemsim.transport.SerialEndpoint;
import com.jkamsker.modemsim.transport.SerialException;
import com.jkamsker.modemsim.transport.SerialOverflowException;
import com.jkamsker.modemsim.transport.SerialRead;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

final class ModemRuntime {
    private final EndpointFactory endpointFactory;
    private final ProfileResolver profileResolver = new ProfileResolver();
    private final RuntimeIo io = new RuntimeIo();

    ModemRuntime() {
        this(DefaultEndpointFactory::create);
    }

    ModemRuntime(EndpointFactory endpointFactory) {
        this.endpointFactory = endpointFactory;
    }

    RuntimeResult run(RuntimeConfig config, List<RawBytes> headlessInputs, int maxReads) {
        return run(config, headlessInputs, maxReads, null, session -> { });
    }

    RuntimeResult run(
            RuntimeConfig config, List<RawBytes> headlessInputs, int maxReads,
            EventSink eventSink, Consumer<HeadlessSession> sessionReady) {
        return run(config, headlessInputs, maxReads, eventSink, sessionReady, new java.util.concurrent.ConcurrentLinkedQueue<>());
    }

    RuntimeResult run(
            RuntimeConfig config, List<RawBytes> headlessInputs, int maxReads,
            EventSink eventSink, Consumer<HeadlessSession> sessionReady, java.util.Queue<RawBytes> dceWrites) {
        PortBinding modemPort = config.modemPort();
        Profile profile = profileResolver.resolve(modemPort.profile());
        Path scenario = modemPort.initialScenario() == null ? config.initialScenario() : modemPort.initialScenario();
        InitialScenarioLoader scenarios = new InitialScenarioLoader();
        profile = profile.withInitialState(scenarios.apply(scenario, profile.initialState()));
        List<InitialScenarioStep> scenarioSteps = scenarios.delayedSteps(scenario);
        MacroEngine macros = config.macros() == null ? MacroEngine.empty()
                : new MacroEngine(new MacroLoader(timerIds(config.macroTimers())).load(config.macros()));
        SerialEndpoint modemEndpoint = endpointFactory.create(modemPort);
        List<RuntimeSidecar> sidecars = new ArrayList<>();
        try (RuntimeEventLog eventLog = RuntimeEventLog.open(config.eventLogPath(), eventSink); modemEndpoint) {
            HeadlessSession session = new HeadlessSession(
                    "main", profile, config.sessionSeed(), eventLog.sink(), macros, config.clockMode().name().toLowerCase(),
                    modemPort.id(), modemPort.role().configName(), RuntimeFingerprints.config(config));
            modemEndpoint.open(config.serialLine());
            modemEndpoint.writeLines(session.snapshot().lines());
            sessionReady.accept(session);
            openSidecars(config, session, sidecars);
            enqueueHeadlessInputs(modemEndpoint, headlessInputs);
            RuntimeResult result = processReads(
                    session, modemEndpoint, modemPort, sidecars, maxReads, scenarioSteps, config.macroTimers(), dceWrites);
            session.stop("normal-stop");
            return new RuntimeResult(result.sessionId(), result.readsProcessed(), result.output(), config.eventLogPath());
        } catch (IOException e) {
            throw new IllegalStateException("Runtime failed: " + e.getMessage(), e);
        } finally {
            closeSidecars(sidecars);
        }
    }

    private RuntimeResult processReads(
            HeadlessSession session, SerialEndpoint endpoint, PortBinding binding,
            List<RuntimeSidecar> sidecars, int maxReads,
            List<InitialScenarioStep> scenarioSteps, List<RuntimeTimer> timers, java.util.Queue<RawBytes> dceWrites)
            throws IOException {
        RawBytes output = RawBytes.empty();
        int reads = 0;
        int nextScenarioStep = 0;
        int nextTimer = 0;
        long nowMs = 0;
        Long startedAtNanos = null;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                output = output.append(io.processSidecars(session, endpoint, sidecars));
                output = output.append(io.processQueuedDce(session, endpoint, sidecars, dceWrites));
                output = output.append(io.processLineInputs(session, endpoint, sidecars));
            } catch (IOException e) {
                return handlePortLoss(session, endpoint, binding, output, reads, e);
            }
            DueProgress due = processDue(session, endpoint, sidecars, scenarioSteps, nextScenarioStep, timers, nextTimer, nowMs);
            nextScenarioStep = due.nextScenarioStep();
            nextTimer = due.nextTimer();
            output = output.append(due.output());
            if (maxReads >= 0 && reads >= maxReads) {
                break;
            }
            SerialRead read;
            try {
                read = io.read(endpoint);
            } catch (SerialOverflowException e) {
                session.diagnostic(EventType.RX_OVERFLOW, "rx-overflow:" + binding.id() + ":" + e.getMessage());
                continue;
            } catch (IOException e) {
                return handlePortLoss(session, endpoint, binding, output, reads, e);
            }
            if (read == null) {
                Long nextDueMs = nextDueMs(scenarioSteps, nextScenarioStep, timers, nextTimer);
                if (nextDueMs != null) {
                    SessionResponse dueResponse = session.advanceTo(nextDueMs * 1_000_000L);
                    io.writeResponse(endpoint, session, sidecars, dueResponse);
                    output = output.append(dueResponse.output());
                    nowMs = nextDueMs;
                    continue;
                }
                break;
            }
            long elapsedNanos;
            long elapsedLastNanos;
            try {
                startedAtNanos = startedAtNanos == null ? read.firstByteMonotonicNanos() : startedAtNanos;
                elapsedNanos = Math.max(0, read.firstByteMonotonicNanos() - startedAtNanos);
                elapsedLastNanos = Math.max(elapsedNanos, read.lastByteMonotonicNanos() - startedAtNanos);
                nowMs = elapsedNanos / 1_000_000L;
                SessionResponse scheduled = session.advanceTo(elapsedNanos);
                io.writeResponse(endpoint, session, sidecars, scheduled);
                output = output.append(scheduled.output());
                DueProgress readDue = processDue(
                        session, endpoint, sidecars, scenarioSteps, nextScenarioStep, timers, nextTimer, nowMs);
                nextScenarioStep = readDue.nextScenarioStep();
                nextTimer = readDue.nextTimer();
                output = output.append(readDue.output());
            } catch (IOException e) {
                return handlePortLoss(session, endpoint, binding, output, reads, e);
            }
            if (read.bytes().isEmpty()) {
                continue;
            }
            io.mirror(session, sidecars, com.jkamsker.modemsim.monitor.Direction.DTE_TO_DCE, read.bytes());
            SessionResponse response = session.receiveTimed(read.bytes(), elapsedNanos, elapsedLastNanos);
            try {
                io.writeResponse(endpoint, session, sidecars, response);
            } catch (IOException e) {
                return handlePortLoss(session, endpoint, binding, output, reads, e);
            }
            output = output.append(response.output());
            reads++;
        }
        return new RuntimeResult("main", reads, output);
    }

    private DueProgress processDue(
            HeadlessSession session, SerialEndpoint endpoint, List<RuntimeSidecar> sidecars,
            List<InitialScenarioStep> scenarioSteps, int nextScenarioStep,
            List<RuntimeTimer> timers, int nextTimer, long nowMs) throws IOException {
        RawBytes output = RawBytes.empty();
        ScenarioProgress scenarioProgress = processScenarioSteps(
                session, endpoint, sidecars, scenarioSteps, nextScenarioStep, nowMs);
        output = output.append(scenarioProgress.output());
        ScenarioProgress timerProgress = processTimers(session, endpoint, sidecars, timers, nextTimer, nowMs);
        output = output.append(timerProgress.output());
        return new DueProgress(scenarioProgress.next(), timerProgress.next(), output);
    }

    private Long nextDueMs(
            List<InitialScenarioStep> scenarioSteps, int nextScenarioStep, List<RuntimeTimer> timers, int nextTimer) {
        Long next = null;
        if (nextScenarioStep < scenarioSteps.size()) {
            next = scenarioSteps.get(nextScenarioStep).atMs();
        }
        if (nextTimer < timers.size()) {
            long timerMs = timers.get(nextTimer).atMs();
            next = next == null ? timerMs : Math.min(next, timerMs);
        }
        return next;
    }

    private ScenarioProgress processTimers(
            HeadlessSession session, SerialEndpoint endpoint, List<RuntimeSidecar> sidecars,
            List<RuntimeTimer> timers, int next, long nowMs) throws IOException {
        RawBytes output = RawBytes.empty();
        while (next < timers.size() && timers.get(next).atMs() <= nowMs) {
            SessionResponse response = session.fireTimer(timers.get(next).id());
            io.writeResponse(endpoint, session, sidecars, response);
            output = output.append(response.output());
            next++;
        }
        return new ScenarioProgress(next, output);
    }

    private ScenarioProgress processScenarioSteps(
            HeadlessSession session, SerialEndpoint endpoint, List<RuntimeSidecar> sidecars,
            List<InitialScenarioStep> steps, int next, long nowMs) throws IOException {
        RawBytes output = RawBytes.empty();
        while (next < steps.size() && steps.get(next).atMs() <= nowMs) {
            SessionResponse response = steps.get(next).apply(session);
            io.writeResponse(endpoint, session, sidecars, response);
            output = output.append(response.output());
            next++;
        }
        return new ScenarioProgress(next, output);
    }

    private record ScenarioProgress(int next, RawBytes output) { }

    private record DueProgress(int nextScenarioStep, int nextTimer, RawBytes output) { }

    private RuntimeResult handlePortLoss(
            HeadlessSession session, SerialEndpoint endpoint, PortBinding binding,
            RawBytes output, int reads, IOException e) {
        session.portLost("port-lost:" + binding.id() + ":" + e.getMessage());
        endpoint.writeLines(session.snapshot().lines());
        return new RuntimeResult("main", reads, output);
    }

    private java.util.Set<String> timerIds(List<RuntimeTimer> timers) {
        return timers.stream().map(RuntimeTimer::id).collect(java.util.stream.Collectors.toSet());
    }

    private void enqueueHeadlessInputs(SerialEndpoint endpoint, List<RawBytes> inputs) {
        if (!(endpoint instanceof HeadlessEndpoint headless)) {
            return;
        }
        long now = 0;
        for (RawBytes input : inputs) {
            headless.enqueueRead(input, now++);
        }
    }

    private void openSidecars(RuntimeConfig config, HeadlessSession session, List<RuntimeSidecar> sidecars)
            throws SerialException {
        for (PortBinding binding : config.enabledSidecars()) {
            if (binding.role() == PortRole.MANUAL_DCE_INJECTION && !config.allowUnsafeDceTransmit()) {
                session.diagnostic(EventType.AUDIT_FAILURE, "manual-dce-disabled:" + binding.id());
                if (config.strictOptionalPorts()) {
                    throw new SerialException("manual-dce-injection requires allowUnsafeDceTransmit");
                }
                continue;
            }
            SerialEndpoint endpoint = endpointFactory.create(binding);
            try {
                endpoint.open(config.serialLine());
                sidecars.add(new RuntimeSidecar(binding, endpoint));
            } catch (SerialException e) {
                endpoint.close();
                session.diagnostic(EventType.PORT_LOST, "optional-port-open-failed:" + binding.id() + ":" + e.getMessage());
                if (config.strictOptionalPorts()) {
                    throw e;
                }
            }
        }
    }

    private void closeSidecars(List<RuntimeSidecar> sidecars) {
        for (RuntimeSidecar sidecar : sidecars) {
            sidecar.close();
        }
    }
}
