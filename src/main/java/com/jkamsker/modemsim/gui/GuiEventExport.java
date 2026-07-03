package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.monitor.ModemEventJson;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class GuiEventExport {
    String jsonl(List<ModemEvent> events) {
        return ModemEventJson.toJsonLines(events);
    }

    String transcript(List<ModemEvent> events) {
        return events.stream()
                .filter(event -> event.eventType() == EventType.RX_BYTES || event.eventType() == EventType.TX_BYTES)
                .filter(event -> !event.rawHex().isBlank())
                .map(event -> event.direction() + " " + event.rawHex())
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");
    }

    String coverage(List<ModemEvent> events) {
        Map<String, Long> counts = events.stream().collect(Collectors.groupingBy(
                event -> event.eventType().name(), java.util.TreeMap::new, Collectors.counting()));
        return counts.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("\n"));
    }

    String replayReport(List<ModemEvent> events) {
        long replayMarkers = events.stream().filter(event -> event.eventType() == EventType.REPLAY_MARKER).count();
        long schedulerEvents = events.stream().filter(event -> event.eventType().name().startsWith("SCHEDULER_")).count();
        return "events=" + events.size() + "\nreplayMarkers=" + replayMarkers + "\nschedulerEvents=" + schedulerEvents;
    }
}
