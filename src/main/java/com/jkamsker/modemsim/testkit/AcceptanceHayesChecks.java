package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;

final class AcceptanceHayesChecks {
    void run() {
        expect("ATE0\r", "\r\nOK\r\n");
        expect("ATE1\r", "\r\nOK\r\n");
        expect("ATQ0\r", "\r\nOK\r\n");
        expect("ATQ1\r", "");
        expect("ATV0\r", "0\r");
        expect("ATV1\r", "\r\nOK\r\n");
        expect("ATZ\r", "\r\nOK\r\n");
        expect("AT&F\r", "\r\nOK\r\n");
        expect("AT&W\r", "\r\nOK\r\n");
        String view = session().receive(RawBytes.ascii("AT&V\r")).outputAscii();
        require(view.contains("E0 Q0 V1") && view.contains("S3=13") && view.endsWith("\r\nOK\r\n"), view);
    }

    private void expect(String command, String expected) {
        String actual = session().receive(RawBytes.ascii(command)).outputAscii();
        require(actual.equals(expected), command + " expected " + expected + " but got " + actual);
    }

    private HeadlessSession session() {
        return new HeadlessSession("acceptance-hayes", BuiltinProfiles.byId("generic-hayes-v250"), 12345);
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
