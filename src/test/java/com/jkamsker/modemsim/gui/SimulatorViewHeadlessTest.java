package com.jkamsker.modemsim.gui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TabPane;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("gui")
class SimulatorViewHeadlessTest {
    private static final AtomicBoolean STARTED = new AtomicBoolean();

    @Test
    void startsJavaFxViewAndAppliesReadOnlyToRealControls() throws Exception {
        startToolkit();
        Parent root = fx(() -> new SimulatorView().root());

        List<Boolean> disabled = fx(() -> {
            ((CheckBox) find(root, "session.readOnly")).setSelected(true);
            return List.of(
                    find(root, "state.apply").isDisabled(),
                    find(root, "inject.sendDte").isDisabled(),
                    find(root, "fault.networkOutage").isDisabled(),
                    find(root, "macro.reload").isDisabled(),
                    find(root, "replay.runSelected").isDisabled(),
                    find(root, "replay.driveFromCapturedInput").isDisabled(),
                    find(root, "replay.playToDte").isDisabled(),
                    find(root, "log.export").isDisabled(),
                    find(root, "export.jsonl").isDisabled());
        });

        assertThat(find(root, "session.initialScenario")).isNotNull();
        assertThat(find(root, "session.dataFormat")).isNotNull();
        assertThat(find(root, "state.signalRssi")).isNotNull();
        assertThat(find(root, "macro.id")).isNotNull();
        assertThat(disabled).containsExactly(true, true, true, true, true, true, true, false, false);
    }

    @Test
    void urcHelperUsesSafeGuiPath() throws Exception {
        startToolkit();
        Parent root = fx(() -> new SimulatorView(true).root());

        String output = fx(() -> {
            ((CheckBox) find(root, "inject.safetyConfirm")).setSelected(true);
            ((Button) find(root, "inject.sendUrc")).fire();
            return ((TextArea) find(root, "session.output")).getText();
        });

        assertThat(output).contains("+CREG: 4");
    }

    private static void startToolkit() throws Exception {
        if (STARTED.compareAndSet(false, true)) {
            try {
                FutureTask<Void> task = new FutureTask<>(() -> null);
                Platform.startup(task);
                task.get();
            } catch (IllegalStateException ignored) {
                // JavaFX toolkit can be initialized by another GUI test class in this JVM.
            }
        }
    }

    private static <T> T fx(java.util.concurrent.Callable<T> action) throws Exception {
        FutureTask<T> task = new FutureTask<>(action);
        Platform.runLater(task);
        return task.get();
    }

    private static Node find(Node node, String id) {
        if (id.equals(node.getId())) {
            return node;
        }
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                Node found = find(child, id);
                if (found != null) {
                    return found;
                }
            }
        }
        if (node instanceof TabPane tabPane) {
            for (var tab : tabPane.getTabs()) {
                Node found = find(tab.getContent(), id);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
