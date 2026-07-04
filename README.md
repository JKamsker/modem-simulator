# Modem Simulator

Java 24 COM/serial modem simulator for deterministic AT-command testing, profile validation, and headless acceptance runs. The project implements the v1 surface from the Initial-Spec package in `docs/Tasks/Initial-Spec`.

The simulator is aimed at software that expects a Hayes, GSM, or vendor-specific modem on a serial port: embedded controllers, routers, PLCs, legacy desktop software, integration test rigs, and regression suites.

## What It Does

- Parses AT/Hayes command streams with configurable line endings, echo, quiet mode, verbose/numeric result codes, S-registers, `A/`, and `+++`.
- Implements core V.250 behavior such as `AT`, `ATE`, `ATQ`, `ATV`, `ATS`, `ATZ`, `AT&F`, `AT&W`, `AT&V`, dial/connect, escape, online-command mode, and hangup flows.
- Implements key 3GPP TS 27.007 cellular commands including `+CREG`, `+CSQ`, `+CPIN`, `+CMEE`, `+COPS`, and identity commands.
- Implements the 3GPP TS 27.005 SMS text-mode workflow around `+CMGF`, `+CMGS`, prompt handling, Ctrl-Z submit, ESC cancel, storage helpers, service center configuration, and deterministic CMS errors.
- Supports XML modem profiles, inherited profile metadata, XML macro rules, XML scenarios, YAML config validation, and JSON coverage reports.
- Provides deterministic headless sessions with virtual time, seeded delay sampling, event capture, replay validation/playback, and an in-memory serial endpoint for tests.
- Includes built-in acceptance profiles for Sierra-like cellular behavior and Westermo PSTN/GSM-family behavior.
- Enforces an 85% line-coverage gate and Java source size checks in `mvn verify`.

## Repository Layout

```text
docs/Tasks/Initial-Spec/      Source-of-record v1 specification, schemas, examples, references
src/main/java/com/jkamsker/   Java implementation
src/main/resources/coverage/  v1 target coverage declarations
src/test/java/com/jkamsker/   Unit, acceptance, validation, and branch coverage tests
src/test/resources/           Test profiles and config fixtures
scripts/check-code-size.sh    Java source-size guard used by Maven verify
.github/workflows/ci.yml      Ubuntu and Windows CI with Java 24
```

The Java package root is `com.jkamsker.modemsim`.

## Requirements

- JDK 24
- Git
- No local Maven installation is required; use the checked-in Maven wrapper.

Check your Java version:

```bash
java --version
```

On Windows, use `mvnw.cmd` in place of `./mvnw`.

## Quick Start

Build, test, validate coverage, and run the source-size guard:

```bash
./mvnw -B clean verify
```

Run the full Linux coverage lane, including GUI-tagged tests and JavaFX view classes:

```bash
xvfb-run -a ./mvnw -B -Pcoverage-all verify
```

Build the ZIP distribution:

```bash
./mvnw -B package
unzip -l target/modem-simulator-*-dist.zip
```

The distribution contains CLI launchers (`bin/modemsim`, `bin/modemsim.cmd`) and GUI launchers (`bin/modemsim-gui`, `bin/modemsim-gui.cmd`). All launchers use `--enable-native-access=ALL-UNNAMED`, which is required for classpath use of jSerialComm on Java 24.

## Installed ZIP Quick Start

After unpacking a release ZIP, run the launchers directly from the extracted directory:

```bash
unzip modem-simulator-*-dist.zip -d modem-simulator
cd modem-simulator/modem-simulator-*
bin/modemsim test --suite acceptance --case A01
bin/modemsim validate-config src/test/resources/config/valid.yaml
bin/modemsim validate-profile docs/Tasks/Initial-Spec/examples/modem-profile.sample.xml
bin/modemsim coverage verify --profiles v1-targets
bin/modemsim replay src/test/resources/replay/basic-at-events.jsonl --mode validate-recompute
```

On Windows, use the `.cmd` launchers:

```bat
bin\modemsim.cmd test --suite acceptance --case A01
bin\modemsim-gui.cmd
```

The ZIP is self-contained for runtime dependencies. You still need JDK 24 on `PATH` or `JAVA_HOME`, and serial access depends on OS permissions and any virtual COM-pair driver you use.

Run the full acceptance suite:

```bash
./mvnw -q exec:java -Dexec.args="test --suite acceptance --case all"
```

Run one acceptance case:

```bash
./mvnw -q exec:java -Dexec.args="test --suite acceptance --case A01"
./mvnw -q exec:java -Dexec.args="test --suite cellular --case creg"
```

Validate the sample modem profile:

```bash
./mvnw -q exec:java -Dexec.args="validate-profile docs/Tasks/Initial-Spec/examples/modem-profile.sample.xml"
```

Verify all v1 target coverage declarations:

```bash
./mvnw -q exec:java -Dexec.args="coverage verify --profiles v1-targets"
```

List visible serial ports:

```bash
./mvnw -q exec:java -Dexec.args="list-ports"
```

## CLI Reference

The Maven `exec` plugin runs `com.jkamsker.modemsim.app.ModemSimCli`.

```text
run --config <config.yaml> [--input-ascii <text>|--input-hex <hex>] [--max-reads <n>]
run --port <port> --baud <rate> --profile <profile-id|profile.xml> [--endpoint serial|headless]
validate-profile <profile.xml>
validate-macros <macros.xml>
validate-scenario <scenario.xml>
validate-config <config.yaml>
coverage verify --profiles v1-targets
test --suite acceptance --case <case-id|all>
test --tags <tag> --case <alias>
replay <jsonl|yaml-log|trm> --mode validate-recompute [--profile <profile-id|profile.xml>] [--seed <long>]
replay <jsonl|trm> --mode drive-from-captured-input [--profile <profile-id|profile.xml>] [--seed <long>] [--confirm-divergence]
replay <jsonl|trm> --mode play-to-dte [--endpoint serial|headless] [--port <port>] [--baud <rate>] [--timing none|recorded] [--confirm-divergence]
list-ports
headless --profile <profile-id|profile.xml> --script <transcript.jsonl|transcript.yaml> [--seed <long>]
```

Useful examples:

```bash
./mvnw -q exec:java -Dexec.args="validate-macros docs/Tasks/Initial-Spec/examples/macros.sms-error-123.xml"
./mvnw -q exec:java -Dexec.args="validate-scenario docs/Tasks/Initial-Spec/examples/scenario.no-network.xml"
./mvnw -q exec:java -Dexec.args="validate-config src/test/resources/config/valid.yaml"
./mvnw -q exec:java -Dexec.args="test --suite acceptance --case A09"
./mvnw -q exec:java -Dexec.args="test --tags gui --case live-log"
./mvnw -q exec:java -Dexec.args="run --endpoint headless --port HEADLESS --baud 115200 --profile sierra-hl6-hl8-v20 --input-ascii AT\\r"
./mvnw -q exec:java -Dexec.args="replay src/test/resources/replay/basic-at-events.jsonl --mode validate-recompute"
./mvnw -q exec:java -Dexec.args="replay src/test/resources/replay/basic-at-events.jsonl --mode play-to-dte --endpoint headless"
./mvnw -q exec:java -Dexec.args="headless --profile sierra-hl6-hl8-v20 --script src/test/resources/replay/basic-at.jsonl"
```

The `test` command accepts implemented acceptance IDs (`A01` through `A32`) and spec-oriented aliases such as `creg`, `profile-macro-negatives`, `text-cmgs`, `deterministic-delays`, `live-log`, `macro-hot-reload-timers`, `audit-backpressure`, `diagnostics`, `golden-yaml-loader`, and `s-register-bounds`.

`drive-from-captured-input` and `play-to-dte` validate replay metadata before continuing when the transcript carries event metadata. Hash or metadata divergence requires `--confirm-divergence`; hard replay failures such as redacted replay bytes or captured `replayDivergent=true` events are rejected even with confirmation. `.trm` transcripts are payload/timing captures: each `RX ... (ms): payload` is matched to the next following `TX` line, and payloads are treated as ASCII. Without `--port`, use `--endpoint headless` for deterministic dry runs. With `--timing recorded`, the player preserves captured TX spacing.

## Implemented Acceptance Surface

The checked-in acceptance suite covers the v1 cases from `docs/Tasks/Initial-Spec/spec/13_teststrategie_abnahme.md`:

- Basic serial/headless responsiveness and `AT -> OK`
- Hayes/V.250 command behavior
- Cellular registration, signal, operator, SIM PIN, and CME modes
- SMS text-mode submit, SMS storage helpers, and rate limiting
- Deterministic scheduler delay sampling
- XML macro matching, faults, and custom byte responses
- Profile, schema, coverage, config, and XML-hardening checks
- GUI control policy model, shared runtime event streaming, macro reload validation, replay/export panes, and unsafe-DCE transmit guardrails
- Unknown-command policies for `OK`, `ERR`, `ERROR`, and restart behavior
- Parser error/`ATA`, macro hot-reload timer, audit backpressure, transport diagnostics, source-size, golden YAML, and profiled S-register bounds gates

Run them with:

```bash
./mvnw -q exec:java -Dexec.args="test --suite acceptance --case all"
```

## Architecture

At runtime, a session is the unit of state and determinism:

```text
Serial or headless endpoint
        |
        v
Session actor
        |
        +-- AT parser
        +-- command router
        +-- profile state
        +-- macro engine
        +-- deterministic scheduler
        +-- event sink
```

Important packages:

```text
app          CLI entry point, runtime launcher, replay command, distribution scripts
transport    Serial abstractions, jSerialComm adapter, headless endpoint
parser       AT framing, parsing, raw byte helpers
commands     Hayes, cellular, SMS handlers and response formatting
profiles     Built-in profiles, XML loading, metadata inheritance, semantic validation
state        Immutable modem, SIM, network, signal, SMS, and line state
macros       XML macro loading, matching, actions, and fault services
scheduler    Virtual clock and deterministic response ordering
session      Headless session orchestration and event publishing
replay       Replay validation and playback model
gui          GUI view, controller, event display, control catalog, and read-only policy model
validation   XML, JSON schema, config, scenario, and coverage validators
testkit      v1 acceptance suite
```

## Profiles And Coverage

v1 target coverage files live in:

```text
src/main/resources/coverage/v1-targets/
```

Current v1 targets include:

- `generic-hayes-v250`
- `3gpp-27007-r18`
- `3gpp-27005-r16`
- `sierra-common`
- `sierra-hl6-hl8-v20`
- `westermo-common`
- `westermo-td22-6177-2203`
- `westermo-td36-6618-2202`
- `westermo-gd01-6196-2220`
- `westermo-gdw11-6615-2220`

Coverage validation is about explicit command disposition, not pretending every vendor command is fully implemented. Unknown commands in v1 target coverage must be driven to zero by marking each command as implemented, stubbed, intentionally unsupported, or not applicable.

XML profiles carry more than executable state. The loader preserves and inherits command declarations, S-register declarations, coverage metadata, and deviation notes. Child profiles override entries with the same command/register/deviation ID while inheriting entries they do not redeclare.

## Specification

The authoritative v1 spec is:

```text
docs/Tasks/Initial-Spec/
```

Start here:

- `docs/Tasks/Initial-Spec/README.md`
- `docs/Tasks/Initial-Spec/spec/01_zielbild_scope.md`
- `docs/Tasks/Initial-Spec/spec/02_architektur.md`
- `docs/Tasks/Initial-Spec/spec/04_at_parser_und_command_engine.md`
- `docs/Tasks/Initial-Spec/spec/13_teststrategie_abnahme.md`

Example inputs:

- `docs/Tasks/Initial-Spec/examples/modem-profile.sample.xml`
- `docs/Tasks/Initial-Spec/examples/macros.sms-error-123.xml`
- `docs/Tasks/Initial-Spec/examples/macros.faults-and-custom-responses.xml`
- `docs/Tasks/Initial-Spec/examples/scenario.no-network.xml`

Schemas:

- `docs/Tasks/Initial-Spec/schemas/modem-profile.schema.xsd`
- `docs/Tasks/Initial-Spec/schemas/macro-schema-draft.xsd`
- `docs/Tasks/Initial-Spec/schemas/scenario.schema.xsd`
- `docs/Tasks/Initial-Spec/schemas/config.schema.json`
- `docs/Tasks/Initial-Spec/schemas/coverage.schema.json`
- `docs/Tasks/Initial-Spec/schemas/event-log.schema.json`

## Quality Gates

`./mvnw -B verify` is the standard local and CI gate. It runs:

- Java compilation with release 24
- JUnit tests
- v1 acceptance tests
- JaCoCo line coverage reporting and enforcement
- minimum bundle line coverage: `85%`
- non-generated Java source-size guard

`./mvnw -B -Pcoverage-all verify` removes the display-safe GUI JaCoCo exclusions and includes GUI-tagged tests. Run it under `xvfb-run` on Linux. This is the stricter coverage gate used by CI to keep the full non-generated production bundle above the 85% line threshold.

The source-size guard fails at 300 lines or more for non-generated Java source files under `src/`; keep them under 300 lines.

CI runs the same Maven verify target on:

- `ubuntu-24.04`
- `windows-2025`

CI also has a Linux GUI-headless lane, a full-coverage lane with GUI classes counted, Linux and Windows ZIP distribution smoke lanes, and automatic `serial-it` lanes. Linux provisions a `socat` PTY pair. Windows attempts to provision com0com on the hosted runner and emits an explicit warning/summary if the virtual COM pair cannot be used there.

To enable the checked-in local hook for the same source-size guard:

```bash
git config core.hooksPath .githooks
```

## Development Notes

- Keep production Java packages under `com.jkamsker.modemsim`.
- Prefer small, direct classes over clever abstractions.
- Keep non-generated Java source below 300 lines.
- Add focused tests when changing command behavior, parsing, state transitions, XML validation, scheduler ordering, or profile semantics.
- Use the spec examples and schemas for validation tests instead of duplicating ad hoc fixtures.
- Run `./mvnw -B clean verify` before pushing changes that touch code, schemas, test resources, or build configuration.

## Serial Port Notes

The project includes a jSerialComm-backed endpoint for real serial ports and a headless endpoint for deterministic tests. It does not implement kernel drivers or create virtual COM pairs by itself. For full serial integration, provide a physical serial adapter or an OS-level virtual serial pair, then point the simulator at the desired port through the runtime integration layer.

jSerialComm exposes all six modem-control line getters plus DTR and RTS setters. The simulator reads DTR/DSR/DCD/RI/RTS/CTS in model field order, and maps writable simulator output signals onto the available local controls deterministically: DCD asserts DTR, and CTS asserts RTS. Runtime tests cover DTR drop/reassert behavior; true hardware-line behavior still depends on the adapter and null-modem wiring.

Linux CI creates its serial pair with `socat`; locally you can run the same lane by exporting `MODEMSIM_SERIAL_MODEM_PORT` and `MODEMSIM_SERIAL_DTE_PORT`, then running:

```bash
./mvnw -B -DexcludedGroups= -Dgroups=serial-it test
```

On Windows, use com0com or real hardware to provide the same two environment variables locally. Hosted CI attempts com0com automatically, but may skip the Windows serial lane with an explicit warning if the runner cannot expose the created pair to jSerialComm.

## Security And Test Safety

- XML loading is hardened against XXE-style inputs.
- v1 has no HTTP or WebSocket control API.
- Unsafe DCE transmit paths are modeled behind explicit GUI/read-only policy checks.
- Event and replay design is built around deterministic state transitions and mandatory redaction-aware artifacts.
- Raw payloads containing PINs, SMS bodies, IMSI/ICCID/IMEI values, or phone numbers are redacted in logs. Replay accepts state-redacted logs when raw bytes are intact and rejects artifacts where replay-critical bytes were redacted.

## Common Problems

`Unsupported class file major version` or compile failures:

Use JDK 24. Older JDKs cannot compile this project.

`list-ports` shows nothing:

Confirm the serial adapter or virtual port pair is visible to the OS and that your user has permission to access it.

Coverage check fails:

Run `./mvnw -B verify`, open `target/site/jacoco/index.html`, and add behavior-focused tests for the uncovered branch or class. Do not lower the threshold to hide regressions.

GUI coverage check fails locally:

Use `xvfb-run -a ./mvnw -B -Pcoverage-all verify` on Linux. On Windows or macOS, run the default `verify` gate plus the GUI-focused tests in an environment with a display server.

## License

No license file is currently present. Treat the repository as private or all-rights-reserved until a license is added.
