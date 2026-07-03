package com.jkamsker.modemsim.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationReportRedactionTest {
    @Test
    void errorsAndWarningsRedactSensitiveAttributeValues() {
        ValidationReport report = ValidationReport.ok();

        report.error("bad profile pin=\"9876\" imsi=\"262010123456789\"");
        report.warning("imei=\"359762080000001\" is invalid");

        assertThat(report.errors().getFirst())
                .contains("pin=\"<redacted>\"")
                .doesNotContain("9876")
                .doesNotContain("262010123456789");
        assertThat(report.warnings().getFirst())
                .contains("imei=\"<redacted>\"")
                .doesNotContain("359762080000001");
    }
}
