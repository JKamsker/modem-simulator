package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.macros.MacroLoader;
import com.jkamsker.modemsim.monitor.ModemEventJson;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.app.RuntimePortGroupAcceptance;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.profiles.Dialect;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.profiles.ProfileXmlLoader;
import com.jkamsker.modemsim.profiles.UnknownAtCommandPolicy;
import com.jkamsker.modemsim.replay.ReplayStepLoader;
import com.jkamsker.modemsim.replay.ReplayValidator;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.ModemLifecycle;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.validation.ConfigValidator;
import com.jkamsker.modemsim.validation.CoverageValidator;
import com.jkamsker.modemsim.validation.SchemaLocator;
import com.jkamsker.modemsim.validation.ScenarioValidator;

import java.nio.file.Path;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class AcceptanceSuite {
    private static final Path SAMPLE_PROFILE = spec("docs/Tasks/Initial-Spec/examples/modem-profile.sample.xml");
    private static final Path SMS_MACROS = spec("docs/Tasks/Initial-Spec/examples/macros.sms-error-123.xml");
    private static final Path FAULT_MACROS = spec("docs/Tasks/Initial-Spec/examples/macros.faults-and-custom-responses.xml");

    public List<String> caseIds() {
        return java.util.stream.IntStream.rangeClosed(1, 25)
                .mapToObj(value -> "A%02d".formatted(value))
                .toList();
    }

    public AcceptanceResult run(String caseId) {
        String normalized = AcceptanceCaseRegistry.normalize(caseId);
        try {
            switch (normalized) {
                case "A01" -> require(session().receive(RawBytes.ascii("AT\r")).outputHex(), "0D0A4F4B0D0A");
                case "A02" -> new AcceptanceHayesChecks().run();
                case "A03" -> requireContains(session().receive(RawBytes.ascii("AT+CREG?\r")).outputAscii(), "+CREG: 2,1");
                case "A04" -> requireContains(session().receive(RawBytes.ascii("AT+CSQ\r")).outputAscii(), "+CSQ: 18,0");
                case "A05" -> new AcceptanceProfileChecks(SAMPLE_PROFILE).run();
                case "A06" -> cpin();
                case "A07" -> requireContains(session().receive(RawBytes.ascii("AT+COPS?\r")).outputAscii(), "Telekom.de");
                case "A08" -> cmee();
                case "A09" -> smsText();
                case "A10" -> new AcceptanceTimingChecks().smsRateLimit();
                case "A11" -> new AcceptanceTimingChecks().deterministicDelay();
                case "A12" -> smsMacro();
                case "A13" -> new GuiAcceptanceChecks().liveLog();
                case "A14" -> new GuiAcceptanceChecks().injection();
                case "A15" -> noControlApi();
                case "A16" -> {
                    require(new CoverageValidator().verifyV1Targets(spec("src/main/resources/coverage/v1-targets")).valid());
                    new AcceptanceProfileChecks(SAMPLE_PROFILE).runBuiltins();
                }
                case "A17" -> require(!new ProfileXmlLoader()
                        .validate(Path.of("src/test/resources/profiles/xxe-profile.xml")).valid());
                case "A18" -> replay();
                case "A19" -> dataMode();
                case "A20" -> pduAndStorage();
                case "A21" -> packetRegistration();
                case "A22" -> portGroup();
                case "A23" -> new AcceptanceFaultChecks(FAULT_MACROS).run();
                case "A24" -> customResponse();
                case "A25" -> unknownPolicies();
                default -> throw new IllegalArgumentException("Unknown acceptance case: " + caseId);
            }
            return AcceptanceResult.pass(caseId);
        } catch (RuntimeException e) {
            return AcceptanceResult.fail(caseId, e.getMessage());
        }
    }

    public String defaultCaseForSuite(String suite) {
        return AcceptanceCaseRegistry.defaultCaseForSuite(suite);
    }

    private HeadlessSession session() {
        return new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
    }

    private void cpin() {
        Profile profile = BuiltinProfiles.acceptanceSierra().withInitialState(
                BuiltinProfiles.acceptanceSierra().initialState()
                        .withSim(BuiltinProfiles.acceptanceSierra().initialState().sim()
                                .withState(SimState.SIM_PIN_REQUIRED))
                        .withNetwork(BuiltinProfiles.acceptanceSierra().initialState().network().withRegistration(0)));
        HeadlessSession s = new HeadlessSession("main", profile, 12345);
        requireContains(s.receive(RawBytes.ascii("AT+CPIN?\r")).outputAscii(), "SIM PIN");
        requireContains(s.receive(RawBytes.ascii("AT+CPIN=\"0000\"\r")).outputAscii(), "ERROR");
        require(s.snapshot().sim().pinRetries() == 2);
        requireContains(s.receive(RawBytes.ascii("AT+CPIN=\"1234\"\r")).outputAscii(), "OK");
        require(s.snapshot().sim().state() == SimState.READY);
        require(s.snapshot().network().stat() == 0);
    }

    private void cmee() {
        require(cmeeOutput(0), "\r\nERROR\r\n");
        requireContains(cmeeOutput(1), "+CME ERROR: 13");
        requireContains(cmeeOutput(2), "+CME ERROR: SIM failure");
    }

    private void smsText() {
        HeadlessSession s = session();
        require(s.receive(RawBytes.ascii("AT+CMGF=1\r")).outputHex(), "0D0A4F4B0D0A");
        require(s.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r")).outputHex(), "0D0A3E20");
        s.receive(RawBytes.ascii("payload\u001A"));
        requireContains(s.drainScheduled().outputAscii(), "+CMGS:");
    }

    private void smsMacro() {
        HeadlessSession s = macroSession(SMS_MACROS);
        s.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        s.receive(RawBytes.ascii("smscommand dst\u001A"));
        requireContains(s.drainScheduled().outputAscii(), "+CMS ERROR: 123");
    }

    private void noControlApi() {
        var hits = forbiddenControlApiReferences();
        require(hits.isEmpty());
        String httpToken = "http";
        String wsToken = "web" + "socket";
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            String name = thread.getName().toLowerCase();
            require(!name.contains(httpToken) && !name.contains(wsToken));
        }
    }

    private List<String> forbiddenControlApiReferences() {
        return new ForbiddenApiScanner().scan();
    }

    private void replay() {
        var steps = new ReplayStepLoader().load(spec("src/test/resources/replay/basic-at-events.jsonl"));
        var report = new ReplayValidator().validateRecompute(session(), steps, true);
        require(report.valid(), String.join("; ", report.divergences()));
        require(schedulerReplayValid());
    }

    private boolean schedulerReplayValid() {
        try {
            HeadlessSession capture = session();
            var events = new ArrayList<>(capture.receive(RawBytes.ascii("ATD123\r")).events());
            events.addAll(capture.drainScheduled().events());
            Path log = Files.createTempFile("modemsim-scheduler-replay", ".jsonl");
            Files.writeString(log, ModemEventJson.toJsonLines(events));
            var report = new ReplayValidator().validateRecompute(session(), new ReplayStepLoader().load(log), true);
            if (!report.valid()) {
                throw new IllegalStateException(String.join("; ", report.divergences()));
            }
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("scheduler replay failed: " + e.getMessage(), e);
        }
    }

    private void dataMode() {
        HeadlessSession s = session();
        s.receive(RawBytes.ascii("ATD123\r"));
        require(s.snapshot().call().mode() == CallMode.DIALING);
        requireContains(s.drainScheduled().outputAscii(), "CONNECT");
        require(s.snapshot().lines().dcd());
        s.advanceTime(1_000);
        require(s.receive(RawBytes.ascii("+++")).outputAscii().isEmpty());
        requireContains(s.advanceTime(1_000).outputAscii(), "OK");
        requireContains(s.receive(RawBytes.ascii("ATH\r")).outputAscii(), "OK");
        require(!s.snapshot().lines().dcd());
    }

    private void pduAndStorage() {
        HeadlessSession s = session();
        s.receive(RawBytes.ascii("AT+CMGF=0\r"));
        s.receive(RawBytes.ascii("AT+CMGS=4\r"));
        s.receive(RawBytes.ascii("0011\u001A"));
        s.drainScheduled();
        requireContains(s.receive(RawBytes.ascii("AT+CMGL=\"ALL\"\r")).outputAscii(), "+CMGL");
    }

    private void packetRegistration() {
        requireContains(session().receive(RawBytes.ascii("AT+CGREG?\r")).outputAscii(), "+CGREG");
        requireContains(session().receive(RawBytes.ascii("AT+CEREG?\r")).outputAscii(), "+CEREG");
    }

    private void portGroup() {
        ConfigValidator validator = new ConfigValidator();
        require(validator.validate(spec("src/test/resources/config/valid.yaml")).valid());
        require(!validator.validate(spec("src/test/resources/config/invalid-port-override.yaml")).valid());
        require(!validator.validate(spec("src/test/resources/config/invalid-manual-dce-permission.yaml")).valid());
        new RuntimePortGroupAcceptance().run();
    }

    private void customResponse() {
        require(macroSession(FAULT_MACROS).receive(RawBytes.ascii("AT+CMSG123\r")).outputHex(), "0D4552520D");
    }

    private void unknownPolicies() {
        for (UnknownAtCommandPolicy policy : UnknownAtCommandPolicy.values()) {
            Dialect d = new Dialect(false, false, true,
                    Dialect.v250().commandTerminator(), Dialect.v250().responseTerminator(),
                    Dialect.v250().resetPolicy(), Dialect.v250().lineModel(),
                    policy, Dialect.v250().smsPromptBytes());
            HeadlessSession s = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra().withDialect(d), 12345);
            String output = s.receive(RawBytes.ascii("AT+UNKNOWN\r")).outputAscii();
            switch (policy) {
                case OK -> requireContains(output, "OK");
                case ERR -> requireContains(output, "ERR");
                case ERROR -> requireContains(output, "ERROR");
                case RESTART -> {
                    require(output.isEmpty());
                    require(s.snapshot().modem().lifecycle() == ModemLifecycle.REBOOTING);
                }
            }
        }
    }

    private String cmeeOutput(int mode) {
        Profile base = BuiltinProfiles.acceptanceSierra();
        Profile profile = base.withInitialState(base.initialState().withSim(base.initialState().sim().withState(SimState.SIM_FAILURE)));
        HeadlessSession s = new HeadlessSession("main", profile, 12345);
        s.receive(RawBytes.ascii("AT+CMEE=" + mode + "\r"));
        return s.receive(RawBytes.ascii("AT+CPIN?\r")).outputAscii();
    }

    private HeadlessSession macroSession(Path path) {
        var macros = new MacroLoader().load(path);
        return new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345,
                new com.jkamsker.modemsim.monitor.InMemoryEventSink(), new MacroEngine(macros));
    }

    private static Path spec(String path) {
        return SchemaLocator.projectPath(path);
    }

    private void require(boolean condition) {
        if (!condition) {
            throw new IllegalStateException("acceptance check failed");
        }
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message.isBlank() ? "acceptance check failed" : message);
        }
    }

    private void require(String actual, String expected) {
        if (!actual.equals(expected)) {
            throw new IllegalStateException("expected " + expected + " but got " + actual);
        }
    }

    private void requireContains(String actual, String expected) {
        if (!actual.contains(expected)) {
            throw new IllegalStateException("expected output to contain " + expected + " but got " + actual);
        }
    }
}
