package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.HeadlessEndpoint;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class RuntimePortGroupAcceptance {
    public void run() {
        try {
            requireSnifferMirrorsWithoutDrivingParser();
            requireManualDceAuditedInjection();
        } catch (IOException e) {
            throw new IllegalStateException("port-group acceptance failed", e);
        }
    }

    private void requireSnifferMirrorsWithoutDrivingParser() throws IOException {
        Map<String, HeadlessEndpoint> endpoints = new LinkedHashMap<>();
        ModemRuntime runtime = new ModemRuntime(binding -> endpoint(endpoints, binding));
        endpoints.computeIfAbsent("sniffer", key -> new HeadlessEndpoint())
                .enqueueRead(RawBytes.ascii("AT+CSQ\r"), 0);
        java.nio.file.Path eventLog = Files.createTempFile("modemsim-sniffer", ".jsonl");

        try {
            RuntimeResult result = runtime.run(config(eventLog, true, false), List.of(RawBytes.ascii("AT\r")), 1);
            require(result.output().toHex().equals("0D0A4F4B0D0A"), "sniffer changed main-port output");
            String mirrored = endpoints.get("sniffer").written().ascii();
            require(mirrored.contains("DTE_TO_DCE 41540D"), "sniffer did not mirror DTE bytes");
            require(mirrored.contains("DCE_TO_DTE 0D0A4F4B0D0A"), "sniffer did not mirror DCE bytes");
            require(Files.readString(result.eventLogPath()).contains("sniffer-input-ignored:sniffer"),
                    "sniffer input was not audited as ignored");
        } finally {
            Files.deleteIfExists(eventLog);
        }
    }

    private void requireManualDceAuditedInjection() throws IOException {
        Map<String, HeadlessEndpoint> endpoints = new LinkedHashMap<>();
        ModemRuntime runtime = new ModemRuntime(binding -> {
            HeadlessEndpoint endpoint = endpoint(endpoints, binding);
            if (binding.role() == PortRole.MANUAL_DCE_INJECTION) {
                endpoint.enqueueRead(RawBytes.ascii("+CREG: 4\r\n"), 0);
            }
            return endpoint;
        });
        java.nio.file.Path eventLog = Files.createTempFile("modemsim-manual-dce", ".jsonl");

        try {
            RuntimeResult result = runtime.run(config(eventLog, false, true), List.of(), 1);
            require(endpoints.get("modem").written().ascii().equals("+CREG: 4\r\n"),
                    "manual DCE bytes were not written to the modem endpoint");
            String log = Files.readString(result.eventLogPath());
            require(log.contains("\"eventType\":\"INJECTION\""), "manual DCE injection was not audited");
            require(log.contains("\"injectionType\":\"raw-dce-to-dte\""), "manual DCE injection type missing");
        } finally {
            Files.deleteIfExists(eventLog);
        }
    }

    private RuntimeConfig config(java.nio.file.Path eventLog, boolean sniffer, boolean manualDce) {
        return new RuntimeConfig(12345, ClockMode.VIRTUAL, false, manualDce, eventLog,
                new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE),
                List.of(
                        new PortBinding("modem", EndpointType.HEADLESS, PortRole.MODEM_SIMULATION,
                                null, true, "sierra-hl6-hl8-v20", "tagged-text"),
                        new PortBinding("sniffer", EndpointType.HEADLESS, PortRole.SNIFFER,
                                null, sniffer, null, "tagged-text"),
                        new PortBinding("manual", EndpointType.HEADLESS, PortRole.MANUAL_DCE_INJECTION,
                                null, manualDce, null, "tagged-text")));
    }

    private HeadlessEndpoint endpoint(Map<String, HeadlessEndpoint> endpoints, PortBinding binding) {
        return endpoints.computeIfAbsent(binding.id(), key -> new HeadlessEndpoint());
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
