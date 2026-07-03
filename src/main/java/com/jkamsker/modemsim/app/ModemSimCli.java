package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.macros.MacroLoader;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.ProfileXmlLoader;
import com.jkamsker.modemsim.transport.PortDiscovery;
import com.jkamsker.modemsim.testkit.AcceptanceResult;
import com.jkamsker.modemsim.testkit.AcceptanceSuite;
import com.jkamsker.modemsim.validation.ConfigValidator;
import com.jkamsker.modemsim.validation.CoverageValidator;
import com.jkamsker.modemsim.validation.ScenarioValidator;
import com.jkamsker.modemsim.validation.ValidationReport;

import java.nio.file.Path;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ModemSimCli {
    private ModemSimCli() {
    }

    public static void main(String[] args) {
        int exit = new ModemSimCliRunner().run(args);
        if (exit != 0) {
            System.exit(exit);
        }
    }

    static final class ModemSimCliRunner {
        private final PrintStream out;
        private final PrintStream err;

        ModemSimCliRunner() {
            this(System.out, System.err);
        }

        ModemSimCliRunner(PrintStream out, PrintStream err) {
            this.out = out;
            this.err = err;
        }

        int run(String[] args) {
            if (args.length == 0) {
                usage();
                return 2;
            }
            try {
                return dispatch(args);
            } catch (RuntimeException e) {
                err.println(e.getMessage());
                return 1;
            }
        }

        private int dispatch(String[] args) {
            return switch (args[0]) {
                case "run" -> runRuntime(args);
                case "validate-profile" -> report(new ProfileXmlLoader().validate(pathArg(args, 1)));
                case "validate-macros" -> report(new MacroLoader().validate(pathArg(args, 1)));
                case "validate-scenario" -> report(new ScenarioValidator().validate(pathArg(args, 1)));
                case "validate-config" -> report(new ConfigValidator().validate(pathArg(args, 1)));
                case "coverage" -> coverage(args);
                case "test" -> test(args);
                case "list-ports" -> listPorts();
                default -> {
                    usage();
                    yield 2;
                }
            };
        }

        private int runRuntime(String[] args) {
            Path configPath = pathOption(args, "--config");
            RuntimeConfig config = new RuntimeConfigLoader().load(configPath);
            RuntimeResult result = new ModemRuntime().run(config, runInputs(args), intOption(args, "--max-reads", -1));
            out.println("RUN " + result.sessionId()
                    + " reads=" + result.readsProcessed()
                    + " outputHex=" + result.output().toHex());
            return 0;
        }

        private int coverage(String[] args) {
            if (args.length >= 3 && args[1].equals("verify") && args[2].equals("--profiles")) {
                Path dir = args.length >= 4 && !args[3].equals("v1-targets")
                        ? Path.of(args[3])
                        : Path.of("src/main/resources/coverage/v1-targets");
                return report(new CoverageValidator().verifyV1Targets(dir));
            }
            usage();
            return 2;
        }

        private int test(String[] args) {
            String caseId = option(args, "--case", "all");
            AcceptanceSuite suite = new AcceptanceSuite();
            var results = caseId.equals("all")
                    ? suite.caseIds().stream().map(suite::run).toList()
                    : java.util.List.of(suite.run(caseId));
            results.forEach(result -> out.println(result.caseId() + " " + (result.passed() ? "OK" : result.message())));
            return results.stream().allMatch(AcceptanceResult::passed) ? 0 : 1;
        }

        private int listPorts() {
            new PortDiscovery().listSystemPorts().forEach(out::println);
            return 0;
        }

        private int report(ValidationReport report) {
            if (report.valid()) {
                out.println("OK");
                report.warnings().forEach(warning -> out.println("WARN: " + warning));
                return 0;
            }
            report.errors().forEach(error -> err.println("ERROR: " + error));
            return 1;
        }

        private Path pathArg(String[] args, int index) {
            if (args.length <= index) {
                throw new IllegalArgumentException("Missing path argument: " + Arrays.toString(args));
            }
            return Path.of(args[index]);
        }

        private Path pathOption(String[] args, String name) {
            String value = option(args, name, null);
            if (value == null) {
                throw new IllegalArgumentException("Missing option: " + name);
            }
            return Path.of(value);
        }

        private int intOption(String[] args, String name, int fallback) {
            String value = option(args, name, null);
            return value == null ? fallback : Integer.parseInt(value);
        }

        private List<RawBytes> runInputs(String[] args) {
            List<RawBytes> inputs = new ArrayList<>();
            for (int i = 0; i < args.length - 1; i++) {
                if (args[i].equals("--input-hex")) {
                    inputs.add(RawBytes.hex(args[++i]));
                } else if (args[i].equals("--input-ascii")) {
                    inputs.add(RawBytes.ascii(unescape(args[++i])));
                }
            }
            return inputs;
        }

        private String unescape(String value) {
            return value.replace("\\r", "\r")
                    .replace("\\n", "\n")
                    .replace("\\t", "\t")
                    .replace("\\u001A", "\u001A");
        }

        private String option(String[] args, String name, String fallback) {
            for (int i = 0; i < args.length - 1; i++) {
                if (args[i].equals(name)) {
                    return args[i + 1];
                }
            }
            return fallback;
        }

        private void usage() {
            err.println("""
                    Usage:
                      modemsim run --config config.yaml
                      modemsim validate-profile <profile.xml>
                      modemsim validate-macros <macros.xml>
                      modemsim validate-scenario <scenario.xml>
                      modemsim validate-config <config.yaml>
                      modemsim coverage verify --profiles v1-targets
                      modemsim test --suite acceptance --case A01
                      modemsim list-ports
                    """);
        }
    }
}
