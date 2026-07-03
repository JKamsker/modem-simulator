package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.ResponseFormatter;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.CallRuntime;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SmsRateLimit;

import java.time.OffsetDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public final class SmsSubmitProcessor {
    private final Map<String, Deque<Long>> acceptedSubmitNanos = new HashMap<>();

    public SmsSubmitResult submit(PendingSms pending, String body, long nowNanos, ModemState state) {
        ResponseFormatter formatter = new ResponseFormatter(state);
        ModemState commandMode = state.withCall(CallRuntime.command());
        RawBytes simError = simError(formatter, commandMode);
        if (simError != null) {
            return new SmsSubmitResult(commandMode, simError, "CME_ERROR");
        }
        Integer cmsError = validateSubmit(pending, body, commandMode, nowNanos);
        if (cmsError != null) {
            return new SmsSubmitResult(commandMode, formatter.line("+CMS ERROR: " + cmsError), "CMS_ERROR");
        }
        bucket(commandMode, pending).addLast(nowNanos);
        int reference = commandMode.sms().nextMessageReference();
        ModemState stored = commandMode.withSms(commandMode.sms().storeOutbound(
                pending.destination(), pending.pduMode() ? null : body, pending.pduMode() ? body : null,
                OffsetDateTime.parse("2026-01-01T00:00:00Z").plusNanos(nowNanos)));
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

    private RawBytes simError(ResponseFormatter formatter, ModemState state) {
        if (state.sim().state() == SimState.READY) {
            return null;
        }
        if (state.settings().cmee() == 0) {
            return formatter.result("ERROR", state.settings().verbose());
        }
        int code = switch (state.sim().state()) {
            case SIM_NOT_INSERTED -> 10;
            case SIM_PIN_REQUIRED -> 11;
            case SIM_PUK_REQUIRED -> 12;
            case SIM_FAILURE -> 13;
            case SIM_BUSY -> 14;
            case SIM_WRONG -> 15;
            default -> 3;
        };
        String text = switch (state.sim().state()) {
            case SIM_NOT_INSERTED -> "SIM not inserted";
            case SIM_PIN_REQUIRED -> "SIM PIN required";
            case SIM_PUK_REQUIRED -> "SIM PUK required";
            case SIM_FAILURE -> "SIM failure";
            case SIM_BUSY -> "SIM busy";
            case SIM_WRONG -> "SIM wrong";
            default -> "operation not allowed";
        };
        return formatter.line("+CME ERROR: " + (state.settings().cmee() == 1 ? code : text));
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
