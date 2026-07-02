package com.jkamsker.modemsim.profiles;

import com.jkamsker.modemsim.state.NetworkDelay;
import com.jkamsker.modemsim.state.NetworkRuntime;
import com.jkamsker.modemsim.state.SimRuntime;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.validation.ValidationReport;

import java.util.HashSet;
import java.util.Set;

public final class ProfileSemanticValidator {
    public ValidationReport validate(Profile profile) {
        ValidationReport report = ValidationReport.ok();
        validateRequiredState(profile, report);
        validateSimNetwork(profile, report);
        validateNetwork(profile, report);
        return report;
    }

    private void validateRequiredState(Profile profile, ValidationReport report) {
        boolean cellular = profile.profileKind().equals("cellular") || profile.profileKind().equals("hybrid");
        if (cellular && profile.initialState().network() == null) {
            report.error(profile.id() + ": cellular profile requires network state");
        }
        if (cellular && profile.initialState().signal() == null) {
            report.error(profile.id() + ": cellular profile requires signal state");
        }
        if (cellular && profile.initialState().sim() == null) {
            report.error(profile.id() + ": cellular profile requires sim state");
        }
    }

    private void validateSimNetwork(Profile profile, ValidationReport report) {
        SimRuntime sim = profile.initialState().sim();
        NetworkRuntime network = profile.initialState().network();
        if (sim == null) {
            return;
        }
        if (sim.pinQueryEnabled() && blank(sim.pinRef()) && blank(sim.testPin())) {
            report.error(profile.id() + ": pinQueryEnabled requires pinRef or test pin");
        }
        if (!blank(sim.pinRef()) && !blank(sim.testPin())) {
            report.error(profile.id() + ": pin and pinRef cannot both be configured for runtime profiles");
        }
        if (network != null && sim.state() != SimState.READY && (network.stat() == 1 || network.stat() == 5)) {
            report.error(profile.id() + ": locked or failed SIM cannot be actively registered");
        }
    }

    private void validateNetwork(Profile profile, ValidationReport report) {
        NetworkRuntime network = profile.initialState().network();
        if (network == null) {
            return;
        }
        String expectedNumeric = network.operator().mcc() + network.operator().mnc();
        if (!network.operator().numeric().equals(expectedNumeric)) {
            report.error(profile.id() + ": operator numeric must equal MCC+MNC");
        }
        if (network.smsRateLimit().rejectCmsError() == 310) {
            report.error(profile.id() + ": CMS 310 cannot be used for SMS rate limits");
        }
        Set<String> operations = new HashSet<>();
        for (NetworkDelay delay : network.delays().values()) {
            if (delay.minMs() > delay.maxMs()) {
                report.error(profile.id() + ": delay minMs must not exceed maxMs for " + delay.operation());
            }
            if (!operations.add(delay.operation())) {
                report.error(profile.id() + ": duplicate delay operation " + delay.operation());
            }
        }
        if (!network.registeredForCircuitServices()
                && (network.lac() != null || network.ci() != null || network.act() != null)) {
            report.error(profile.id() + ": non-registered CREG state must not expose lac/ci/act");
        }
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
