package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.NetworkRuntime;
import com.jkamsker.modemsim.state.SimRuntime;
import com.jkamsker.modemsim.state.SimState;

import java.util.List;

public final class CellularHandler implements CommandHandler {
    @Override
    public CommandResult handle(Profile profile, ModemState state, ParsedCommand command) {
        return switch (command.normalizedName()) {
            case "+CREG", "+CGREG", "+CEREG" -> registration(state, command);
            case "+CSQ" -> csq(state, command);
            case "+CPIN" -> cpin(state, command);
            case "+COPS" -> cops(state, command);
            case "+CMEE" -> cmee(state, command);
            case "+CCLK" -> cclk(state, command);
            case "+CFUN" -> cfun(state, command);
            case "+CGMI" -> identityLine(profile, state, profile.identity().manufacturer());
            case "+CGMM" -> identityLine(profile, state, profile.identity().model());
            case "+CGMR" -> identityLine(profile, state, profile.identity().revision());
            case "+CGSN" -> identityLine(profile, state, profile.identity().imei());
            default -> null;
        };
    }

    private CommandResult registration(ModemState state, ParsedCommand command) {
        if (state.network() == null) {
            return CommandResult.error(state, "CellularHandler");
        }
        if (state.sim().state() != SimState.READY) {
            return simStateError(state);
        }
        return switch (command.kind()) {
            case EXTENDED_SET -> setRegistrationMode(state, command.normalizedName(), command.arguments());
            case EXTENDED_READ -> registrationRead(state, command.normalizedName());
            case EXTENDED_TEST -> new CommandResult(
                    state, List.of(new TextFrame(command.normalizedName() + ": (0-3)")),
                    ResultCode.OK, "CellularHandler", false);
            default -> CommandResult.ok(state, "CellularHandler");
        };
    }

    private CommandResult setRegistrationMode(ModemState state, String name, String arguments) {
        Integer cregN = parseNumber(arguments);
        if (cregN == null || cregN < 0 || cregN > 3) {
            return CommandResult.error(state, "CellularHandler");
        }
        ModemState next = name.equals("+CREG") ? state.withNetwork(state.network().withCregN(cregN)) : state;
        return CommandResult.ok(next, "CellularHandler");
    }

    private CommandResult registrationRead(ModemState state, String name) {
        NetworkRuntime network = state.network();
        String line = name + ": " + network.cregN() + "," + network.stat();
        if (network.registeredForCircuitServices() && network.cregN() >= 2) {
            line += ",\"" + network.lac() + "\",\"" + network.ci() + "\"," + network.act();
        } else if (network.cregN() == 3 && network.stat() == 3) {
            line += "," + valueOrZero(network.rejectCauseType()) + "," + valueOrDefault(network.rejectCause(), 11);
        }
        return new CommandResult(state, List.of(new TextFrame(line)), ResultCode.OK, "CellularHandler", false);
    }

    private CommandResult csq(ModemState state, ParsedCommand command) {
        if (state.sim().state() != SimState.READY) {
            return simStateError(state);
        }
        if (command.kind().name().endsWith("TEST")) {
            return new CommandResult(state, List.of(new TextFrame("+CSQ: (0-31,99),(0-7,99)")),
                    ResultCode.OK, "CellularHandler", false);
        }
        return new CommandResult(
                state,
                List.of(new TextFrame("+CSQ: " + state.signal().rssi() + "," + state.signal().ber())),
                ResultCode.OK,
                "CellularHandler",
                false);
    }

    private CommandResult cpin(ModemState state, ParsedCommand command) {
        return switch (command.kind()) {
            case EXTENDED_READ -> cpinRead(state);
            case EXTENDED_SET -> cpinSet(state, unquote(command.arguments()));
            default -> CommandResult.error(state, "CellularHandler");
        };
    }

    private CommandResult cpinRead(ModemState state) {
        return switch (state.sim().state()) {
            case READY -> line(state, "+CPIN: READY");
            case SIM_PIN_REQUIRED -> line(state, "+CPIN: SIM PIN");
            case SIM_PUK_REQUIRED -> line(state, "+CPIN: SIM PUK");
            case SIM_NOT_INSERTED -> CmeError.SIM_NOT_INSERTED.result(state, "CellularHandler");
            case SIM_FAILURE -> CmeError.SIM_FAILURE.result(state, "CellularHandler");
            case SIM_BUSY -> CmeError.SIM_BUSY.result(state, "CellularHandler");
            case SIM_WRONG -> CmeError.SIM_WRONG.result(state, "CellularHandler");
            default -> CmeError.OPERATION_NOT_ALLOWED.result(state, "CellularHandler");
        };
    }

    private CommandResult cpinSet(ModemState state, String pin) {
        if (state.sim().state() != SimState.SIM_PIN_REQUIRED) {
            return CommandResult.ok(state, "CellularHandler");
        }
        String expectedPin = expectedPin(state.sim());
        if (pin.equals(expectedPin)) {
            return CommandResult.ok(state.withSim(state.sim().withState(SimState.READY)), "CellularHandler");
        }
        SimRuntime sim = state.sim().withPinRetries(Math.max(0, state.sim().pinRetries() - 1));
        return CmeError.INCORRECT_PASSWORD.result(state.withSim(sim), "CellularHandler");
    }

    private String expectedPin(SimRuntime sim) {
        if (sim.testPin() != null && !sim.testPin().isBlank()) {
            return sim.testPin();
        }
        return "TEST_SIM_PIN".equals(sim.pinRef()) ? "1234" : "";
    }

    private CommandResult cops(ModemState state, ParsedCommand command) {
        if (state.network() == null) {
            return CommandResult.error(state, "CellularHandler");
        }
        if (state.sim().state() != SimState.READY) {
            return simStateError(state);
        }
        if (command.kind().name().endsWith("READ")) {
            var operator = state.network().operator();
            String line = "+COPS: " + operator.selectionModeCode() + ","
                    + operator.formatCode() + ",\"" + operator.displayName() + "\"," + state.network().act();
            return line(state, line);
        }
        if (command.kind().name().endsWith("TEST")) {
            return line(state, "+COPS: (0,1,2,3,4),(0,1,2)");
        }
        return CommandResult.ok(state, "CellularHandler");
    }

    private CommandResult cmee(ModemState state, ParsedCommand command) {
        return switch (command.kind()) {
            case EXTENDED_SET -> setCmee(state, command.arguments());
            case EXTENDED_READ -> line(state, "+CMEE: " + state.settings().cmee());
            case EXTENDED_TEST -> line(state, "+CMEE: (0-2)");
            default -> CommandResult.ok(state, "CellularHandler");
        };
    }

    private CommandResult cclk(ModemState state, ParsedCommand command) {
        return switch (command.kind()) {
            case EXTENDED_READ -> line(state, "+CCLK: \"00/01/01,00:00:00+00\"");
            case EXTENDED_TEST -> line(state, "+CCLK: \"yy/MM/dd,hh:mm:ss+zz\"");
            case EXTENDED_SET -> CommandResult.ok(state, "CellularHandler");
            default -> CommandResult.error(state, "CellularHandler");
        };
    }

    private CommandResult cfun(ModemState state, ParsedCommand command) {
        return switch (command.kind()) {
            case EXTENDED_READ -> line(state, "+CFUN: 1");
            case EXTENDED_TEST -> line(state, "+CFUN: (0,1),(0)");
            case EXTENDED_SET -> cfunSet(state, command.arguments());
            default -> CommandResult.ok(state, "CellularHandler");
        };
    }

    private CommandResult cfunSet(ModemState state, String arguments) {
        Integer mode = parseNumber(arguments.split(",")[0]);
        return mode != null && (mode == 0 || mode == 1)
                ? CommandResult.ok(state, "CellularHandler")
                : CommandResult.error(state, "CellularHandler");
    }

    private CommandResult setCmee(ModemState state, String arguments) {
        Integer mode = parseNumber(arguments);
        if (mode == null || mode < 0 || mode > 2) {
            return CommandResult.error(state, "CellularHandler");
        }
        return CommandResult.ok(state.withSettings(state.settings().withCmee(mode)), "CellularHandler");
    }

    private CommandResult simStateError(ModemState state) {
        return switch (state.sim().state()) {
            case SIM_PIN_REQUIRED -> CmeError.SIM_PIN_REQUIRED.result(state, "CellularHandler");
            case SIM_PUK_REQUIRED -> CmeError.SIM_PUK_REQUIRED.result(state, "CellularHandler");
            case SIM_NOT_INSERTED -> CmeError.SIM_NOT_INSERTED.result(state, "CellularHandler");
            case SIM_FAILURE -> CmeError.SIM_FAILURE.result(state, "CellularHandler");
            case SIM_BUSY -> CmeError.SIM_BUSY.result(state, "CellularHandler");
            case SIM_WRONG -> CmeError.SIM_WRONG.result(state, "CellularHandler");
            default -> CmeError.OPERATION_NOT_ALLOWED.result(state, "CellularHandler");
        };
    }

    private CommandResult identityLine(Profile profile, ModemState state, String value) {
        String safe = value == null ? profile.id() : value;
        return line(state, safe);
    }

    private CommandResult line(ModemState state, String value) {
        return new CommandResult(state, List.of(new TextFrame(value)), ResultCode.OK, "CellularHandler", false);
    }

    private int valueOrZero(Integer value) {
        return valueOrDefault(value, 0);
    }

    private int valueOrDefault(Integer value, int fallback) {
        return value == null ? fallback : value;
    }

    private Integer parseNumber(String value) {
        try {
            return Integer.valueOf(value.trim());
        } catch (RuntimeException e) {
            return null;
        }
    }

    private String unquote(String value) {
        return value != null && value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")
                ? value.substring(1, value.length() - 1)
                : value;
    }
}
