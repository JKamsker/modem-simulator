package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.parser.RawBytes;

import java.nio.file.Path;

record RuntimeResult(String sessionId, int readsProcessed, RawBytes output, Path eventLogPath) {
    RuntimeResult(String sessionId, int readsProcessed, RawBytes output) {
        this(sessionId, readsProcessed, output, null);
    }
}
