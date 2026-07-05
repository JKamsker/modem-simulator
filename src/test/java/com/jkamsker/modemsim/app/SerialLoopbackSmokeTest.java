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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("serial-it")
class SerialLoopbackSmokeTest {
    @TempDir
    Path tempDir;

    @Test
    @Timeout(10)
    @DisplayName("A01 serial-it: real serial AT returns exact OK frame")
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

            assertThat(response.toHex()).isEqualTo("0D0A4F4B0D0A");
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
                        modemPort, true, fixedDelayProfile(), "tagged-text")));

        try (var executor = Executors.newSingleThreadExecutor(); var dte = new JSerialCommEndpoint(dtePort)) {
            dte.open(line);
            var runtime = executor.submit(() -> new ModemRuntime().run(config, List.of(), 14));
            Thread.sleep(250);
            dte.write(RawBytes.ascii("ATD123\r").toByteArray(), 0, 7);
            long dialStartNanos = System.nanoTime();

            TimedRead connect = readUntilTimed(dte, "\r\nCONNECT\r\n", 8_000_000_000L);
            Thread.sleep(1_200);
            dte.write(RawBytes.ascii("+++").toByteArray(), 0, 3);
            RawBytes escape = readUntil(dte, "\r\nOK\r\n", 5_000_000_000L);
            dte.write(RawBytes.ascii("ATH\r").toByteArray(), 0, 4);
            RawBytes hangup = readUntil(dte, "\r\nOK\r\n", 5_000_000_000L);
            RuntimeResult result = runtime.get();
            int sampledDialDelayMs = sampledDelayMs(result.eventLogPath(), "dial");

            assertThat(connect.bytes().ascii()).contains("\r\nCONNECT\r\n");
            assertThat(escape.ascii()).contains("\r\nOK\r\n");
            assertThat(hangup.ascii()).contains("\r\nOK\r\n");
            assertWithinSerialTolerance(elapsedMs(dialStartNanos, connect.finishedAtNanos()), sampledDialDelayMs);
            assertWithinSerialTolerance(dialEventElapsedMs(result.eventLogPath()), sampledDialDelayMs);
            assertDcdTransitions(result.eventLogPath());
            assertThat(result.readsProcessed()).isEqualTo(14);
        }
    }

    @Test
    @Timeout(20)
    void runtimeEmitsDelayedSmsSubmitResultOverRealSerialApi() throws Exception {
        String modemPort = System.getenv("MODEMSIM_SERIAL_MODEM_PORT");
        String dtePort = System.getenv("MODEMSIM_SERIAL_DTE_PORT");
        assertThat(modemPort).isNotBlank();
        assertThat(dtePort).isNotBlank();
        SerialConfig line = new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE);
        RuntimeConfig config = new RuntimeConfig(
                12345, ClockMode.MONOTONIC, true, false, tempDir.resolve("serial-sms-delay-it.jsonl"), line,
                List.of(new PortBinding("modem", EndpointType.SERIAL, PortRole.MODEM_SIMULATION,
                        modemPort, true, fixedDelayProfile(), "tagged-text")));
        RawBytes cmgs = RawBytes.ascii("AT+CMGS=\"+491701234567\"\r");
        RawBytes body = RawBytes.ascii("payload\u001A");
        RawBytes at = RawBytes.ascii("AT\r");

        try (var executor = Executors.newSingleThreadExecutor(); var dte = new JSerialCommEndpoint(dtePort)) {
            dte.open(line);
            var runtime = executor.submit(() ->
                    new ModemRuntime().run(config, List.of(), cmgs.length() + body.length() + at.length()));
            Thread.sleep(250);
            dte.write(cmgs.toByteArray(), 0, cmgs.length());
            RawBytes prompt = readUntil(dte, "\r\n> ", 5_000_000_000L);
            dte.write(body.toByteArray(), 0, body.length());
            long submitStartNanos = System.nanoTime();

            TimedRead submit = readUntilTimed(dte, "\r\nOK\r\n", 8_000_000_000L);
            dte.write(at.toByteArray(), 0, at.length());
            RawBytes finalOk = readUntilOk(dte);
            RuntimeResult result = runtime.get();
            int sampledSmsDelayMs = sampledDelayMs(result.eventLogPath(), "sms-submit");

            assertThat(prompt.ascii()).contains("\r\n> ");
            assertThat(submit.bytes().ascii()).contains("+CMGS:");
            assertThat(finalOk.ascii()).contains("\r\nOK\r\n");
            assertWithinSerialTolerance(elapsedMs(submitStartNanos, submit.finishedAtNanos()), sampledSmsDelayMs);
            assertThat(result.readsProcessed()).isEqualTo(cmgs.length() + body.length() + at.length());
        }
    }

    private void assertWithinSerialTolerance(long actualMs, int expectedMs) {
        long toleranceMs = Math.max(20, expectedMs / 10);
        assertThat(actualMs).isBetween(expectedMs - toleranceMs, expectedMs + toleranceMs);
    }

    private int sampledDelayMs(Path eventLog, String operation) throws Exception {
        var pattern = Pattern.compile("\"operation\":\"" + operation + "\".*?\"sampledDelayMs\":(\\d+)",
                Pattern.DOTALL);
        var matcher = pattern.matcher(Files.readString(eventLog));
        assertThat(matcher.find()).as("missing scheduler delay for " + operation).isTrue();
        return Integer.parseInt(matcher.group(1));
    }

    private long dialEventElapsedMs(Path eventLog) throws Exception {
        String log = Files.readString(eventLog);
        long parsed = monotonicNanos(log, "PARSED_COMMAND ATD",
                "\"eventType\":\"PARSED_COMMAND\".*?\"name\":\"ATD\"");
        long connect = monotonicNanos(log, "TX_BYTES CONNECT",
                "\"eventType\":\"TX_BYTES\".*?\"rawHex\":\"0D0A434F4E4E4543540D0A\"");
        return (connect - parsed) / 1_000_000L;
    }

    private long monotonicNanos(String log, String description, String eventPattern) {
        var pattern = Pattern.compile("\"monotonicNanos\":(\\d+).*?" + eventPattern);
        var matcher = pattern.matcher(log);
        assertThat(matcher.find()).as("missing " + description + " event").isTrue();
        return Long.parseLong(matcher.group(1));
    }

    private void assertDcdTransitions(Path eventLog) throws Exception {
        String log = Files.readString(eventLog);
        assertThat(log).contains("\"dcd\":true");
        assertThat(log).contains("\"dcd\":false");
    }

    private long elapsedMs(long startNanos, long endNanos) {
        return (endNanos - startNanos) / 1_000_000L;
    }

    private String fixedDelayProfile() throws Exception {
        Path path = tempDir.resolve("serial-delay-profile.xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <modem-simulator version="1.0">
                  <profile id="serial-delay-profile" vendor="test" status="test-fixture" profileKind="cellular"
                           extends="sierra-hl6-hl8-v20">
                    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
                    <initial-state>
                      <network cregN="2" stat="1">
                        <delays>
                          <delay operation="dial" minMs="3000" maxMs="3000"/>
                          <delay operation="sms-submit" minMs="3000" maxMs="3000"/>
                        </delays>
                      </network>
                    </initial-state>
                  </profile>
                </modem-simulator>
                """);
        return path.toString();
    }

    private RawBytes readUntilOk(JSerialCommEndpoint endpoint) throws Exception {
        return readUntil(endpoint, "\r\nOK\r\n", 5_000_000_000L);
    }

    private RawBytes readUntil(JSerialCommEndpoint endpoint, String expected, long timeoutNanos) throws Exception {
        return readUntilTimed(endpoint, expected, timeoutNanos).bytes();
    }

    private TimedRead readUntilTimed(JSerialCommEndpoint endpoint, String expected, long timeoutNanos) throws Exception {
        RawBytes response = RawBytes.empty();
        long deadline = System.nanoTime() + timeoutNanos;
        while (System.nanoTime() < deadline && !response.ascii().contains(expected)) {
            response = response.append(endpoint.read().bytes());
        }
        return new TimedRead(response, System.nanoTime());
    }

    private record TimedRead(RawBytes bytes, long finishedAtNanos) { }
}
