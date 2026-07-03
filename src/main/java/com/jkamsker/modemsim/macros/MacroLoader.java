package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.state.FreezeMode;
import com.jkamsker.modemsim.validation.Dom;
import com.jkamsker.modemsim.validation.SchemaLocator;
import com.jkamsker.modemsim.validation.ValidationReport;
import com.jkamsker.modemsim.validation.XmlSecurity;
import org.w3c.dom.Element;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class MacroLoader {
    private static final Set<String> STATE_PATHS = Set.of(
            "state.sim.state",
            "state.network.stat",
            "state.signal.rssi",
            "state.signal.ber",
            "state.modem.lifecycle",
            "state.modem.freezeMode",
            "state.modem.bootDelayMs",
            "state.modemLines.dtr",
            "state.modemLines.dsr",
            "state.modemLines.dcd",
            "state.modemLines.ri",
            "state.modemLines.rts",
            "state.modemLines.cts");

    public MacroSet load(Path path) {
        ValidationReport report = validate(path);
        report.throwIfInvalid();
        return parse(path);
    }

    public ValidationReport validate(Path path) {
        ValidationReport report = ValidationReport.ok();
        try {
            XmlSecurity.validate(path, SchemaLocator.schemaPath("macro-schema-draft.xsd"));
            validateXmlSemantics(XmlSecurity.parse(path).getDocumentElement(), report);
            validateCompiledSemantics(parse(path), report);
        } catch (Exception e) {
            report.error(e.getMessage());
        }
        return report;
    }

    private MacroSet parse(Path path) {
        try {
            Element root = XmlSecurity.parse(path).getDocumentElement();
            List<MacroRule> rules = new ArrayList<>();
            int order = 0;
            for (Element child : Dom.children(root, null)) {
                if (child.getTagName().equals("macro")) {
                    rules.add(parseMacro(child, order++));
                } else if (child.getTagName().equals("custom-response")) {
                    rules.add(parseCustomResponse(child, order++));
                }
            }
            return new MacroSet(hash(path), rules);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot parse macro file: " + path, e);
        }
    }

    private MacroRule parseMacro(Element macro, int order) {
        MatchSpec match = parseMatch(Dom.child(macro, "match"));
        List<MacroAction> actions = new ArrayList<>();
        for (Element action : Dom.children(Dom.child(macro, "then"), null)) {
            actions.add(action(action));
        }
        return new MacroRule(
                macro.getAttribute("id"),
                Dom.intAttr(macro, "priority", 0),
                order,
                phase(macro.getAttribute("phase")),
                Dom.boolAttr(macro, "enabled", true),
                match,
                actions);
    }

    private MacroRule parseCustomResponse(Element custom, int order) {
        Element ifElement = Dom.child(custom, "if");
        Element send = Dom.child(custom, "send");
        MatchSpec match = new MatchSpec(
                Dom.attr(ifElement, "command", null),
                Dom.attr(ifElement, "mode", null),
                Dom.attr(ifElement, "rawGlob", null),
                regex(Dom.attr(ifElement, "rawRegex", null)),
                "at-command",
                null,
                null,
                null,
                null,
                null,
                null);
        MacroAction action = new MacroAction("send", attributes(send), null);
        return new MacroRule(custom.getAttribute("id"), Dom.intAttr(custom, "priority", 0),
                order, MacroPhase.REPLACE, Dom.boolAttr(custom, "enabled", true), match, List.of(action));
    }

    private MatchSpec parseMatch(Element match) {
        Element destination = Dom.child(match, "destination");
        Element body = Dom.child(match, "body");
        return new MatchSpec(
                Dom.attr(match, "command", null),
                Dom.attr(match, "mode", null),
                Dom.attr(match, "rawGlob", null),
                regex(Dom.attr(match, "rawRegex", null)),
                Dom.attr(match, "type", "at-command"),
                Dom.attr(destination, "equals", null),
                Dom.attr(destination, "contains", null),
                regex(Dom.attr(destination, "regex", null)),
                Dom.attr(body, "equals", null),
                Dom.attr(body, "contains", null),
                regex(Dom.attr(body, "regex", null)));
    }

    private MacroAction action(Element action) {
        return new MacroAction(action.getTagName(), attributes(action), action.getTextContent().trim());
    }

    private Map<String, String> attributes(Element element) {
        Map<String, String> result = new LinkedHashMap<>();
        for (int i = 0; i < element.getAttributes().getLength(); i++) {
            var attribute = element.getAttributes().item(i);
            result.put(attribute.getNodeName(), attribute.getNodeValue());
        }
        return result;
    }

    private Pattern regex(String value) {
        return value == null ? null : Pattern.compile(value, Pattern.CASE_INSENSITIVE);
    }

    private void validateXmlSemantics(Element root, ValidationReport report) {
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
        validateMatchElement(macro.getAttribute("id"), Dom.child(macro, "match"), report);
        for (Element action : Dom.children(Dom.child(macro, "then"), null)) {
            validateAction(macro.getAttribute("id"), action, report);
        }
        if (phase(macro.getAttribute("phase")) == MacroPhase.ON_TIMER) {
            report.error(macro.getAttribute("id") + ": on-timer requires runtime timer configuration");
        }
    }

    private void validateMatchElement(String id, Element match, ValidationReport report) {
        int directCount = countAttributes(match, "command", "rawGlob", "rawRegex");
        validateStringPredicate(id, "destination", Dom.child(match, "destination"), report);
        validateStringPredicate(id, "body", Dom.child(match, "body"), report);
        boolean hasSmsPredicate = Dom.child(match, "destination") != null || Dom.child(match, "body") != null;
        if (directCount > 1) {
            report.error(id + ": match must not mix command, rawGlob and rawRegex");
        }
        if (directCount == 0 && !hasSmsPredicate) {
            report.error(id + ": match must declare a command, raw predicate, or SMS predicate");
        }
    }

    private void validateStringPredicate(
            String id, String name, Element predicate, ValidationReport report) {
        if (predicate != null && countAttributes(predicate, "equals", "contains", "regex") != 1) {
            report.error(id + ": " + name + " predicate must declare exactly one of equals, contains or regex");
        }
    }

    private void validateAction(String id, Element action, ValidationReport report) {
        switch (action.getTagName()) {
            case "emit" -> validatePayloadAction(id, action, "emit", report);
            case "set" -> {
                if (!STATE_PATHS.contains(action.getAttribute("path"))) {
                    report.error(id + ": unknown state path " + action.getAttribute("path"));
                }
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
                report.error(id + ": fault " + action.getAttribute("type")
                        + " does not accept " + attribute);
            }
        }
    }

    private void validateCompiledSemantics(MacroSet macroSet, ValidationReport report) {
        for (MacroRule rule : macroSet.rules()) {
            if (rule.actions().isEmpty()) {
                report.error(rule.id() + ": macro must contain at least one action");
            }
            if (!hasMatchTarget(rule.match())) {
                report.error(rule.id() + ": compiled match has no target");
            }
        }
    }

    private boolean hasMatchTarget(MatchSpec match) {
        boolean command = !blank(match.command()) || !blank(match.rawGlob()) || match.rawRegex() != null;
        boolean sms = "sms-submit".equals(match.type())
                && (!blank(match.destinationEquals()) || !blank(match.destinationContains())
                || match.destinationRegex() != null || !blank(match.bodyEquals())
                || !blank(match.bodyContains()) || match.bodyRegex() != null);
        return command || sms || "state-change".equals(match.type());
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

    private String hash(Path path) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return "sha256:" + java.util.HexFormat.of().formatHex(digest.digest(Files.readAllBytes(path)));
    }

    public static FaultAction fault(MacroAction action) {
        return new FaultAction(
                action.attr("type"),
                integer(action.attr("durationMs")),
                integer(action.attr("stat")),
                integer(action.attr("rssi")),
                integer(action.attr("ber")),
                action.attr("freezeMode") == null ? null : FreezeMode.valueOf(action.attr("freezeMode")));
    }

    private static Integer integer(String value) {
        return value == null || value.isBlank() ? null : Integer.valueOf(value);
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
