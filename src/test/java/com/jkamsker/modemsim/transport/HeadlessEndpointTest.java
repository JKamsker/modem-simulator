package com.jkamsker.modemsim.transport;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemLines;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HeadlessEndpointTest {
    @Test
    void queuesReadsCapturesWritesAndStoresLineState() throws Exception {
        HeadlessEndpoint endpoint = new HeadlessEndpoint();
        endpoint.open(SerialConfig.defaults());
        endpoint.enqueueRead(RawBytes.ascii("AT\r"), 10);

        assertThat(endpoint.read().bytes().ascii()).isEqualTo("AT\r");
        endpoint.write(new byte[] {'O', 'K'}, 0, 2);
        assertThat(endpoint.written().ascii()).isEqualTo("OK");

        ModemLines lines = new ModemLines(false, true, true, false, false, true);
        endpoint.writeLines(lines);
        assertThat(endpoint.readLines()).isEqualTo(lines);
        endpoint.close();
    }

    @Test
    void emptyReadFailsClearly() {
        assertThatThrownBy(() -> new HeadlessEndpoint().read())
                .isInstanceOf(IOException.class)
                .hasMessageContaining("No headless input");
    }
}
