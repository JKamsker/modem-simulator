package com.jkamsker.modemsim.profiles;

import com.jkamsker.modemsim.parser.RawBytes;

public record Dialect(
        boolean defaultEcho,
        boolean defaultQuiet,
        boolean defaultVerbose,
        ResetPolicy resetPolicy,
        LineModel lineModel,
        UnknownAtCommandPolicy unknownPolicy,
        RawBytes smsPromptBytes
) {
    public static Dialect v250() {
        return new Dialect(
                false,
                false,
                true,
                ResetPolicy.NVRAM_ON_ATZ,
                LineModel.MINIMAL_V250,
                UnknownAtCommandPolicy.ERROR,
                RawBytes.hex("0D0A3E20"));
    }
}
