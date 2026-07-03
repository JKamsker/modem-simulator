package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.validation.StateValueValidator;

public record MacroStatePredicate(String path, String comparator, String value) {
    public boolean matches(ModemState state) {
        String actual = StateValueValidator.read(state, path);
        return switch (comparator) {
            case "equals" -> StateValueValidator.equalValue(actual, value);
            case "lessThan" -> actual != null && Integer.parseInt(actual) < Integer.parseInt(value);
            case "greaterThan" -> actual != null && Integer.parseInt(actual) > Integer.parseInt(value);
            default -> false;
        };
    }
}
