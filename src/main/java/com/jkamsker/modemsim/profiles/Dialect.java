package com.jkamsker.modemsim.profiles;

import com.jkamsker.modemsim.parser.RawBytes;

public record Dialect(
        boolean defaultEcho,
        boolean defaultQuiet,
        boolean defaultVerbose,
        int commandTerminator,
        int responseTerminator,
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
                13,
                10,
                ResetPolicy.NVRAM_ON_ATZ,
                LineModel.MINIMAL_V250,
                UnknownAtCommandPolicy.ERROR,
                RawBytes.hex("0D0A3E20"));
    }

    public Dialect withDefaultEcho(boolean value) {
        return new Dialect(value, defaultQuiet, defaultVerbose, commandTerminator, responseTerminator,
                resetPolicy, lineModel, unknownPolicy, smsPromptBytes);
    }
}
