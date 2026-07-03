package com.jkamsker.modemsim.session;

@FunctionalInterface
interface ScheduledAction {
    ScheduledBatch run();
}
