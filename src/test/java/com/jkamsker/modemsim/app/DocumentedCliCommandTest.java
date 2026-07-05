package com.jkamsker.modemsim.app;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentedCliCommandTest {
    private static final Path ACCEPTANCE_SPEC = Path.of(
            "docs/Tasks/Initial-Spec/spec/13_teststrategie_abnahme.md");
    private static final Pattern CASE_ROW = Pattern.compile("^\\|\\s*A\\d{2}\\s*\\|.*");
    private static final Pattern COMMAND_SNIPPET = Pattern.compile("`([^`]+)`");

    @Test
    void documentedAcceptanceMatrixCommandsExecuteThroughCliRunner() throws Exception {
        List<String> commands = documentedCommands();

        assertThat(commands).isNotEmpty();
        for (String command : commands) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayOutputStream err = new ByteArrayOutputStream();
            int exit = new ModemSimCli.ModemSimCliRunner(new PrintStream(out), new PrintStream(err))
                    .run(args(command));

            assertThat(exit)
                    .as("documented command failed: %s%nstdout:%s%nstderr:%s", command, out, err)
                    .isZero();
        }
    }

    private List<String> documentedCommands() throws Exception {
        return Files.readAllLines(ACCEPTANCE_SPEC).stream()
                .filter(line -> CASE_ROW.matcher(line).matches())
                .flatMap(row -> COMMAND_SNIPPET.matcher(tableCells(row).getLast()).results())
                .map(match -> match.group(1))
                .filter(command -> command.startsWith("modemsim "))
                .toList();
    }

    private String[] args(String command) {
        return command.substring("modemsim ".length()).split("\\s+");
    }

    private static List<String> tableCells(String line) {
        List<String> cells = new ArrayList<>();
        StringBuilder cell = new StringBuilder();
        boolean inCode = false;
        for (int i = 0; i < line.length(); i++) {
            char value = line.charAt(i);
            if (value == '`') {
                inCode = !inCode;
                cell.append(value);
            } else if (value == '|' && !inCode) {
                cells.add(cell.toString().trim());
                cell.setLength(0);
            } else {
                cell.append(value);
            }
        }
        cells.add(cell.toString().trim());
        return cells.subList(1, cells.size() - 1);
    }
}
