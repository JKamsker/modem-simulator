package com.alegs3.modemsim.state;

public record SmsRateLimit(int maxMessages, int windowSeconds, String scope, int rejectCmsError) {
    public static SmsRateLimit none() {
        return new SmsRateLimit(0, 0, "session", 500);
    }

    public boolean enabled() {
        return maxMessages > 0 && windowSeconds > 0;
    }
}
