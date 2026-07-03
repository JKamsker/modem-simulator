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
        require(session.receive(RawBytes.ascii("AT+UNKNOWN\r")).outputAscii().contains("ERROR"), profile.id() + " error failed");
        HeadlessSession data = new HeadlessSession("profile-data-" + profile.id(), profile, 12345);
        String connect = data.receive(RawBytes.ascii("ATD123\r")).outputAscii() + data.drainScheduled().outputAscii();
        require(connect.contains("CONNECT"), profile.id() + " data mode failed");
        if (profile.profileKind().equals("cellular") || profile.profileKind().equals("hybrid")) {
            HeadlessSession sms = new HeadlessSession("profile-sms-" + profile.id(), profile, 12345);
            require(sms.receive(RawBytes.ascii("AT+CPIN?\r")).outputAscii().contains("READY"), profile.id() + " CPIN failed");
            require(sms.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r")).outputHex().equals("0D0A3E20"),
                    profile.id() + " SMS prompt failed");
        }
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
