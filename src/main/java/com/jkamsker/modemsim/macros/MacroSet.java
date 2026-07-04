package com.jkamsker.modemsim.macros;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;

public record MacroSet(String hash, Long randomSeed, List<MacroRule> rules) {
    public static final String EMPTY_HASH = "sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

    public static MacroSet empty() {
        return new MacroSet(EMPTY_HASH, null, List.of());
    }

    public static String effectiveHash(Long randomSeed, List<MacroRule> rules) {
        try {
            List<MacroRule> orderedRules = rules.stream()
                    .sorted(ruleOrder())
                    .toList();
            String canonical = "seed=" + randomSeed + ";rules=" + orderedRules;
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(canonical.getBytes(StandardCharsets.UTF_8));
            return "sha256:" + HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot hash macro set", e);
        }
    }

    public MacroSet {
        rules = rules.stream().sorted(ruleOrder()).toList();
    }

    private static Comparator<MacroRule> ruleOrder() {
        return Comparator.comparingInt(MacroRule::priority).reversed()
                .thenComparingInt(MacroRule::order);
    }
}
