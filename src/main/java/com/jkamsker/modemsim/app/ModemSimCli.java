package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.macros.MacroLoader;
import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.profiles.ProfileXmlLoader;
import com.jkamsker.modemsim.replay.ReplayReport;
import com.jkamsker.modemsim.replay.ReplayStepLoader;
import com.jkamsker.modemsim.replay.ReplayValidator;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.transport.PortDiscovery;
import com.jkamsker.modemsim.scheduler.ClockMode;
import com.jkamsker.modemsim.testkit.AcceptanceResult;
import com.jkamsker.modemsim.testkit.AcceptanceSuite;
import com.jkamsker.modemsim.validation.ConfigValidator;
import com.jkamsker.modemsim.validation.CoverageValidator;
import com.jkamsker.modemsim.validation.SchemaLocator;
import com.jkamsker.modemsim.validation.ScenarioValidator;
import com.jkamsker.modemsim.validation.ValidationReport;
import com.jkamsker.modemsim.transport.FlowControl;
import com.jkamsker.modemsim.transport.Parity;
import com.jkamsker.modemsim.transport.SerialConfig;

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
        System.exit(exit);
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
                case "headless" -> headless(args);
                case "validate-profile" -> report(new ProfileXmlLoader().validate(pathArg(args, 1)));
                case "validate-macros" -> validateMacros(args);
                case "validate-scenario" -> report(new ScenarioValidator().validate(pathArg(args, 1)));
                case "validate-config" -> report(new ConfigValidator().validate(pathArg(args, 1)));
                case "coverage" -> coverage(args);
                case "test" -> test(args);
                case "replay" -> replay(args);
                case "list-ports" -> listPorts();
                default -> {
                    usage();
                    yield 2;
                }
            };
        }

        private int runRuntime(String[] args) {
            RuntimeConfig config = option(args, "--config", null) == null
                    ? directRuntimeConfig(args)
                    : new RuntimeConfigLoader().load(pathOption(args, "--config"));
            RuntimeResult result = new ModemRuntime().run(config, runInputs(args), intOption(args, "--max-reads", -1));
            out.println("RUN " + result.sessionId()
                    + " reads=" + result.readsProcessed()
                    + " outputHex=" + result.output().toHex()
                    + " log=" + result.eventLogPath());
            return 0;
        }

        private RuntimeConfig directRuntimeConfig(String[] args) {
            String port = option(args, "--port", null);
            if (port == null) {
                throw new IllegalArgumentException("Missing option: --config or --port");
            }
            EndpointType endpoint = EndpointType.fromConfig(option(args, "--endpoint", "serial"));
            String seedOption = option(args, "--seed", null);
            long seed = seedOption == null && endpoint != EndpointType.HEADLESS
                    ? new java.security.SecureRandom().nextLong(1, Long.MAX_VALUE)
                    : (seedOption == null ? 12345L : Long.parseLong(seedOption));
            return new RuntimeConfig(
                    seed,
                    endpoint == EndpointType.HEADLESS ? ClockMode.VIRTUAL : ClockMode.MONOTONIC,
                    false,
                    false,
                    null,
                    new SerialConfig(intOption(args, "--baud", 115200), 8, 1, Parity.NONE, FlowControl.NONE),
                    java.util.List.of(new PortBinding(
                            "modem", endpoint, PortRole.MODEM_SIMULATION,
                            endpoint == EndpointType.HEADLESS ? null : port, true,
                            option(args, "--profile", "sierra-hl6-hl8-v20"), "tagged-text")));
        }

        private int replay(String[] args) {
            return new ReplayCommand(out, err).run(args);
        }

        private int validateMacros(String[] args) {
            Path macros = pathArg(args, 1);
            String configPath = option(args, "--config", null);
            if (configPath != null) {
                RuntimeConfig config = new RuntimeConfigLoader().load(Path.of(configPath));
                var timerIds = config.macroTimers().stream()
                        .map(RuntimeTimer::id)
                        .collect(java.util.stream.Collectors.toSet());
                return report(new MacroLoader(timerIds, true).validate(macros));
            }
            return report(new MacroLoader(timerIdOptions(args), option(args, "--session-seed", null) != null)
                    .validate(macros));
        }

        private int headless(String[] args) {
            String profile = option(args, "--profile", "sierra-hl6-hl8-v20");
            Path script = pathOption(args, "--script");
            long seed = longOption(args, "--seed", 12345L);
            var steps = new ReplayStepLoader().load(script);
            HeadlessSession session = new HeadlessSession("headless", new ProfileResolver().resolve(profile), seed);
            ReplayReport report = new ReplayValidator().validateRecompute(session, steps);
            if (report.valid()) {
                out.println("HEADLESS OK steps=" + steps.size());
                return 0;
            }
            report.divergences().forEach(divergence -> err.println("DIVERGENCE: " + divergence));
            return 1;
        }

        private int coverage(String[] args) {
            if (args.length >= 3 && args[1].equals("verify") && args[2].equals("--profiles")) {
                Path dir = args.length >= 4 && !args[3].equals("v1-targets")
                        ? Path.of(args[3])
                        : SchemaLocator.projectPath("src/main/resources/coverage/v1-targets");
                return report(new CoverageValidator().verifyV1Targets(dir));
            }
            usage();
            return 2;
        }

        private int test(String[] args) {
            AcceptanceSuite suite = new AcceptanceSuite();
            String suiteName = option(args, "--suite", "acceptance");
            String tagName = option(args, "--tags", null);
            String defaultCase = tagName == null
                    ? suite.defaultCaseForSuite(suiteName)
                    : suite.defaultCaseForSuite(tagName);
            String caseId = option(args, "--case", defaultCase);
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

        private long longOption(String[] args, String name, long fallback) {
            String value = option(args, name, null);
            return value == null ? fallback : Long.parseLong(value);
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

        private java.util.Set<String> timerIdOptions(String[] args) {
            java.util.Set<String> values = new java.util.LinkedHashSet<>();
            for (int i = 0; i < args.length - 1; i++) {
                if (args[i].equals("--timer-id")) {
                    values.add(args[i + 1]);
                }
            }
            return values;
        }

        private void usage() {
            err.println("""
                    Usage:
                      modemsim run --config config.yaml
                      modemsim run --port COM7 --baud 115200 --profile sierra-hl6-hl8-v20
                      modemsim headless --profile sierra-hl6-hl8-v20 --script tests/transcript.jsonl
                      modemsim validate-profile <profile.xml>
                      modemsim validate-macros <macros.xml> [--config config.yaml|--timer-id id --session-seed seed]
                      modemsim validate-scenario <scenario.xml>
                      modemsim validate-config <config.yaml>
                      modemsim coverage verify --profiles v1-targets
                      modemsim test --suite acceptance --case A01
                      modemsim replay logs/session.jsonl --mode validate-recompute --profile sierra-hl6-hl8-v20
                      modemsim list-ports
                    """);
        }
    }
}
