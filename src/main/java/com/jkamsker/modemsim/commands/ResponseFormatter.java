package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemState;

public final class ResponseFormatter {
    private final int s3;
    private final int s4;

    public ResponseFormatter(ModemState state) {
        this.s3 = state.settings().s3();
        this.s4 = state.settings().s4();
    }

    public RawBytes line(String line) {
        return RawBytes.copyOf(new byte[] {(byte) s3, (byte) s4})
                .append(RawBytes.ascii(line))
                .append(RawBytes.copyOf(new byte[] {(byte) s3, (byte) s4}));
    }

    public RawBytes prompt() {
        return RawBytes.copyOf(new byte[] {(byte) s3, (byte) s4, '>', ' '});
    }
}
