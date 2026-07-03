package com.jkamsker.modemsim.validation;

import com.jkamsker.modemsim.state.FreezeMode;
import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.ModemLifecycle;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SmsStorage;

import java.util.Set;

public final class StateValueValidator {
    private static final Set<String> PATHS = Set.of(
            "state.sim.state", "state.sim.pinRetries", "state.sim.pukRetries",
            "state.network.cregN", "state.network.stat", "state.network.lac", "state.network.ci",
            "state.network.act", "state.network.rejectCauseType", "state.network.rejectCause",
            "state.signal.rssi", "state.signal.ber",
            "state.sms.textMode", "state.sms.storage",
            "state.call.mode", "state.call.carrier", "state.call.dialedNumber",
            "state.modem.lifecycle", "state.modem.freezeMode", "state.modem.bootDelayMs",
            "state.modemLines.dtr", "state.modemLines.dsr", "state.modemLines.dcd",
            "state.modemLines.ri", "state.modemLines.rts", "state.modemLines.cts");
    private static final Set<String> NUMERIC_PATHS = Set.of(
            "state.sim.pinRetries", "state.sim.pukRetries",
            "state.network.cregN", "state.network.stat", "state.network.act",
            "state.network.rejectCauseType", "state.network.rejectCause",
            "state.signal.rssi", "state.signal.ber", "state.modem.bootDelayMs");

    private StateValueValidator() {
    }

    public static Set<String> paths() {
        return PATHS;
    }

    public static boolean isNumericPath(String path) {
        return NUMERIC_PATHS.contains(path);
    }

    public static String validate(String path, String value) {
        try {
            switch (path) {
                case "state.sim.state" -> SimState.valueOf(value);
                case "state.sim.pinRetries", "state.sim.pukRetries" -> range(path, value, 0, 10);
                case "state.network.cregN" -> range(path, value, 0, 3);
                case "state.network.stat" -> range(path, value, 0, 11);
                case "state.network.lac", "state.network.ci" -> hex(path, value);
                case "state.network.act" -> range(path, value, 0, 13);
                case "state.network.rejectCauseType", "state.network.rejectCause" -> range(path, value, 0, 65535);
                case "state.signal.rssi" -> csq(path, value, 31);
                case "state.signal.ber" -> csq(path, value, 7);
                case "state.sms.textMode" -> bool(path, value);
                case "state.sms.storage" -> SmsStorage.valueOf(value);
                case "state.call.mode" -> callMode(value);
                case "state.call.carrier" -> bool(path, value);
                case "state.call.dialedNumber" -> {
                    if (value.isBlank()) {
                        throw new IllegalArgumentException(path);
                    }
                }
                case "state.modem.lifecycle" -> ModemLifecycle.valueOf(value);
                case "state.modem.freezeMode" -> FreezeMode.valueOf(value);
                case "state.modem.bootDelayMs" -> range(path, value, 0, Integer.MAX_VALUE);
                case "state.modemLines.dtr", "state.modemLines.dsr", "state.modemLines.dcd",
                        "state.modemLines.ri", "state.modemLines.rts", "state.modemLines.cts" -> bool(path, value);
                default -> {
                    return "unknown state path " + path;
                }
            }
            return null;
        } catch (RuntimeException e) {
            return path + " has invalid value " + value;
        }
    }

    public static String read(ModemState state, String path) {
        return switch (path) {
            case "state.sim.state" -> state.sim().state().name();
            case "state.sim.pinRetries" -> Integer.toString(state.sim().pinRetries());
            case "state.sim.pukRetries" -> Integer.toString(state.sim().pukRetries());
            case "state.network.cregN" -> Integer.toString(state.network().cregN());
            case "state.network.stat" -> Integer.toString(state.network().stat());
            case "state.network.lac" -> state.network().lac();
            case "state.network.ci" -> state.network().ci();
            case "state.network.act" -> nullable(state.network().act());
            case "state.network.rejectCauseType" -> nullable(state.network().rejectCauseType());
            case "state.network.rejectCause" -> nullable(state.network().rejectCause());
            case "state.signal.rssi" -> Integer.toString(state.signal().rssi());
            case "state.signal.ber" -> Integer.toString(state.signal().ber());
            case "state.sms.textMode" -> Boolean.toString(state.sms().textMode());
            case "state.sms.storage" -> state.sms().storage().name();
            case "state.call.mode" -> state.call().mode().pathValue();
            case "state.call.carrier" -> Boolean.toString(state.call().carrier());
            case "state.call.dialedNumber" -> state.call().dialedNumber();
            case "state.modem.lifecycle" -> state.modem().lifecycle().name();
            case "state.modem.freezeMode" -> state.modem().freezeMode().name();
            case "state.modem.bootDelayMs" -> Integer.toString(state.modem().bootDelayMs());
            case "state.modemLines.dtr" -> Boolean.toString(state.lines().dtr());
            case "state.modemLines.dsr" -> Boolean.toString(state.lines().dsr());
            case "state.modemLines.dcd" -> Boolean.toString(state.lines().dcd());
            case "state.modemLines.ri" -> Boolean.toString(state.lines().ri());
            case "state.modemLines.rts" -> Boolean.toString(state.lines().rts());
            case "state.modemLines.cts" -> Boolean.toString(state.lines().cts());
            default -> null;
        };
    }

    public static boolean equalValue(String actual, String expected) {
        return actual != null && (actual.equals(expected) || actual.equals(canonicalMode(expected)));
    }

    private static void range(String path, String value, int min, int max) {
        int parsed = Integer.parseInt(value);
        if (parsed < min || parsed > max) {
            throw new IllegalArgumentException(path);
        }
    }

    private static void csq(String path, String value, int maxKnown) {
        int parsed = Integer.parseInt(value);
        if (parsed != 99 && (parsed < 0 || parsed > maxKnown)) {
            throw new IllegalArgumentException(path);
        }
    }

    private static void bool(String path, String value) {
        if (!value.equals("true") && !value.equals("false")) {
            throw new IllegalArgumentException(path);
        }
    }

    private static void callMode(String value) {
        for (CallMode mode : CallMode.values()) {
            if (mode.name().equals(value) || mode.pathValue().equals(value)) {
                return;
            }
        }
        throw new IllegalArgumentException("state.call.mode");
    }

    private static String canonicalMode(String value) {
        for (CallMode mode : CallMode.values()) {
            if (mode.name().equals(value)) {
                return mode.pathValue();
            }
        }
        return value;
    }

    private static String nullable(Object value) {
        return value == null ? null : value.toString();
    }

    private static void hex(String path, String value) {
        if (!value.matches("[0-9A-Fa-f]+")) {
            throw new IllegalArgumentException(path);
        }
    }
}
