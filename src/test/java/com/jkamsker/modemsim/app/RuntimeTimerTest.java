package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.HeadlessEndpoint;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RuntimeTimerTest {
    @TempDir
    Path tempDir;

    @Test
    void runtimeFiresConfiguredOnTimerMacroWhenClockAdvances() throws Exception {
        Path macros = tempDir.resolve("timer.xml");
        Files.writeString(macros, """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="timer-urc" phase="on-timer">
                    <match type="timer" timerId="heartbeat"/>
                    <then><emit line="+TIMER: 1"/></then>
                  </macro>
                </macros>
                """);
        HeadlessEndpoint endpoint = new HeadlessEndpoint();
        endpoint.enqueueRead(RawBytes.empty(), 1_000_000L);
        RuntimeConfig config = new RuntimeConfig(
                12345, ClockMode.VIRTUAL, false, false, tempDir.resolve("timer.jsonl"),
                null, macros, List.of(new RuntimeTimer("heartbeat", 1)),
                new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE),
                List.of(new PortBinding("modem", EndpointType.HEADLESS, PortRole.MODEM_SIMULATION,
                        null, true, "sierra-hl6-hl8-v20", "tagged-text")));

        RuntimeResult result = new ModemRuntime(binding -> endpoint).run(config, List.of(), 1);

        assertThat(result.output().ascii()).contains("+TIMER: 1");
        assertThat(Files.readString(result.eventLogPath()))
                .contains("\"eventType\":\"MACRO_DECISION\"")
                .contains("\"macroId\":\"timer-urc\"");
    }

    @Test
    void runtimeFiresConfiguredTimerWithoutDteReads() throws Exception {
        Path macros = tempDir.resolve("no-read-timer.xml");
        Files.writeString(macros, """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="timer-urc" phase="on-timer">
                    <match type="timer" timerId="heartbeat"/>
                    <then><emit line="+TIMER: NO_READ"/></then>
                  </macro>
                </macros>
                """);
        RuntimeConfig config = new RuntimeConfig(
                12345, ClockMode.VIRTUAL, false, false, tempDir.resolve("no-read-timer.jsonl"),
                null, macros, List.of(new RuntimeTimer("heartbeat", 1)),
                new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE),
                List.of(new PortBinding("modem", EndpointType.HEADLESS, PortRole.MODEM_SIMULATION,
                        null, true, "sierra-hl6-hl8-v20", "tagged-text")));

        RuntimeResult result = new ModemRuntime(binding -> new HeadlessEndpoint()).run(config, List.of(), -1);

        assertThat(result.output().ascii()).contains("+TIMER: NO_READ");
        assertThat(Files.readString(result.eventLogPath())).contains("\"macroId\":\"timer-urc\"");
    }
}
