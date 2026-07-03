package com.jkamsker.modemsim.monitor;

import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.CallRuntime;
import com.jkamsker.modemsim.state.SimRuntime;
import com.jkamsker.modemsim.state.SmsMessage;
import com.jkamsker.modemsim.state.SmsRuntime;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class EventStateRedactor {
    private static final String REDACTED = "<redacted>";

    public boolean containsSensitiveData(ModemState state) {
        return state != null
                && (containsSimSecrets(state.sim()) || containsSmsSecrets(state.sms()) || containsCallNumber(state.call()));
    }

    public List<String> classes(ModemState first, ModemState second) {
        LinkedHashSet<String> classes = new LinkedHashSet<>();
        addClasses(classes, first);
        addClasses(classes, second);
        return new ArrayList<>(classes);
    }

    public ModemState redactSensitiveData(ModemState state) {
        if (!containsSensitiveData(state)) {
            return state;
        }
        return new ModemState(
                state.sim() == null ? null : redactedSim(state.sim()),
                state.network(),
                state.signal(),
                state.sms() == null ? null : redactedSms(state.sms()),
                state.call() == null ? null : redactedCall(state.call()),
                state.modem(),
                state.lines(),
                state.settings(),
                state.version());
    }

    private void addClasses(LinkedHashSet<String> classes, ModemState state) {
        if (state == null) {
            return;
        }
        if (state.sim() != null && state.sim().testPin() != null) {
            classes.add("pin");
        }
        if (state.sim() != null && state.sim().testPuk() != null) {
            classes.add("puk");
        }
        if (state.sim() != null && state.sim().imsi() != null) {
            classes.add("imsi");
        }
        if (state.sim() != null && state.sim().iccid() != null) {
            classes.add("iccid");
        }
        if (state.sms() != null && containsSmsBody(state.sms())) {
            classes.add("sms-body");
        }
        if (state.sms() != null && containsSmsMsisdn(state.sms())) {
            classes.add("msisdn");
        }
        if (state.call() != null
                && (state.call().dialedNumber() != null || state.call().incomingNumber() != null)) {
            classes.add("msisdn");
        }
    }

    private boolean containsSimSecrets(SimRuntime sim) {
        return sim != null && (sim.testPin() != null || sim.testPuk() != null
                || sim.imsi() != null || sim.iccid() != null);
    }

    private boolean containsSmsSecrets(SmsRuntime sms) {
        return sms != null && (sms.smsc() != null || containsSmsMsisdn(sms) || containsSmsBody(sms));
    }

    private boolean containsSmsBody(SmsRuntime sms) {
        return sms != null && sms.messages().values().stream()
                .anyMatch(message -> message.text() != null || message.pdu() != null);
    }

    private boolean containsSmsMsisdn(SmsRuntime sms) {
        return sms.smsc() != null || sms.messages().values().stream()
                .anyMatch(message -> message.sender() != null || message.recipient() != null);
    }

    private boolean containsCallNumber(CallRuntime call) {
        return call != null && call.dialedNumber() != null;
    }

    private SimRuntime redactedSim(SimRuntime sim) {
        return new SimRuntime(
                sim.state(),
                sim.pinQueryEnabled(),
                sim.pinRef(),
                sim.testPin() == null ? null : REDACTED,
                sim.pukRef(),
                sim.testPuk() == null ? null : REDACTED,
                sim.pinRetries(),
                sim.pukRetries(),
                sim.imsi() == null ? null : REDACTED,
                sim.iccid() == null ? null : REDACTED);
    }

    private SmsRuntime redactedSms(SmsRuntime sms) {
        Map<Integer, SmsMessage> messages = new LinkedHashMap<>();
        for (var entry : sms.messages().entrySet()) {
            SmsMessage message = entry.getValue();
            messages.put(entry.getKey(), new SmsMessage(
                    message.index(),
                    message.storage(),
                    message.status(),
                    message.sender() == null ? null : REDACTED,
                    message.recipient() == null ? null : REDACTED,
                    message.timestamp(),
                    message.text() == null ? null : REDACTED,
                    message.pdu() == null ? null : REDACTED));
        }
        return new SmsRuntime(
                sms.textMode(),
                sms.smsc() == null ? null : REDACTED,
                sms.cnmi(),
                sms.storage(),
                sms.writeStorage(),
                sms.receiveStorage(),
                sms.nextMessageReference(),
                messages);
    }

    private CallRuntime redactedCall(CallRuntime call) {
        return new CallRuntime(
                call.mode(),
                call.carrier(),
                call.dialedNumber() == null ? null : REDACTED,
                call.incomingNumber() == null ? null : REDACTED);
    }
}
