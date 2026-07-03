package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.state.CallMode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProtocolRegressionTest {
    @Test
    void postGuardEscapeIsCancelledByImmediateDteByte() {
        HeadlessSession session = connectedSession();

        session.receive(RawBytes.ascii("+++"));
        session.receive(RawBytes.ascii("A"));

        assertThat(session.advanceTime(1_000).outputHex()).isEmpty();
        assertThat(session.snapshot().call().mode()).isEqualTo(CallMode.ONLINE_DATA);
    }

    @Test
    void s2RegisterSelectsEscapeCharacter() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
        session.receive(RawBytes.ascii("ATS2=35\r"));
        session.receive(RawBytes.ascii("ATD123\r"));
        session.drainScheduled();
        session.advanceTime(1_000);

        session.receive(RawBytes.ascii("+++"));
        assertThat(session.advanceTime(1_000).outputHex()).isEmpty();
        session.receive(RawBytes.ascii("###"));

        assertThat(session.advanceTime(1_000).outputAscii()).isEqualTo("\r\nOK\r\n");
        assertThat(session.snapshot().call().mode()).isEqualTo(CallMode.ONLINE_COMMAND);
    }

    @Test
    void c1RecomputesDcdFromCarrierState() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        session.receive(RawBytes.ascii("AT&C0\r"));
        assertThat(session.snapshot().lines().dcd()).isTrue();
        session.receive(RawBytes.ascii("AT&C1\r"));

        assertThat(session.snapshot().lines().dcd()).isFalse();
    }

    @Test
    void crlfBeforeLastFrameDoesNotPoisonRepeatCommand() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        assertThat(session.receive(RawBytes.ascii("AT\r\nAT\r")).outputAscii()).isEqualTo("\r\nOK\r\n\r\nOK\r\n");

        assertThat(session.receive(RawBytes.ascii("A/")).outputAscii()).isEqualTo("\r\nOK\r\n");
    }

    @Test
    void atzRestoresSettingsSavedByAtwForNvramPolicy() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);

        session.receive(RawBytes.ascii("ATS7=42\r"));
        session.receive(RawBytes.ascii("AT&W\r"));
        session.receive(RawBytes.ascii("ATS7=12\r"));
        session.receive(RawBytes.ascii("ATZ\r"));

        assertThat(session.snapshot().settings().s7()).isEqualTo(42);
    }

    @Test
    void atwPersistsSettingsToNvramJsonForLaterSessions() throws Exception {
        HeadlessSession session = new HeadlessSession("nvram-regression", BuiltinProfiles.acceptanceSierra(), 12345);

        session.receive(RawBytes.ascii("ATS7=41\r"));
        session.receive(RawBytes.ascii("AT&W\r"));
        HeadlessSession reloaded = new HeadlessSession("nvram-regression", BuiltinProfiles.acceptanceSierra(), 12345);
        reloaded.receive(RawBytes.ascii("ATZ\r"));

        assertThat(java.nio.file.Files.readString(java.nio.file.Path.of(
                "runtime", "sessions", "nvram-regression", "nvram.json"))).contains("\"s7\" : 41");
        assertThat(reloaded.snapshot().settings().s7()).isEqualTo(41);
    }

    @Test
    void escapeSequenceWithTerminatorIsNotAccepted() {
        HeadlessSession session = connectedSession();

        session.receive(RawBytes.ascii("+++\r"));

        assertThat(session.advanceTime(1_000).outputHex()).isEmpty();
        assertThat(session.snapshot().call().mode()).isEqualTo(CallMode.ONLINE_DATA);
    }

    @Test
    void errorStopsOnlyCurrentCommandLine() {
        HeadlessSession session = new HeadlessSession("line-abort", BuiltinProfiles.acceptanceSierra(), 12345);

        SessionResponse response = session.receive(RawBytes.ascii("AT+BOGUS;AT\rAT\r"));

        assertThat(response.outputAscii()).isEqualTo("\r\nERROR\r\n\r\nOK\r\n");
    }

    @Test
    void invalidHayesFlagsAndTerminatorRegistersReturnError() {
        HeadlessSession session = new HeadlessSession("hayes-validation", BuiltinProfiles.acceptanceSierra(), 12345);

        assertThat(session.receive(RawBytes.ascii("ATE2\r")).outputAscii()).isEqualTo("\r\nERROR\r\n");
        assertThat(session.receive(RawBytes.ascii("ATQ9\r")).outputAscii()).isEqualTo("\r\nERROR\r\n");
        assertThat(session.receive(RawBytes.ascii("ATV8\r")).outputAscii()).isEqualTo("\r\nERROR\r\n");
        assertThat(session.receive(RawBytes.ascii("ATS3=300\r")).outputAscii()).isEqualTo("\r\nERROR\r\n");
        assertThat(session.snapshot().settings().s3()).isEqualTo('\r');
        assertThat(session.receive(RawBytes.ascii("ATS3=59\r")).outputAscii()).isEqualTo(";\nOK;\n");
        assertThat(session.receive(RawBytes.ascii("ATS3=X;")).outputAscii()).isEqualTo(";\nERROR;\n");
        assertThat(session.snapshot().settings().s3()).isEqualTo(';');
        assertThat(session.receive(RawBytes.ascii("ATS999=1;")).outputAscii()).isEqualTo(";\nERROR;\n");
    }

    @Test
    void malformedLineReturnsErrorInsteadOfThrowing() {
        HeadlessSession session = new HeadlessSession("parse-error", BuiltinProfiles.acceptanceSierra(), 12345);

        assertThat(session.receive(RawBytes.ascii("BOGUS\r")).outputAscii()).isEqualTo("\r\nERROR\r\n");
        assertThat(session.receive(RawBytes.ascii("AT\r")).outputAscii()).isEqualTo("\r\nOK\r\n");
    }

    @Test
    void malformedLineHonorsQuietMode() {
        HeadlessSession session = new HeadlessSession("quiet-parse-error", BuiltinProfiles.acceptanceSierra(), 12345);

        session.receive(RawBytes.ascii("ATQ1\r"));

        assertThat(session.receive(RawBytes.ascii("BOGUS\r")).outputHex()).isEmpty();
    }

    @Test
    void numericFinalResultUsesV0TerminatorShape() {
        HeadlessSession session = new HeadlessSession("v0", BuiltinProfiles.acceptanceSierra(), 12345);

        assertThat(session.receive(RawBytes.ascii("ATV0\r")).outputHex()).isEqualTo("300D");
        assertThat(session.receive(RawBytes.ascii("AT\r")).outputHex()).isEqualTo("300D");
    }

    @Test
    void escapeResponseUsesNumericResultCodeInV0Mode() {
        HeadlessSession session = new HeadlessSession("escape-v0", BuiltinProfiles.acceptanceSierra(), 12345);
        session.receive(RawBytes.ascii("ATV0\r"));
        session.receive(RawBytes.ascii("ATD123\r"));
        session.drainScheduled();
        session.advanceTime(1_000);

        session.receive(RawBytes.ascii("+++"));

        assertThat(session.advanceTime(1_000).outputHex()).isEqualTo("300D");
    }

    private HeadlessSession connectedSession() {
        HeadlessSession session = new HeadlessSession("main", BuiltinProfiles.acceptanceSierra(), 12345);
        session.receive(RawBytes.ascii("ATD123\r"));
        session.drainScheduled();
        session.advanceTime(1_000);
        return session;
    }
}
