package com.alegs3.modemsim.macros;

import com.alegs3.modemsim.state.FreezeMode;
import com.alegs3.modemsim.validation.Dom;
import com.alegs3.modemsim.validation.SchemaLocator;
import com.alegs3.modemsim.validation.ValidationReport;
import com.alegs3.modemsim.validation.XmlSecurity;
import org.w3c.dom.Element;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class MacroLoader {
    public MacroSet load(Path path) {
        ValidationReport report = validate(path);
        report.throwIfInvalid();
        return parse(path);
    }

    public ValidationReport validate(Path path) {
        ValidationReport report = ValidationReport.ok();
        try {
            XmlSecurity.validate(path, SchemaLocator.schemaPath("macro-schema-draft.xsd"));
            parse(path);
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
                Dom.attr(body, "contains", null));
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
}
