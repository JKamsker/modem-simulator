package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.macros.MacroLoader;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.state.ModemLifecycle;

import java.nio.file.Path;

final class AcceptanceFaultChecks {
    private final Path faultMacros;

    AcceptanceFaultChecks(Path faultMacros) {
        this.faultMacros = faultMacros;
    }

    void run() {
        networkOutageRestore();
        rebootLifecycle();
        freezeBlocksResponses();
    }

    private void networkOutageRestore() {
        HeadlessSession s = session();
        s.receive(RawBytes.ascii("AT+TESTNETOUTAGE\r"));
        require(s.snapshot().network().stat() == 4 && s.snapshot().signal().rssi() == 99, "outage not applied");
        require(s.advanceTime(5_000).outputAscii().contains("OK"), "restore output missing");
        require(s.snapshot().network().stat() == 1 && s.snapshot().signal().rssi() == 18, "restore not applied");
    }

    private void rebootLifecycle() {
        HeadlessSession reboot = session();
        reboot.receive(RawBytes.ascii("AT+TESTREBOOT\r"));
        require(reboot.snapshot().modem().lifecycle() == ModemLifecycle.REBOOTING, "reboot not started");
        reboot.advanceTime(3_000);
        require(reboot.snapshot().modem().lifecycle() == ModemLifecycle.READY, "reboot did not complete");
        require(!reboot.snapshot().lines().dcd() && reboot.snapshot().lines().dsr(), "reboot lines not restored");
    }

    private void freezeBlocksResponses() {
        HeadlessSession frozen = session();
        require(frozen.receive(RawBytes.ascii("AT+TESTFREEZE\r")).outputAscii().isEmpty(), "freeze emitted output");
        require(frozen.snapshot().modem().lifecycle() == ModemLifecycle.FROZEN, "freeze not applied");
        require(frozen.receive(RawBytes.ascii("AT\r")).outputAscii().isEmpty(), "frozen modem responded");
    }

    private HeadlessSession session() {
        var macros = new MacroLoader().load(faultMacros);
        return new HeadlessSession("acceptance-faults", BuiltinProfiles.acceptanceSierra(), 12345,
                new com.jkamsker.modemsim.monitor.InMemoryEventSink(), new MacroEngine(macros));
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
