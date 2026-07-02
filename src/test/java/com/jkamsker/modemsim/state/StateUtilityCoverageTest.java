package com.jkamsker.modemsim.state;

import com.jkamsker.modemsim.commands.ResultCode;
import com.jkamsker.modemsim.scheduler.ScheduledEmission;
import com.jkamsker.modemsim.scheduler.SourcePriority;
import com.jkamsker.modemsim.parser.RawBytes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StateUtilityCoverageTest {
    @Test
    void sessionSettingsCoversWritableRegistersAndFallbacks() {
        SessionSettings settings = SessionSettings.defaults()
                .withRegister(3, 1)
                .withRegister(4, 2)
                .withRegister(5, 3)
                .withRegister(7, 4)
                .withRegister(12, 5)
                .withRegister(99, 6);

        assertThat(settings.register(3)).isEqualTo(1);
        assertThat(settings.register(4)).isEqualTo(2);
        assertThat(settings.register(5)).isEqualTo(3);
        assertThat(settings.register(7)).isEqualTo(4);
        assertThat(settings.register(12)).isEqualTo(5);
        assertThat(settings.register(99)).isZero();
    }

    @Test
    void operatorInfoCoversAllModesAndFormats() {
        assertThat(new OperatorInfo("manual", "short", "Long", "Short", "26201", "262", "01")
                .selectionModeCode()).isEqualTo(1);
        assertThat(new OperatorInfo("deregister", "numeric", "Long", "Short", "26201", "262", "01")
                .selectionModeCode()).isEqualTo(2);
        assertThat(new OperatorInfo("set-format", "long", "Long", "Short", "26201", "262", "01")
                .selectionModeCode()).isEqualTo(3);
        OperatorInfo manualAutomatic = new OperatorInfo("manual-automatic", "numeric", "Long", "Short", "26201", "262", "01");
        assertThat(manualAutomatic.selectionModeCode()).isEqualTo(4);
        assertThat(manualAutomatic.formatCode()).isEqualTo(2);
        assertThat(manualAutomatic.displayName()).isEqualTo("26201");
        assertThat(new OperatorInfo("automatic", "short", "Long", "Short", "26201", "262", "01").displayName())
                .isEqualTo("Short");
    }

    @Test
    void scheduledEmissionOrdersByDueSequenceThenPriority() {
        ScheduledEmission first = new ScheduledEmission(1, 1, SourcePriority.MACRO, RawBytes.empty(), 0, false, "a", 0);
        ScheduledEmission second = new ScheduledEmission(1, 2, SourcePriority.INTERNAL, RawBytes.empty(), 0, false, "a", 0);
        ScheduledEmission third = new ScheduledEmission(2, 1, SourcePriority.INTERNAL, RawBytes.empty(), 0, false, "a", 0);

        assertThat(first.compareTo(second)).isNegative();
        assertThat(first.compareTo(third)).isNegative();
        assertThat(second.compareTo(first)).isPositive();
        assertThat(ResultCode.OK.text(false)).isEqualTo("0");
    }
}
