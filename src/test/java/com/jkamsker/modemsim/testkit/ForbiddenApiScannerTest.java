package com.jkamsker.modemsim.testkit;

import org.junit.jupiter.api.Test;

import java.net.ServerSocket;

import static org.assertj.core.api.Assertions.assertThat;

class ForbiddenApiScannerTest {
    @Test
    void detectsCurrentProcessTcpListener() throws Exception {
        try (ServerSocket ignored = new ServerSocket(0)) {
            assertThat(new ForbiddenApiScanner().scanCurrentProcessListeners())
                    .anySatisfy(hit -> assertThat(hit).contains("process-listener"));
        }
    }
}
