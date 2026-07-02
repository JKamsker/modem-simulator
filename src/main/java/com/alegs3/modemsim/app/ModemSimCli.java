package com.alegs3.modemsim.app;

import com.alegs3.modemsim.macros.MacroLoader;
import com.alegs3.modemsim.profiles.ProfileXmlLoader;
import com.alegs3.modemsim.transport.PortDiscovery;
import com.alegs3.modemsim.validation.ConfigValidator;
import com.alegs3.modemsim.validation.CoverageValidator;
import com.alegs3.modemsim.validation.ScenarioValidator;
import com.alegs3.modemsim.validation.ValidationReport;

import java.nio.file.Path;
import java.io.PrintStream;
import java.util.Arrays;

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
                case "validate-profile" -> report(new ProfileXmlLoader().validate(pathArg(args, 1)));
                case "validate-macros" -> report(new MacroLoader().validate(pathArg(args, 1)));
                case "validate-scenario" -> report(new ScenarioValidator().validate(pathArg(args, 1)));
                case "validate-config" -> report(new ConfigValidator().validate(pathArg(args, 1)));
                case "coverage" -> coverage(args);
                case "list-ports" -> listPorts();
                default -> {
                    usage();
                    yield 2;
                }
            };
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

        private void usage() {
            err.println("""
                    Usage:
                      modemsim validate-profile <profile.xml>
                      modemsim validate-macros <macros.xml>
                      modemsim validate-scenario <scenario.xml>
                      modemsim validate-config <config.yaml>
                      modemsim coverage verify --profiles v1-targets
                      modemsim list-ports
                    """);
        }
    }
}
