package com.alegs3.modemsim.testkit;

public record AcceptanceResult(String caseId, boolean passed, String message) {
    public static AcceptanceResult pass(String caseId) {
        return new AcceptanceResult(caseId, true, "passed");
    }

    public static AcceptanceResult fail(String caseId, String message) {
        return new AcceptanceResult(caseId, false, message);
    }
}
