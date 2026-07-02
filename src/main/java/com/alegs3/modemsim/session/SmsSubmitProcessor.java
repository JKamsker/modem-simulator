package com.alegs3.modemsim.session;

import com.alegs3.modemsim.commands.ResponseFormatter;
import com.alegs3.modemsim.parser.RawBytes;
import com.alegs3.modemsim.state.CallRuntime;
import com.alegs3.modemsim.state.ModemState;
import com.alegs3.modemsim.state.SimState;
import com.alegs3.modemsim.state.SmsRateLimit;

import java.util.ArrayDeque;
import java.util.Deque;

public final class SmsSubmitProcessor {
    private final Deque<Long> acceptedSubmitNanos = new ArrayDeque<>();

    public SmsSubmitResult submit(PendingSms pending, String body, long nowNanos, ModemState state) {
        ResponseFormatter formatter = new ResponseFormatter(state);
        ModemState commandMode = state.withCall(CallRuntime.command());
        Integer cmsError = validateSubmit(commandMode, nowNanos);
        if (cmsError != null) {
            return new SmsSubmitResult(commandMode, formatter.line("+CMS ERROR: " + cmsError), "CMS_ERROR");
        }
        acceptedSubmitNanos.addLast(nowNanos);
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

    private Integer validateSubmit(ModemState state, long nowNanos) {
        if (state.sim().state() != SimState.READY) {
            return 500;
        }
        if (state.network() == null || !state.network().registeredForCircuitServices()) {
            return state.network() == null ? 500 : state.network().smsRateLimit().rejectCmsError();
        }
        SmsRateLimit limit = state.network().smsRateLimit();
        if (!limit.enabled()) {
            return null;
        }
        long windowNanos = limit.windowSeconds() * 1_000_000_000L;
        while (!acceptedSubmitNanos.isEmpty() && acceptedSubmitNanos.peekFirst() <= nowNanos - windowNanos) {
            acceptedSubmitNanos.removeFirst();
        }
        return acceptedSubmitNanos.size() >= limit.maxMessages() ? limit.rejectCmsError() : null;
    }
}
