package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.CommandResult;
import com.jkamsker.modemsim.commands.ResponseFormatter;
import com.jkamsker.modemsim.macros.MacroDecision;
import com.jkamsker.modemsim.macros.MacroEngine;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.state.ModemState;

final class SessionStateChangeMacros {
    private SessionStateChangeMacros() {
    }

    static Result run(
            MacroEngine engine, MacroCommandRouter router, Profile profile, ModemState before, ModemState after) {
        if (before.version() == after.version()) {
            return new Result(after, RawBytes.empty());
        }
        MacroDecision decision = engine.evaluateStateChange(before, after, profile);
        if (!decision.matched()) {
            return new Result(after, RawBytes.empty());
        }
        CommandResult result = router.executeStateChange(decision, after);
        return new Result(result.state(), SessionFrameRenderer.render(result.frames(), result.state()));
    }

    static RawBytes registrationUrc(ModemState before, ModemState after) {
        if (before.network() == null || after.network() == null
                || before.network().stat() == after.network().stat() || after.network().cregN() == 0) {
            return RawBytes.empty();
        }
        String line = "+CREG: " + after.network().stat();
        if (after.network().registeredForCircuitServices() && after.network().cregN() >= 2) {
            line += ",\"" + after.network().lac() + "\",\"" + after.network().ci() + "\"," + after.network().act();
        }
        return new ResponseFormatter(after).line(line);
    }

    record Result(ModemState state, RawBytes output) {
    }
}
