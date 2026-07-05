package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.validation.StateValueValidator;

public record MacroStatePredicate(String path, String comparator, String value) {
    public boolean matches(ModemState state) {
        String actual = StateValueValidator.read(state, path);
        return switch (comparator) {
            case "equals" -> StateValueValidator.equalValue(actual, value);
            case "lessThan" -> compare(actual, value, -1);
            case "greaterThan" -> compare(actual, value, 1);
            default -> false;
        };
    }

    private boolean compare(String actual, String expected, int direction) {
        try {
            if (actual == null) {
                return false;
            }
            int comparison = Integer.compare(Integer.parseInt(actual), Integer.parseInt(expected));
            return direction < 0 ? comparison < 0 : comparison > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
