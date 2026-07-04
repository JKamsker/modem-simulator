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
    private final Map<String, Element> profiles = new LinkedHashMap<>();
    private final Map<String, ResolvedProfile> resolved = new LinkedHashMap<>();

    ProfileXmlInheritanceResolver(ProfileXmlLoader loader, Element root) {
        this.loader = loader;
        profiles.putAll(BuiltinProfiles.profileElements());
        for (Element profile : Dom.children(root, "profile")) {
            profiles.put(profile.getAttribute("id"), profile);
        }
    }

    Profile resolve(Element profile) {
        return resolveProfile(profile.getAttribute("id"), new HashSet<>()).profile();
    }

    private ResolvedProfile resolveProfile(String id, Set<String> stack) {
        ResolvedProfile existing = resolved.get(id);
        if (existing != null) {
            return existing;
        }
        if (!stack.add(id)) {
            throw new IllegalArgumentException(id + ": cyclic parent profile");
        }
        Element local = profiles.get(id);
        if (local == null) {
            throw new IllegalArgumentException("Unknown parent profile: " + id);
        }
        ResolvedProfile profile = resolveLocal(local, stack);
        stack.remove(id);
        resolved.put(id, profile);
        return profile;
    }

    private ResolvedProfile resolveLocal(Element element, Set<String> stack) {
        Profile child = loader.parseProfile(element);
        ProfileXmlStateSource childSource = ProfileXmlStateSource.from(element);
        ResolvedProfile inherited = null;
        for (String parent : child.parents()) {
            ResolvedProfile parentProfile = resolveProfile(parent, stack);
            inherited = mergeParent(inherited, parentProfile);
        }
        return inherited == null ? new ResolvedProfile(child, childSource)
                : overlay(inherited, child, element, childSource);
    }

    private ResolvedProfile mergeParent(ResolvedProfile left, ResolvedProfile right) {
        if (left == null) {
            return right;
        }
        Profile l = left.profile();
        Profile r = right.profile();
        List<ProfileCommand> commands = mergeBy(l.commands(), r.commands(), ProfileCommand::name);
        List<ProfileRegister> registers = mergeBy(l.registers(), r.registers(), ProfileRegister::name);
        ModemState state = loader.applyRegistersAndDialect(
                mergeState(l.initialState(), r.initialState(), r.dialect(), right.source()), r.dialect(), registers);
        return new ResolvedProfile(new Profile(
                r.id(), r.parents(), r.vendor(), r.status(), r.profileKind(),
                right.source().modelFamily() ? r.modelFamily() : l.modelFamily(),
                right.source().manualVersion() ? r.manualVersion() : l.manualVersion(),
                right.source().manualDate() ? r.manualDate() : l.manualDate(),
                r.dialect(),
                right.source().identity() ? r.identity() : l.identity(),
                right.source().errorPolicy() ? r.errorPolicy() : l.errorPolicy(),
                state,
                commands, registers, mergeCoverage(l.coverage(), r.coverage()),
                mergeBy(l.deviations(), r.deviations(), ProfileDeviation::id)),
                left.source().merge(right.source()));
    }

    private ResolvedProfile overlay(
            ResolvedProfile parentProfile, Profile child, Element childElement, ProfileXmlStateSource childSource) {
        Profile parent = parentProfile.profile();
        Dialect dialect = Dom.child(childElement, "dialect") == null ? parent.dialect() : child.dialect();
        Identity identity = Dom.child(childElement, "identity") == null ? parent.identity() : child.identity();
        ErrorPolicy errorPolicy = Dom.child(childElement, "error-policy") == null
                ? parent.errorPolicy() : child.errorPolicy();
        List<ProfileCommand> commands = metadata(parent.commands(), child.commands(), childElement,
                "commands", ProfileCommand::name);
        List<ProfileRegister> registers = metadata(parent.registers(), child.registers(), childElement,
                "registers", ProfileRegister::name);
        ModemState state = loader.applyRegistersAndDialect(
                mergeState(parent.initialState(), child.initialState(), dialect, childSource), dialect, registers);
        return new ResolvedProfile(new Profile(
                child.id(), child.parents(), child.vendor(), child.status(), child.profileKind(),
                value(child.modelFamily(), parent.modelFamily()),
                value(child.manualVersion(), parent.manualVersion()),
                value(child.manualDate(), parent.manualDate()),
                dialect, identity, errorPolicy, state,
                commands,
                registers,
                Dom.child(childElement, "coverage") == null ? parent.coverage()
                        : mergeCoverage(parent.coverage(), child.coverage()),
                metadata(parent.deviations(), child.deviations(), childElement, "deviations", ProfileDeviation::id)),
                parentProfile.source().merge(childSource));
    }

    private <T> List<T> metadata(
            List<T> parent, List<T> child, Element childElement, String elementName, Function<T, String> key) {
        return Dom.child(childElement, elementName) == null ? parent : mergeBy(parent, child, key);
    }

    private String value(String child, String parent) {
        return child == null || child.isBlank() ? parent : child;
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

    private ModemState mergeState(
            ModemState parent, ModemState child, Dialect dialect, ProfileXmlStateSource source) {
        if (!source.initial()) {
            return loader.applyDialect(parent, dialect);
        }
        return loader.applyDialect(new ModemState(
                source.sim() ? child.sim() : parent.sim(),
                mergeNetwork(parent.network(), child.network(), source.network()),
                source.signal() ? child.signal() : parent.signal(),
                source.sms() ? child.sms() : parent.sms(),
                source.call() ? child.call() : parent.call(),
                source.modem() ? child.modem() : parent.modem(),
                source.lines() ? child.lines() : parent.lines(),
                parent.settings(),
                0), dialect);
    }

    private NetworkRuntime mergeNetwork(
            NetworkRuntime parent, NetworkRuntime child, ProfileXmlStateSource.Network source) {
        if (!source.present() || child == null || parent == null) {
            return source.present() && child != null ? child : parent;
        }
        return new NetworkRuntime(
                source.cregN() ? child.cregN() : parent.cregN(),
                source.stat() ? child.stat() : parent.stat(),
                source.lac() ? child.lac() : parent.lac(),
                source.ci() ? child.ci() : parent.ci(),
                source.act() ? child.act() : parent.act(),
                source.rejectCauseType() ? child.rejectCauseType() : parent.rejectCauseType(),
                source.rejectCause() ? child.rejectCause() : parent.rejectCause(),
                source.operator() ? child.operator() : parent.operator(),
                source.rateLimit() ? child.smsRateLimit() : parent.smsRateLimit(),
                source.delays() ? child.delays() : parent.delays());
    }

    private record ResolvedProfile(Profile profile, ProfileXmlStateSource source) {
    }
}
