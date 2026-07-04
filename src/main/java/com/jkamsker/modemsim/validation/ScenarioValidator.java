package com.jkamsker.modemsim.validation;

import org.w3c.dom.Element;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public final class ScenarioValidator {
    public ValidationReport validate(Path scenarioPath) {
        return validateScenario(scenarioPath).report();
    }

    public ValidatedScenario validateScenario(Path scenarioPath) {
        ValidationReport report = ValidationReport.ok();
        Element root = null;
        try {
            root = XmlSecurity.parseValidated(scenarioPath, SchemaLocator.schemaPath("scenario.schema.xsd"))
                    .getDocumentElement();
            validateSemantics(root, report);
        } catch (ValidationException e) {
            report.error(e.getMessage());
        }
        return new ValidatedScenario(root, report);
    }

    private void validateSemantics(Element root, ValidationReport report) {
        Map<String, String> knownState = validateInitialState(root.getAttribute("id"), Dom.child(root, "initial-state"), report);
        long lastStepMs = -1;
        for (Element step : Dom.children(root, "step")) {
            long atMs = Long.parseLong(step.getAttribute("atMs"));
            if (atMs < lastStepMs) {
                report.error(root.getAttribute("id") + ": scenario steps must be ordered by atMs");
            }
            lastStepMs = atMs;
            for (Element action : Dom.children(step, null)) {
                if (action.getTagName().equals("set")) {
                    knownState.put(action.getAttribute("path"), action.getAttribute("value"));
                } else if (action.getTagName().equals("fault")) {
                    applyFault(action, knownState);
                }
                validateAction(root.getAttribute("id"), action, report);
            }
            validateCombinedState(root.getAttribute("id"), knownState, report);
        }
        for (Element expect : Dom.children(root, "expect")) {
            if (Dom.children(expect, "line").isEmpty() && Dom.child(expect, "result") == null) {
                report.error(root.getAttribute("id") + ": expectation must declare a line or result");
            }
        }
    }

    private Map<String, String> validateInitialState(String scenarioId, Element initial, ValidationReport report) {
        Map<String, String> knownState = new java.util.LinkedHashMap<>();
        if (initial == null) {
            return knownState;
        }
        Element sim = Dom.child(initial, "sim");
        Element network = Dom.child(initial, "network");
        Element signal = Dom.child(initial, "signal");
        if (sim != null && sim.hasAttribute("state")) {
            addStateValueError(scenarioId, "state.sim.state", sim.getAttribute("state"), report);
            knownState.put("state.sim.state", sim.getAttribute("state"));
        }
        if (network != null) {
            if (network.hasAttribute("cregN")) {
                addStateValueError(scenarioId, "state.network.cregN", network.getAttribute("cregN"), report);
            }
            if (network.hasAttribute("stat")) {
                addStateValueError(scenarioId, "state.network.stat", network.getAttribute("stat"), report);
                knownState.put("state.network.stat", network.getAttribute("stat"));
            }
        }
        if (signal != null) {
            validateCsq("rssi", Dom.intAttr(signal, "rssi", 99), 31, report);
            validateCsq("ber", Dom.intAttr(signal, "ber", 99), 7, report);
        }
        validateCombinedState(scenarioId, knownState, report);
        return knownState;
    }

    private void validateAction(String scenarioId, Element action, ValidationReport report) {
        if (action.getTagName().equals("set")) {
            String path = action.getAttribute("path");
            if (!StateValueValidator.paths().contains(path)) {
                report.error(scenarioId + ": unknown state path " + path);
            }
            addStateValueError(scenarioId, path, action.getAttribute("value"), report);
        }
        if (action.getTagName().equals("fault")) {
            validateFaultAction(scenarioId, action, report);
        }
    }

    private void validateFaultAction(String scenarioId, Element action, ValidationReport report) {
        Set<String> allowed = switch (action.getAttribute("type")) {
            case "network-restore" -> Set.of("stat", "rssi", "ber");
            case "modem-reboot" -> Set.of("durationMs");
            case "modem-freeze" -> Set.of("freezeMode");
            default -> Set.of();
        };
        for (String attribute : Set.of("durationMs", "stat", "rssi", "ber", "freezeMode")) {
            if (action.hasAttribute(attribute) && !allowed.contains(attribute)) {
                report.error(scenarioId + ": fault " + action.getAttribute("type") + " does not accept " + attribute);
            }
        }
        addFaultValueError(scenarioId, action, "stat", "state.network.stat", report);
        addFaultValueError(scenarioId, action, "rssi", "state.signal.rssi", report);
        addFaultValueError(scenarioId, action, "ber", "state.signal.ber", report);
        addFaultValueError(scenarioId, action, "freezeMode", "state.modem.freezeMode", report);
    }

    private void applyFault(Element action, Map<String, String> knownState) {
        switch (action.getAttribute("type")) {
            case "network-outage" -> knownState.put("state.network.stat", "4");
            case "network-restore" -> knownState.put("state.network.stat", Dom.attr(action, "stat", "1"));
            default -> {
            }
        }
    }

    private void addFaultValueError(
            String scenarioId, Element action, String attribute, String path, ValidationReport report) {
        if (action.hasAttribute(attribute)) {
            addStateValueError(scenarioId, path, action.getAttribute(attribute), report);
        }
    }

    private void addStateValueError(String scenarioId, String path, String value, ValidationReport report) {
        String error = StateValueValidator.validate(path, value);
        if (error != null) {
            report.error(scenarioId + ": " + error);
        }
    }

    private void validateCsq(String field, int value, int maxKnown, ValidationReport report) {
        if (value != 99 && (value < 0 || value > maxKnown)) {
            report.error(field + " must be 0.." + maxKnown + " or 99");
        }
    }

    private void validateCombinedState(String id, Map<String, String> patches, ValidationReport report) {
        String error = StateInvariantValidator.lockedRegistration(id, patches);
        if (error != null) {
            report.error(error);
        }
    }

    public record ValidatedScenario(Element root, ValidationReport report) {
    }
}
