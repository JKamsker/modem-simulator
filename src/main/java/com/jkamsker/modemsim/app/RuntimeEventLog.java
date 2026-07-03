package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.monitor.EventSink;
import com.jkamsker.modemsim.monitor.JsonlEventSink;

import java.nio.file.Path;

final class RuntimeEventLog implements AutoCloseable {
    private static final Path DEFAULT_LOG = Path.of("runtime", "sessions", "main", "events.jsonl");

    private final EventSink sink;
    private final AutoCloseable closeable;

    private RuntimeEventLog(EventSink sink, AutoCloseable closeable) {
        this.sink = sink;
        this.closeable = closeable;
    }

    static RuntimeEventLog open(Path path, EventSink override) {
        if (override != null) {
            return new RuntimeEventLog(override, () -> { });
        }
        JsonlEventSink jsonl = new JsonlEventSink(path == null ? DEFAULT_LOG : path);
        return new RuntimeEventLog(jsonl, jsonl);
    }

    EventSink sink() {
        return sink;
    }

    @Override
    public void close() {
        try {
            closeable.close();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot close event log", e);
        }
    }
}
