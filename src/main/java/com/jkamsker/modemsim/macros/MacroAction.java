package com.jkamsker.modemsim.macros;

import java.util.Map;

public record MacroAction(String type, Map<String, String> attributes, String text) {
    public String attr(String name) {
        return attributes.get(name);
    }
}
