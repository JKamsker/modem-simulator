package com.jkamsker.modemsim.state;

import java.util.LinkedHashMap;
import java.util.Map;

public record NetworkRuntime(
        int cregN,
        int stat,
        String lac,
        String ci,
        Integer act,
        Integer rejectCauseType,
        Integer rejectCause,
        OperatorInfo operator,
        SmsRateLimit smsRateLimit,
        Map<String, NetworkDelay> delays
) {
    public NetworkRuntime {
        delays = Map.copyOf(delays);
    }

    public static NetworkRuntime registered() {
        Map<String, NetworkDelay> delays = new LinkedHashMap<>();
        delays.put("sms-submit", new NetworkDelay("sms-submit", 500, 2500));
        delays.put("dial", new NetworkDelay("dial", 1000, 5000));
        return new NetworkRuntime(2, 1, "00C3", "00001234", 7, null, null,
                OperatorInfo.telekom(), new SmsRateLimit(5, 60, "session", 500), delays);
    }

    public boolean registeredForCircuitServices() {
        return stat == 1 || stat == 5;
    }

    public NetworkRuntime withCregN(int value) {
        return new NetworkRuntime(value, stat, lac, ci, act, rejectCauseType, rejectCause,
                operator, smsRateLimit, delays);
    }

    public NetworkRuntime withRegistration(int value) {
        if (value == 1 || value == 5) {
            NetworkRuntime fallback = registered();
            return new NetworkRuntime(cregN, value, lac == null ? fallback.lac() : lac,
                    ci == null ? fallback.ci() : ci, act == null ? fallback.act() : act,
                    rejectCauseType, rejectCause, operator, smsRateLimit, delays);
        }
        return new NetworkRuntime(cregN, value, null, null, null, rejectCauseType, rejectCause,
                operator, smsRateLimit, delays);
    }
}
