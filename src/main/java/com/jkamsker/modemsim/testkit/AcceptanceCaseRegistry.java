package com.jkamsker.modemsim.testkit;

final class AcceptanceCaseRegistry {
    private AcceptanceCaseRegistry() {
    }

    static String defaultCaseForSuite(String suite) {
        return switch (suite) {
            case "hayes" -> "A02";
            case "validation" -> "A05";
            case "cellular" -> "A03";
            case "sms" -> "A09";
            case "scheduler" -> "A11";
            case "macros" -> "A12";
            case "gui" -> "A13";
            case "security" -> "A15";
            case "xml-hardening" -> "A17";
            case "replay" -> "A18";
            case "serial-config" -> "A22";
            case "faults" -> "A23";
            case "profiles" -> "A25";
            case "parser" -> "A26";
            case "build" -> "A30";
            case "golden" -> "A31";
            default -> "all";
        };
    }

    static String normalize(String caseId) {
        return switch (caseId) {
            case "creg" -> "A03";
            case "profile-macro-negatives" -> "A05";
            case "csq" -> "A04";
            case "cpin" -> "A06";
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
