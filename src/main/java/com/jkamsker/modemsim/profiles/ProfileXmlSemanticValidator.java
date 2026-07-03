package com.jkamsker.modemsim.profiles;

import com.jkamsker.modemsim.validation.Dom;
import com.jkamsker.modemsim.validation.ValidationReport;
import org.w3c.dom.Element;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

final class ProfileXmlSemanticValidator {
    private static final Set<String> BUILTIN_PARENT_IDS = Set.of(
            "generic-hayes-v250",
            "3gpp-27007-r18",
            "3gpp-27005-r16",
            "sierra-common",
            "sierra-hl6-hl8-v20",
            "westermo-common",
            "westermo-td22-6177-2203",
            "westermo-td36-6618-2202",
            "westermo-gd01-6196-2220",
            "westermo-gdw11-6615-2220");

    Set<String> profileIds(Element root) {
        Set<String> ids = new HashSet<>(BUILTIN_PARENT_IDS);
        Dom.children(root, "profile").forEach(profile -> ids.add(profile.getAttribute("id")));
        return ids;
    }

    void validateRoot(Element root, ValidationReport report) {
        Map<String, Element> profiles = new LinkedHashMap<>();
        for (Element profile : Dom.children(root, "profile")) {
            profiles.put(profile.getAttribute("id"), profile);
        }
        for (String id : profiles.keySet()) {
            detectCycle(id, profiles, new HashSet<>(), new HashSet<>(), report);
        }
        for (Element profile : profiles.values()) {
            detectInheritanceConflicts(profile, profiles, report);
        }
    }

    void validate(Element profile, Set<String> knownIds, ValidationReport report) {
        validateParents(profile, knownIds, report);
        Element initial = Dom.child(profile, "initial-state");
        String kind = Dom.attr(profile, "profileKind", "cellular");
        var parents = ProfileXmlSupport.parents(profile.getAttribute("extends"));
        boolean inherited = !parents.isEmpty();
        boolean cellular = Set.of("cellular", "hybrid").contains(kind);
        if (cellular && !inherited) {
            requireChild(profile, initial, "sim", report);
            requireChild(profile, initial, "network", report);
            requireChild(profile, initial, "signal", report);
        }
        if (!Set.of("base", "isdn").contains(kind) && !inherited) {
            requireChild(profile, initial, "call", report);
            requireChild(profile, initial, "modem-lines", report);
        }
        if (Set.of("pstn", "isdn").contains(kind) && initial != null
                && (Dom.child(initial, "sim") != null || Dom.child(initial, "network") != null
                || Dom.child(initial, "signal") != null)) {
            report.error(profile.getAttribute("id") + ": " + kind + " profile must not require mobile state blocks");
        }
        Element network = initial == null ? null : Dom.child(initial, "network");
        if (cellular && network != null && Dom.child(network, "operator") == null) {
            report.error(profile.getAttribute("id") + ": cellular profile requires operator metadata");
        }
        validateDelayOperations(profile, network, report);
        validateCoverage(profile, Dom.child(profile, "coverage"), report);
    }

    private void detectCycle(
            String id, Map<String, Element> profiles, Set<String> visiting,
            Set<String> visited, ValidationReport report) {
        if (visited.contains(id) || !profiles.containsKey(id)) {
            return;
        }
        if (!visiting.add(id)) {
            report.error(id + ": cyclic parent profile");
            return;
        }
        for (String parent : ProfileXmlSupport.parents(profiles.get(id).getAttribute("extends"))) {
            detectCycle(parent, profiles, visiting, visited, report);
        }
        visiting.remove(id);
        visited.add(id);
    }

    private void validateParents(Element profile, Set<String> knownIds, ValidationReport report) {
        for (String parent : ProfileXmlSupport.parents(profile.getAttribute("extends"))) {
            if (!knownIds.contains(parent)) {
                report.error(profile.getAttribute("id") + ": missing parent profile " + parent);
            }
        }
    }

    private void detectInheritanceConflicts(Element profile, Map<String, Element> profiles, ValidationReport report) {
        Map<String, String> commands = new LinkedHashMap<>();
        Map<String, String> registers = new LinkedHashMap<>();
        for (String parent : ProfileXmlSupport.parents(profile.getAttribute("extends"))) {
            collectParentMetadata(profile.getAttribute("id"), parent, profiles, commands, registers, new HashSet<>(), report);
        }
    }

    private void collectParentMetadata(
            String childId, String parentId, Map<String, Element> profiles,
            Map<String, String> commands, Map<String, String> registers,
            Set<String> seen, ValidationReport report) {
        Element parent = profiles.get(parentId);
        if (parent == null || !seen.add(parentId)) {
            return;
        }
        for (String grandParent : ProfileXmlSupport.parents(parent.getAttribute("extends"))) {
            collectParentMetadata(childId, grandParent, profiles, commands, registers, seen, report);
        }
        collectNames(childId, parentId, commands, Dom.child(parent, "commands"), "command", "name", "command", report);
        collectNames(childId, parentId, registers, Dom.child(parent, "registers"), "register", "name", "register", report);
    }

    private void collectNames(
            String childId, String parentId, Map<String, String> seen, Element root,
            String elementName, String attribute, String label, ValidationReport report) {
        if (root == null) {
            return;
        }
        for (Element element : Dom.children(root, elementName)) {
            String name = element.getAttribute(attribute);
            String previous = seen.putIfAbsent(name, parentId);
            if (previous != null && !previous.equals(parentId)) {
                report.error(childId + ": inherited " + label + " conflict " + name
                        + " from " + previous + " and " + parentId);
            }
        }
    }

    private void requireChild(Element profile, Element initial, String name, ValidationReport report) {
        if (initial == null || Dom.child(initial, name) == null) {
            report.error(profile.getAttribute("id") + ": cellular profile requires " + name + " state");
        }
    }

    private void validateDelayOperations(Element profile, Element network, ValidationReport report) {
        Element delays = network == null ? null : Dom.child(network, "delays");
        if (delays == null) {
            return;
        }
        Set<String> operations = new HashSet<>();
        for (Element delay : Dom.children(delays, "delay")) {
            String operation = delay.getAttribute("operation");
            if (!operations.add(operation)) {
                report.error(profile.getAttribute("id") + ": duplicate delay operation " + operation);
            }
        }
    }

    private void validateCoverage(Element profile, Element coverage, ValidationReport report) {
        if (coverage == null) {
            return;
        }
        String id = profile.getAttribute("id");
        if (Dom.intAttr(coverage, "unknown", 0) != 0) {
            report.error(id + ": coverage unknown must be zero");
        }
        int commandsTotal = Dom.intAttr(coverage, "commandsTotal", 0);
        Set<String> commands = new HashSet<>();
        for (Element command : Dom.children(coverage, "command")) {
            String name = command.getAttribute("name");
            if (!commands.add(name)) {
                report.error(id + ": duplicate coverage command " + name);
            }
        }
        if (commandsTotal < commands.size()) {
            report.error(id + ": coverage commandsTotal must cover listed commands");
        }
    }
}
