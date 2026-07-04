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
    private Map<String, Element> localProfiles = Map.of();

    Set<String> profileIds(Element root) {
        Set<String> ids = new HashSet<>(BUILTIN_PARENT_IDS);
        Dom.children(root, "profile").forEach(profile -> ids.add(profile.getAttribute("id")));
        return ids;
    }

    void validateRoot(Element root, ValidationReport report) {
        Map<String, Element> profiles = new LinkedHashMap<>();
        for (Element profile : Dom.children(root, "profile")) {
            String id = profile.getAttribute("id");
            if (profiles.containsKey(id)) {
                report.error(id + ": duplicate profile id");
            } else {
                profiles.put(id, profile);
            }
        }
        localProfiles = Map.copyOf(profiles);
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
        if (cellular && network != null && Dom.child(network, "operator") == null
                && !parentSuppliesOperator(parents, new HashSet<>())) {
            report.error(profile.getAttribute("id") + ": cellular profile requires operator metadata");
        }
        validateNetworkLocation(profile, network, report);
        validateDelayOperations(profile, network, report);
        validateDuplicateRegisters(profile, report);
        validateCoverage(profile, kind, Dom.child(profile, "coverage"), report);
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
        if (BUILTIN_PARENT_IDS.contains(profile.getAttribute("id"))) {
            return;
        }
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
        if (!seen.add(parentId)) {
            return;
        }
        if (parent == null) {
            collectBuiltinMetadata(childId, parentId, commands, registers, report);
            return;
        }
        for (String grandParent : ProfileXmlSupport.parents(parent.getAttribute("extends"))) {
            collectParentMetadata(childId, grandParent, profiles, commands, registers, seen, report);
        }
        collectNames(childId, parentId, commands, Dom.child(parent, "commands"), "command", "name", "command", report);
        collectNames(childId, parentId, registers, Dom.child(parent, "registers"), "register", "name", "register", report);
    }

    private void collectBuiltinMetadata(
            String childId, String parentId, Map<String, String> commands,
            Map<String, String> registers, ValidationReport report) {
        if (!BUILTIN_PARENT_IDS.contains(parentId)) {
            return;
        }
        Profile parent = BuiltinProfiles.byId(parentId);
        for (ProfileCommand command : parent.commands()) {
            collectName(childId, parentId, commands, command.name(), "command", report);
        }
        for (ProfileRegister register : parent.registers()) {
            collectName(childId, parentId, registers, register.name(), "register", report);
        }
    }

    private void collectNames(
            String childId, String parentId, Map<String, String> seen, Element root,
            String elementName, String attribute, String label, ValidationReport report) {
        if (root == null) {
            return;
        }
        for (Element element : Dom.children(root, elementName)) {
            collectName(childId, parentId, seen, element.getAttribute(attribute), label, report);
        }
    }

    private void collectName(
            String childId, String parentId, Map<String, String> seen,
            String name, String label, ValidationReport report) {
        String previous = seen.putIfAbsent(name, parentId);
        if (previous != null && !previous.equals(parentId)) {
            report.warning(childId + ": inherited " + label + " conflict " + name
                    + " from " + previous + " and " + parentId);
        }
    }

    private void requireChild(Element profile, Element initial, String name, ValidationReport report) {
        if (initial == null || Dom.child(initial, name) == null) {
            report.error(profile.getAttribute("id") + ": cellular profile requires " + name + " state");
        }
    }

    private boolean parentSuppliesOperator(java.util.List<String> parents, Set<String> seen) {
        for (String parentId : parents) {
            if (!seen.add(parentId)) {
                continue;
            }
            Element parent = localProfiles.get(parentId);
            if (parent != null && profileSuppliesOperator(parent, seen)) {
                return true;
            }
            if (parent == null && BUILTIN_PARENT_IDS.contains(parentId)) {
                var network = BuiltinProfiles.byId(parentId).initialState().network();
                if (network != null && network.operator() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean profileSuppliesOperator(Element profile, Set<String> seen) {
        Element initial = Dom.child(profile, "initial-state");
        Element network = initial == null ? null : Dom.child(initial, "network");
        if (network != null && Dom.child(network, "operator") != null) {
            return true;
        }
        return parentSuppliesOperator(ProfileXmlSupport.parents(profile.getAttribute("extends")), seen);
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

    private void validateCoverage(Element profile, String kind, Element coverage, ValidationReport report) {
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
            if (Set.of("pstn", "isdn").contains(kind) && cellularCommand(name)) {
                report.error(id + ": " + kind + " profile must not claim cellular coverage command " + name);
            }
        }
        if (commandsTotal < commands.size()) {
            report.error(id + ": coverage commandsTotal must cover listed commands");
        }
    }

    private void validateNetworkLocation(Element profile, Element network, ValidationReport report) {
        if (network == null || Set.of("1", "5").contains(network.getAttribute("stat")) || compatibilityDeviation(profile)) {
            return;
        }
        if (network.hasAttribute("lac") || network.hasAttribute("ci") || network.hasAttribute("act")) {
            report.error(profile.getAttribute("id") + ": non-registered CREG state must not expose lac/ci/act");
        }
    }

    private boolean compatibilityDeviation(Element profile) {
        Element deviations = Dom.child(profile, "deviations");
        return deviations != null && Dom.children(deviations, "deviation").stream()
                .anyMatch(deviation -> "compatibility".equals(deviation.getAttribute("severity")));
    }

    private boolean cellularCommand(String name) {
        return name.startsWith("AT+C") || name.startsWith("+C");
    }

    private void validateDuplicateRegisters(Element profile, ValidationReport report) {
        Element registers = Dom.child(profile, "registers");
        if (registers == null) {
            return;
        }
        Set<String> names = new HashSet<>();
        for (Element register : Dom.children(registers, "register")) {
            String name = register.getAttribute("name");
            if (!names.add(name)) {
                report.error(profile.getAttribute("id") + ": duplicate register " + name);
            }
        }
    }
}
