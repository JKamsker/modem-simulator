package com.alegs3.modemsim.app;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class ModemSimCliTest {
    @Test
    void validationCommandsReturnSuccessForSpecExamples() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ModemSimCli.ModemSimCliRunner runner = new ModemSimCli.ModemSimCliRunner(
                new PrintStream(out), new PrintStream(err));

        assertThat(runner.run(new String[] {
                "validate-profile", "docs/Tasks/Initial-Spec/examples/modem-profile.sample.xml"})).isZero();
        assertThat(runner.run(new String[] {
                "validate-macros", "docs/Tasks/Initial-Spec/examples/macros.sms-error-123.xml"})).isZero();
        assertThat(runner.run(new String[] {
                "validate-scenario", "docs/Tasks/Initial-Spec/examples/scenario.no-network.xml"})).isZero();
        assertThat(runner.run(new String[] {"coverage", "verify", "--profiles", "v1-targets"})).isZero();
        assertThat(runner.run(new String[] {"test", "--suite", "acceptance", "--case", "A01"})).isZero();
        assertThat(err.toString()).isEmpty();
    }
}
