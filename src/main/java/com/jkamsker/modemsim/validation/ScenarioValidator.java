package com.jkamsker.modemsim.validation;

import org.w3c.dom.Element;

import java.nio.file.Path;
import java.util.Set;

public final class ScenarioValidator {
    private static final Set<String> STATE_PATHS = Set.of(
            "state.sim.state",
            "state.network.stat",
            "state.signal.rssi",
            "state.signal.ber",
            "state.modemLines.dtr",
            "state.modemLines.dsr",
            "state.modemLines.dcd",
            "state.modemLines.ri");

    public ValidationReport validate(Path scenarioPath) {
        ValidationReport report = ValidationReport.ok();
        try {
            XmlSecurity.validate(scenarioPath, SchemaLocator.schemaPath("scenario.schema.xsd"));
            validateSemantics(XmlSecurity.parse(scenarioPath).getDocumentElement(), report);
        } catch (Exception e) {
            report.error(e.getMessage());
        }
        return report;
    }

    private void validateSemantics(Element root, ValidationReport report) {
        validateInitialState(Dom.child(root, "initial-state"), report);
        long lastStepMs = -1;
        for (Element step : Dom.children(root, "step")) {
            long atMs = Long.parseLong(step.getAttribute("atMs"));
            if (atMs < lastStepMs) {
                report.error(root.getAttribute("id") + ": scenario steps must be ordered by atMs");
            }
            lastStepMs = atMs;
            for (Element action : Dom.children(step, null)) {
                validateAction(root.getAttribute("id"), action, report);
            }
        }
        for (Element expect : Dom.children(root, "expect")) {
            if (Dom.children(expect, "line").isEmpty() && Dom.child(expect, "result") == null) {
                report.error(root.getAttribute("id") + ": expectation must declare a line or result");
            }
        }
    }

    private void validateInitialState(Element initial, ValidationReport report) {
        if (initial == null) {
            return;
        }
        Element network = Dom.child(initial, "network");
        Element signal = Dom.child(initial, "signal");
        if (network != null) {
            int stat = Dom.intAttr(network, "stat", 0);
            if (stat > 11) {
                report.error("network stat must be between 0 and 11");
            }
        }
        if (signal != null) {
            validateCsq("rssi", Dom.intAttr(signal, "rssi", 99), 31, report);
            validateCsq("ber", Dom.intAttr(signal, "ber", 99), 7, report);
        }
    }

    private void validateAction(String scenarioId, Element action, ValidationReport report) {
        if (action.getTagName().equals("set") && !STATE_PATHS.contains(action.getAttribute("path"))) {
            report.error(scenarioId + ": unknown state path " + action.getAttribute("path"));
        }
        if (action.getTagName().equals("fault")) {
            validateCsq("fault rssi", Dom.intAttr(action, "rssi", 99), 31, report);
            validateCsq("fault ber", Dom.intAttr(action, "ber", 99), 7, report);
        }
    }

    private void validateCsq(String field, int value, int maxKnown, ValidationReport report) {
        if (value != 99 && (value < 0 || value > maxKnown)) {
            report.error(field + " must be 0.." + maxKnown + " or 99");
        }
    }
}
