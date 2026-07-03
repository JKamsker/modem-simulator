package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.ResponseFormatter;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.CallRuntime;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SmsRateLimit;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public final class SmsSubmitProcessor {
    private final Map<String, Deque<Long>> acceptedSubmitNanos = new HashMap<>();

    public SmsSubmitResult submit(PendingSms pending, String body, long nowNanos, ModemState state) {
        ResponseFormatter formatter = new ResponseFormatter(state);
        ModemState commandMode = state.withCall(CallRuntime.command());
        Integer cmsError = validateSubmit(pending, body, commandMode, nowNanos);
        if (cmsError != null) {
            return new SmsSubmitResult(commandMode, formatter.line("+CMS ERROR: " + cmsError), "CMS_ERROR");
        }
        bucket(commandMode, pending).addLast(nowNanos);
        int reference = commandMode.sms().nextMessageReference();
        ModemState stored = commandMode.withSms(commandMode.sms().storeOutbound(
                pending.destination(), pending.pduMode() ? null : body, pending.pduMode() ? body : null));
        RawBytes response = formatter.line("+CMGS: " + reference)
                .append(formatter.line("OK"));
        return new SmsSubmitResult(stored, response, "OK");
    }

    public SmsSubmitResult abort(ModemState state) {
        ModemState commandMode = state.withCall(CallRuntime.command());
        RawBytes response = new ResponseFormatter(commandMode).line("OK");
        return new SmsSubmitResult(commandMode, response, "OK");
    }

    private Integer validateSubmit(PendingSms pending, String body, ModemState state, long nowNanos) {
        if (state.sim().state() != SimState.READY) {
            return 500;
        }
        if (state.network() == null || !state.network().registeredForCircuitServices()) {
            return state.network() == null ? 500 : state.network().smsRateLimit().rejectCmsError();
        }
        if (state.sms().selectedStorageFull()) {
            return 322;
        }
        if (pending.pduMode() && body.length() != pending.pduLength()) {
            return 304;
        }
        SmsRateLimit limit = state.network().smsRateLimit();
        if (!limit.enabled()) {
            return null;
        }
        Deque<Long> accepted = bucket(state, pending);
        long windowNanos = limit.windowSeconds() * 1_000_000_000L;
        while (!accepted.isEmpty() && accepted.peekFirst() <= nowNanos - windowNanos) {
            accepted.removeFirst();
        }
        return accepted.size() >= limit.maxMessages() ? limit.rejectCmsError() : null;
    }

    private Deque<Long> bucket(ModemState state, PendingSms pending) {
        SmsRateLimit limit = state.network() == null ? SmsRateLimit.none() : state.network().smsRateLimit();
        String key = switch (limit.scope()) {
            case "recipient" -> "recipient:" + String.valueOf(pending.destination());
            case "operator" -> "operator:" + state.network().operator().numeric();
            default -> "session";
        };
        return acceptedSubmitNanos.computeIfAbsent(key, ignored -> new ArrayDeque<>());
    }
}
