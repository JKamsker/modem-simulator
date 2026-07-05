package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SessionActorCommandTest {
    @Test
    void submitRunsSessionCommandsThroughActorGateway() {
        SessionActor actor = new HeadlessSession("actor", BuiltinProfiles.acceptanceSierra(), 12345);

        SessionResponse response = actor.submit(SessionCommand.receive(RawBytes.ascii("AT\r")));

        assertThat(response.outputAscii()).contains("OK");
    }
}
