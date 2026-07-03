package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemLines;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.HeadlessEndpoint;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;
import com.jkamsker.modemsim.transport.SerialEndpoint;
import com.jkamsker.modemsim.transport.SerialException;
import com.jkamsker.modemsim.transport.SerialPortLostException;
import com.jkamsker.modemsim.transport.SerialRead;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ModemRuntimeTest {
    @TempDir
    Path tempDir;

    @Test
    void headlessRuntimeProcessesInputThroughConfiguredProfileAndWritesJsonlLog() throws Exception {
        ModemRuntime runtime = new ModemRuntime(binding -> new HeadlessEndpoint());

        RuntimeResult result = runtime.run(config(false, false), List.of(RawBytes.ascii("AT\r")), -1);

        assertThat(result.readsProcessed()).isEqualTo(1);
        assertThat(result.output().toHex()).isEqualTo("0D0A4F4B0D0A");
        assertThat(Files.readString(result.eventLogPath()))
                .contains("\"eventType\":\"SESSION_START\"")
                .contains("\"eventType\":\"RX_BYTES\"")
                .contains("\"eventType\":\"SESSION_STOP\"")
                .contains("\"redaction\":{\"applied\":true");
    }

    @Test
    void optionalSidecarOpenFailureIsIgnoredUnlessStrictPolicyIsEnabled() {
        ModemRuntime runtime = new ModemRuntime(this::endpoint);

        RuntimeResult result = runtime.run(config(true, false), List.of(RawBytes.ascii("AT\r")), -1);

        assertThat(result.readsProcessed()).isEqualTo(1);
        assertThat(readLog(result.eventLogPath())).contains("optional-port-open-failed:sniffer:sidecar unavailable");
        assertThatThrownBy(() -> runtime.run(config(true, true), List.of(), 0))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Runtime failed: sidecar unavailable");
    }

    @Test
    void snifferMirrorsMainTrafficButDoesNotFeedParser() {
        Map<String, HeadlessEndpoint> endpoints = new LinkedHashMap<>();
        ModemRuntime runtime = new ModemRuntime(binding -> endpoint(endpoints, binding));

        RuntimeResult result = runtime.run(config(true, false), List.of(RawBytes.ascii("AT\r")), -1);

        assertThat(result.output().toHex()).isEqualTo("0D0A4F4B0D0A");
        assertThat(endpoints.get("sniffer").written().ascii())
                .contains("DTE_TO_DCE 41540D")
                .contains("DCE_TO_DTE 0D0A4F4B0D0A");
    }

    @Test
    void delayedDialIsNotDrainedOnSameRead() {
        HeadlessEndpoint endpoint = new HeadlessEndpoint();
        endpoint.enqueueRead(RawBytes.ascii("ATD123\r"), 0);
        ModemRuntime runtime = new ModemRuntime(binding -> endpoint);

        RuntimeResult result = runtime.run(config(false, false), List.of(), 1);

        assertThat(result.output().toHex()).isEmpty();
        assertThat(readLog(result.eventLogPath())).contains("\"eventType\":\"SCHEDULER_ENQUEUE\"");
    }

    @Test
    void delayedDialEmitsWhenLaterReadAdvancesClock() {
        HeadlessEndpoint endpoint = new HeadlessEndpoint();
        endpoint.enqueueRead(RawBytes.ascii("ATD123\r"), 0);
        endpoint.enqueueRead(RawBytes.empty(), 5_000_000_000L);
        ModemRuntime runtime = new ModemRuntime(binding -> endpoint);

        RuntimeResult result = runtime.run(config(false, false), List.of(), 2);

        assertThat(result.output().ascii()).isEqualTo("\r\nCONNECT\r\n");
        assertThat(readLog(result.eventLogPath())).contains("\"eventType\":\"SCHEDULER_EMIT\"");
    }

    @Test
    void manualDceSidecarInjectsRawDceOnlyWhenUnsafeTransmitIsAllowed() {
        Map<String, HeadlessEndpoint> endpoints = new LinkedHashMap<>();
        ModemRuntime runtime = new ModemRuntime(binding -> {
            HeadlessEndpoint endpoint = endpoint(endpoints, binding);
            if (binding.role() == PortRole.MANUAL_DCE_INJECTION) {
                endpoint.enqueueRead(RawBytes.ascii("+CREG: 4\r\n"), 0);
            }
            return endpoint;
        });

        RuntimeResult result = runtime.run(manualConfig(true), List.of(), 1);

        assertThat(result.output().ascii()).isEqualTo("+CREG: 4\r\n");
        assertThat(endpoints.get("modem").written().ascii()).isEqualTo("+CREG: 4\r\n");
        assertThat(readLog(result.eventLogPath()))
                .contains("\"eventType\":\"INJECTION\"")
                .contains("\"injectionType\":\"raw-dce-to-dte\"");
    }

    @Test
    void runtimePublishesPortLostAndDropsLinesWhenMainPortReadFails() {
        LostReadEndpoint endpoint = new LostReadEndpoint();
        ModemRuntime runtime = new ModemRuntime(binding -> endpoint);

        RuntimeResult result = runtime.run(config(false, false), List.of(), 1);

        assertThat(result.readsProcessed()).isZero();
        assertThat(endpoint.lines.dsr()).isFalse();
        assertThat(endpoint.lines.dcd()).isFalse();
        assertThat(readLog(result.eventLogPath()))
                .contains("\"eventType\":\"PORT_LOST\"")
                .contains("\"port\":\"modem\"")
                .contains("\"portRole\":\"modem-simulation\"")
                .contains("\"dsr\":false")
                .contains("\"dcd\":false");
    }

    @Test
    void resolverAcceptsEveryV1CoverageTargetId() throws Exception {
        ProfileResolver resolver = new ProfileResolver();
        try (var files = Files.list(Path.of("src/main/resources/coverage/v1-targets"))) {
            assertThat(files.filter(path -> path.getFileName().toString().endsWith(".json"))
                    .map(path -> path.getFileName().toString().replace(".json", ""))
                    .map(id -> resolver.resolve(id).id()))
                    .contains(
                            "generic-hayes-v250",
                            "3gpp-27007-r18",
                            "3gpp-27005-r16",
                            "sierra-common",
                            "sierra-hl6-hl8-v20",
                            "westermo-common",
                            "westermo-td22-6177-2203",
                            "westermo-td36-6618-2202",
                            "westermo-gd01-6196-2220",
                            "westermo-gdw11-6615-2220");
        }
    }

    @Test
    void builtInProfilesExposeSpecMetadataWithoutSelfCycles() throws Exception {
        ProfileResolver resolver = new ProfileResolver();
        assertThat(resolver.resolve("generic-hayes-v250").status()).isEqualTo("normative-base");
        assertThat(resolver.resolve("generic-hayes-v250").profileKind()).isEqualTo("base");
        assertThat(resolver.resolve("sierra-common").parents())
                .containsExactly("generic-hayes-v250", "3gpp-27007-r18", "3gpp-27005-r16");
        assertThat(resolver.resolve("westermo-common").parents()).containsExactly("generic-hayes-v250");
        assertThat(resolver.resolve("westermo-common").profileKind()).isEqualTo("base");
        assertThat(resolver.resolve("westermo-gdw11-6615-2220").profileKind()).isEqualTo("hybrid");
        try (var files = Files.list(Path.of("src/main/resources/coverage/v1-targets"))) {
            files.filter(path -> path.getFileName().toString().endsWith(".json"))
                    .map(path -> path.getFileName().toString().replace(".json", ""))
                    .map(resolver::resolve)
                    .forEach(profile -> {
                        assertThat(profile.parents()).doesNotContain(profile.id());
                        assertThat(profile.modelFamily()).isNotBlank();
                        assertThat(profile.manualVersion()).isNotBlank();
                        assertThat(profile.manualDate()).isNotBlank();
                    });
        }
    }

    private RuntimeConfig config(boolean sidecarEnabled, boolean strict) {
        return new RuntimeConfig(
                12345,
                com.jkamsker.modemsim.scheduler.ClockMode.VIRTUAL,
                strict,
                false,
                tempDir.resolve("runtime-" + sidecarEnabled + "-" + strict + ".jsonl"),
                new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE),
                List.of(
                        new PortBinding("modem", EndpointType.HEADLESS, PortRole.MODEM_SIMULATION,
                                null, true, "sierra-hl6-hl8-v20", "tagged-text"),
                        new PortBinding("sniffer", EndpointType.HEADLESS, PortRole.SNIFFER,
                                null, sidecarEnabled, null, "tagged-text")));
    }

    private RuntimeConfig manualConfig(boolean allowUnsafeDce) {
        return new RuntimeConfig(
                12345,
                com.jkamsker.modemsim.scheduler.ClockMode.VIRTUAL,
                false,
                allowUnsafeDce,
                tempDir.resolve("manual-" + allowUnsafeDce + ".jsonl"),
                new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE),
                List.of(
                        new PortBinding("modem", EndpointType.HEADLESS, PortRole.MODEM_SIMULATION,
                                null, true, "sierra-hl6-hl8-v20", "tagged-text"),
                        new PortBinding("manual", EndpointType.HEADLESS, PortRole.MANUAL_DCE_INJECTION,
                                null, true, null, "tagged-text")));
    }

    private SerialEndpoint endpoint(PortBinding binding) {
        return binding.isModemSimulation() ? new HeadlessEndpoint() : new FailingEndpoint();
    }

    private HeadlessEndpoint endpoint(Map<String, HeadlessEndpoint> endpoints, PortBinding binding) {
        HeadlessEndpoint endpoint = new HeadlessEndpoint();
        endpoints.put(binding.id(), endpoint);
        return endpoint;
    }

    private String readLog(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private static final class FailingEndpoint implements SerialEndpoint {
        @Override
        public void open(SerialConfig config) throws SerialException {
            throw new SerialException("sidecar unavailable");
        }

        @Override
        public SerialRead read() throws IOException {
            throw new IOException("not opened");
        }

        @Override
        public void write(byte[] buffer, int offset, int length) {
        }

        @Override
        public ModemLines readLines() {
            return ModemLines.ready();
        }

        @Override
        public void writeLines(ModemLines lines) {
        }

        @Override
        public void close() {
        }
    }

    private static final class LostReadEndpoint implements SerialEndpoint {
        private ModemLines lines = ModemLines.ready();

        @Override
        public void open(SerialConfig config) {
        }

        @Override
        public SerialRead read() throws IOException {
            throw new SerialPortLostException("port removed");
        }

        @Override
        public void write(byte[] buffer, int offset, int length) {
        }

        @Override
        public ModemLines readLines() {
            return lines;
        }

        @Override
        public void writeLines(ModemLines lines) {
            this.lines = lines;
        }

        @Override
        public void close() {
        }
    }

}
