package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.monitor.ModemEventJson;
import com.jkamsker.modemsim.macros.MacroLoader;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.profiles.ProfileXmlLoader;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.validation.SchemaLocator;
import com.jkamsker.modemsim.validation.ValidationReport;
import com.jkamsker.modemsim.validation.XmlSecurity;

import java.nio.file.Path;
import java.util.Map;

final class AcceptanceProfileChecks {
    private final Path sampleProfile;

    AcceptanceProfileChecks(Path sampleProfile) {
        this.sampleProfile = sampleProfile;
    }

    void run() {
        ProfileXmlLoader loader = new ProfileXmlLoader();
        require(loader.validate(sampleProfile).valid(), "sample profile validation failed");
        Profile profile = loader.load(sampleProfile);
        require(profile.initialState().sim().pinRetries() == 3, "missing SIM retries");
        require(profile.initialState().network().operator().numeric().equals("26201"), "missing operator");
        require(profile.initialState().network().smsRateLimit().maxMessages() == 5, "missing SMS rate limit");
        require(profile.initialState().network().delays().containsKey("sms-submit"), "missing delay model");
        require(profile.initialState().lines().dsr() && profile.initialState().lines().cts(), "missing modem lines");
        String jsonl = ModemEventJson.toJsonLines(new HeadlessSession("profile-redaction", profile, 12345)
                .receive(RawBytes.ascii("AT+CPIN=\"9876\"\r")).events());
        require(!jsonl.contains("9876") && jsonl.contains("<redacted>"), "PIN leaked into event log");
        String pukJsonl = ModemEventJson.toJsonLines(new HeadlessSession("profile-puk-redaction",
                pukLocked(profile), 12345).receive(RawBytes.ascii("AT+CPIN=\"87654321\",\"2468\"\r")).events());
        require(!pukJsonl.contains("87654321") && !pukJsonl.contains("2468")
                && pukJsonl.contains("puk"), "PUK leaked into event log");
        runNegatives();
    }

    void runNegatives() {
        ProfileXmlLoader loader = new ProfileXmlLoader();
        Map<String, String> profiles = Map.of(
                "profile.pin-and-pinref.xml", "pin and pinRef",
                "profile.puk-and-pukref.xml", "puk and pukRef",
                "profile.sim-puk-required-without-puk.xml", "SIM_PUK_REQUIRED requires",
                "profile.delay-min-gt-max.xml", "delay minMs",
                "profile.operator-numeric-mismatch.xml", "operator numeric");
        profiles.forEach((file, expected) -> {
            Path path = negative(file);
            XmlSecurity.validate(path, SchemaLocator.schemaPath("modem-profile.schema.xsd"));
            ValidationReport report = loader.validate(path);
            require(!report.valid() && contains(report, expected), file + " did not fail for " + expected);
        });
        Path macro = negative("macros.jitter-without-session-seed.xml");
        XmlSecurity.validate(macro, SchemaLocator.schemaPath("macro-schema-draft.xsd"));
        ValidationReport report = new MacroLoader().validate(macro);
        require(!report.valid() && contains(report, "requires sessionSeed"), "macro jitter fixture did not fail");
    }

    void runBuiltins() {
        for (String id : BuiltinProfiles.ids()) {
            smoke(BuiltinProfiles.byId(id));
        }
    }

    private void smoke(Profile profile) {
        HeadlessSession session = new HeadlessSession("profile-" + profile.id(), profile, 12345);
        require(session.receive(RawBytes.ascii("AT\r")).outputAscii().contains("OK"), profile.id() + " AT failed");
        require(session.receive(RawBytes.ascii("ATI\r")).outputAscii().contains(profile.identity().manufacturer()),
                profile.id() + " identity failed");
        require(session.receive(RawBytes.ascii("AT&V\r")).outputAscii().contains("S3="),
                profile.id() + " config view failed");
        require(session.receive(RawBytes.ascii("AT+UNKNOWN\r")).outputAscii().contains("ERROR"), profile.id() + " error failed");
        HeadlessSession data = new HeadlessSession("profile-data-" + profile.id(), profile, 12345);
        String connect = data.receive(RawBytes.ascii("ATD123\r")).outputAscii() + data.drainScheduled().outputAscii();
        require(connect.contains("CONNECT"), profile.id() + " data mode failed");
        require(data.snapshot().lines().dcd(), profile.id() + " DCD not asserted");
        data.advanceTime(1_000);
        data.receive(RawBytes.ascii("+++"));
        data.advanceTime(1_000);
        require(data.receive(RawBytes.ascii("ATH\r")).outputAscii().contains("OK"), profile.id() + " hangup failed");
        require(!data.snapshot().lines().dcd(), profile.id() + " DCD not cleared");
        if (cellularCommands(profile)) {
            HeadlessSession cellular = new HeadlessSession("profile-cellular-" + profile.id(), profile, 12345);
            require(cellular.receive(RawBytes.ascii("AT+CPIN?\r")).outputAscii().contains("READY"), profile.id() + " CPIN failed");
            require(cellular.receive(RawBytes.ascii("AT+CREG?\r")).outputAscii().contains("+CREG"), profile.id() + " CREG failed");
            require(cellular.receive(RawBytes.ascii("AT+CSQ\r")).outputAscii().contains("+CSQ"), profile.id() + " CSQ failed");
            require(cellular.receive(RawBytes.ascii("AT+COPS?\r")).outputAscii().contains("+COPS"), profile.id() + " COPS failed");
            require(cellular.receive(RawBytes.ascii("AT+CGMI\r")).outputAscii().contains(profile.identity().manufacturer()),
                    profile.id() + " CGMI failed");
        } else {
            require(new HeadlessSession("profile-pstn-" + profile.id(), profile, 12345)
                    .receive(RawBytes.ascii("AT+CSQ\r")).outputAscii().contains("ERROR"),
                    profile.id() + " unexpectedly accepted cellular command");
        }
        if (smsCommands(profile)) {
            HeadlessSession sms = new HeadlessSession("profile-sms-" + profile.id(), profile, 12345);
            require(sms.receive(RawBytes.ascii("AT+CMGF=1\r")).outputAscii().contains("OK"), profile.id() + " CMGF failed");
            require(sms.receive(RawBytes.ascii("AT+CPMS?\r")).outputAscii().contains("+CPMS"), profile.id() + " CPMS failed");
            require(sms.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r")).outputHex().equals("0D0A3E20"),
                    profile.id() + " SMS prompt failed");
        }
    }

    private boolean cellularCommands(Profile profile) {
        return profile.profileKind().equals("cellular")
                || profile.profileKind().equals("hybrid")
                || profile.id().equals("3gpp-27007-r18");
    }

    private boolean smsCommands(Profile profile) {
        return profile.profileKind().equals("cellular")
                || profile.profileKind().equals("hybrid")
                || profile.id().equals("3gpp-27005-r16");
    }

    private Path negative(String file) {
        return SchemaLocator.projectPath("docs/Tasks/Initial-Spec/examples/negative/" + file);
    }

    private Profile pukLocked(Profile profile) {
        var base = profile.initialState();
        return profile.withInitialState(base
                .withSim(base.sim().withState(SimState.SIM_PUK_REQUIRED))
                .withNetwork(base.network().withRegistration(0)));
    }

    private boolean contains(ValidationReport report, String text) {
        return report.errors().stream().anyMatch(error -> error.contains(text));
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
