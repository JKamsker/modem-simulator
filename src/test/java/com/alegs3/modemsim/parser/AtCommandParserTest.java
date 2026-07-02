package com.alegs3.modemsim.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AtCommandParserTest {
    private final AtCommandParser parser = new AtCommandParser(8);

    @Test
    void parsesBasicCommandChain() {
        var commands = parser.parse(RawBytes.ascii("ATE0V1Q0\r"), EntryMode.COMMAND);

        assertThat(commands)
                .extracting(ParsedCommand::normalizedName)
                .containsExactly("ATE", "ATV", "ATQ");
        assertThat(commands).extracting(ParsedCommand::arguments).containsExactly("0", "1", "0");
    }

    @Test
    void parsesExtendedChainWithoutSplittingQuotedSemicolon() {
        var commands = parser.parse(RawBytes.ascii("AT+CMEE=2;+X=\"a;b\";+CSQ\r"), EntryMode.COMMAND);

        assertThat(commands)
                .extracting(ParsedCommand::normalizedName)
                .containsExactly("+CMEE", "+X", "+CSQ");
        assertThat(commands.get(1).arguments()).isEqualTo("\"a;b\"");
        assertThat(commands.get(2).kind()).isEqualTo(CommandKind.EXTENDED_EXEC);
    }

    @Test
    void parsesSRegisterForms() {
        var commands = parser.parse(RawBytes.ascii("ATS7=60S12?\r"), EntryMode.COMMAND);

        assertThat(commands).extracting(ParsedCommand::normalizedName).containsExactly("S7", "S12");
        assertThat(commands).extracting(ParsedCommand::kind)
                .containsExactly(CommandKind.S_REGISTER_WRITE, CommandKind.S_REGISTER_READ);
    }

    @Test
    void appliesBackspaceBeforeTokenizing() {
        var commands = parser.parse(RawBytes.copyOf(new byte[] {'A', 'T', 'X', 8, 'I', '\r'}), EntryMode.COMMAND);

        assertThat(commands).extracting(ParsedCommand::normalizedName).containsExactly("ATI");
    }
}
