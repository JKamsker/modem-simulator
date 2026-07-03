package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.state.SimState;

final class GuiStatePatchFactory {
    private GuiStatePatchFactory() {
    }

    static GuiSessionController.GuiStatePatch fromText(String text) {
        String[] parts = text.split("=", 2);
        if (parts.length != 2) {
            return empty();
        }
        return switch (parts[0].trim()) {
            case "network.stat" -> fromValues(null, "", "", parts[1], "", "", "", "", "", "");
            case "network.cregN" -> fromValues(null, "", "", "", parts[1], "", "", "", "", "");
            case "signal" -> fromValues(null, "", "", "", "", "", "", "", parts[1], "");
            case "lines.dcd" -> fromValues(null, "", "", "", "", "", "", "", "", parts[1]);
            default -> empty();
        };
    }

    static GuiSessionController.GuiStatePatch fromValues(
            String simState,
            String pinRetries,
            String pukRetries,
            String networkStat,
            String cregN,
            String lac,
            String ci,
            String act,
            String signal,
            String lines) {
        int[] pair = pair(signal);
        return new GuiSessionController.GuiStatePatch(
                simState == null || simState.isBlank() ? null : SimState.valueOf(simState),
                integer(pinRetries),
                integer(pukRetries),
                integer(networkStat),
                integer(cregN),
                blankToNull(lac),
                blankToNull(ci),
                integer(act),
                pair[0] < 0 ? null : pair[0],
                pair[1] < 0 ? null : pair[1],
                dcd(lines));
    }

    private static GuiSessionController.GuiStatePatch empty() {
        return fromValues(null, "", "", "", "", "", "", "", "", "");
    }

    private static int[] pair(String value) {
        if (value == null || !value.contains(",")) {
            return new int[] {-1, -1};
        }
        String[] parts = value.split(",", 2);
        return new int[] {integer(parts[0], -1), integer(parts[1], -1)};
    }

    private static Boolean dcd(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.equalsIgnoreCase("true") || value.toUpperCase().contains("DCD");
    }

    private static Integer integer(String value) {
        Integer parsed = integer(value, Integer.MIN_VALUE);
        return parsed == Integer.MIN_VALUE ? null : parsed;
    }

    private static int integer(String value, int fallback) {
        try {
            return value == null || value.isBlank() ? fallback : Integer.parseInt(value.trim());
        } catch (RuntimeException e) {
            return fallback;
        }
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
