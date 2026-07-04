package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.profiles.ProfileXmlLoader;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.state.NetworkRuntime;
import com.jkamsker.modemsim.state.SignalRuntime;

import java.nio.file.Files;

final class AcceptanceCellularChecks {
    void creg() {
        HeadlessSession registered = session(BuiltinProfiles.acceptanceSierra());
        requireContains(registered.receive(RawBytes.ascii("AT+CREG=2\r")).outputAscii(), "OK");
        require(registered.receive(RawBytes.ascii("AT+CREG?\r")).outputAscii(),
                "\r\n+CREG: 2,1,\"00C3\",\"00001234\",7\r\n\r\nOK\r\n");

        HeadlessSession denied = session(deniedRegistrationProfile());
        require(denied.receive(RawBytes.ascii("AT+CREG?\r")).outputAscii(),
                "\r\n+CREG: 3,3,0,11\r\n\r\nOK\r\n");
    }

    void csq() {
        require(session(BuiltinProfiles.acceptanceSierra()).receive(RawBytes.ascii("AT+CSQ\r")).outputAscii(),
                "\r\n+CSQ: 18,0\r\n\r\nOK\r\n");
        Profile noNetwork = BuiltinProfiles.acceptanceSierra().withInitialState(
                BuiltinProfiles.acceptanceSierra().initialState()
                        .withNetwork(BuiltinProfiles.acceptanceSierra().initialState().network().withRegistration(0))
                        .withSignal(SignalRuntime.unknown()));
        require(session(noNetwork).receive(RawBytes.ascii("AT+CSQ\r")).outputAscii(),
                "\r\n+CSQ: 99,99\r\n\r\nOK\r\n");
        requireInvalidRssiProfileRejected();
    }

    private Profile deniedRegistrationProfile() {
        Profile base = BuiltinProfiles.acceptanceSierra();
        NetworkRuntime network = base.initialState().network();
        NetworkRuntime denied = new NetworkRuntime(3, 3, null, null, null, null, null,
                network.operator(), network.smsRateLimit(), network.delays());
        return base.withInitialState(base.initialState().withNetwork(denied));
    }

    private void requireInvalidRssiProfileRejected() {
        try {
            var path = Files.createTempFile("modemsim-invalid-rssi-profile", ".xml");
            path.toFile().deleteOnExit();
            Files.writeString(path, """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <modem-simulator version="1.0">
                      <profile id="bad-rssi" vendor="test" status="candidate" profileKind="cellular">
                        <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                        <initial-state>
                          <sim state="READY"/>
                          <network cregN="2" stat="1"/>
                          <signal rssi="32" ber="0"/>
                          <call mode="command" carrier="false"/>
                          <modem-lines dtr="true" dsr="true" dcd="false" ri="false" rts="true" cts="true"/>
                        </initial-state>
                      </profile>
                    </modem-simulator>
                    """);
            var report = new ProfileXmlLoader().validate(path);
            require(!report.valid(), "invalid RSSI profile unexpectedly passed validation");
            require(report.errors().stream().anyMatch(error -> error.contains("CsqRssiType")),
                    "invalid RSSI profile failed for the wrong reason: " + report.errors());
        } catch (java.io.IOException e) {
            throw new IllegalStateException("invalid RSSI acceptance fixture failed", e);
        }
    }

    private HeadlessSession session(Profile profile) {
        return new HeadlessSession("acceptance-cellular", profile, 12345);
    }

    private void require(String actual, String expected) {
        if (!actual.equals(expected)) {
            throw new IllegalStateException("expected " + expected + " but got " + actual);
        }
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private void requireContains(String actual, String expected) {
        if (!actual.contains(expected)) {
            throw new IllegalStateException("expected output to contain " + expected + " but got " + actual);
        }
    }
}
