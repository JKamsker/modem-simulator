package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.EventRedactor;
import com.jkamsker.modemsim.parser.AtToken;
import com.jkamsker.modemsim.parser.CommandSpan;
import com.jkamsker.modemsim.parser.ParsedCommand;

import java.util.LinkedHashMap;
import java.util.Map;

final class ParsedCommandEventData {
    private ParsedCommandEventData() {
    }

    static Map<String, Object> from(ParsedCommand command, EventRedactor redactor) {
        if (command == null) {
            return null;
        }
        Map<String, Object> data = new LinkedHashMap<>();
        String arguments = redactor.redactCommandArguments(command);
        String rawText = redactor.redactCommandRawText(command);
        data.put("name", command.normalizedName());
        data.put("kind", command.kind().name());
        data.put("arguments", arguments);
        data.put("rawText", rawText);
        data.put("rawStartOffset", command.rawStartOffset());
        data.put("rawEndOffset", command.rawEndOffset());
        data.put("span", spanData(command.span()));
        data.put("typedTokens", command.typedTokens().stream()
                .map(token -> tokenData(token, rawText, arguments))
                .toList());
        data.put("quoted", command.quoted());
        data.put("pduContext", command.pduContext());
        data.put("entryMode", command.entryMode().name());
        return data;
    }

    private static Map<String, Object> tokenData(AtToken token, String rawText, String arguments) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("type", token.type());
        data.put("text", tokenText(token, rawText, arguments));
        data.put("span", spanData(token.span()));
        data.put("quoted", token.quoted());
        return data;
    }

    private static String tokenText(AtToken token, String rawText, String arguments) {
        if ("<redacted>".equals(rawText) || ("arguments".equals(token.type()) && "<redacted>".equals(arguments))) {
            return "<redacted>";
        }
        return token.text();
    }

    private static Map<String, Object> spanData(CommandSpan span) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("startOffset", span.startOffset());
        data.put("endOffset", span.endOffset());
        return data;
    }
}
