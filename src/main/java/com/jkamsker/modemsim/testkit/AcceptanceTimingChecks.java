package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;

final class AcceptanceTimingChecks {
    void smsRateLimit() {
        HeadlessSession s = session();
        for (int i = 0; i < 5; i++) {
            submit(s, "accepted-" + i);
        }
        submit(s, "blocked");
        require(s.drainScheduled().outputAscii().contains("+CMS ERROR: 500"), "sixth SMS was not rate-limited");
        s.advanceTime(60_001);
        submit(s, "accepted-after-window");
        require(s.drainScheduled().outputAscii().contains("+CMGS:"), "SMS window did not reset");
    }

    void deterministicDelay() {
        HeadlessSession smsA = session();
        HeadlessSession smsB = session();
        HeadlessSession dialA = session();
        HeadlessSession dialB = session();
        int firstSms = sampledDelay(smsA, "sms-submit", () -> submit(smsA, "payload"));
        int secondSms = sampledDelay(smsB, "sms-submit", () -> submit(smsB, "payload"));
        int firstDial = sampledDelay(dialA, "dial", () -> dialA.receive(RawBytes.ascii("ATD123\r")));
        int secondDial = sampledDelay(dialB, "dial", () -> dialB.receive(RawBytes.ascii("ATD123\r")));
        require(firstSms == secondSms && firstSms >= 500 && firstSms <= 2500, "SMS delay not deterministic/in range");
        require(firstDial == secondDial && firstDial >= 1000 && firstDial <= 5000, "dial delay not deterministic/in range");
    }

    private int sampledDelay(HeadlessSession session, String operation, Runnable trigger) {
        trigger.run();
        return session.drainScheduled().events().stream()
                .filter(event -> event.scheduler() != null)
                .filter(event -> operation.equals(event.scheduler().get("operation")))
                .map(event -> ((Number) event.scheduler().get("sampledDelayMs")).intValue())
                .findFirst()
                .orElseGet(() -> session.receive(RawBytes.empty()).events().stream()
                        .filter(event -> event.scheduler() != null)
                        .map(event -> ((Number) event.scheduler().get("sampledDelayMs")).intValue())
                        .findFirst().orElseThrow());
    }

    private void submit(HeadlessSession s, String body) {
        s.receive(RawBytes.ascii("AT+CMGS=\"+491701234567\"\r"));
        s.receive(RawBytes.ascii(body + "\u001A"));
    }

    private HeadlessSession session() {
        return new HeadlessSession("acceptance-timing", BuiltinProfiles.acceptanceSierra(), 12345);
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
