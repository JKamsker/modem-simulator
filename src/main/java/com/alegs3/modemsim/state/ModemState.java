package com.alegs3.modemsim.state;

public record ModemState(
        SimRuntime sim,
        NetworkRuntime network,
        SignalRuntime signal,
        SmsRuntime sms,
        CallRuntime call,
        ModemRuntimeInfo modem,
        ModemLines lines,
        SessionSettings settings,
        long version
) {
    public static ModemState cellularReady() {
        return new ModemState(
                SimRuntime.ready(), NetworkRuntime.registered(), SignalRuntime.medium(),
                SmsRuntime.defaults(), CallRuntime.command(), ModemRuntimeInfo.ready(),
                ModemLines.ready(), SessionSettings.defaults(), 0);
    }

    public static ModemState pstnReady() {
        return new ModemState(
                SimRuntime.ready(), null, SignalRuntime.unknown(), SmsRuntime.defaults(),
                CallRuntime.command(), ModemRuntimeInfo.ready(), ModemLines.ready(),
                SessionSettings.defaults(), 0);
    }

    public ModemState withSim(SimRuntime value) {
        return copy(value, network, signal, sms, call, modem, lines, settings);
    }

    public ModemState withNetwork(NetworkRuntime value) {
        return copy(sim, value, signal, sms, call, modem, lines, settings);
    }

    public ModemState withSignal(SignalRuntime value) {
        return copy(sim, network, value, sms, call, modem, lines, settings);
    }

    public ModemState withSms(SmsRuntime value) {
        return copy(sim, network, signal, value, call, modem, lines, settings);
    }

    public ModemState withCall(CallRuntime value) {
        return copy(sim, network, signal, sms, value, modem, lines, settings);
    }

    public ModemState withModem(ModemRuntimeInfo value) {
        return copy(sim, network, signal, sms, call, value, lines, settings);
    }

    public ModemState withLines(ModemLines value) {
        return copy(sim, network, signal, sms, call, modem, value, settings);
    }

    public ModemState withSettings(SessionSettings value) {
        return copy(sim, network, signal, sms, call, modem, lines, value);
    }

    private ModemState copy(
            SimRuntime nextSim,
            NetworkRuntime nextNetwork,
            SignalRuntime nextSignal,
            SmsRuntime nextSms,
            CallRuntime nextCall,
            ModemRuntimeInfo nextModem,
            ModemLines nextLines,
            SessionSettings nextSettings) {
        return new ModemState(
                nextSim, nextNetwork, nextSignal, nextSms, nextCall,
                nextModem, nextLines, nextSettings, version + 1);
    }
}
