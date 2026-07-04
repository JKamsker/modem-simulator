package com.jkamsker.modemsim.testkit;

import org.junit.jupiter.api.Test;

import java.net.ServerSocket;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class ForbiddenApiScannerTest {
    @Test
    void detectsCurrentProcessTcpListener() throws Exception {
        try (ServerSocket ignored = new ServerSocket(0)) {
            List<String> hits = new ForbiddenApiScanner().scanCurrentProcessListeners();
            assumeFalse(SourceSizeGateAcceptance.windows() && hits.isEmpty(),
                    "Windows netstat did not report the listener");
            assertThat(hits).anySatisfy(hit -> assertThat(hit).contains("process-listener"));
        }
    }
}
