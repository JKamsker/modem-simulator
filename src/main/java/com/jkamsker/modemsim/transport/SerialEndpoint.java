package com.jkamsker.modemsim.transport;

import com.jkamsker.modemsim.state.ModemLines;

import java.io.IOException;

public interface SerialEndpoint extends AutoCloseable {
    void open(SerialConfig config) throws SerialException;

    SerialRead read() throws IOException;

    void write(byte[] buffer, int offset, int length) throws IOException;

    ModemLines readLines();

    void writeLines(ModemLines lines);

    @Override
    void close();
}
