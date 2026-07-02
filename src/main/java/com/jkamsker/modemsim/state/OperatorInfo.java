package com.jkamsker.modemsim.state;

public record OperatorInfo(
        String selectionMode,
        String format,
        String longName,
        String shortName,
        String numeric,
        String mcc,
        String mnc
) {
    public static OperatorInfo telekom() {
        return new OperatorInfo("automatic", "long", "Telekom.de", "TDG", "26201", "262", "01");
    }

    public int selectionModeCode() {
        return switch (selectionMode) {
            case "manual" -> 1;
            case "deregister" -> 2;
            case "set-format" -> 3;
            case "manual-automatic" -> 4;
            default -> 0;
        };
    }

    public int formatCode() {
        return switch (format) {
            case "short" -> 1;
            case "numeric" -> 2;
            default -> 0;
        };
    }

    public String displayName() {
        return switch (format) {
            case "short" -> shortName;
            case "numeric" -> numeric;
            default -> longName;
        };
    }
}
