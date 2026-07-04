package com.jkamsker.modemsim.transport;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JSerialCommEndpointTest {
    @Test
    void permissionDeniedOpenFailuresAreReportedAsPortBusy() {
        assertThat(JSerialCommEndpoint.diagnosticForOpenFailure(true, 5)).isEqualTo("PORT_BUSY");
        assertThat(JSerialCommEndpoint.diagnosticForOpenFailure(true, 13)).isEqualTo("PORT_BUSY");
    }

    @Test
    void absentPortsRemainDistinctFromOpenFailuresOnPresentPorts() {
        assertThat(JSerialCommEndpoint.diagnosticForOpenFailure(false, 5)).isEqualTo("PORT_NOT_FOUND");
        assertThat(JSerialCommEndpoint.diagnosticForOpenFailure(true, 123)).isEqualTo("PORT_BUSY");
    }
}
