package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.NetworkDelay;

final class SessionDelayScheduler {
    private SessionDelayScheduler() {
    }

    static RawBytes schedule(
            SessionSchedulerBridge scheduler, String operation, RawBytes payload, ModemState state) {
        NetworkDelay delay = state.network() == null ? null : state.network().delays().get(operation);
        return scheduler.scheduleOrReturn(operation, payload, delay, state);
    }

    static RawBytes schedule(
            SessionSchedulerBridge scheduler, String operation, RawBytes payload, int delayMs, ModemState state) {
        NetworkDelay delay = delayMs <= 0 ? null : new NetworkDelay(operation, delayMs, delayMs);
        return scheduler.scheduleOrReturn(operation, payload, delay, state);
    }

    static RawBytes schedule(
            SessionSchedulerBridge scheduler, String operation, RawBytes payload, NetworkDelay delay, ModemState state) {
        return scheduler.scheduleOrReturn(operation, payload, delay, state);
    }

    static ScheduledPayload scheduleWithMetadata(
            SessionSchedulerBridge scheduler, String operation, RawBytes payload, NetworkDelay delay, ModemState state) {
        return scheduler.scheduleWithMetadata(operation, payload, delay, state);
    }
}
