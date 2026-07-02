package com.alegs3.modemsim.transport;

import com.alegs3.modemsim.parser.RawBytes;
import com.alegs3.modemsim.state.ModemLines;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

public final class HeadlessEndpoint implements SerialEndpoint {
    private final Queue<SerialRead> reads = new ArrayDeque<>();
    private RawBytes written = RawBytes.empty();
    private ModemLines lines = ModemLines.ready();

    @Override
    public void open(SerialConfig config) {
    }

    public void enqueueRead(RawBytes bytes, long nowNanos) {
        reads.add(new SerialRead(bytes, nowNanos, nowNanos));
    }

    @Override
    public SerialRead read() throws IOException {
        SerialRead read = reads.poll();
        if (read == null) {
            throw new IOException("No headless input available");
        }
        return read;
    }

    @Override
    public void write(byte[] buffer, int offset, int length) {
        byte[] copy = java.util.Arrays.copyOfRange(buffer, offset, offset + length);
        written = written.append(RawBytes.copyOf(copy));
    }

    @Override
    public ModemLines readLines() {
        return lines;
    }

    @Override
    public void writeLines(ModemLines lines) {
        this.lines = lines;
    }

    public RawBytes written() {
        return written;
    }

    @Override
    public void close() {
    }
}
