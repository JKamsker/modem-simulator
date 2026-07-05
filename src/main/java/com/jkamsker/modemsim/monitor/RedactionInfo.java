package com.jkamsker.modemsim.monitor;

import java.util.List;

public record RedactionInfo(boolean applied, String policy, List<String> fields, List<String> classes) {
    public RedactionInfo {
        fields = List.copyOf(fields);
        classes = List.copyOf(classes);
    }

    public static RedactionInfo none() {
        return new RedactionInfo(false, "default-v1", List.of(), List.of());
    }

    public static RedactionInfo applied(List<String> fields, List<String> classes) {
        return new RedactionInfo(true, "default-v1", fields, classes);
    }
}
