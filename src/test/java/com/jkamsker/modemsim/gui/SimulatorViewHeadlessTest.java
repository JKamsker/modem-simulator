package com.jkamsker.modemsim.gui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
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
        SimulatorView view = new SimulatorView();
        Parent root = fx(view::root);

        try {
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
        } finally {
            close(view);
        }
    }

    @Test
    void urcHelperUsesSafeGuiPath() throws Exception {
        startToolkit();
        SimulatorView view = new SimulatorView(true);
        Parent root = fx(view::root);

        try {
            String output = fx(() -> {
                ((CheckBox) find(root, "inject.safetyConfirm")).setSelected(true);
                ((Button) find(root, "inject.sendUrc")).fire();
                return ((TextArea) find(root, "session.output")).getText();
            });

            assertThat(output).contains("+CREG: 4");
        } finally {
            close(view);
        }
    }

    @Test
    void realViewControlsDriveLogInjectionStateMacroAndReplayWorkflows() throws Exception {
        startToolkit();
        SimulatorView view = new SimulatorView(true);
        Parent root = fx(view::root);

        try {
            fx(() -> {
                ((Button) find(root, "inject.sendDte")).fire();
                TableView<?> table = (TableView<?>) find(root, "log.table");
                assertThat(table.getColumns()).hasSizeGreaterThanOrEqualTo(16);
                assertThat(table.getItems()).hasSizeGreaterThanOrEqualTo(4);

                ((TextField) find(root, "log.filter")).setText("TX_BYTES");
                assertThat(table.getItems()).hasSize(1);
                ((Button) find(root, "log.export")).fire();
                String export = ((TextArea) find(root, "export.preview")).getText();
                assertThat(export).contains("\"eventType\":\"TX_BYTES\"")
                        .doesNotContain("\"eventType\":\"RX_BYTES\"");
                ((TextField) find(root, "log.filter")).clear();

                ((Button) find(root, "inject.sendDce")).fire();
                assertThat(((TextArea) find(root, "session.output")).getText())
                        .contains("Unsafe DCE transmit");
                ((CheckBox) find(root, "inject.safetyConfirm")).setSelected(true);
                ((Button) find(root, "inject.sendUrc")).fire();
                assertThat(((TextArea) find(root, "session.output")).getText()).contains("+CREG: 4");

                ((ComboBox<?>) find(root, "state.networkStat")).getSelectionModel().select(4);
                ((Button) find(root, "state.apply")).fire();
                assertThat(table.getItems().stream().map(String::valueOf))
                        .anyMatch(text -> text.contains("STATE_CHANGE"));

                ((Button) find(root, "macro.reload")).fire();
                assertThat(((Label) find(root, "macro.hash")).getText()).startsWith("sha256:");
                assertThat(find(root, "replay.validate")).isNotNull();
                assertThat(find(root, "replay.runSelected")).isNotNull();
                assertThat(find(root, "replay.driveFromCapturedInput")).isNotNull();
                assertThat(find(root, "replay.playToDte")).isNotNull();
                return null;
            });
        } finally {
            close(view);
        }
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

    private static void close(SimulatorView view) throws Exception {
        fx(() -> {
            view.close();
            return null;
        });
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
