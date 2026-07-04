package com.jkamsker.modemsim.transport;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JSerialCommEndpointTest {
    @Test
    void permissionDeniedOpenFailuresHaveDistinctDiagnostic() {
        assertThat(JSerialCommEndpoint.diagnosticForOpenFailure(true, 1)).isEqualTo("PORT_PERMISSION_DENIED");
        assertThat(JSerialCommEndpoint.diagnosticForOpenFailure(true, 5)).isEqualTo("PORT_PERMISSION_DENIED");
        assertThat(JSerialCommEndpoint.diagnosticForOpenFailure(true, 13)).isEqualTo("PORT_PERMISSION_DENIED");
    }

    @Test
    void absentPortsRemainDistinctFromOpenFailuresOnPresentPorts() {
        assertThat(JSerialCommEndpoint.diagnosticForOpenFailure(false, 5)).isEqualTo("PORT_NOT_FOUND");
        assertThat(JSerialCommEndpoint.diagnosticForOpenFailure(true, 123)).isEqualTo("PORT_BUSY");
    }

    @Test
    void lineSnapshotKeepsModemLineFieldOrder() {
        var lines = JSerialCommEndpoint.lineSnapshot(true, false, true, false, true, false);

        assertThat(lines.dtr()).isTrue();
        assertThat(lines.dsr()).isFalse();
        assertThat(lines.dcd()).isTrue();
        assertThat(lines.ri()).isFalse();
        assertThat(lines.rts()).isTrue();
        assertThat(lines.cts()).isFalse();
    }
}
