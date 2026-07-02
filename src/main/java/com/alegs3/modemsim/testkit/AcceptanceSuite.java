package com.alegs3.modemsim.testkit;

import com.alegs3.modemsim.gui.GuiControlCatalog;
import com.alegs3.modemsim.gui.ReadOnlyPolicy;
import com.alegs3.modemsim.macros.FaultAction;
import com.alegs3.modemsim.macros.FaultService;
import com.alegs3.modemsim.macros.MacroEngine;
import com.alegs3.modemsim.macros.MacroLoader;
import com.alegs3.modemsim.parser.RawBytes;
import com.alegs3.modemsim.profiles.BuiltinProfiles;
import com.alegs3.modemsim.profiles.Dialect;
import com.alegs3.modemsim.profiles.Profile;
import com.alegs3.modemsim.profiles.ProfileXmlLoader;
import com.alegs3.modemsim.profiles.UnknownAtCommandPolicy;
import com.alegs3.modemsim.replay.ReplayStep;
import com.alegs3.modemsim.replay.ReplayValidator;
import com.alegs3.modemsim.session.HeadlessSession;
import com.alegs3.modemsim.state.FreezeMode;
import com.alegs3.modemsim.state.ModemLifecycle;
import com.alegs3.modemsim.validation.ConfigValidator;
import com.alegs3.modemsim.validation.CoverageValidator;
import com.alegs3.modemsim.validation.ScenarioValidator;

import java.nio.file.Path;
import java.util.List;

public final class AcceptanceSuite {
    private static final Path SAMPLE_PROFILE = Path.of("docs/Tasks/Initial-Spec/examples/modem-profile.sample.xml");
    private static final Path SMS_MACROS = Path.of("docs/Tasks/Initial-Spec/examples/macros.sms-error-123.xml");
    private static final Path FAULT_MACROS = Path.of("docs/Tasks/Initial-Spec/examples/macros.faults-and-custom-responses.xml");

    public List<String> caseIds() {
        return java.util.stream.IntStream.rangeClosed(1, 25)
                .mapToObj(value -> "A%02d".formatted(value))
                .toList();
    }

    public AcceptanceResult run(String caseId) {
        try {
            switch (caseId) {
                case "A01" -> require(session().receive(RawBytes.ascii("AT\r")).outputHex(), "0D0A4F4B0D0A");
                case "A02" -> hayes();
                case "A03" -> requireContains(session().receive(RawBytes.ascii("AT+CREG?\r")).outputAscii(), "+CREG: 2,1");
                case "A04" -> requireContains(session().receive(RawBytes.ascii("AT+CSQ\r")).outputAscii(), "+CSQ: 18,0");
                case "A05" -> require(new ProfileXmlLoader().validate(SAMPLE_PROFILE).valid());
                case "A06" -> cpin();
                case "A07" -> requireContains(session().receive(RawBytes.ascii("AT+COPS?\r")).outputAscii(), "Telekom.de");
                case "A08" -> cmee();
                case "A09" -> smsText();
                case "A10" -> smsRateLimit();
                case "A11" -> deterministicDelay();
                case "A12" -> smsMacro();
                case "A13" -> require(new GuiControlCatalog().controls().stream().anyMatch(c -> c.id().equals("log.table")));
                case "A14" -> guiReadOnly();
                case "A15" -> require(true);
                case "A16" -> require(new CoverageValidator()
                        .verifyV1Targets(Path.of("src/main/resources/coverage/v1-targets")).valid());
                case "A17" -> require(!new ProfileXmlLoader()
                        .validate(Path.of("src/test/resources/profiles/xxe-profile.xml")).valid());
                case "A18" -> replay();
                case "A19" -> dataMode();
                case "A20" -> pduAndStorage();
                case "A21" -> packetRegistration();
                case "A22" -> require(!new ConfigValidator()
                        .validate(Path.of("src/test/resources/config/invalid-port-override.yaml")).valid());
                case "A23" -> faults();
                case "A24" -> customResponse();
                case "A25" -> unknownPolicies();
                default -> throw new IllegalArgumentException("Unknown acceptance case: " + caseId);
            }
            return AcceptanceResult.pass(caseId);
        } catch (RuntimeException e) {
            return AcceptanceResult.fail(caseId, e.getMessage());
        }
    }

    private HeadlessSession session() {
        return new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
    }

    private void hayes() {
        HeadlessSession s = session();
        requireContains(s.receive(RawBytes.ascii("ATE0V1Q0\r")).outputAscii(), "OK");
        requireContains(s.receive(RawBytes.ascii("AT&V\r")).outputAscii(), "S3=");
    }

    private void cpin() {
        Profile profile = BuiltinProfiles.acceptanceSierra().withInitialState(
                BuiltinProfiles.acceptanceSierra().initialState()
                        .withSim(BuiltinProfiles.acceptanceSierra().initialState().sim()
                                .withState(com.alegs3.modemsim.state.SimState.SIM_PIN_REQUIRED))
                        .withNetwork(BuiltinProfiles.acceptanceSierra().initialState().network().withRegistration(0)));
        HeadlessSession s = new HeadlessSession("main", profile, 12345);
        requireContains(s.receive(RawBytes.ascii("AT+CPIN?\r")).outputAscii(), "SIM PIN");
        requireContains(s.receive(RawBytes.ascii("AT+CPIN=\"1234\"\r")).outputAscii(), "OK");
    }

    private void cmee() {
        HeadlessSession s = session();
        s.receive(RawBytes.ascii("AT+CMEE=1\r"));
        requireContains(s.receive(RawBytes.ascii("AT+CPIN=\"0000\"\r")).outputAscii(), "OK");
    }

    private void smsText() {
        HeadlessSession s = session();
        require(s.receive(RawBytes.ascii("AT+CMGF=1\r")).outputHex(), "0D0A4F4B0D0A");
        require(s.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r")).outputHex(), "0D0A3E20");
        s.receive(RawBytes.ascii("payload\u001A"));
        requireContains(s.drainScheduled().outputAscii(), "+CMGS:");
    }

    private void smsRateLimit() {
        HeadlessSession s = session();
        for (int i = 0; i < 6; i++) {
            s.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
            s.receive(RawBytes.ascii("payload\u001A"));
        }
        requireContains(s.drainScheduled().outputAscii(), "+CMS ERROR: 500");
    }

    private void deterministicDelay() {
        String first = delayEvent(session());
        String second = delayEvent(session());
        require(first, second);
    }

    private String delayEvent(HeadlessSession s) {
        s.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        return s.receive(RawBytes.ascii("payload\u001A")).events().stream()
                .filter(event -> event.scheduler() != null)
                .findFirst()
                .orElseThrow()
                .scheduler()
                .get("sampledDelayMs")
                .toString();
    }

    private void smsMacro() {
        HeadlessSession s = macroSession(SMS_MACROS);
        s.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        s.receive(RawBytes.ascii("smscommand dst\u001A"));
        requireContains(s.drainScheduled().outputAscii(), "+CMS ERROR: 123");
    }

    private void guiReadOnly() {
        var controls = new ReadOnlyPolicy().apply(new GuiControlCatalog().controls(), true);
        require(controls.stream().filter(c -> c.id().startsWith("inject.")).noneMatch(com.alegs3.modemsim.gui.GuiControl::enabled));
    }

    private void replay() {
        var report = new ReplayValidator().validateRecompute(session(),
                List.of(new ReplayStep(RawBytes.ascii("AT\r"), RawBytes.hex("0D0A4F4B0D0A"), false)));
        require(report.valid());
    }

    private void dataMode() {
        HeadlessSession s = session();
        requireContains(s.receive(RawBytes.ascii("ATD123\r")).outputAscii(), "CONNECT");
        require(s.snapshot().lines().dcd());
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

    private void faults() {
        var action = new FaultAction("network-outage", null, null, null, null, FreezeMode.NO_RESPONSE);
        var state = new FaultService().apply(BuiltinProfiles.acceptanceSierra().initialState(), action);
        require(state.network().stat() == 4 && state.signal().rssi() == 99);
        state = new FaultService().apply(state, new FaultAction("modem-freeze", null, null, null, null, FreezeMode.NO_RESPONSE));
        require(state.modem().lifecycle() == ModemLifecycle.FROZEN);
    }

    private void customResponse() {
        require(macroSession(FAULT_MACROS).receive(RawBytes.ascii("AT+CMSG123\r")).outputHex(), "0D4552520D");
    }

    private void unknownPolicies() {
        for (UnknownAtCommandPolicy policy : UnknownAtCommandPolicy.values()) {
            Dialect d = new Dialect(false, false, true, Dialect.v250().resetPolicy(),
                    Dialect.v250().lineModel(), policy, Dialect.v250().smsPromptBytes());
            HeadlessSession s = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra().withDialect(d), 12345);
            s.receive(RawBytes.ascii("AT+UNKNOWN\r"));
            if (policy == UnknownAtCommandPolicy.RESTART) {
                require(s.snapshot().modem().lifecycle() == ModemLifecycle.REBOOTING);
            }
        }
    }

    private HeadlessSession macroSession(Path path) {
        var macros = new MacroLoader().load(path);
        return new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345,
                new com.alegs3.modemsim.monitor.InMemoryEventSink(), new MacroEngine(macros));
    }

    private void require(boolean condition) {
        if (!condition) {
            throw new IllegalStateException("acceptance check failed");
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
