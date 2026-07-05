package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.commands.CommandResult;
import com.jkamsker.modemsim.macros.MacroEventAction;

import java.util.LinkedHashMap;
import java.util.Map;

final class SessionEventData {
    private SessionEventData() {
    }

    static Map<String, Object> macroEvent(MacroEventAction event) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("type", event.type());
        data.put("message", event.message());
        return data;
    }

    static String macroId(CommandResult result) {
        if (result == null || result.handler() == null) {
            return null;
        }
        for (String part : result.handler().split("\\+")) {
            if (part.startsWith("Macro:")) {
                return part.substring("Macro:".length());
            }
        }
        return null;
    }

    static String macroId(String operation) {
        return operation != null && operation.startsWith("macro-") ? operation.substring("macro-".length()) : null;
    }
}
