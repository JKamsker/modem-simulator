package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.parser.CommandKind;
import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SmsMessage;
import com.jkamsker.modemsim.state.SmsStorage;

import java.util.ArrayList;
import java.util.List;

public final class SmsHandler implements CommandHandler {
    @Override
    public CommandResult handle(Profile profile, ModemState state, ParsedCommand command) {
        return switch (command.normalizedName()) {
            case "+CMGF" -> cmgf(state, command);
            case "+CMGS" -> cmgs(state, command);
            case "+CMGR" -> cmgr(state, command);
            case "+CMGL" -> cmgl(state, command);
            case "+CMGD" -> cmgd(state, command);
            case "+CNMI" -> cnmi(state, command);
            case "+CPMS" -> cpms(state, command);
            case "+CSCA" -> csca(state, command);
            case "+CSCS" -> cscs(state, command);
            default -> null;
        };
    }

    private CommandResult cmgf(ModemState state, ParsedCommand command) {
        return switch (command.kind()) {
            case EXTENDED_SET -> setCmgf(state, command.arguments());
            case EXTENDED_READ -> line(state, "+CMGF: " + (state.sms().textMode() ? 1 : 0));
            case EXTENDED_TEST -> line(state, "+CMGF: (0,1)");
            default -> CommandResult.invalidParameter(state, "SmsHandler");
        };
    }

    private CommandResult setCmgf(ModemState state, String arguments) {
        if (!arguments.equals("0") && !arguments.equals("1")) {
            return CommandResult.invalidParameter(state, "SmsHandler");
        }
        return CommandResult.ok(state.withSms(state.sms().withTextMode(arguments.equals("1"))), "SmsHandler");
    }

    private CommandResult cmgs(ModemState state, ParsedCommand command) {
        return switch (command.kind()) {
            case EXTENDED_SET -> cmgsSet(state, command.arguments());
            default -> CommandResult.invalidParameter(state, "SmsHandler");
        };
    }

    private CommandResult cmgsSet(ModemState state, String arguments) {
        if (!validCmgsArguments(state, arguments)) {
            return CommandResult.invalidParameter(state, "SmsHandler");
        }
        CallMode mode = state.sms().textMode() ? CallMode.SMS_TEXT_ENTRY : CallMode.SMS_PDU_ENTRY;
        ModemState entry = state.withCall(state.call().withMode(mode));
        return new CommandResult(
                entry,
                List.of(new PromptFrame(new ResponseFormatter(state).prompt())),
                null,
                "SmsHandler",
                true);
    }

    private CommandResult cmgr(ModemState state, ParsedCommand command) {
        Integer index = parseIndex(command.arguments());
        if (index == null) {
            return CommandResult.invalidParameter(state, "SmsHandler");
        }
        SmsMessage message = state.sms().messagesInSelectedStorage().get(index);
        if (message == null) {
            return CmsError.INVALID_INDEX.result(state, "SmsHandler");
        }
        return new CommandResult(state, messageFrames("+CMGR", message, false), ResultCode.OK, "SmsHandler", false);
    }

    private CommandResult cmgl(ModemState state, ParsedCommand command) {
        if (command.kind() == CommandKind.EXTENDED_TEST) {
            return line(state, "+CMGL: (\"REC UNREAD\",\"REC READ\",\"STO UNSENT\",\"STO SENT\",\"ALL\")");
        }
        List<ResponseFrame> frames = new ArrayList<>();
        String stat = unquote(command.arguments());
        if (stat.isBlank()) {
            stat = "ALL";
        }
        if (!validCmglStat(stat)) {
            return CommandResult.invalidParameter(state, "SmsHandler");
        }
        for (SmsMessage message : state.sms().messagesInSelectedStorage().values()) {
            if (stat.equalsIgnoreCase("ALL") || message.status().equalsIgnoreCase(stat)) {
                frames.addAll(messageFrames("+CMGL", message, true));
            }
        }
        return new CommandResult(state, frames, ResultCode.OK, "SmsHandler", false);
    }

    private CommandResult cmgd(ModemState state, ParsedCommand command) {
        Integer index = parseIndex(command.arguments());
        if (index == null) {
            return CommandResult.invalidParameter(state, "SmsHandler");
        }
        if (!state.sms().messagesInSelectedStorage().containsKey(index)) {
            return CmsError.INVALID_INDEX.result(state, "SmsHandler");
        }
        return CommandResult.ok(state.withSms(state.sms().delete(index)), "SmsHandler");
    }

    private CommandResult cnmi(ModemState state, ParsedCommand command) {
        return switch (command.kind()) {
            case EXTENDED_SET -> validCnmi(command.arguments())
                    ? CommandResult.ok(state.withSms(state.sms().withCnmi(command.arguments())), "SmsHandler")
                    : CommandResult.invalidParameter(state, "SmsHandler");
            case EXTENDED_READ -> line(state, "+CNMI: " + state.sms().cnmi());
            case EXTENDED_TEST -> line(state, "+CNMI: (0-3),(0-3),(0,2),(0,1),(0,1)");
            default -> CommandResult.ok(state, "SmsHandler");
        };
    }

    private CommandResult cpms(ModemState state, ParsedCommand command) {
        return switch (command.kind()) {
            case EXTENDED_READ -> line(state, "+CPMS: " + memoryTuple(state, state.sms().storage().name()) + ","
                    + memoryTuple(state, state.sms().writeStorage().name()) + ","
                    + memoryTuple(state, state.sms().receiveStorage().name()));
            case EXTENDED_SET -> cpmsSet(state, command.arguments());
            default -> line(state, "+CPMS: (\"ME\",\"SM\",\"MT\"),(\"ME\",\"SM\",\"MT\"),(\"ME\",\"SM\",\"MT\")");
        };
    }

    private CommandResult cpmsSet(ModemState state, String arguments) {
        String[] stores = arguments.split(",", -1);
        SmsStorage[] selected = new SmsStorage[3];
        for (String store : stores) {
            if (!validStorage(unquote(store))) {
                return CommandResult.invalidParameter(state, "SmsHandler");
            }
        }
        try {
            selected[0] = SmsStorage.valueOf(unquote(stores[0]));
            selected[1] = stores.length > 1 ? SmsStorage.valueOf(unquote(stores[1])) : selected[0];
            selected[2] = stores.length > 2 ? SmsStorage.valueOf(unquote(stores[2])) : selected[1];
            return CommandResult.ok(state.withSms(state.sms().withStorages(selected[0], selected[1], selected[2])),
                    "SmsHandler");
        } catch (IllegalArgumentException e) {
            return CommandResult.invalidParameter(state, "SmsHandler");
        }
    }

    private String memoryTuple(ModemState state, String store) {
        SmsStorage storage = SmsStorage.valueOf(store);
        return "\"" + store + "\"," + state.sms().used(storage) + "," + state.sms().capacity(storage);
    }

    private CommandResult csca(ModemState state, ParsedCommand command) {
        return switch (command.kind()) {
            case EXTENDED_READ -> line(state, "+CSCA: \"" + state.sms().smsc() + "\",145");
            case EXTENDED_SET -> {
                String smsc = validSmsc(command.arguments());
                yield smsc == null ? CommandResult.invalidParameter(state, "SmsHandler")
                        : CommandResult.ok(state.withSms(state.sms().withSmsc(smsc)), "SmsHandler");
            }
            default -> CommandResult.invalidParameter(state, "SmsHandler");
        };
    }

    private CommandResult cscs(ModemState state, ParsedCommand command) {
        return switch (command.kind()) {
            case EXTENDED_READ -> line(state, "+CSCS: \"GSM\"");
            case EXTENDED_TEST -> line(state, "+CSCS: (\"GSM\",\"IRA\",\"UCS2\")");
            case EXTENDED_SET -> CommandResult.ok(state, "SmsHandler");
            default -> CommandResult.invalidParameter(state, "SmsHandler");
        };
    }

    private boolean validCmgsArguments(ModemState state, String arguments) {
        if (state.sms().textMode()) {
            String destination = unquote(arguments);
            return arguments.trim().startsWith("\"") && destination.matches("\\+?[0-9]{3,20}");
        }
        String pduLength = arguments.trim();
        return pduLength.matches("[0-9]+") && parseInt(pduLength, -1) >= 0;
    }

    private List<ResponseFrame> messageFrames(String prefix, SmsMessage message, boolean list) {
        String header = list ? prefix + ": " + message.index() + "," : prefix + ": ";
        if (message.pdu() != null) {
            return List.of(
                    new TextFrame(header + "\"" + message.status() + "\",," + (message.pdu().length() / 2)),
                    new TextFrame(message.pdu()));
        }
        String text = message.text() == null ? "" : message.text();
        String address = message.sender() == null ? message.recipient() : message.sender();
        return List.of(
                new TextFrame(header + "\"" + message.status() + "\",\"" + (address == null ? "" : address) + "\""),
                new TextFrame(text));
    }

    private CommandResult line(ModemState state, String value) {
        return new CommandResult(state, List.of(new TextFrame(value)), ResultCode.OK, "SmsHandler", false);
    }

    private int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value.trim());
        } catch (RuntimeException e) {
            return fallback;
        }
    }

    private Integer parseIndex(String value) {
        String trimmed = value == null ? "" : value.trim();
        int parsed = trimmed.matches("[0-9]+") ? parseInt(trimmed, -1) : -1;
        return parsed >= 0 ? parsed : null;
    }

    private boolean validCnmi(String arguments) {
        String[] parts = arguments.split(",");
        if (parts.length != 5) {
            return false;
        }
        int[] max = {3, 3, 2, 1, 1};
        for (int i = 0; i < parts.length; i++) {
            int value = parseInt(parts[i].trim(), -1);
            if (value < 0 || value > max[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean validCmglStat(String stat) {
        return List.of("REC UNREAD", "REC READ", "STO UNSENT", "STO SENT", "ALL")
                .stream().anyMatch(value -> value.equalsIgnoreCase(stat));
    }

    private boolean validStorage(String value) {
        try {
            SmsStorage.valueOf(value);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private String validSmsc(String arguments) {
        String[] parts = (arguments == null ? "" : arguments).split(",", 2);
        String first = parts[0].trim();
        String number = unquote(first);
        if (!first.startsWith("\"") || !first.endsWith("\"") || !number.matches("\\+?[0-9]{3,20}")) {
            return null;
        }
        return parts.length == 1 || parseInt(parts[1].trim(), -1) >= 0 ? number : null;
    }

    private String unquote(String value) {
        String trimmed = value == null ? "" : value.trim();
        return trimmed.length() >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")
                ? trimmed.substring(1, trimmed.length() - 1)
                : trimmed;
    }
}
