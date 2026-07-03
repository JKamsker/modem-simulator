package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.app.RuntimeTimer;
import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.session.HeadlessSession;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GuiAcceptanceHarness {
    private static final AtomicBoolean STARTED = new AtomicBoolean();

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("live-log-filter-export")) {
            try {
                new GuiAcceptanceHarness().liveLogFilterExportInProcess();
            } finally {
                Platform.exit();
            }
            return;
        }
        throw new IllegalArgumentException("Unknown GUI acceptance probe");
    }

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

    public void liveLogFilterExport() {
        if (displayMissing() && !Boolean.getBoolean("modemsim.gui.acceptance.child")) {
            runUnderXvfb();
            return;
        }
        liveLogFilterExportInProcess();
    }

    private void liveLogFilterExportInProcess() {
        try {
            startToolkit();
            Parent root = fx(() -> new SimulatorView().root());
            fx(() -> {
                ((Button) find(root, "inject.sendDte")).fire();
                TableView<?> table = (TableView<?>) find(root, "log.table");
                int total = table.getItems().size();
                ((TextField) find(root, "log.filter")).setText("TX_BYTES");
                require(total > table.getItems().size() && table.getItems().size() == 1,
                        "filtered rows=" + table.getItems().size() + " total=" + total);
                ((Button) find(root, "log.export")).fire();
                String export = ((TextArea) find(root, "export.preview")).getText();
                require(export.contains("\"eventType\":\"TX_BYTES\""), "filtered export missing TX_BYTES: " + export);
                require(!export.contains("\"eventType\":\"RX_BYTES\""), "filtered export leaked RX_BYTES: " + export);
                return null;
            });
        } catch (Exception e) {
            throw new IllegalStateException("GUI live-log filter acceptance check failed: " + e.getMessage(), e);
        }
    }

    private void runUnderXvfb() {
        try {
            String java = Path.of(System.getProperty("java.home"), "bin", "java").toString();
            Process process = new ProcessBuilder("xvfb-run", "-a", java,
                    "-Dmodemsim.gui.acceptance.child=true", "-cp", childClasspath(),
                    GuiAcceptanceHarness.class.getName(), "live-log-filter-export")
                    .redirectErrorStream(true)
                    .start();
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            require(process.waitFor() == 0, "xvfb JavaFX probe failed: " + output);
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Cannot start xvfb JavaFX probe", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while running xvfb JavaFX probe", e);
        }
    }

    private boolean displayMissing() {
        String display = System.getenv("DISPLAY");
        return display == null || display.isBlank();
    }

    private String childClasspath() {
        LinkedHashSet<String> entries = new LinkedHashSet<>();
        addClasspathProperty(entries);
        addCodeSource(entries, GuiAcceptanceHarness.class);
        addCodeSource(entries, Platform.class);
        addCodeSource(entries, Button.class);
        addClassLoaderUrls(entries, Thread.currentThread().getContextClassLoader());
        return String.join(File.pathSeparator, entries);
    }

    private void addClasspathProperty(LinkedHashSet<String> entries) {
        String classpath = System.getProperty("java.class.path", "");
        for (String entry : classpath.split(java.util.regex.Pattern.quote(File.pathSeparator))) {
            if (!entry.isBlank()) {
                entries.add(entry);
            }
        }
    }

    private void addClassLoaderUrls(LinkedHashSet<String> entries, ClassLoader loader) {
        for (ClassLoader current = loader; current != null; current = current.getParent()) {
            if (current instanceof URLClassLoader urls) {
                for (URL url : urls.getURLs()) {
                    entries.add(Path.of(url.getPath()).toString());
                }
            }
        }
    }

    private void addCodeSource(LinkedHashSet<String> entries, Class<?> type) {
        var source = type.getProtectionDomain().getCodeSource();
        if (source != null) {
            entries.add(Path.of(source.getLocation().getPath()).toString());
        }
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
            controller.fireTimer("heartbeat");
            String enabledBefore = controller.enabledMacros();
            require(!controller.reloadMacros(invalid).errors().isBlank());
            require(enabledBefore.equals(controller.enabledMacros()));
            require(controller.advanceTime(1000).outputAscii().contains("+TIMER"));
            controller.fireTimer("heartbeat");
            require(controller.reloadMacros(valid).response().events().stream().anyMatch(event ->
                    event.eventType() == EventType.SCHEDULER_EMIT
                            && Boolean.TRUE.equals(event.scheduler().get("cancelled"))
                            && "macro-timer-urc".equals(event.scheduler().get("operation"))));
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
                    <then><delay ms="1000"/><emit line="+TIMER"/></then>
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

    private static void startToolkit() throws Exception {
        if (STARTED.compareAndSet(false, true)) {
            FutureTask<Void> task = new FutureTask<>(() -> null);
            Platform.startup(task);
            task.get();
        }
    }

    private static <T> T fx(java.util.concurrent.Callable<T> action) throws Exception {
        FutureTask<T> task = new FutureTask<>(action);
        Platform.runLater(task);
        return task.get();
    }

    private static Node find(Node node, String id) {
        if (id.equals(node.getId())) { return node; }
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                Node found = find(child, id);
                if (found != null) { return found; }
            }
        }
        if (node instanceof TabPane tabPane) {
            for (var tab : tabPane.getTabs()) {
                Node found = find(tab.getContent(), id);
                if (found != null) { return found; }
            }
        }
        return null;
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
        require(condition, "GUI acceptance check failed");
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
