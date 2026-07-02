package com.alegs3.modemsim.monitor;

import java.util.List;

public record RedactionInfo(boolean applied, String policy, List<String> fields, List<String> classes) {
    public static RedactionInfo none() {
        return new RedactionInfo(true, "default-v1", List.of(), List.of());
    }
}
