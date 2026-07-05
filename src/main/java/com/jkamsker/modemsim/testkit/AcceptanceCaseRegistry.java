package com.jkamsker.modemsim.testkit;

import java.util.List;

final class AcceptanceCaseRegistry {
    private AcceptanceCaseRegistry() {
    }

    static String defaultCaseForSuite(String suite, List<String> allCaseIds) {
        List<String> cases = casesForSuite(suite, allCaseIds);
        return suite.equals("acceptance") ? "all" : cases.getFirst();
    }

    static String defaultCaseForSelector(String suite, String tag, List<String> allCaseIds) {
        if (tag == null) {
            return defaultCaseForSuite(suite, allCaseIds);
        }
        return casesForSelector(suite, tag, allCaseIds).getFirst();
    }

    static List<String> selectCaseIds(String suite, String tag, String requestedCase, List<String> allCaseIds) {
        List<String> selectedCases = casesForSelector(suite, tag, allCaseIds);
        if (requestedCase.equals("all")) {
            return selectedCases;
        }
        String normalized = normalize(requestedCase);
        if (!selectedCases.contains(normalized)) {
            throw new IllegalArgumentException("Acceptance case " + requestedCase
                    + " is not in " + selectorLabel(suite, tag));
        }
        return List.of(requestedCase);
    }

    private static List<String> casesForSelector(String suite, String tag, List<String> allCaseIds) {
        List<String> suiteCases = casesForSuite(suite, allCaseIds);
        if (tag == null) {
            return suiteCases;
        }
        List<String> tagCases = casesForTag(tag);
        List<String> selectedCases = tagCases.stream()
                .filter(suiteCases::contains)
                .toList();
        if (selectedCases.isEmpty()) {
            throw new IllegalArgumentException("Acceptance suite " + suite + " does not include tag " + tag);
        }
        return selectedCases;
    }

    private static List<String> casesForSuite(String suite, List<String> allCaseIds) {
        return switch (suite) {
            case "acceptance" -> allCaseIds;
            case "hayes" -> List.of("A02", "A19");
            case "validation" -> List.of("A05");
            case "cellular" -> List.of("A03", "A04", "A06", "A07", "A08", "A21");
            case "sms" -> List.of("A09", "A10", "A20");
            case "scheduler" -> List.of("A11");
            case "macros" -> List.of("A12", "A24");
            case "security" -> List.of("A15", "A17", "A28");
            case "serial-config" -> List.of("A22", "A29");
            case "faults" -> List.of("A23");
            case "profiles" -> List.of("A25", "A32");
            case "parser" -> List.of("A26");
            case "build" -> List.of("A30");
            default -> throw new IllegalArgumentException("Unknown acceptance suite: " + suite);
        };
    }

    private static List<String> casesForTag(String tag) {
        return switch (tag) {
            case "gui" -> List.of("A13", "A14", "A27");
            case "serial-it" -> List.of("A01", "A11");
            default -> throw new IllegalArgumentException("Unknown acceptance tag: " + tag);
        };
    }

    private static String selectorLabel(String suite, String tag) {
        return tag == null ? "suite " + suite : "suite " + suite + " with tag " + tag;
    }

    static String normalize(String caseId) {
        return switch (caseId) {
            case "creg" -> "A03";
            case "profile-macro-negatives" -> "A05";
            case "csq" -> "A04";
            case "cpin", "cpin-puk" -> "A06";
            case "cops" -> "A07";
            case "cmee" -> "A08";
            case "text-cmgs" -> "A09";
            case "rate-limit" -> "A10";
            case "deterministic-delays" -> "A11";
            case "sms-error-123" -> "A12";
            case "live-log" -> "A13";
            case "injection" -> "A14";
            case "no-http-ws" -> "A15";
            case "coverage" -> "A16";
            case "xml-hardening", "xxe" -> "A17";
            case "data-mode-lines" -> "A19";
            case "storage-pdu" -> "A20";
            case "packet-registration-stubs" -> "A21";
            case "port-group" -> "A22";
            case "lifecycle-and-network" -> "A23";
            case "custom-response-cmsg" -> "A24";
            case "unknown-at-command-policy" -> "A25";
            case "malformed-and-ata" -> "A26";
            case "macro-hot-reload-timers" -> "A27";
            case "audit-backpressure" -> "A28";
            case "diagnostics" -> "A29";
            case "source-size-gate" -> "A30";
            case "golden-yaml-loader" -> "A31";
            case "s-register-bounds" -> "A32";
            default -> caseId;
        };
    }
}
