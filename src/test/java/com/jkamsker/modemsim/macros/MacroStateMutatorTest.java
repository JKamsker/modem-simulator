package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.FreezeMode;
import com.jkamsker.modemsim.state.ModemLifecycle;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SmsStorage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MacroStateMutatorTest {
    @Test
    void appliesEverySupportedStatePatchFamily() {
        var source = BuiltinProfiles.acceptanceSierra().initialState();

        var next = new MacroStateMutator().apply(source, List.of(
                patch("state.sim.state", "SIM_PIN_REQUIRED"),
                patch("state.sim.pinRetries", "2"),
                patch("state.sim.pukRetries", "7"),
                patch("state.network.cregN", "3"),
                patch("state.network.stat", "5"),
                patch("state.network.lac", "00FF"),
                patch("state.network.ci", "00001234"),
                patch("state.network.act", "7"),
                patch("state.network.rejectCauseType", "0"),
                patch("state.network.rejectCause", "11"),
                patch("state.signal.rssi", "12"),
                patch("state.signal.ber", "3"),
                patch("state.sms.textMode", "false"),
                patch("state.sms.storage", "ME"),
                patch("state.call.mode", "online-data"),
                patch("state.call.carrier", "true"),
                patch("state.call.dialedNumber", "+491701234567"),
                patch("state.call.incomingNumber", "+491709999999"),
                patch("state.modem.lifecycle", "FROZEN"),
                patch("state.modem.freezeMode", "HOLD_TX"),
                patch("state.modem.bootDelayMs", "3000"),
                patch("state.modemLines.dtr", "false"),
                patch("state.modemLines.dsr", "false"),
                patch("state.modemLines.dcd", "true"),
                patch("state.modemLines.ri", "true"),
                patch("state.modemLines.rts", "false"),
                patch("state.modemLines.cts", "false")));

        assertThat(next.sim().state()).isEqualTo(SimState.SIM_PIN_REQUIRED);
        assertThat(next.sim().pukRef()).isEqualTo("TEST_SIM_PUK");
        assertThat(next.sim().pinRetries()).isEqualTo(2);
        assertThat(next.sim().pukRetries()).isEqualTo(7);
        assertThat(next.network().cregN()).isEqualTo(3);
        assertThat(next.network().stat()).isZero();
        assertThat(next.network().lac()).isNull();
        assertThat(next.network().ci()).isNull();
        assertThat(next.network().act()).isNull();
        assertThat(next.network().rejectCauseType()).isZero();
        assertThat(next.network().rejectCause()).isEqualTo(11);
        assertThat(next.signal().rssi()).isEqualTo(12);
        assertThat(next.signal().ber()).isEqualTo(3);
        assertThat(next.sms().textMode()).isFalse();
        assertThat(next.sms().storage()).isEqualTo(SmsStorage.ME);
        assertThat(next.call().mode()).isEqualTo(CallMode.ONLINE_DATA);
        assertThat(next.call().carrier()).isTrue();
        assertThat(next.call().dialedNumber()).isEqualTo("+491701234567");
        assertThat(next.call().incomingNumber()).isEqualTo("+491709999999");
        assertThat(next.modem().lifecycle()).isEqualTo(ModemLifecycle.FROZEN);
        assertThat(next.modem().freezeMode()).isEqualTo(FreezeMode.HOLD_TX);
        assertThat(next.modem().bootDelayMs()).isEqualTo(3000);
        assertThat(next.lines().dtr()).isFalse();
        assertThat(next.lines().dsr()).isFalse();
        assertThat(next.lines().dcd()).isTrue();
        assertThat(next.lines().ri()).isTrue();
        assertThat(next.lines().rts()).isFalse();
        assertThat(next.lines().cts()).isFalse();
    }

    @Test
    void ignoresUnknownPathsAndRejectsInvalidCallModes() {
        var source = BuiltinProfiles.acceptanceSierra().initialState();
        var mutator = new MacroStateMutator();

        assertThat(mutator.apply(source, List.of(patch("state.unknown", "x")))).isSameAs(source);
        assertThatThrownBy(() -> mutator.apply(source, List.of(patch("state.call.mode", "invalid"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid call mode");
    }

    private static MacroStatePatch patch(String path, String value) {
        return new MacroStatePatch(path, value);
    }
}
