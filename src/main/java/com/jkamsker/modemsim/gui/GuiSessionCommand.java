package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.session.SessionResponse;

import java.util.function.Supplier;

record GuiSessionCommand(String type, Supplier<SessionResponse> action) {
}
