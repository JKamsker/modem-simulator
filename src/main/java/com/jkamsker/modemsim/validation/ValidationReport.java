package com.jkamsker.modemsim.validation;

import java.util.ArrayList;
import java.util.List;

public final class ValidationReport {
    private final List<String> errors = new ArrayList<>();
    private final List<String> warnings = new ArrayList<>();

    public static ValidationReport ok() {
        return new ValidationReport();
    }

    public void error(String message) {
        errors.add(message);
    }

    public void warning(String message) {
        warnings.add(message);
    }

    public boolean valid() {
        return errors.isEmpty();
    }

    public List<String> errors() {
        return List.copyOf(errors);
    }

    public List<String> warnings() {
        return List.copyOf(warnings);
    }

    public void merge(ValidationReport other) {
        errors.addAll(other.errors);
        warnings.addAll(other.warnings);
    }

    public void throwIfInvalid() {
        if (!valid()) {
            throw new ValidationException(String.join("; ", errors));
        }
    }
}
