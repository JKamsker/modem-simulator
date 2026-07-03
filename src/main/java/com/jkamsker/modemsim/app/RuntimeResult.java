package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.parser.RawBytes;

record RuntimeResult(String sessionId, int readsProcessed, RawBytes output) {
}
