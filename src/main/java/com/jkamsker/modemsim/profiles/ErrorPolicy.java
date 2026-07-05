package com.jkamsker.modemsim.profiles;

public record ErrorPolicy(
        String invalidParameter,
        String stateFailure,
        String smsFailure,
        String timeout
) {
    public static ErrorPolicy defaults() {
        return new ErrorPolicy("CME_OR_ERROR", "CME", "CMS", "NO_RESPONSE");
    }
}
