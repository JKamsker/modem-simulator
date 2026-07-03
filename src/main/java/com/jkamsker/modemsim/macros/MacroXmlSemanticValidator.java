package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.validation.Dom;
import com.jkamsker.modemsim.validation.StateInvariantValidator;
import com.jkamsker.modemsim.validation.StateValueValidator;
import com.jkamsker.modemsim.validation.ValidationReport;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Set;

final class MacroXmlSemanticValidator {
    private final Set<String> timerIds;

    MacroXmlSemanticValidator() {
        this(Set.of());
    }

    MacroXmlSemanticValidator(boolean timerDefinitionsAvailable) {
        this(timerDefinitionsAvailable ? Set.of("*") : Set.of());
    }

    MacroXmlSemanticValidator(Set<String> timerIds) {
        this.timerIds = Set.copyOf(timerIds);
    }

    void validate(Element root, ValidationReport report) {
        Set<String> ids = new java.util.HashSet<>();
        for (Element child : Dom.children(root, null)) {
            String id = child.getAttribute("id");
            if (!ids.add(id)) {
                report.error("duplicate macro id: " + id);
            }
            if (child.getTagName().equals("custom-response")) {
                validateCustomResponse(child, report);
            } else {
                validateMacroElement(child, report);
            }
        }
    }

    private void validateCustomResponse(Element custom, ValidationReport report) {
        Element ifElement = Dom.child(custom, "if");
        int matchCount = countAttributes(ifElement, "command", "rawGlob", "rawRegex");
        if (matchCount != 1) {
            report.error(custom.getAttribute("id") + ": custom-response if must declare exactly one match criterion");
        }
        if (countAttributes(Dom.child(custom, "send"), "text", "rawHex", "line") != 1) {
            report.error(custom.getAttribute("id") + ": custom-response send must declare exactly one payload");
        }
    }

    private void validateMacroElement(Element macro, ValidationReport report) {
        String id = macro.getAttribute("id");
        Element match = Dom.child(macro, "match");
        MacroPhase macroPhase = phase(macro.getAttribute("phase"));
        if (macroPhase == MacroPhase.ON_TIMER) {
            validateTimer(id, match, report);
        } else if (macroPhase == MacroPhase.ON_STATE_CHANGE && !"state-change".equals(Dom.attr(match, "type", ""))) {
            report.error(id + ": on-state-change macro requires match type=\"state-change\"");
        }
        validateMatchElement(id, match, report);
        java.util.Map<String, String> knownState = validateWhenElement(id, Dom.child(macro, "when"), report);
        java.util.Map<String, String> patches = new java.util.LinkedHashMap<>();
        for (Element action : Dom.children(Dom.child(macro, "then"), null)) {
            if (action.getTagName().equals("set")) {
                patches.put(action.getAttribute("path"), action.getAttribute("value"));
                knownState.put(action.getAttribute("path"), action.getAttribute("value"));
            } else if (action.getTagName().equals("fault")) {
                applyFault(action, knownState);
            }
            validateAction(id, action, report);
        }
        validateCombinedState(id, knownState, report);
    }

    private void validateTimer(String id, Element match, ValidationReport report) {
        String timerId = Dom.attr(match, "timerId", null);
        if (!"timer".equals(Dom.attr(match, "type", ""))) {
            report.error(id + ": on-timer macro requires match type=\"timer\"");
        }
        if (timerIds.isEmpty()) {
            report.error(id + ": on-timer macro requires a runtime timer definition");
        } else if (!timerIds.contains("*") && (timerId == null || !timerIds.contains(timerId))) {
            report.error(id + ": on-timer macro references unknown timerId " + timerId);
        }
    }

    private java.util.Map<String, String> validateWhenElement(String id, Element when, ValidationReport report) {
        java.util.Map<String, String> knownState = new java.util.LinkedHashMap<>();
        if (when == null) {
            return knownState;
        }
        for (Element state : Dom.children(when, "state")) {
            String path = state.getAttribute("path");
            if (!StateValueValidator.paths().contains(path)) {
                report.error(id + ": unknown state path " + path);
            }
            if (countAttributes(state, "equals", "lessThan", "greaterThan") != 1) {
                report.error(id + ": state predicate must declare exactly one comparator");
            }
            if ((state.hasAttribute("lessThan") || state.hasAttribute("greaterThan"))
                    && !StateValueValidator.isNumericPath(path)) {
                report.error(id + ": range comparator requires numeric state path " + path);
            }
            String equals = Dom.attr(state, "equals", null);
            if (equals != null) {
                addStateValueError(id, path, equals, report);
                knownState.put(path, equals);
            }
        }
        return knownState;
    }

    private void validateMatchElement(String id, Element match, ValidationReport report) {
        int directCount = countAttributes(match, "command", "rawGlob", "rawRegex");
        validateStringPredicate(id, "destination", Dom.child(match, "destination"), report);
        validateStringPredicate(id, "body", Dom.child(match, "body"), report);
        boolean hasSmsPredicate = Dom.child(match, "destination") != null || Dom.child(match, "body") != null;
        boolean stateOrTimer = Set.of("state-change", "timer").contains(Dom.attr(match, "type", ""));
        if (directCount > 1) {
            report.error(id + ": match must not mix command, rawGlob and rawRegex");
        }
        if (directCount == 0 && !hasSmsPredicate && !stateOrTimer) {
            report.error(id + ": match must declare a command, raw predicate, or SMS predicate");
        }
    }

    private void validateStringPredicate(String id, String name, Element predicate, ValidationReport report) {
        if (predicate != null && countAttributes(predicate, "equals", "contains", "regex") != 1) {
            report.error(id + ": " + name + " predicate must declare exactly one of equals, contains or regex");
        }
    }

    private void validateAction(String id, Element action, ValidationReport report) {
        switch (action.getTagName()) {
            case "emit" -> validatePayloadAction(id, action, "emit", report);
            case "set" -> {
                if (!StateValueValidator.paths().contains(action.getAttribute("path"))) {
                    report.error(id + ": unknown state path " + action.getAttribute("path"));
                }
                addStateValueError(id, action.getAttribute("path"), action.getAttribute("value"), report);
            }
            case "fault" -> validateFaultAction(id, action, report);
            default -> {
            }
        }
    }

    private void validatePayloadAction(String id, Element action, String name, ValidationReport report) {
        int payloads = countAttributes(action, "line", "raw", "rawHex")
                + (action.getTextContent().trim().isEmpty() ? 0 : 1);
        if (payloads != 1) {
            report.error(id + ": " + name + " must declare exactly one payload");
        }
    }

    private void validateFaultAction(String id, Element action, ValidationReport report) {
        Set<String> allowed = switch (action.getAttribute("type")) {
            case "network-restore" -> Set.of("stat", "rssi", "ber");
            case "modem-reboot" -> Set.of("durationMs");
            case "modem-freeze" -> Set.of("freezeMode");
            default -> Set.of();
        };
        for (String attribute : List.of("durationMs", "stat", "rssi", "ber", "freezeMode")) {
            if (action.hasAttribute(attribute) && !allowed.contains(attribute)) {
                report.error(id + ": fault " + action.getAttribute("type") + " does not accept " + attribute);
            }
        }
        validateFaultValue(id, action, "stat", "state.network.stat", report);
        validateFaultValue(id, action, "rssi", "state.signal.rssi", report);
        validateFaultValue(id, action, "ber", "state.signal.ber", report);
        validateFaultValue(id, action, "freezeMode", "state.modem.freezeMode", report);
    }

    private void applyFault(Element action, java.util.Map<String, String> knownState) {
        switch (action.getAttribute("type")) {
            case "network-outage" -> knownState.put("state.network.stat", "4");
            case "network-restore" -> knownState.put("state.network.stat", Dom.attr(action, "stat", "1"));
            default -> {
            }
        }
    }

    private void validateFaultValue(
            String id, Element action, String attribute, String statePath, ValidationReport report) {
        if (action.hasAttribute(attribute)) {
            addStateValueError(id, statePath, action.getAttribute(attribute), report);
        }
    }

    private void addStateValueError(String id, String path, String value, ValidationReport report) {
        String error = StateValueValidator.validate(path, value);
        if (error != null) {
            report.error(id + ": " + error);
        }
    }

    private void validateCombinedState(String id, java.util.Map<String, String> patches, ValidationReport report) {
        String error = StateInvariantValidator.lockedRegistration(id, patches);
        if (error != null) {
            report.error(error);
        }
    }

    private int countAttributes(Element element, String... names) {
        if (element == null) {
            return 0;
        }
        int count = 0;
        for (String name : names) {
            if (element.hasAttribute(name) && !element.getAttribute(name).isBlank()) {
                count++;
            }
        }
        return count;
    }

    private MacroPhase phase(String value) {
        return MacroPhase.valueOf(value.replace('-', '_').toUpperCase());
    }
}
