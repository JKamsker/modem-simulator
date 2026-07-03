package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.app.RuntimeTimer;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class GuiAcceptanceHarness {
    public void liveLog() {
        GuiSessionController controller = controller(false);
        var response = controller.rawDteToDce("AT\\r");
        var types = response.events().stream().map(event -> event.eventType()).toList();
        require(types.containsAll(List.of(
                EventType.INJECTION, EventType.RX_BYTES, EventType.PARSED_COMMAND,
                EventType.HANDLER_RESULT, EventType.TX_BYTES)));
        require(response.events().stream().anyMatch(event -> event.parsedCommand() != null));
        require(response.events().stream().anyMatch(event -> "HayesHandler".equals(event.handler())));
    }

    public void injection() {
        GuiSessionController controller = controller(false);
        require(controller.rawDteToDce("AT\\r").events().stream()
                .anyMatch(event -> event.injectionType() != null));
        require(controller.parsedCommand("AT+CSQ").events().stream()
                .noneMatch(event -> event.eventType() == EventType.RX_BYTES));
        requireThrows(() -> controller.urc("+CREG: 4", false));
        require(controller(true).urc("+CREG: 4", true).events().stream()
                .anyMatch(event -> "raw-dce-to-dte".equals(event.injectionType())));
        require(controller.applyState(GuiStatePatchFactory.fromText("network.stat=4")).events().stream()
                .anyMatch(event -> event.eventType() == EventType.STATE_CHANGE));
        requireThrows(() -> controller.rawDceToDte("+CREG: 4\\r\\n", false));
        require(readOnlyDisables("replay.runSelected"));
    }

    public void macroHotReloadTimers() {
        try {
            GuiSessionController controller = controller(false);
            Path valid = timerMacro("heartbeat");
            Path invalid = timerMacro("unknown");
            controller.start(GuiSessionOptions.headless()
                    .withMacroTimers(List.of(new RuntimeTimer("heartbeat", 1000))));
            require(controller.reloadMacros(valid).errors().isBlank());
            controller.macroToggleStatus("disable", "timer-urc");
            String enabledBefore = controller.enabledMacros();
            require(!controller.reloadMacros(invalid).errors().isBlank());
            require(enabledBefore.equals(controller.enabledMacros()));
        } catch (java.io.IOException e) {
            throw new IllegalStateException("GUI macro reload acceptance check failed", e);
        }
    }

    private Path timerMacro(String timerId) throws java.io.IOException {
        Path path = Files.createTempFile("modemsim-timer-macro", ".xml");
        Files.writeString(path, """
                <?xml version="1.0" encoding="UTF-8"?>
                <macros version="1.0">
                  <macro id="timer-urc" phase="on-timer">
                    <match type="timer" timerId="%s"/>
                    <then><emit line="+TIMER"/></then>
                  </macro>
                </macros>
                """.formatted(timerId));
        return path;
    }

    private GuiSessionController controller(boolean unsafeDce) {
        return new GuiSessionController(new HeadlessSession("gui-acceptance",
                BuiltinProfiles.acceptanceSierra(), 12345), unsafeDce);
    }

    private boolean readOnlyDisables(String id) {
        return new ReadOnlyPolicy().apply(new GuiControlCatalog().controls(), true).stream()
                .anyMatch(control -> control.id().equals(id) && !control.enabled());
    }

    private void requireThrows(Runnable action) {
        try {
            action.run();
            require(false);
        } catch (RuntimeException expected) {
            require(expected.getMessage().contains("Unsafe DCE transmit"));
        }
    }

    private void require(boolean condition) {
        if (!condition) {
            throw new IllegalStateException("GUI acceptance check failed");
        }
    }
}
