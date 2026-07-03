package com.jkamsker.modemsim.monitor;

public final class ModemEventCopies {
    private ModemEventCopies() {
    }

    public static ModemEvent withDroppedEventCount(ModemEvent event, long droppedEventCount) {
        return new ModemEvent(
                event.timestamp(), event.monotonicNanos(), event.sequence(), event.sessionId(),
                event.eventType(), event.direction(), event.rawHex(), event.textEscaped(),
                event.parsedCommand(), event.profile(), event.port(), event.portRole(),
                event.profileHash(), event.configHash(), event.macroHash(), event.initialStateHash(),
                event.sessionSeed(), event.clockMode(), event.macroId(), event.latencyMs(),
                event.injectionType(), droppedEventCount, event.replayDivergent(), event.handler(),
                event.result(), event.scheduler(), event.stateBefore(), event.stateAfter(), event.redaction());
    }

    public static ModemEvent droppedEventsSummary(ModemEvent source, long sequence, long droppedEventCount) {
        return new ModemEvent(
                source.timestamp(), source.monotonicNanos(), sequence, source.sessionId(),
                EventType.DROPPED_EVENTS, Direction.NONE, "", null, null,
                source.profile(), source.port(), source.portRole(), source.profileHash(),
                source.configHash(), source.macroHash(), source.initialStateHash(), source.sessionSeed(),
                source.clockMode(), null, null, null, droppedEventCount, false, null,
                "dropped-events", null, null, null, RedactionInfo.none());
    }
}
