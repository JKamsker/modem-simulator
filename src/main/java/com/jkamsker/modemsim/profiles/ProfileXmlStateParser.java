package com.jkamsker.modemsim.profiles;

import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.CallRuntime;
import com.jkamsker.modemsim.state.FreezeMode;
import com.jkamsker.modemsim.state.ModemLifecycle;
import com.jkamsker.modemsim.state.ModemLines;
import com.jkamsker.modemsim.state.ModemRuntimeInfo;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.NetworkDelay;
import com.jkamsker.modemsim.state.NetworkRuntime;
import com.jkamsker.modemsim.state.OperatorInfo;
import com.jkamsker.modemsim.state.SessionSettings;
import com.jkamsker.modemsim.state.SignalRuntime;
import com.jkamsker.modemsim.state.SimRuntime;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SmsRateLimit;
import com.jkamsker.modemsim.state.SmsRuntime;
import com.jkamsker.modemsim.state.SmsStorage;
import com.jkamsker.modemsim.validation.Dom;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

final class ProfileXmlStateParser {
    ModemState parse(Element profile, Dialect dialect) {
        Element initial = Dom.child(profile, "initial-state");
        String kind = Dom.attr(profile, "profileKind", "cellular");
        ModemState base = Set.of("pstn", "isdn", "base").contains(kind)
                ? ModemState.pstnReady() : ModemState.cellularReady();
        if (initial == null) {
            return applyDialect(base, dialect);
        }
        return applyDialect(new ModemState(
                parseSim(Dom.child(initial, "sim"), base.sim()),
                parseNetwork(Dom.child(initial, "network")),
                parseSignal(Dom.child(initial, "signal"), base.signal()),
                parseSms(Dom.child(initial, "sms"), base.sms()),
                parseCall(Dom.child(initial, "call"), base.call()),
                parseModem(Dom.child(initial, "modem"), base.modem()),
                parseLines(Dom.child(initial, "modem-lines"), base.lines()),
                base.settings(), 0), dialect);
    }

    ModemState applyDialect(ModemState state, Dialect dialect) {
        SessionSettings settings = state.settings()
                .withEcho(dialect.defaultEcho())
                .withQuiet(dialect.defaultQuiet())
                .withVerbose(dialect.defaultVerbose())
                .withRegister(3, dialect.commandTerminator())
                .withRegister(4, dialect.responseTerminator());
        return state.withSettings(settings);
    }

    private SimRuntime parseSim(Element sim, SimRuntime fallback) {
        if (sim == null) {
            return fallback;
        }
        boolean pinQueryEnabled = Dom.boolAttr(sim, "pinQueryEnabled", false);
        return new SimRuntime(
                SimState.valueOf(Dom.attr(sim, "state", pinQueryEnabled ? "SIM_PIN_REQUIRED" : "READY")),
                pinQueryEnabled, Dom.attr(sim, "pinRef", null), Dom.attr(sim, "pin", null),
                Dom.attr(sim, "pukRef", null), Dom.attr(sim, "puk", null),
                Dom.intAttr(sim, "pinRetries", 3), Dom.intAttr(sim, "pukRetries", 10),
                Dom.attr(sim, "imsi", null), Dom.attr(sim, "iccid", null));
    }

    private NetworkRuntime parseNetwork(Element network) {
        if (network == null) {
            return null;
        }
        return new NetworkRuntime(
                Dom.intAttr(network, "cregN", 2),
                Dom.intAttr(network, "stat", 0),
                Dom.attr(network, "lac", null),
                Dom.attr(network, "ci", null),
                intObject(network, "act"),
                intObject(network, "rejectCauseType"),
                intObject(network, "rejectCause"),
                parseOperator(Dom.child(network, "operator")),
                parseRateLimit(Dom.child(network, "sms-rate-limit")),
                parseDelays(Dom.child(network, "delays")));
    }

    private OperatorInfo parseOperator(Element operator) {
        if (operator == null) {
            return OperatorInfo.telekom();
        }
        return new OperatorInfo(
                operator.getAttribute("selectionMode"), operator.getAttribute("format"),
                Dom.attr(operator, "longName", ""), Dom.attr(operator, "shortName", ""),
                operator.getAttribute("numeric"), operator.getAttribute("mcc"), operator.getAttribute("mnc"));
    }

    private SmsRateLimit parseRateLimit(Element rate) {
        return rate == null ? SmsRateLimit.none() : new SmsRateLimit(
                Dom.intAttr(rate, "maxMessages", 0), Dom.intAttr(rate, "windowSeconds", 0),
                rate.getAttribute("scope"), Dom.intAttr(rate, "rejectCmsError", 500));
    }

    private Map<String, NetworkDelay> parseDelays(Element delays) {
        Map<String, NetworkDelay> result = new LinkedHashMap<>();
        if (delays == null) {
            return result;
        }
        for (Element delay : Dom.children(delays, "delay")) {
            String operation = delay.getAttribute("operation");
            result.put(operation, new NetworkDelay(
                    operation, Dom.intAttr(delay, "minMs", 0), Dom.intAttr(delay, "maxMs", 0)));
        }
        return result;
    }

    private SignalRuntime parseSignal(Element signal, SignalRuntime fallback) {
        return signal == null ? fallback : new SignalRuntime(
                Dom.intAttr(signal, "rssi", 99), Dom.intAttr(signal, "ber", 99));
    }

    private SmsRuntime parseSms(Element sms, SmsRuntime fallback) {
        return sms == null ? fallback : new SmsRuntime(
                Dom.boolAttr(sms, "textMode", true), Dom.attr(sms, "smsc", fallback.smsc()),
                fallback.cnmi(), SmsStorage.valueOf(Dom.attr(sms, "storage", fallback.storage().name())),
                fallback.nextMessageReference(), fallback.messages());
    }

    private CallRuntime parseCall(Element call, CallRuntime fallback) {
        return call == null ? fallback : new CallRuntime(
                callMode(Dom.attr(call, "mode", "command")),
                Dom.boolAttr(call, "carrier", false), Dom.attr(call, "dialedNumber", null));
    }

    private ModemRuntimeInfo parseModem(Element modem, ModemRuntimeInfo fallback) {
        return modem == null ? fallback : new ModemRuntimeInfo(
                ModemLifecycle.valueOf(Dom.attr(modem, "lifecycle", "READY")),
                FreezeMode.valueOf(Dom.attr(modem, "freezeMode", "NONE")),
                Dom.intAttr(modem, "bootDelayMs", 0));
    }

    private ModemLines parseLines(Element lines, ModemLines fallback) {
        return lines == null ? fallback : new ModemLines(
                Dom.boolAttr(lines, "dtr", true), Dom.boolAttr(lines, "dsr", true),
                Dom.boolAttr(lines, "dcd", false), Dom.boolAttr(lines, "ri", false),
                Dom.boolAttr(lines, "rts", true), Dom.boolAttr(lines, "cts", true));
    }

    private Integer intObject(Element element, String name) {
        String value = Dom.attr(element, name, null);
        return value == null ? null : Integer.valueOf(value);
    }

    private CallMode callMode(String value) {
        return switch (value) {
            case "online-data" -> CallMode.ONLINE_DATA;
            case "online-command" -> CallMode.ONLINE_COMMAND;
            case "dialing" -> CallMode.DIALING;
            default -> CallMode.COMMAND;
        };
    }
}
