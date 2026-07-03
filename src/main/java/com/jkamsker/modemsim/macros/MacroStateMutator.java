package com.jkamsker.modemsim.macros;

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
import com.jkamsker.modemsim.state.StateInvariants;

import java.util.List;

public final class MacroStateMutator {
    public ModemState apply(ModemState source, List<MacroStatePatch> patches) {
        ModemState next = source;
        for (MacroStatePatch patch : patches) {
            next = apply(next, patch);
        }
        return StateInvariants.normalize(next);
    }

    private ModemState apply(ModemState state, MacroStatePatch patch) {
        return switch (patch.path()) {
            case "state.sim.state" -> state.withSim(state.sim().withState(SimState.valueOf(patch.value())));
            case "state.sim.pinRetries" -> state.withSim(state.sim().withPinRetries(integer(patch.value())));
            case "state.sim.pukRetries" -> state.withSim(new SimRuntime(
                    state.sim().state(), state.sim().pinQueryEnabled(), state.sim().pinRef(), state.sim().testPin(),
                    state.sim().pinRetries(), integer(patch.value()), state.sim().imsi(), state.sim().iccid()));
            case "state.network.cregN" -> state.withNetwork(network(state).withCregN(integer(patch.value())));
            case "state.network.stat" -> state.withNetwork(network(state).withRegistration(integer(patch.value())));
            case "state.network.lac", "state.network.ci", "state.network.act",
                    "state.network.rejectCauseType", "state.network.rejectCause" -> networkPatch(state, patch);
            case "state.signal.rssi" -> state.withSignal(new SignalRuntime(integer(patch.value()), state.signal().ber()));
            case "state.signal.ber" -> state.withSignal(new SignalRuntime(state.signal().rssi(), integer(patch.value())));
            case "state.sms.textMode" -> state.withSms(state.sms().withTextMode(Boolean.parseBoolean(patch.value())));
            case "state.sms.storage" -> state.withSms(state.sms().withStorage(SmsStorage.valueOf(patch.value())));
            case "state.call.mode", "state.call.carrier", "state.call.dialedNumber" -> callPatch(state, patch);
            case "state.modem.lifecycle", "state.modem.freezeMode", "state.modem.bootDelayMs" -> modemPatch(state, patch);
            case "state.modemLines.dtr", "state.modemLines.dsr", "state.modemLines.dcd",
                    "state.modemLines.ri", "state.modemLines.rts", "state.modemLines.cts" -> linePatch(state, patch);
            default -> state;
        };
    }

    private ModemState networkPatch(ModemState state, MacroStatePatch patch) {
        NetworkRuntime network = network(state);
        return state.withNetwork(new NetworkRuntime(
                network.cregN(), network.stat(),
                patch.path().equals("state.network.lac") ? patch.value() : network.lac(),
                patch.path().equals("state.network.ci") ? patch.value() : network.ci(),
                patch.path().equals("state.network.act") ? integer(patch.value()) : network.act(),
                patch.path().equals("state.network.rejectCauseType")
                        ? integer(patch.value()) : network.rejectCauseType(),
                patch.path().equals("state.network.rejectCause") ? integer(patch.value()) : network.rejectCause(),
                network.operator(), network.smsRateLimit(), network.delays()));
    }

    private ModemState callPatch(ModemState state, MacroStatePatch patch) {
        CallRuntime call = state.call();
        return state.withCall(new CallRuntime(
                patch.path().equals("state.call.mode") ? callMode(patch.value()) : call.mode(),
                patch.path().equals("state.call.carrier") ? Boolean.parseBoolean(patch.value()) : call.carrier(),
                patch.path().equals("state.call.dialedNumber") ? patch.value() : call.dialedNumber(),
                patch.path().equals("state.call.incomingNumber") ? patch.value() : call.incomingNumber()));
    }

    private ModemState modemPatch(ModemState state, MacroStatePatch patch) {
        ModemRuntimeInfo modem = state.modem();
        return state.withModem(new ModemRuntimeInfo(
                patch.path().equals("state.modem.lifecycle") ? ModemLifecycle.valueOf(patch.value()) : modem.lifecycle(),
                patch.path().equals("state.modem.freezeMode") ? FreezeMode.valueOf(patch.value()) : modem.freezeMode(),
                patch.path().equals("state.modem.bootDelayMs") ? integer(patch.value()) : modem.bootDelayMs()));
    }

    private ModemState linePatch(ModemState state, MacroStatePatch patch) {
        ModemLines lines = state.lines();
        boolean value = Boolean.parseBoolean(patch.value());
        return state.withLines(new ModemLines(
                patch.path().equals("state.modemLines.dtr") ? value : lines.dtr(),
                patch.path().equals("state.modemLines.dsr") ? value : lines.dsr(),
                patch.path().equals("state.modemLines.dcd") ? value : lines.dcd(),
                patch.path().equals("state.modemLines.ri") ? value : lines.ri(),
                patch.path().equals("state.modemLines.rts") ? value : lines.rts(),
                patch.path().equals("state.modemLines.cts") ? value : lines.cts()));
    }

    private NetworkRuntime network(ModemState state) {
        return state.network() == null ? NetworkRuntime.registered() : state.network();
    }

    private CallMode callMode(String value) {
        for (CallMode mode : CallMode.values()) {
            if (mode.name().equals(value) || mode.pathValue().equals(value)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid call mode " + value);
    }

    private Integer integer(String value) {
        return Integer.parseInt(value);
    }
}
