package com.jkamsker.modemsim.testkit;

import org.junit.jupiter.api.Test;

import com.jkamsker.modemsim.app.ModemSimCli;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class AcceptanceSuiteTest {
    private static final Path ACCEPTANCE_SPEC = Path.of(
            "docs/Tasks/Initial-Spec/spec/13_teststrategie_abnahme.md");
    private static final Pattern CASE_ROW = Pattern.compile("^\\|\\s*A\\d{2}\\s*\\|.*");
    private static final Pattern COMMAND_SNIPPET = Pattern.compile("`([^`]+)`");

    @Test
    void allAcceptanceCasesPass() {
        AcceptanceSuite suite = new AcceptanceSuite();

        for (String caseId : suite.caseIds()) {
            AcceptanceResult result = suite.run(caseId);

            assertThat(result.passed())
                    .as("%s failed: %s", caseId, result.message())
                    .isTrue();
        }
    }

    @Test
    void suiteCaseIdsMatchSpecAcceptanceMatrix() throws IOException {
        assertThat(new AcceptanceSuite().caseIds()).containsExactlyElementsOf(specCaseIds());
    }

    @Test
    void documentedCommandsRouteToSpecAcceptanceCases() throws IOException {
        AcceptanceSuite suite = new AcceptanceSuite();
        List<SpecCommand> commands = specCommands();

        assertThat(commands).isNotEmpty();
        for (SpecCommand command : commands) {
            String actualCase = routedCase(command.value(), suite);

            assertThat(actualCase)
                    .as("documented command routes to the wrong case: %s", command.value())
                    .isEqualTo(command.caseId());
            assertDocumentedCommandExecutable(command.value());
        }
    }

    @Test
    void allGoldenYamlFixturesConformAndReplay() {
        new GoldenTranscriptAcceptance().run();
    }

    private static List<String> specCaseIds() throws IOException {
        return caseRows().stream()
                .map(row -> row.cells().getFirst())
                .toList();
    }

    private static List<SpecCommand> specCommands() throws IOException {
        return caseRows().stream()
                .flatMap(row -> COMMAND_SNIPPET.matcher(row.cells().getLast()).results()
                        .map(match -> match.group(1))
                        .filter(command -> command.startsWith("modemsim "))
                        .map(command -> new SpecCommand(row.cells().getFirst(), command)))
                .toList();
    }

    private static List<SpecRow> caseRows() throws IOException {
        return Files.readAllLines(ACCEPTANCE_SPEC).stream()
                .filter(line -> CASE_ROW.matcher(line).matches())
                .map(AcceptanceSuiteTest::row)
                .toList();
    }

    private static SpecRow row(String line) {
        List<String> cells = tableCells(line);

        assertThat(cells).as("acceptance matrix row should keep the documented table shape").hasSize(5);
        return new SpecRow(cells);
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

        assertThat(cells).as("acceptance matrix row should start and end with pipes")
                .startsWith("")
                .endsWith("");
        return cells.subList(1, cells.size() - 1);
    }

    private static String routedCase(String command, AcceptanceSuite suite) {
        if (command.startsWith("modemsim coverage verify ")) {
            return "A16";
        }
        if (command.startsWith("modemsim replay ")) {
            return "A18";
        }
        Optional<String> caseArg = argument(command, "--case");
        if (caseArg.isPresent()) {
            return AcceptanceCaseRegistry.normalize(caseArg.get());
        }

        String suiteArg = argument(command, "--suite").orElseThrow(
                () -> new AssertionError("documented test command has no --suite or --case: " + command));
        return suite.defaultCaseForSuite(suiteArg);
    }

    private static void assertDocumentedCommandExecutable(String command) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        int exit = new ModemSimCli.ModemSimCliRunner(new PrintStream(out), new PrintStream(err))
                .run(args(command));

        assertThat(exit)
                .as("documented command failed: %s%nstdout:%s%nstderr:%s", command, out, err)
                .isZero();
    }

    private static String[] args(String command) {
        return command.substring("modemsim ".length()).split("\\s+");
    }

    private static Optional<String> argument(String command, String name) {
        String[] parts = command.split("\\s+");
        for (int i = 0; i < parts.length - 1; i++) {
            if (parts[i].equals(name)) {
                return Optional.of(parts[i + 1]);
            }
        }
        return Optional.empty();
    }

    private record SpecRow(List<String> cells) {
    }

    private record SpecCommand(String caseId, String value) {
    }
}
