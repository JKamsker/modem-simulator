package com.jkamsker.modemsim.profiles;

import com.jkamsker.modemsim.validation.Dom;
import org.w3c.dom.Element;

import java.util.List;

final class ProfileXmlMetadataParser {
    ProfileXmlMetadata parse(Element profile) {
        List<ProfileCommand> commands = commands(Dom.child(profile, "commands"));
        return new ProfileXmlMetadata(
                commands,
                registers(Dom.child(profile, "registers")),
                coverage(Dom.child(profile, "coverage"), commands),
                deviations(Dom.child(profile, "deviations")));
    }

    private List<ProfileCommand> commands(Element commands) {
        if (commands == null) {
            return List.of();
        }
        return Dom.children(commands, "command").stream().map(this::command).toList();
    }

    private ProfileCommand command(Element command) {
        return new ProfileCommand(
                command.getAttribute("name"),
                command.getAttribute("status"),
                Dom.attr(command, "handler", null),
                Dom.attr(command, "reason", null));
    }

    private List<ProfileRegister> registers(Element registers) {
        if (registers == null) {
            return List.of();
        }
        return Dom.children(registers, "register").stream().map(this::register).toList();
    }

    private ProfileRegister register(Element register) {
        return new ProfileRegister(
                register.getAttribute("name"),
                Dom.intAttr(register, "default", 0),
                integer(register, "min"),
                integer(register, "max"),
                Dom.boolAttr(register, "writable", true),
                Dom.boolAttr(register, "persistent", false));
    }

    private ProfileCoverage coverage(Element coverage, List<ProfileCommand> declaredCommands) {
        if (coverage == null) {
            return null;
        }
        List<ProfileCommand> covered = Dom.children(coverage, "command").stream().map(this::command).toList();
        return new ProfileCoverage(
                coverage.getAttribute("source"),
                Dom.intAttr(coverage, "commandsTotal", 0),
                Dom.intAttr(coverage, "unknown", 0),
                covered.isEmpty() ? declaredCommands : covered);
    }

    private List<ProfileDeviation> deviations(Element deviations) {
        if (deviations == null) {
            return List.of();
        }
        return Dom.children(deviations, "deviation").stream()
                .map(deviation -> new ProfileDeviation(
                        deviation.getAttribute("id"),
                        Dom.attr(deviation, "severity", null),
                        deviation.getTextContent().trim()))
                .toList();
    }

    private Integer integer(Element element, String name) {
        String value = Dom.attr(element, name, null);
        return value == null ? null : Integer.valueOf(value);
    }
}
