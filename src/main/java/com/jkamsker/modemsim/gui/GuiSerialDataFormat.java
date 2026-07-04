package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.transport.Parity;

final class GuiSerialDataFormat {
    private GuiSerialDataFormat() {
    }

    static String[] values() {
        return new String[] {"8N1", "8E1", "8O1", "7E1", "7O1", "7N2", "7E2", "8N2", "8M1", "8S1"};
    }

    static int dataBits(String value) {
        String normalized = clean(value);
        return Character.digit(normalized.charAt(0), 10);
    }

    static int stopBits(String value) {
        String normalized = clean(value);
        return Character.digit(normalized.charAt(2), 10);
    }

    static Parity parity(String value) {
        return switch (clean(value).charAt(1)) {
            case 'E' -> Parity.EVEN;
            case 'O' -> Parity.ODD;
            case 'M' -> Parity.MARK;
            case 'S' -> Parity.SPACE;
            default -> Parity.NONE;
        };
    }

    private static String clean(String value) {
        String normalized = value == null || value.isBlank() ? "8N1" : value.trim().toUpperCase();
        if (!normalized.matches("[5-8][NEOMS][12]")) {
            throw new IllegalArgumentException("Unsupported data format: " + value);
        }
        return normalized;
    }
}
