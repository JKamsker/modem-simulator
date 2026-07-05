package com.jkamsker.modemsim.parser;

public record AtToken(String type, String text, CommandSpan span, boolean quoted) {
}
