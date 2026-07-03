package com.jkamsker.modemsim.profiles;

import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.NetworkRuntime;
import com.jkamsker.modemsim.validation.Dom;
import org.w3c.dom.Element;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

final class ProfileXmlInheritanceResolver {
    private final ProfileXmlLoader loader;
    private final Map<String, Element> localProfiles = new LinkedHashMap<>();
    private final Map<String, Profile> resolved = new LinkedHashMap<>();

    ProfileXmlInheritanceResolver(ProfileXmlLoader loader, Element root) {
        this.loader = loader;
        for (Element profile : Dom.children(root, "profile")) {
            localProfiles.put(profile.getAttribute("id"), profile);
        }
    }

    Profile resolve(Element profile) {
        return resolve(profile.getAttribute("id"), new HashSet<>());
    }

    private Profile resolve(String id, Set<String> stack) {
        Profile existing = resolved.get(id);
        if (existing != null) {
            return existing;
        }
        if (!stack.add(id)) {
            throw new IllegalArgumentException(id + ": cyclic parent profile");
        }
        Element local = localProfiles.get(id);
        Profile profile = local == null ? BuiltinProfiles.byId(id) : resolveLocal(local, stack);
        stack.remove(id);
        resolved.put(id, profile);
        return profile;
    }

    private Profile resolveLocal(Element element, Set<String> stack) {
        Profile child = loader.parseProfile(element);
        Profile inherited = null;
        for (String parent : child.parents()) {
            Profile parentProfile = resolve(parent, stack);
            Element parentElement = localProfiles.get(parent);
            inherited = inherited == null ? parentProfile
                    : parentElement == null ? mergeParent(inherited, parentProfile)
                    : overlay(inherited, parentProfile, parentElement);
        }
        return inherited == null ? child : overlay(inherited, child, element);
    }

    private Profile mergeParent(Profile left, Profile right) {
        return left == null ? right : new Profile(
                right.id(), right.parents(), right.vendor(), right.status(), right.profileKind(),
                right.dialect(), right.identity(), right.initialState(),
                mergeBy(left.commands(), right.commands(), ProfileCommand::name),
                mergeBy(left.registers(), right.registers(), ProfileRegister::name),
                mergeCoverage(left.coverage(), right.coverage()),
                mergeBy(left.deviations(), right.deviations(), ProfileDeviation::id));
    }

    private Profile overlay(Profile parent, Profile child, Element childElement) {
        Dialect dialect = Dom.child(childElement, "dialect") == null ? parent.dialect() : child.dialect();
        Identity identity = Dom.child(childElement, "identity") == null ? parent.identity() : child.identity();
        ModemState state = mergeState(parent.initialState(), child.initialState(), dialect, childElement);
        return new Profile(
                child.id(), child.parents(), child.vendor(), child.status(), child.profileKind(),
                dialect, identity, state,
                metadata(parent.commands(), child.commands(), childElement, "commands", ProfileCommand::name),
                metadata(parent.registers(), child.registers(), childElement, "registers", ProfileRegister::name),
                Dom.child(childElement, "coverage") == null ? parent.coverage()
                        : mergeCoverage(parent.coverage(), child.coverage()),
                metadata(parent.deviations(), child.deviations(), childElement, "deviations", ProfileDeviation::id));
    }

    private <T> List<T> metadata(
            List<T> parent, List<T> child, Element childElement, String elementName, Function<T, String> key) {
        return Dom.child(childElement, elementName) == null ? parent : mergeBy(parent, child, key);
    }

    private <T> List<T> mergeBy(List<T> parent, List<T> child, Function<T, String> key) {
        Map<String, T> merged = new LinkedHashMap<>();
        parent.forEach(item -> merged.put(key.apply(item), item));
        child.forEach(item -> merged.put(key.apply(item), item));
        return List.copyOf(merged.values());
    }

    private ProfileCoverage mergeCoverage(ProfileCoverage parent, ProfileCoverage child) {
        if (child == null) {
            return parent;
        }
        if (parent == null) {
            return child;
        }
        return new ProfileCoverage(child.source(), child.commandsTotal(), child.unknown(),
                mergeBy(parent.commands(), child.commands(), ProfileCommand::name));
    }

    private ModemState mergeState(ModemState parent, ModemState child, Dialect dialect, Element childElement) {
        Element initial = Dom.child(childElement, "initial-state");
        if (initial == null) {
            return loader.applyDialect(parent, dialect);
        }
        return loader.applyDialect(new ModemState(
                Dom.child(initial, "sim") == null ? parent.sim() : child.sim(),
                mergeNetwork(parent.network(), child.network(), Dom.child(initial, "network")),
                Dom.child(initial, "signal") == null ? parent.signal() : child.signal(),
                Dom.child(initial, "sms") == null ? parent.sms() : child.sms(),
                Dom.child(initial, "call") == null ? parent.call() : child.call(),
                Dom.child(initial, "modem") == null ? parent.modem() : child.modem(),
                Dom.child(initial, "modem-lines") == null ? parent.lines() : child.lines(),
                parent.settings(),
                0), dialect);
    }

    private NetworkRuntime mergeNetwork(NetworkRuntime parent, NetworkRuntime child, Element element) {
        if (element == null || parent == null) {
            return element == null ? parent : child;
        }
        return new NetworkRuntime(
                element.hasAttribute("cregN") ? child.cregN() : parent.cregN(),
                element.hasAttribute("stat") ? child.stat() : parent.stat(),
                Dom.attr(element, "lac", parent.lac()),
                Dom.attr(element, "ci", parent.ci()),
                element.hasAttribute("act") ? child.act() : parent.act(),
                element.hasAttribute("rejectCauseType") ? child.rejectCauseType() : parent.rejectCauseType(),
                element.hasAttribute("rejectCause") ? child.rejectCause() : parent.rejectCause(),
                Dom.child(element, "operator") == null ? parent.operator() : child.operator(),
                Dom.child(element, "sms-rate-limit") == null ? parent.smsRateLimit() : child.smsRateLimit(),
                Dom.child(element, "delays") == null ? parent.delays() : child.delays());
    }
}
