package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.state.FreezeMode;
import com.jkamsker.modemsim.commands.LineEnding;
import com.jkamsker.modemsim.validation.Dom;
import com.jkamsker.modemsim.validation.SchemaLocator;
import com.jkamsker.modemsim.validation.ValidationReport;
import com.jkamsker.modemsim.validation.XmlSecurity;
import org.w3c.dom.Element;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class MacroLoader {
    private final MacroXmlSemanticValidator xmlSemanticValidator;

    public MacroLoader() {
        this(Set.of(), false);
    }

    public MacroLoader(boolean timerDefinitionsAvailable) {
        this(timerDefinitionsAvailable ? java.util.Set.of("*") : java.util.Set.of(), false);
    }

    public MacroLoader(java.util.Set<String> timerIds) {
        this(timerIds, false);
    }

    public MacroLoader(java.util.Set<String> timerIds, boolean sessionSeedAvailable) {
        this.xmlSemanticValidator = new MacroXmlSemanticValidator(timerIds, sessionSeedAvailable);
    }

    public MacroSet load(Path path) {
        ValidatedMacro validated = validateMacro(path);
        validated.report().throwIfInvalid();
        return validated.macroSet();
    }

    public ValidationReport validate(Path path) {
        return validateMacro(path).report();
    }

    private ValidatedMacro validateMacro(Path path) {
        ValidationReport report = ValidationReport.ok();
        MacroSet macroSet = null;
        try {
            XmlSecurity.validate(path, SchemaLocator.schemaPath("macro-schema-draft.xsd"));
            Element root = XmlSecurity.parse(path).getDocumentElement();
            xmlSemanticValidator.validate(root, report);
            macroSet = parse(root);
            validateCompiledSemantics(macroSet, report);
        } catch (Exception e) {
            report.error(e.getMessage());
        }
        return new ValidatedMacro(macroSet, report);
    }

    private MacroSet parse(Element root) {
        try {
            List<MacroRule> rules = new ArrayList<>();
            int order = 0;
            String defaultLineEnding = lineEnding(root);
            for (Element child : Dom.children(root, null)) {
                if (child.getTagName().equals("macro")) {
                    rules.add(parseMacro(child, order++, defaultLineEnding));
                } else if (child.getTagName().equals("custom-response")) {
                    rules.add(parseCustomResponse(child, order++));
                }
            }
            Long randomSeed = randomSeed(root);
            return new MacroSet(MacroSet.effectiveHash(randomSeed, defaultLineEnding, rules),
                    randomSeed, defaultLineEnding, rules);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot parse macro file", e);
        }
    }

    private record ValidatedMacro(MacroSet macroSet, ValidationReport report) {
    }

    private MacroRule parseMacro(Element macro, int order, String defaultLineEnding) {
        MatchSpec match = parseMatch(Dom.child(macro, "match"));
        List<MacroAction> actions = new ArrayList<>();
        for (Element action : Dom.children(Dom.child(macro, "then"), null)) {
            actions.add(action(action, defaultLineEnding));
        }
        return new MacroRule(
                macro.getAttribute("id"),
                Dom.intAttr(macro, "priority", 0),
                order,
                phase(macro.getAttribute("phase")),
                Dom.boolAttr(macro, "enabled", true),
                parseCondition(Dom.child(macro, "when")),
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
                null,
                null);
        MacroAction action = new MacroAction("send", attributes(send), null);
        return new MacroRule(custom.getAttribute("id"), Dom.intAttr(custom, "priority", 0),
                order, MacroPhase.REPLACE, Dom.boolAttr(custom, "enabled", true),
                MacroCondition.always(), match, List.of(action));
    }

    private MacroCondition parseCondition(Element when) {
        if (when == null) {
            return MacroCondition.always();
        }
        List<MacroStatePredicate> states = new ArrayList<>();
        for (Element state : Dom.children(when, "state")) {
            states.add(new MacroStatePredicate(
                    state.getAttribute("path"), comparator(state), comparatorValue(state)));
        }
        List<MacroProfilePredicate> profiles = new ArrayList<>();
        for (Element profile : Dom.children(when, "profile")) {
            profiles.add(new MacroProfilePredicate(Dom.attr(profile, "id", null), Dom.attr(profile, "status", null)));
        }
        return new MacroCondition(states, profiles);
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
                regex(Dom.attr(body, "regex", null)),
                Dom.attr(match, "timerId", null));
    }

    private MacroAction action(Element action, String defaultLineEnding) {
        Map<String, String> attributes = attributes(action);
        if (action.getTagName().equals("emit") && !attributes.containsKey("lineEnding")) {
            attributes.put("lineEnding", defaultLineEnding);
        }
        return new MacroAction(action.getTagName(), attributes, action.getTextContent().trim());
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
        return command || sms || "state-change".equals(match.type()) || "timer".equals(match.type());
    }

    private MacroPhase phase(String value) {
        return MacroPhase.valueOf(value.replace('-', '_').toUpperCase());
    }

    private Long randomSeed(Element root) {
        String value = root.getAttribute("randomSeed");
        return value == null || value.isBlank() ? null : Long.valueOf(value);
    }

    private String lineEnding(Element root) {
        return LineEnding.from(root.getAttribute("lineEnding")).name();
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

    public static MacroStatePatch statePatch(MacroAction action) {
        return new MacroStatePatch(action.attr("path"), action.attr("value"));
    }

    public static MacroEventAction event(MacroAction action) {
        return new MacroEventAction(action.attr("type"), action.attr("message"));
    }

    private static Integer integer(String value) {
        return value == null || value.isBlank() ? null : Integer.valueOf(value);
    }

    private String comparator(Element state) {
        if (state.hasAttribute("equals")) {
            return "equals";
        }
        return state.hasAttribute("lessThan") ? "lessThan" : "greaterThan";
    }

    private String comparatorValue(Element state) {
        return state.getAttribute(comparator(state));
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
