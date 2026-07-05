package com.jkamsker.modemsim.monitor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class JsonlEventSink implements EventSink, AutoCloseable {
    private final BufferedWriter writer;

    public JsonlEventSink(Path path) {
        try {
            Path parent = path.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            writer = Files.newBufferedWriter(path,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot open event log: " + path, e);
        }
    }

    @Override
    public synchronized void publish(ModemEvent event) {
        try {
            writer.write(ModemEventJson.toJson(event));
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new AuditLogException("Cannot write audit event", e);
        }
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot close event log", e);
        }
    }
}
