package com.jkamsker.modemsim.commands;

import com.jkamsker.modemsim.parser.ParsedCommand;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.ModemState;
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
            default -> null;
        };
    }

    private CommandResult cmgf(ModemState state, ParsedCommand command) {
        return switch (command.kind()) {
            case EXTENDED_SET -> setCmgf(state, command.arguments());
            case EXTENDED_READ -> line(state, "+CMGF: " + (state.sms().textMode() ? 1 : 0));
            case EXTENDED_TEST -> line(state, "+CMGF: (0,1)");
            default -> CommandResult.error(state, "SmsHandler");
        };
    }

    private CommandResult setCmgf(ModemState state, String arguments) {
        if (!arguments.equals("0") && !arguments.equals("1")) {
            return CommandResult.error(state, "SmsHandler");
        }
        return CommandResult.ok(state.withSms(state.sms().withTextMode(arguments.equals("1"))), "SmsHandler");
    }

    private CommandResult cmgs(ModemState state, ParsedCommand command) {
        if (command.kind().name().endsWith("SET")) {
            CallMode mode = state.sms().textMode() ? CallMode.SMS_TEXT_ENTRY : CallMode.SMS_PDU_ENTRY;
            ModemState entry = state.withCall(state.call().withMode(mode));
            return new CommandResult(
                    entry,
                    List.of(new PromptFrame(new ResponseFormatter(state).prompt())),
                    null,
                    "SmsHandler",
                    true);
        }
        return CommandResult.error(state, "SmsHandler");
    }

    private CommandResult cmgr(ModemState state, ParsedCommand command) {
        SmsMessage message = state.sms().messages().get(parseInt(command.arguments(), -1));
        if (message == null) {
            return CmsError.INVALID_INDEX.result(state, "SmsHandler");
        }
        return new CommandResult(state, messageFrames("+CMGR", message), ResultCode.OK, "SmsHandler", false);
    }

    private CommandResult cmgl(ModemState state, ParsedCommand command) {
        List<ResponseFrame> frames = new ArrayList<>();
        for (SmsMessage message : state.sms().messages().values()) {
            frames.addAll(messageFrames("+CMGL: " + message.index(), message));
        }
        return new CommandResult(state, frames, ResultCode.OK, "SmsHandler", false);
    }

    private CommandResult cmgd(ModemState state, ParsedCommand command) {
        int index = parseInt(command.arguments(), -1);
        if (!state.sms().messages().containsKey(index)) {
            return CmsError.INVALID_INDEX.result(state, "SmsHandler");
        }
        return CommandResult.ok(state.withSms(state.sms().delete(index)), "SmsHandler");
    }

    private CommandResult cnmi(ModemState state, ParsedCommand command) {
        return switch (command.kind()) {
            case EXTENDED_SET -> validCnmi(command.arguments())
                    ? CommandResult.ok(state.withSms(state.sms().withCnmi(command.arguments())), "SmsHandler")
                    : CommandResult.error(state, "SmsHandler");
            case EXTENDED_READ -> line(state, "+CNMI: " + state.sms().cnmi());
            case EXTENDED_TEST -> line(state, "+CNMI: (0-3),(0-3),(0,2),(0,1),(0,1)");
            default -> CommandResult.ok(state, "SmsHandler");
        };
    }

    private CommandResult cpms(ModemState state, ParsedCommand command) {
        if (command.kind().name().endsWith("READ")) {
            int used = state.sms().messages().size();
            String store = state.sms().storage().name();
            return line(state, "+CPMS: \"" + store + "\"," + used + ",50,\"" + store + "\"," + used + ",50");
        }
        if (command.kind().name().endsWith("SET")) {
            String value = unquote(command.arguments().split(",")[0]);
            try {
                return CommandResult.ok(state.withSms(state.sms().withStorage(SmsStorage.valueOf(value))), "SmsHandler");
            } catch (IllegalArgumentException e) {
                return CommandResult.error(state, "SmsHandler");
            }
        }
        return line(state, "+CPMS: (\"ME\",\"SM\",\"MT\"),(\"ME\",\"SM\",\"MT\")");
    }

    private CommandResult csca(ModemState state, ParsedCommand command) {
        if (command.kind().name().endsWith("READ")) {
            return line(state, "+CSCA: \"" + state.sms().smsc() + "\",145");
        }
        if (command.kind().name().endsWith("SET")) {
            return CommandResult.ok(state.withSms(state.sms().withSmsc(unquote(command.arguments()))), "SmsHandler");
        }
        return CommandResult.error(state, "SmsHandler");
    }

    private List<ResponseFrame> messageFrames(String prefix, SmsMessage message) {
        String text = message.text() == null ? "" : message.text();
        return List.of(
                new TextFrame(prefix + ": \"" + message.status() + "\",\"" + message.recipient() + "\""),
                new TextFrame(text));
    }

    private CommandResult line(ModemState state, String value) {
        return new CommandResult(state, List.of(new TextFrame(value)), ResultCode.OK, "SmsHandler", false);
    }

    private int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value.replace("\"", ""));
        } catch (RuntimeException e) {
            return fallback;
        }
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

    private String unquote(String value) {
        String trimmed = value == null ? "" : value.trim();
        return trimmed.length() >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")
                ? trimmed.substring(1, trimmed.length() - 1)
                : trimmed;
    }
}
