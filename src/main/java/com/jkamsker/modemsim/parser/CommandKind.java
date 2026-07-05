package com.jkamsker.modemsim.parser;

public enum CommandKind {
    BASIC,
    EXTENDED_EXEC,
    EXTENDED_SET,
    EXTENDED_READ,
    EXTENDED_TEST,
    S_REGISTER_READ,
    S_REGISTER_WRITE,
    SPECIAL_REPEAT,
    SPECIAL_ESCAPE
}
