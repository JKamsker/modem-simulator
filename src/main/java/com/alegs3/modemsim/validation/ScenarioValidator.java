package com.alegs3.modemsim.validation;

import java.nio.file.Path;

public final class ScenarioValidator {
    public ValidationReport validate(Path scenarioPath) {
        ValidationReport report = ValidationReport.ok();
        try {
            XmlSecurity.validate(scenarioPath, SchemaLocator.schemaPath("scenario.schema.xsd"));
        } catch (Exception e) {
            report.error(e.getMessage());
        }
        return report;
    }
}
