package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.testkit.ForbiddenApiScanner;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class RuntimeNoControlApiAcceptance {
    public List<String> listenerHitsDuringHeadlessSmoke() {
        List<String> hits = new CopyOnWriteArrayList<>();
        try {
            java.nio.file.Path eventLog = Files.createTempFile("modemsim-no-control-api", ".jsonl");
            eventLog.toFile().deleteOnExit();
            RuntimeConfig config = new RuntimeConfig(
                    12345, ClockMode.VIRTUAL, false, false,
                    eventLog,
                    new SerialConfig(115200, 8, 1, Parity.NONE, FlowControl.NONE),
                    List.of(new PortBinding("modem", EndpointType.HEADLESS, PortRole.MODEM_SIMULATION,
                            null, true, "sierra-hl6-hl8-v20", "tagged-text")));
            new ModemRuntime().run(config, List.of(RawBytes.ascii("AT\r")), 1, null,
                    session -> hits.addAll(new ForbiddenApiScanner().scanCurrentProcessListeners()));
            return List.copyOf(hits);
        } catch (IOException e) {
            throw new IllegalStateException("runtime control API probe failed", e);
        }
    }
}
