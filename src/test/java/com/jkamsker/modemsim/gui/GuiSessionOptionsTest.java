package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.transport.Parity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GuiSessionOptionsTest {
    @Test
    void compactDataFormatBuildsSerialConfig() {
        GuiSessionOptions options = GuiSessionOptions.ofFormat(
                "sierra-hl6-hl8-v20", "headless", "", "", "",
                "12345", "9600", "7E2", "RTS_CTS");

        assertThat(options.serialLine().baudRate()).isEqualTo(9600);
        assertThat(options.serialLine().dataBits()).isEqualTo(7);
        assertThat(options.serialLine().stopBits()).isEqualTo(2);
        assertThat(options.serialLine().parity()).isEqualTo(Parity.EVEN);
    }

    @Test
    void friendlyNetworkStateValuesCanBeApplied() {
        var patch = GuiStatePatchFactory.fromValues(
                null, "", "", "4 - unknown", "", "", "", "", "18,0",
                "", "", "", "", "", "", "");

        assertThat(patch.apply(com.jkamsker.modemsim.state.ModemState.cellularReady()).network().stat()).isEqualTo(4);
    }
}
