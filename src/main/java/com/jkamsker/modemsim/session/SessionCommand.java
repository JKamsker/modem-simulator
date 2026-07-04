package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.macros.FaultAction;
import com.jkamsker.modemsim.macros.FaultService;
import com.jkamsker.modemsim.macros.MacroStateMutator;
import com.jkamsker.modemsim.macros.MacroStatePatch;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemState;

import java.util.List;

@FunctionalInterface
public interface SessionCommand {
    SessionResponse execute(HeadlessSession session);

    static SessionCommand receive(RawBytes bytes) {
        return session -> session.receive(bytes);
    }

    static SessionCommand advanceTime(long millis) {
        return session -> session.advanceTime(millis);
    }

    static MacroStateCommand macroSet(MacroStatePatch patch) {
        return state -> new MacroStateMutator().apply(state, List.of(patch));
    }

    static MacroStateCommand macroFault(FaultAction action) {
        return state -> new FaultService().apply(state, action);
    }

    @FunctionalInterface
    interface MacroStateCommand {
        ModemState apply(ModemState state);
    }
}
