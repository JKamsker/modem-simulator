package com.jkamsker.modemsim.testkit;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AcceptanceSuiteTest {
    @Test
    void allAcceptanceCasesPass() {
        AcceptanceSuite suite = new AcceptanceSuite();

        assertThat(suite.caseIds())
                .allSatisfy(caseId -> assertThat(suite.run(caseId))
                        .extracting(AcceptanceResult::passed)
                        .isEqualTo(true));
    }
}
