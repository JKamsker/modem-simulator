package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.app.InitialScenarioLoader;
import com.jkamsker.modemsim.app.RuntimeSessionLauncher;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.macros.FaultAction;
import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.macros.MacroLoader;
import com.jkamsker.modemsim.macros.MacroRule;
import com.jkamsker.modemsim.macros.MacroSet;
import com.jkamsker.modemsim.monitor.InMemoryEventSink;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.profiles.ProfileXmlLoader;
import com.jkamsker.modemsim.validation.ValidationReport;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.session.SessionResponse;
import com.jkamsker.modemsim.state.ModemState;

import java.nio.file.Path;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

final class GuiSessionController {
    private HeadlessSession session;
    private final GuiCommandQueue queue = new GuiCommandQueue();
    private final boolean allowUnsafeDceTransmit;
    private Profile activeProfile;
    private long activeSeed = 12345;
    private String activePort = "gui-headless";
    private MacroSet activeMacroSet = MacroSet.empty();
    private MacroEngine activeMacroEngine = MacroEngine.empty();
    private RuntimeSessionLauncher.RuntimeHandle runtimeHandle;
    private InMemoryEventSink eventSink;
    private final GuiReplayService replayService = new GuiReplayService();
    private final GuiEventExport eventExport = new GuiEventExport();
    private int eventCursor;
    private String runtimeStatus = "Headless session";
    private final Map<String, Boolean> macroOverrides = new LinkedHashMap<>();

    GuiSessionController(HeadlessSession session) {
        this(session, false);
    }

    GuiSessionController(HeadlessSession session, boolean allowUnsafeDceTransmit) {
        this.session = session;
        this.allowUnsafeDceTransmit = allowUnsafeDceTransmit;
        this.activeProfile = BuiltinProfiles.acceptanceSierra();
    }

    SessionResponse start(GuiSessionOptions options) {
        return enqueue("session-start", () -> {
            closeRuntime();
            eventSink = new InMemoryEventSink();
            eventCursor = 0;
            activeProfile = profileWithScenario(resolveProfile(options.profile()), options.initialScenario());
            activeSeed = options.seed();
            activePort = options.eventPort();
            activeMacroEngine = new MacroEngine(effectiveMacroSet());
            session = newHeadlessSession();
            if (options.startsRuntime()) {
                runtimeHandle = new RuntimeSessionLauncher().start(options.runtimeOptions(allowUnsafeDceTransmit), eventSink, ignored -> { });
                if (runtimeHandle.session() != null) {
                    session = runtimeHandle.session();
                }
                runtimeStatus = runtimeHandle.failure() == null ? "Session running" : "Port lost: " + runtimeHandle.failure().getMessage();
            } else {
                runtimeStatus = "Headless session";
            }
            return new SessionResponse(RawBytes.empty(), eventSink.events());
        });
    }

    SessionResponse stop() {
        return enqueue("session-stop", () -> {
            closeRuntime();
            runtimeStatus = "Session stopped";
            return session.stop("gui-stop");
        });
    }

    SessionResponse reconnect(GuiSessionOptions options) {
        stop();
        return start(options);
    }

    SessionResponse rawDteToDce(String text) {
        return enqueue("raw-dte-to-dce",
                () -> session.injectDte(RawBytes.ascii(unescape(text)), "raw-dte-to-dce"));
    }

    SessionResponse parsedCommand(String text) {
        return enqueue("parsed-command",
                () -> session.injectParsedCommand(RawBytes.ascii(ensureTerminator(text)), "parsed-command"));
    }

    SessionResponse rawDceToDte(String text, boolean confirmed) {
        if (!allowUnsafeDceTransmit || !confirmed) {
            throw new IllegalStateException("Unsafe DCE transmit is disabled");
        }
        return enqueue("raw-dce-to-dte", () -> {
            SessionResponse response = session.injectDce(RawBytes.ascii(unescape(text)), "raw-dce-to-dte");
            writeRuntimeDce(response.output());
            return response;
        });
    }

    SessionResponse urc(String text, boolean allowed) {
        return rawDceToDte(text + "\\r\\n", allowed);
    }

    SessionResponse applyState(GuiStatePatch patch) {
        return enqueue("state-change", () -> session.applyState(patch.apply(session.snapshot()), "state-change"));
    }

    SessionResponse fault(String type) {
        return enqueue("fault", () -> session.applyFault(type));
    }

    SessionResponse fault(FaultAction action) {
        return enqueue("fault", () -> session.applyFault(action));
    }

    ModemState snapshot() {
        return session.snapshot();
    }

    SessionResponse pollEvents() {
        if (eventSink == null) {
            return new SessionResponse(RawBytes.empty(), List.of());
        }
        List<ModemEvent> events = eventSink.eventsSince(eventCursor);
        eventCursor += events.size();
        return new SessionResponse(RawBytes.empty(), events);
    }

    String runtimeStatus() {
        if (runtimeHandle != null && runtimeHandle.failure() != null) {
            runtimeStatus = "Port lost: " + runtimeHandle.failure().getMessage();
        }
        return runtimeStatus;
    }

    String jsonl(List<ModemEvent> events) {
        return eventExport.jsonl(events);
    }

    String transcript(List<ModemEvent> events) {
        return eventExport.transcript(events);
    }

    String coverage(List<ModemEvent> events) {
        return eventExport.coverage(events);
    }

    String replayReport(List<ModemEvent> events) { return eventExport.replayReport(events); }

    String replayValidate(Path logPath) {
        return replay(logPath, "validate-recompute", true).message();
    }

    ReplaySummary replay(Path logPath, String mode, boolean virtualClock) {
        return replay(logPath, mode, virtualClock, false);
    }

    ReplaySummary replay(Path logPath, String mode, boolean virtualClock, boolean confirmed) {
        return replay(logPath, mode, virtualClock, confirmed, false);
    }

    ReplaySummary replay(Path logPath, String mode, boolean virtualClock, boolean confirmed, boolean divergenceConfirmed) {
        ReplaySummary summary = replayService.replay(session, logPath, mode, virtualClock, allowUnsafeDceTransmit,
                confirmed, divergenceConfirmed, activeProfile, activeSeed, activePort, activeMacroEngine);
        if (mode.equals("play-to-dte")) {
            writeRuntimeDce(summary.response().output());
        }
        return summary;
    }

    MacroSummary reloadMacros(Path path) {
        MacroLoader loader = new MacroLoader();
        ValidationReport report = loader.validate(path);
        if (!report.valid()) {
            String errors = String.join("; ", report.errors());
            SessionResponse response = enqueue("audit-failure",
                    () -> session.diagnostic(EventType.AUDIT_FAILURE, "macro-reload-failed:" + path + ":" + errors));
            return new MacroSummary("", errors, "", "", response);
        }
        activeMacroSet = loader.load(path);
        var ids = activeMacroSet.rules().stream().map(MacroRule::id).collect(Collectors.toSet());
        macroOverrides.keySet().retainAll(ids);
        activeMacroEngine = new MacroEngine(effectiveMacroSet());
        SessionResponse response = enqueue("macro-reload", () -> session.replaceMacroEngine(activeMacroEngine, "reload:" + path));
        String customResponses = activeMacroSet.rules().stream()
                .filter(this::isCustomResponse)
                .map(MacroRule::id)
                .collect(Collectors.joining(", "));
        return new MacroSummary(activeMacroSet.hash(), "", customResponses, enabledMacroIds(), response);
    }

    String macroToggleStatus(String action) {
        return macroToggleStatus(action, "");
    }

    String macroToggleStatus(String action, String macroId) {
        if (macroId == null || macroId.isBlank()) {
            return "Macro ID is required";
        }
        if (activeMacroSet.rules().stream().noneMatch(rule -> rule.id().equals(macroId))) {
            return "Unknown macro ID: " + macroId;
        }
        boolean enabled = action.equalsIgnoreCase("enable");
        macroOverrides.put(macroId, enabled);
        activeMacroEngine = new MacroEngine(effectiveMacroSet());
        queue.submit(new GuiSessionCommand(
                "macro-toggle", () -> session.replaceMacroEngine(activeMacroEngine, "macro-" + action + ":" + macroId)));
        return "Macro " + macroId + " " + (enabled ? "enabled" : "disabled");
    }

    private SessionResponse enqueue(String type, java.util.function.Supplier<SessionResponse> action) {
        InMemoryEventSink beforeSink = eventSink;
        int start = eventSink == null ? eventCursor : Math.min(eventCursor, eventSink.eventCount());
        SessionResponse response = queue.submit(new GuiSessionCommand(type, action));
        if (eventSink != null) {
            start = eventSink == beforeSink ? start : 0;
            List<ModemEvent> events = eventSink.eventsSince(start);
            eventCursor = eventSink.eventCount();
            return new SessionResponse(response.output(), events);
        }
        return response;
    }

    private HeadlessSession newHeadlessSession() {
        return new HeadlessSession(
                "gui", activeProfile, activeSeed, eventSink, activeMacroEngine,
                "virtual", activePort, "modem-simulation");
    }

    private boolean isCustomResponse(MacroRule rule) {
        return rule.actions().stream().anyMatch(action -> action.type().equals("send"));
    }

    private MacroSet effectiveMacroSet() {
        List<MacroRule> rules = activeMacroSet.rules().stream()
                .map(rule -> macroOverrides.containsKey(rule.id())
                        ? new MacroRule(rule.id(), rule.priority(), rule.order(), rule.phase(), macroOverrides.get(rule.id()),
                        rule.condition(), rule.match(), rule.actions())
                        : rule)
                .toList();
        return new MacroSet(MacroSet.effectiveHash(activeMacroSet.randomSeed(), rules), activeMacroSet.randomSeed(), rules);
    }

    private String enabledMacroIds() {
        return effectiveMacroSet().rules().stream().filter(MacroRule::enabled)
                .map(MacroRule::id).collect(Collectors.joining(", "));
    }

    private Profile profileWithScenario(Profile profile, Path scenario) {
        return profile.withInitialState(new InitialScenarioLoader().apply(scenario, profile.initialState()));
    }

    private Profile resolveProfile(String value) {
        if (value == null || value.isBlank()) {
            return BuiltinProfiles.acceptanceSierra();
        }
        Path path = Path.of(value);
        return Files.exists(path) ? new ProfileXmlLoader().load(path) : BuiltinProfiles.byId(value);
    }

    private void closeRuntime() {
        if (runtimeHandle != null) {
            runtimeHandle.close();
            runtimeHandle = null;
        }
    }

    private void writeRuntimeDce(RawBytes bytes) {
        if (runtimeHandle != null && runtimeHandle.running() && !bytes.isEmpty()) {
            runtimeHandle.writeDce(bytes);
        }
    }

    private String ensureTerminator(String value) {
        String unescaped = unescape(value);
        return unescaped.endsWith("\r") ? unescaped : unescaped + "\r";
    }

    private String unescape(String value) {
        return value.replace("\\r", "\r").replace("\\n", "\n").replace("\\u001A", "\u001A");
    }

}
