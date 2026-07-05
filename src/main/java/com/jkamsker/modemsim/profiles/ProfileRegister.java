package com.jkamsker.modemsim.profiles;

public record ProfileRegister(
        String name,
        int defaultValue,
        Integer min,
        Integer max,
        boolean writable,
        boolean persistent
) {
}
