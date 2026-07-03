package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.CallRuntime;
import com.jkamsker.modemsim.state.FreezeMode;
import com.jkamsker.modemsim.state.ModemLifecycle;
import com.jkamsker.modemsim.state.ModemLines;
import com.jkamsker.modemsim.state.ModemRuntimeInfo;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.NetworkRuntime;
import com.jkamsker.modemsim.state.SignalRuntime;
import com.jkamsker.modemsim.state.SimRuntime;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SmsRuntime;
import com.jkamsker.modemsim.state.SmsStorage;

record GuiStatePatch(
        SimState simState, Integer pinRetries, Integer pukRetries,
        Integer networkStat, Integer cregN, String lac, String ci, Integer act,
        Integer rejectCauseType, Integer rejectCause, Integer rssi, Integer ber,
        SmsStorage smsStorage, CallMode callMode, ModemLifecycle lifecycle,
        FreezeMode freezeMode, Boolean dtr, Boolean dsr, Boolean dcd,
        Boolean ri, Boolean rts, Boolean cts
) {
    ModemState apply(ModemState state) {
        ModemState next = applySim(state);
        next = applyNetwork(next);
        next = smsStorage == null ? next : next.withSms(next.sms().withStorage(smsStorage));
        next = callMode == null ? next : applyCall(next);
        next = lifecycle == null && freezeMode == null ? next : applyModem(next);
        next = rssi == null && ber == null ? next : applySignal(next);
        return noLines() ? next : applyLines(next);
    }

    private ModemState applySim(ModemState state) {
        if (simState == null && pinRetries == null && pukRetries == null) {
            return state;
        }
        SimRuntime sim = state.sim();
        return state.withSim(new SimRuntime(
                simState == null ? sim.state() : simState, sim.pinQueryEnabled(), sim.pinRef(), sim.testPin(),
                sim.pukRef(), sim.testPuk(), pinRetries == null ? sim.pinRetries() : pinRetries,
                pukRetries == null ? sim.pukRetries() : pukRetries, sim.imsi(), sim.iccid()));
    }

    private ModemState applyNetwork(ModemState state) {
        NetworkRuntime network = state.network();
        if (network == null || (networkStat == null && cregN == null && lac == null && ci == null
                && act == null && rejectCauseType == null && rejectCause == null)) {
            return state;
        }
        return state.withNetwork(new NetworkRuntime(
                cregN == null ? network.cregN() : cregN, networkStat == null ? network.stat() : networkStat,
                blank(lac) ? network.lac() : lac, blank(ci) ? network.ci() : ci,
                act == null ? network.act() : act,
                rejectCauseType == null ? network.rejectCauseType() : rejectCauseType,
                rejectCause == null ? network.rejectCause() : rejectCause,
                network.operator(), network.smsRateLimit(), network.delays()));
    }

    private ModemState applyCall(ModemState state) {
        CallRuntime call = state.call();
        return state.withCall(new CallRuntime(callMode, call.carrier(), call.dialedNumber(), call.incomingNumber()));
    }

    private ModemState applyModem(ModemState state) {
        ModemRuntimeInfo modem = state.modem();
        return state.withModem(new ModemRuntimeInfo(
                lifecycle == null ? modem.lifecycle() : lifecycle,
                freezeMode == null ? modem.freezeMode() : freezeMode,
                modem.bootDelayMs()));
    }

    private ModemState applySignal(ModemState state) {
        SignalRuntime signal = state.signal();
        return state.withSignal(new SignalRuntime(rssi == null ? signal.rssi() : rssi, ber == null ? signal.ber() : ber));
    }

    private ModemState applyLines(ModemState state) {
        ModemLines lines = state.lines();
        return state.withLines(new ModemLines(
                dtr == null ? lines.dtr() : dtr, dsr == null ? lines.dsr() : dsr,
                dcd == null ? lines.dcd() : dcd, ri == null ? lines.ri() : ri,
                rts == null ? lines.rts() : rts, cts == null ? lines.cts() : cts));
    }

    private boolean noLines() {
        return dtr == null && dsr == null && dcd == null && ri == null && rts == null && cts == null;
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
