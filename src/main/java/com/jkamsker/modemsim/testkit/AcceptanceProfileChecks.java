package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.monitor.ModemEventJson;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.profiles.ProfileXmlLoader;
import com.jkamsker.modemsim.session.HeadlessSession;

import java.nio.file.Path;

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

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
