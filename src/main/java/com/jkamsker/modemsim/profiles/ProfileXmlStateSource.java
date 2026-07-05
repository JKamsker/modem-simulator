package com.jkamsker.modemsim.profiles;

import com.jkamsker.modemsim.validation.Dom;
import org.w3c.dom.Element;

record ProfileXmlStateSource(
        boolean identity,
        boolean errorPolicy,
        boolean modelFamily,
        boolean manualVersion,
        boolean manualDate,
        boolean initial,
        boolean sim,
        Network network,
        boolean signal,
        boolean sms,
        boolean call,
        boolean modem,
        boolean lines
) {
    static ProfileXmlStateSource from(Element profile) {
        boolean identity = Dom.child(profile, "identity") != null;
        boolean errorPolicy = Dom.child(profile, "error-policy") != null;
        boolean modelFamily = profile.hasAttribute("modelFamily");
        boolean manualVersion = profile.hasAttribute("manualVersion");
        boolean manualDate = profile.hasAttribute("manualDate");
        Element initial = Dom.child(profile, "initial-state");
        if (initial == null) {
            return new ProfileXmlStateSource(
                    identity, errorPolicy, modelFamily, manualVersion, manualDate,
                    false, false, Network.none(), false, false, false, false, false);
        }
        Element network = Dom.child(initial, "network");
        return new ProfileXmlStateSource(
                identity,
                errorPolicy,
                modelFamily,
                manualVersion,
                manualDate,
                true,
                Dom.child(initial, "sim") != null,
                Network.from(network),
                Dom.child(initial, "signal") != null,
                Dom.child(initial, "sms") != null,
                Dom.child(initial, "call") != null,
                Dom.child(initial, "modem") != null,
                Dom.child(initial, "modem-lines") != null);
    }

    static ProfileXmlStateSource none() {
        return new ProfileXmlStateSource(false, false, false, false, false,
                false, false, Network.none(), false, false, false, false, false);
    }

    ProfileXmlStateSource merge(ProfileXmlStateSource right) {
        return new ProfileXmlStateSource(
                identity || right.identity,
                errorPolicy || right.errorPolicy,
                modelFamily || right.modelFamily,
                manualVersion || right.manualVersion,
                manualDate || right.manualDate,
                initial || right.initial,
                sim || right.sim,
                network.merge(right.network),
                signal || right.signal,
                sms || right.sms,
                call || right.call,
                modem || right.modem,
                lines || right.lines);
    }

    record Network(
            boolean present,
            boolean cregN,
            boolean stat,
            boolean lac,
            boolean ci,
            boolean act,
            boolean rejectCauseType,
            boolean rejectCause,
            boolean operator,
            boolean rateLimit,
            boolean delays
    ) {
        static Network from(Element network) {
            if (network == null) {
                return none();
            }
            return new Network(
                    true,
                    network.hasAttribute("cregN"),
                    network.hasAttribute("stat"),
                    network.hasAttribute("lac"),
                    network.hasAttribute("ci"),
                    network.hasAttribute("act"),
                    network.hasAttribute("rejectCauseType"),
                    network.hasAttribute("rejectCause"),
                    Dom.child(network, "operator") != null,
                    Dom.child(network, "sms-rate-limit") != null,
                    Dom.child(network, "delays") != null);
        }

        static Network none() {
            return new Network(false, false, false, false, false, false, false, false, false, false, false);
        }

        Network merge(Network right) {
            return new Network(
                    present || right.present,
                    cregN || right.cregN,
                    stat || right.stat,
                    lac || right.lac,
                    ci || right.ci,
                    act || right.act,
                    rejectCauseType || right.rejectCauseType,
                    rejectCause || right.rejectCause,
                    operator || right.operator,
                    rateLimit || right.rateLimit,
                    delays || right.delays);
        }
    }
}
