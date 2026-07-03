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
        validateRegisters(profile, report);
        validateCoverage(profile, report);
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
        if (!Set.of("cellular", "hybrid").contains(profile.profileKind())) {
            return;
        }
        SimRuntime sim = profile.initialState().sim();
        NetworkRuntime network = profile.initialState().network();
        if (sim == null) {
            return;
        }
        if (sim.pinQueryEnabled() && blank(sim.pinRef()) && blank(sim.testPin())) {
            report.error(profile.id() + ": pinQueryEnabled requires pinRef or test pin");
        }
        if (!blank(sim.testPin()) && !testProfile(profile)) {
            report.error(profile.id() + ": inline test pin is only allowed for test fixture profiles");
        }
        if (!blank(sim.pinRef()) && !blank(sim.testPin())) {
            report.error(profile.id() + ": pin and pinRef cannot both be configured for runtime profiles");
        }
        if (!blank(sim.testPuk()) && !testProfile(profile)) {
            report.error(profile.id() + ": inline test puk is only allowed for test fixture profiles");
        }
        if (!blank(sim.pukRef()) && !blank(sim.testPuk())) {
            report.error(profile.id() + ": puk and pukRef cannot both be configured for runtime profiles");
        }
        if (sim.state() == SimState.SIM_PUK_REQUIRED && blank(sim.pukRef()) && blank(sim.testPuk())) {
            report.error(profile.id() + ": SIM_PUK_REQUIRED requires pukRef or test puk");
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
                && !hasCompatibilityDeviation(profile)
                && (network.lac() != null || network.ci() != null || network.act() != null)) {
            report.error(profile.id() + ": non-registered CREG state must not expose lac/ci/act");
        }
    }

    private boolean testProfile(Profile profile) {
        return Set.of("candidate", "stub", "out-of-scope").contains(profile.status());
    }

    private boolean hasCompatibilityDeviation(Profile profile) {
        return profile.deviations().stream().anyMatch(deviation -> "compatibility".equals(deviation.severity()));
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }

    private void validateRegisters(Profile profile, ValidationReport report) {
        Set<String> names = new HashSet<>();
        for (ProfileRegister register : profile.registers()) {
            if (!names.add(register.name())) {
                report.error(profile.id() + ": duplicate register " + register.name());
            }
            if (!ProfileRegisterCatalog.supports(register)) {
                report.error(profile.id() + ": unsupported register " + register.name());
            }
            if (register.min() != null && register.max() != null && register.min() > register.max()) {
                report.error(profile.id() + ": register min must not exceed max for " + register.name());
            }
            if (!ProfileRegisterCatalog.accepts(register, register.defaultValue())) {
                report.error(profile.id() + ": register default out of range for " + register.name());
            }
        }
    }

    private void validateCoverage(Profile profile, ValidationReport report) {
        ProfileCoverage coverage = profile.coverage();
        if (coverage == null) {
            return;
        }
        if (coverage.commandsTotal() != coverage.commands().size()) {
            report.error(profile.id() + ": coverage commandsTotal must equal command count");
        }
    }
}
