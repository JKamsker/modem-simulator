package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.HeadlessEndpoint;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;
import com.jkamsker.modemsim.validation.ScenarioValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InitialScenarioRuntimeTest {
    @TempDir
    Path tempDir;

    @Test
    void runtimeAppliesInitialScenarioBeforeProcessingInput() {
        RuntimeConfig config = new RuntimeConfig(
                12345,
                ClockMode.VIRTUAL,
                false,
                false,
                tempDir.resolve("scenario-runtime.jsonl"),
                Path.of("docs/Tasks/Initial-Spec/examples/scenario.no-network.xml"),
                new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE),
                List.of(new PortBinding("modem", EndpointType.HEADLESS, PortRole.MODEM_SIMULATION,
                        null, true, "sierra-hl6-hl8-v20", "tagged-text")));
        ModemRuntime runtime = new ModemRuntime(binding -> new HeadlessEndpoint());

        RuntimeResult result = runtime.run(config, List.of(
                RawBytes.ascii("AT+CREG?\r"),
                RawBytes.ascii("AT+CSQ\r")), -1);

        assertThat(result.output().ascii())
                .contains("+CREG: 2,4")
                .contains("+CSQ: 99,99");
    }

    @Test
    void modemPortInitialScenarioOverridesRootAndAppliesStepActions() throws Exception {
        Path scenario = tempDir.resolve("port-scenario.xml");
        Files.writeString(scenario, """
                <?xml version="1.0" encoding="UTF-8"?>
                <scenario id="port-step" version="1.0" clock="virtual">
                  <initial-state><network cregN="2" stat="1"/><signal rssi="18" ber="0"/></initial-state>
                  <step atMs="0"><set path="state.network.stat" value="4"/></step>
                </scenario>
                """);
        RuntimeConfig config = new RuntimeConfig(
                12345, ClockMode.VIRTUAL, false, false, tempDir.resolve("port-scenario.jsonl"),
                null, new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE),
                List.of(new PortBinding("modem", EndpointType.HEADLESS, PortRole.MODEM_SIMULATION,
                        null, true, "sierra-hl6-hl8-v20", scenario, "tagged-text")));

        RuntimeResult result = new ModemRuntime(binding -> new HeadlessEndpoint()).run(config,
                List.of(RawBytes.ascii("AT+CREG?\r")), -1);

        assertThat(result.output().ascii()).contains("+CREG: 2,4");
    }

    @Test
    void delayedScenarioStepsAreNotCollapsedIntoStartupState() throws Exception {
        Path scenario = tempDir.resolve("delayed-scenario.xml");
        Files.writeString(scenario, """
                <?xml version="1.0" encoding="UTF-8"?>
                <scenario id="delayed" version="1.0" clock="virtual">
                  <initial-state><network cregN="2" stat="1"/><signal rssi="18" ber="0"/></initial-state>
                  <step atMs="100000"><set path="state.network.stat" value="4"/></step>
                </scenario>
                """);

        var state = new InitialScenarioLoader().apply(
                scenario, com.jkamsker.modemsim.profiles.BuiltinProfiles.acceptanceSierra().initialState());

        assertThat(state.network().stat()).isEqualTo(1);
    }

    @Test
    void delayedScenarioStepsExecutePatchFaultAndAdvanceActions() throws Exception {
        Path scenario = tempDir.resolve("delayed-actions.xml");
        Files.writeString(scenario, """
                <?xml version="1.0" encoding="UTF-8"?>
                <scenario id="delayed-actions" version="1.0" clock="virtual">
                  <step atMs="250">
                    <set path="state.network.stat" value="4"/>
                    <fault type="network-restore" stat="1" rssi="20" ber="1"/>
                    <advance ms="500"/>
                  </step>
                </scenario>
                """);
        InitialScenarioStep step = new InitialScenarioLoader().delayedSteps(scenario).getFirst();
        HeadlessSession session = new HeadlessSession("delayed-actions", BuiltinProfiles.acceptanceSierra(), 12345);

        var response = step.apply(session);

        assertThat(response.events()).extracting(event -> event.eventType().name())
                .contains("STATE_CHANGE", "FAULT_TRIGGERED");
        assertThat(session.snapshot().network().stat()).isEqualTo(1);
        assertThat(session.snapshot().signal().rssi()).isEqualTo(20);
        assertThat(session.snapshot().signal().ber()).isEqualTo(1);
    }

    @Test
    void scenarioRejectsLockedSimWithActiveRegistration() throws Exception {
        Path scenario = tempDir.resolve("locked-registered.xml");
        Files.writeString(scenario, """
                <?xml version="1.0" encoding="UTF-8"?>
                <scenario id="locked-registered" version="1.0" clock="virtual">
                  <initial-state><sim state="SIM_PIN_REQUIRED"/><network cregN="2" stat="1"/></initial-state>
                </scenario>
                """);

        assertThat(new ScenarioValidator().validate(scenario).errors())
                .anySatisfy(error -> assertThat(error).contains("locked SIM"));
    }

    @Test
    void scenarioInitialStateCanSetAllModemLines() throws Exception {
        Path scenario = tempDir.resolve("all-lines.xml");
        Files.writeString(scenario, """
                <?xml version="1.0" encoding="UTF-8"?>
                <scenario id="all-lines" version="1.0" clock="virtual">
                  <initial-state>
                    <modem-lines dtr="false" dsr="true" dcd="true" ri="true" rts="false" cts="false"/>
                  </initial-state>
                </scenario>
                """);

        var state = new InitialScenarioLoader().apply(scenario, BuiltinProfiles.acceptanceSierra().initialState());

        assertThat(state.lines().dtr()).isFalse();
        assertThat(state.lines().dsr()).isTrue();
        assertThat(state.lines().dcd()).isTrue();
        assertThat(state.lines().ri()).isTrue();
        assertThat(state.lines().rts()).isFalse();
        assertThat(state.lines().cts()).isFalse();
    }
}
