package com.jkamsker.modemsim.macros;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;

public record MacroSet(String hash, Long randomSeed, String lineEnding, List<MacroRule> rules) {
    public static final String EMPTY_HASH = "sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

    public static MacroSet empty() {
        return new MacroSet(EMPTY_HASH, null, "CR", List.of());
    }

    public static String effectiveHash(Long randomSeed, String lineEnding, List<MacroRule> rules) {
        try {
            List<MacroRule> orderedRules = rules.stream()
                    .sorted(ruleOrder())
                    .toList();
            String canonical = "seed=" + randomSeed
                    + ";lineEnding=" + normalizeLineEnding(lineEnding)
                    + ";rules=" + orderedRules;
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(canonical.getBytes(StandardCharsets.UTF_8));
            return "sha256:" + HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot hash macro set", e);
        }
    }

    public MacroSet {
        lineEnding = normalizeLineEnding(lineEnding);
        rules = rules.stream().sorted(ruleOrder()).toList();
    }

    private static Comparator<MacroRule> ruleOrder() {
        return Comparator.comparingInt(MacroRule::priority).reversed()
                .thenComparingInt(MacroRule::order);
    }

    private static String normalizeLineEnding(String value) {
        return value == null || value.isBlank() ? "CR" : value;
    }
}
