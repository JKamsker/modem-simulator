package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.parser.RawBytes;

@FunctionalInterface
public interface SessionCommand {
    SessionResponse execute(HeadlessSession session);

    static SessionCommand receive(RawBytes bytes) {
        return session -> session.receive(bytes);
    }

    static SessionCommand advanceTime(long millis) {
        return session -> session.advanceTime(millis);
    }
}
