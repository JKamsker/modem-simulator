package com.alegs3.modemsim.scheduler;

public final class VirtualClock {
    private long nowNanos;

    public long nowNanos() {
        return nowNanos;
    }

    public void advanceMillis(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Cannot move virtual clock backwards");
        }
        nowNanos += millis * 1_000_000L;
    }

    public void advanceTo(long dueNanos) {
        if (dueNanos > nowNanos) {
            nowNanos = dueNanos;
        }
    }
}
