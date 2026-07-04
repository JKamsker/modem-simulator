package com.jkamsker.modemsim.replay;

import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.parser.RawBytes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

final class TrmReplayParser {
    private static final Pattern LINE = Pattern.compile("^(RX|TX)\\s+\\d+\\s+\\d+\\s+\\((\\d+)\\):\\s?(.*)$");

    ReplayTranscript parse(String name, List<String> lines) {
        List<Entry> entries = entries(lines);
        List<ReplayStep> steps = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            if (entry.direction() == Direction.DTE_TO_DCE) {
                Entry tx = nextTx(entries, i);
                steps.add(new ReplayStep(
                        RawBytes.ascii(entry.payload()),
                        RawBytes.ascii(tx.payload()),
                        false,
                        List.of(txExpectation(tx))));
            }
        }
        return new ReplayTranscript(name, null, java.util.Map.of("format", "trm"), steps);
    }

    private List<Entry> entries(List<String> lines) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            java.util.regex.Matcher matcher = LINE.matcher(line);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Invalid TRM line " + (i + 1) + ": " + line);
            }
            entries.add(new Entry(direction(matcher.group(1)), Long.parseLong(matcher.group(2)), matcher.group(3)));
        }
        return entries;
    }

    private Entry nextTx(List<Entry> entries, int rxIndex) {
        for (int i = rxIndex + 1; i < entries.size(); i++) {
            if (entries.get(i).direction() == Direction.DCE_TO_DTE) {
                return entries.get(i);
            }
        }
        throw new IllegalArgumentException("TRM RX line has no following TX line");
    }

    private ReplayEventExpectation txExpectation(Entry tx) {
        return new ReplayEventExpectation(
                EventType.TX_BYTES,
                null,
                Direction.DCE_TO_DTE,
                null,
                null,
                null,
                tx.timestampMs() * 1_000_000L,
                RawBytes.ascii(tx.payload()).toHex(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                null,
                null);
    }

    private Direction direction(String value) {
        return value.equals("RX") ? Direction.DTE_TO_DCE : Direction.DCE_TO_DTE;
    }

    private record Entry(Direction direction, long timestampMs, String payload) {
    }
}
