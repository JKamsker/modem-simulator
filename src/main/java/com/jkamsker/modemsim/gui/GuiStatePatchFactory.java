package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.FreezeMode;
import com.jkamsker.modemsim.state.ModemLifecycle;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SmsStorage;

final class GuiStatePatchFactory {
    private GuiStatePatchFactory() {
    }

    static GuiStatePatch fromText(String text) {
        String[] parts = text.split("=", 2);
        if (parts.length != 2) {
            return empty();
        }
        return switch (parts[0].trim()) {
            case "network.stat", "state.network.stat" ->
                    fromValues(null, "", "", parts[1], "", "", "", "", "", "", "", "", "", "", "", "");
            case "network.cregN", "state.network.cregN" ->
                    fromValues(null, "", "", "", parts[1], "", "", "", "", "", "", "", "", "", "", "");
            case "state.network.rejectCauseType" ->
                    fromValues(null, "", "", "", "", "", "", "", "", parts[1], "", "", "", "", "", "");
            case "state.network.rejectCause" ->
                    fromValues(null, "", "", "", "", "", "", "", "", "", parts[1], "", "", "", "", "");
            case "signal", "state.signal" ->
                    fromValues(null, "", "", "", "", "", "", "", parts[1], "", "", "", "", "", "", "");
            case "lines.dcd", "state.modemLines.dcd" ->
                    fromValues(null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", parts[1]);
            default -> empty();
        };
    }

    static GuiStatePatch fromValues(
            String simState,
            String pinRetries,
            String pukRetries,
            String networkStat,
            String cregN,
            String lac,
            String ci,
            String act,
            String signal,
            String rejectCauseType,
            String rejectCause,
            String smsStorage,
            String callMode,
            String lifecycle,
            String freezeMode,
            String lines) {
        int[] pair = pair(signal);
        return new GuiStatePatch(
                simState == null || simState.isBlank() ? null : SimState.valueOf(simState),
                integer(pinRetries),
                integer(pukRetries),
                integer(networkStat),
                integer(cregN),
                blankToNull(lac),
                blankToNull(ci),
                integer(act),
                integer(rejectCauseType),
                integer(rejectCause),
                pair[0] < 0 ? null : pair[0],
                pair[1] < 0 ? null : pair[1],
                enumValue(SmsStorage.class, smsStorage),
                enumValue(CallMode.class, callMode),
                enumValue(ModemLifecycle.class, lifecycle),
                enumValue(FreezeMode.class, freezeMode),
                lineFlag(lines, "DTR"),
                lineFlag(lines, "DSR"),
                lineFlag(lines, "DCD"),
                lineFlag(lines, "RI"),
                lineFlag(lines, "RTS"),
                lineFlag(lines, "CTS"));
    }

    private static GuiStatePatch empty() {
        return fromValues(null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    private static int[] pair(String value) {
        if (value == null || !value.contains(",")) {
            return new int[] {-1, -1};
        }
        String[] parts = value.split(",", 2);
        return new int[] {integer(parts[0], -1), integer(parts[1], -1)};
    }

    private static Boolean lineFlag(String value, String flag) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.equalsIgnoreCase("true") || value.toUpperCase().contains(flag);
    }

    private static Integer integer(String value) {
        Integer parsed = integer(value, Integer.MIN_VALUE);
        return parsed == Integer.MIN_VALUE ? null : parsed;
    }

    private static int integer(String value, int fallback) {
        try {
            if (value == null || value.isBlank()) {
                return fallback;
            }
            String trimmed = value.trim();
            int end = 0;
            while (end < trimmed.length() && Character.isDigit(trimmed.charAt(end))) {
                end++;
            }
            return Integer.parseInt(end == 0 ? trimmed : trimmed.substring(0, end));
        } catch (RuntimeException e) {
            return fallback;
        }
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private static <T extends Enum<T>> T enumValue(Class<T> type, String value) {
        return value == null || value.isBlank() ? null : Enum.valueOf(type, value);
    }
}
