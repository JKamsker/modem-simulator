package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.monitor.ModemEvent;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Slider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

final class GuiViewSupport {
    private final Map<String, Node> controls = new HashMap<>();

    void applyReadOnly(boolean readOnly) {
        Map<String, Boolean> enabled = new ReadOnlyPolicy().apply(new GuiControlCatalog().controls(), readOnly)
                .stream()
                .collect(Collectors.toMap(GuiControl::id, GuiControl::enabled));
        controls.forEach((id, node) -> {
            if (enabled.containsKey(id)) {
                node.setDisable(!enabled.get(id));
            }
        });
    }

    Tab tab(String title, Node content) {
        Tab tab = new Tab(title, content);
        tab.setClosable(false);
        return tab;
    }

    GridPane grid() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        return grid;
    }

    void add(GridPane grid, int row, String label, Node control) {
        grid.add(labelWithHelp(label, GuiHelpText.forLabel(label)), 0, row);
        grid.add(control, 1, row);
    }

    Node explainer(String label, String help) {
        return labelWithHelp(label, help);
    }

    TextField field(String id, String value) {
        TextField field = register(new TextField(value), id);
        field.setPrefColumnCount(24);
        return field;
    }

    ComboBox<String> combo(String id, String... values) {
        ComboBox<String> combo = register(new ComboBox<>(FXCollections.observableArrayList(values)), id);
        if (values.length > 0) {
            combo.getSelectionModel().selectFirst();
        }
        return combo;
    }

    Slider slider(String id, double min, double max, double value) {
        Slider slider = register(new Slider(min, max, value), id);
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);
        return slider;
    }

    void select(ComboBox<String> combo, String value) {
        combo.getSelectionModel().select(value);
    }

    Label label(String id, String text) {
        return register(new Label(text), id);
    }

    Button button(String id, String text) {
        return register(new Button(text), id);
    }

    <T extends Node> T register(T node, String id) {
        node.setId(id);
        controls.put(id, node);
        return node;
    }

    TableColumn<ModemEvent, String> column(String name, Function<ModemEvent, String> extractor) {
        TableColumn<ModemEvent, String> column = new TableColumn<>(name);
        column.setCellValueFactory(cell -> new ReadOnlyStringWrapper(extractor.apply(cell.getValue())));
        return column;
    }

    String[] names(Object[] values) {
        String[] names = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            names[i] = values[i].toString();
        }
        return names;
    }

    String value(Object value) {
        return value == null ? "" : value.toString();
    }

    private Node labelWithHelp(String text, String help) {
        Label label = new Label(text);
        Label icon = new Label("?");
        Tooltip tooltip = new Tooltip(help);
        Tooltip.install(label, tooltip);
        Tooltip.install(icon, tooltip);
        return new HBox(4, label, icon);
    }
}
