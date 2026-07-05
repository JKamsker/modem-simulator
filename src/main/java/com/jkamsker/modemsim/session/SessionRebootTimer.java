package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.scheduler.VirtualClock;
import com.jkamsker.modemsim.state.ModemLifecycle;
import com.jkamsker.modemsim.state.ModemRuntimeInfo;
import com.jkamsker.modemsim.state.ModemState;

final class SessionRebootTimer {
    private final VirtualClock clock;
    private final SessionEventPublisher events;
    private long readyAtNanos = -1;

    SessionRebootTimer(VirtualClock clock, SessionEventPublisher events) {
        this.clock = clock;
        this.events = events;
    }

    void arm(ModemState state) {
        readyAtNanos = state.modem().lifecycle() == ModemLifecycle.REBOOTING && state.modem().bootDelayMs() >= 0
                ? clock.nowNanos() + state.modem().bootDelayMs() * 1_000_000L
                : -1;
    }

    ModemState completeIfDue(ModemState state) {
        if (readyAtNanos < 0 || clock.nowNanos() < readyAtNanos) {
            return state;
        }
        ModemState ready = state.withModem(ModemRuntimeInfo.ready())
                .withLines(state.lines().withDsr(true).withDcd(false));
        readyAtNanos = -1;
        events.publishAudit(EventType.STATE_CHANGE, Direction.INTERNAL, "modem-reboot-complete",
                "ready", RawBytes.empty(), state, ready);
        return ready;
    }
}
