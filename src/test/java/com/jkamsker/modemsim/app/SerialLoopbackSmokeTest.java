package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.JSerialCommEndpoint;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("serial-it")
class SerialLoopbackSmokeTest {
    @TempDir
    Path tempDir;

    @Test
    @Timeout(10)
    void runtimeProcessesAtCommandOverRealSerialApi() throws Exception {
        String modemPort = System.getenv("MODEMSIM_SERIAL_MODEM_PORT");
        String dtePort = System.getenv("MODEMSIM_SERIAL_DTE_PORT");
        assertThat(modemPort)
                .as("set MODEMSIM_SERIAL_MODEM_PORT to the modem side of a virtual COM/PTTY pair")
                .isNotBlank();
        assertThat(dtePort)
                .as("set MODEMSIM_SERIAL_DTE_PORT to the DTE side of a virtual COM/PTTY pair")
                .isNotBlank();

        SerialConfig line = new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE);
        RuntimeConfig config = new RuntimeConfig(
                12345, ClockMode.MONOTONIC, true, false, tempDir.resolve("serial-it.jsonl"), line,
                List.of(new PortBinding("modem", EndpointType.SERIAL, PortRole.MODEM_SIMULATION,
                        modemPort, true, "sierra-hl6-hl8-v20", "tagged-text")));

        try (var executor = Executors.newSingleThreadExecutor(); var dte = new JSerialCommEndpoint(dtePort)) {
            dte.open(line);
            var runtime = executor.submit(() -> new ModemRuntime().run(config, List.of(), 3));
            Thread.sleep(250);
            dte.write(RawBytes.ascii("AT\r").toByteArray(), 0, 3);

            RawBytes response = readUntilOk(dte);
            RuntimeResult result = runtime.get();

            assertThat(response.ascii()).contains("\r\nOK\r\n");
            assertThat(result.readsProcessed()).isEqualTo(3);
        }
    }

    @Test
    @Timeout(20)
    void runtimeEmitsDelayedDialResultOverRealSerialApi() throws Exception {
        String modemPort = System.getenv("MODEMSIM_SERIAL_MODEM_PORT");
        String dtePort = System.getenv("MODEMSIM_SERIAL_DTE_PORT");
        assertThat(modemPort).isNotBlank();
        assertThat(dtePort).isNotBlank();
        SerialConfig line = new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE);
        RuntimeConfig config = new RuntimeConfig(
                12345, ClockMode.MONOTONIC, true, false, tempDir.resolve("serial-delay-it.jsonl"), line,
                List.of(new PortBinding("modem", EndpointType.SERIAL, PortRole.MODEM_SIMULATION,
                        modemPort, true, "sierra-hl6-hl8-v20", "tagged-text")));

        try (var executor = Executors.newSingleThreadExecutor(); var dte = new JSerialCommEndpoint(dtePort)) {
            dte.open(line);
            var runtime = executor.submit(() -> new ModemRuntime().run(config, List.of(), 14));
            Thread.sleep(250);
            dte.write(RawBytes.ascii("ATD123\r").toByteArray(), 0, 7);

            RawBytes connect = readUntil(dte, "\r\nCONNECT\r\n", 8_000_000_000L);
            Thread.sleep(1_200);
            dte.write(RawBytes.ascii("+++").toByteArray(), 0, 3);
            RawBytes escape = readUntil(dte, "\r\nOK\r\n", 5_000_000_000L);
            dte.write(RawBytes.ascii("ATH\r").toByteArray(), 0, 4);
            RawBytes hangup = readUntil(dte, "\r\nOK\r\n", 5_000_000_000L);
            RuntimeResult result = runtime.get();

            assertThat(connect.ascii()).contains("\r\nCONNECT\r\n");
            assertThat(escape.ascii()).contains("\r\nOK\r\n");
            assertThat(hangup.ascii()).contains("\r\nOK\r\n");
            assertThat(result.readsProcessed()).isEqualTo(14);
        }
    }

    private RawBytes readUntilOk(JSerialCommEndpoint endpoint) throws Exception {
        return readUntil(endpoint, "\r\nOK\r\n", 5_000_000_000L);
    }

    private RawBytes readUntil(JSerialCommEndpoint endpoint, String expected, long timeoutNanos) throws Exception {
        RawBytes response = RawBytes.empty();
        long deadline = System.nanoTime() + timeoutNanos;
        while (System.nanoTime() < deadline && !response.ascii().contains(expected)) {
            response = response.append(endpoint.read().bytes());
        }
        return response;
    }
}
