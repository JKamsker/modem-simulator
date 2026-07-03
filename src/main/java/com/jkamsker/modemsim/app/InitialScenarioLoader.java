package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.macros.FaultAction;
import com.jkamsker.modemsim.macros.FaultService;
import com.jkamsker.modemsim.macros.MacroStateMutator;
import com.jkamsker.modemsim.macros.MacroStatePatch;
import com.jkamsker.modemsim.state.FreezeMode;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.validation.Dom;
import com.jkamsker.modemsim.validation.ScenarioValidator;
import com.jkamsker.modemsim.validation.StateInvariantValidator;
import com.jkamsker.modemsim.validation.XmlSecurity;
import org.w3c.dom.Element;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class InitialScenarioLoader {
    private final MacroStateMutator mutator = new MacroStateMutator();
    private final FaultService faults = new FaultService();

    public ModemState apply(Path scenarioPath, ModemState state) {
        if (scenarioPath == null) {
            return state;
        }
        new ScenarioValidator().validate(scenarioPath).throwIfInvalid();
        Element root = XmlSecurity.parse(scenarioPath).getDocumentElement();
        Element initial = Dom.child(root, "initial-state");
        ModemState next = initial == null ? state : mutator.apply(state, patches(initial));
        for (Element step : Dom.children(root, "step")) {
            if (Long.parseLong(step.getAttribute("atMs")) == 0) {
                next = applyStep(step, next);
            }
        }
        validateState(scenarioPath, next);
        return next;
    }

    List<InitialScenarioStep> delayedSteps(Path scenarioPath) {
        if (scenarioPath == null) {
            return List.of();
        }
        new ScenarioValidator().validate(scenarioPath).throwIfInvalid();
        Element root = XmlSecurity.parse(scenarioPath).getDocumentElement();
        List<InitialScenarioStep> steps = new ArrayList<>();
        for (Element step : Dom.children(root, "step")) {
            long atMs = Long.parseLong(step.getAttribute("atMs"));
            if (atMs > 0) {
                steps.add(new InitialScenarioStep(atMs, actions(step)));
            }
        }
        return steps;
    }

    private ModemState applyStep(Element step, ModemState state) {
        ModemState next = state;
        for (Element action : Dom.children(step, null)) {
            if (action.getTagName().equals("set")) {
                next = mutator.apply(next, List.of(new MacroStatePatch(action.getAttribute("path"), action.getAttribute("value"))));
            } else if (action.getTagName().equals("fault")) {
                next = faults.apply(next, fault(action));
            }
        }
        return next;
    }

    private List<InitialScenarioAction> actions(Element step) {
        List<InitialScenarioAction> actions = new ArrayList<>();
        for (Element action : Dom.children(step, null)) {
            if (action.getTagName().equals("set")) {
                actions.add(new InitialScenarioAction(
                        new MacroStatePatch(action.getAttribute("path"), action.getAttribute("value")), null, null));
            } else if (action.getTagName().equals("fault")) {
                actions.add(new InitialScenarioAction(null, fault(action), null));
            } else if (action.getTagName().equals("advance")) {
                actions.add(new InitialScenarioAction(null, null, Long.valueOf(action.getAttribute("ms"))));
            }
        }
        return actions;
    }

    private FaultAction fault(Element action) {
        return new FaultAction(
                action.getAttribute("type"),
                integer(action, "durationMs"),
                integer(action, "stat"),
                integer(action, "rssi"),
                integer(action, "ber"),
                action.hasAttribute("freezeMode") ? FreezeMode.valueOf(action.getAttribute("freezeMode")) : null);
    }

    private List<MacroStatePatch> patches(Element initial) {
        List<MacroStatePatch> patches = new ArrayList<>();
        add(patches, Dom.child(initial, "sim"), "state", "state.sim.state");
        add(patches, Dom.child(initial, "network"), "cregN", "state.network.cregN");
        add(patches, Dom.child(initial, "network"), "stat", "state.network.stat");
        add(patches, Dom.child(initial, "signal"), "rssi", "state.signal.rssi");
        add(patches, Dom.child(initial, "signal"), "ber", "state.signal.ber");
        Element lines = Dom.child(initial, "modem-lines");
        add(patches, lines, "dtr", "state.modemLines.dtr");
        add(patches, lines, "dsr", "state.modemLines.dsr");
        add(patches, lines, "dcd", "state.modemLines.dcd");
        add(patches, lines, "ri", "state.modemLines.ri");
        add(patches, lines, "rts", "state.modemLines.rts");
        add(patches, lines, "cts", "state.modemLines.cts");
        return patches;
    }

    private void add(List<MacroStatePatch> patches, Element element, String attribute, String path) {
        if (element != null && element.hasAttribute(attribute)) {
            patches.add(new MacroStatePatch(path, element.getAttribute(attribute)));
        }
    }

    private Integer integer(Element element, String attribute) {
        return element.hasAttribute(attribute) ? Integer.valueOf(element.getAttribute(attribute)) : null;
    }

    private void validateState(Path scenarioPath, ModemState state) {
        String error = StateInvariantValidator.lockedRegistration(scenarioPath.toString(), state);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
    }
}
