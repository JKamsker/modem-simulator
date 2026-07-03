# Modem Simulator

Java 24 COM/serial modem simulator for deterministic AT-command testing, profile validation, and headless acceptance runs. The project implements the v1 surface from the Initial-Spec package in `docs/Tasks/Initial-Spec`.

The simulator is aimed at software that expects a Hayes, GSM, or vendor-specific modem on a serial port: embedded controllers, routers, PLCs, legacy desktop software, integration test rigs, and regression suites.

## What It Does

- Parses AT/Hayes command streams with configurable line endings, echo, quiet mode, verbose/numeric result codes, S-registers, `A/`, and `+++`.
- Implements core V.250 behavior such as `AT`, `ATE`, `ATQ`, `ATV`, `ATS`, `ATZ`, `AT&F`, `AT&W`, `AT&V`, dial/connect, escape, online-command mode, and hangup flows.
- Implements key 3GPP TS 27.007 cellular commands including `+CREG`, `+CSQ`, `+CPIN`, `+CMEE`, `+COPS`, and identity commands.
- Implements the 3GPP TS 27.005 SMS text-mode workflow around `+CMGF`, `+CMGS`, prompt handling, Ctrl-Z submit, ESC cancel, storage helpers, service center configuration, and deterministic CMS errors.
- Supports XML modem profiles, XML macro rules, XML scenarios, YAML config validation, and JSON coverage reports.
- Provides deterministic headless sessions with virtual time, seeded delay sampling, event capture, replay validation building blocks, and an in-memory serial endpoint for tests.
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

Build the ZIP distribution:

```bash
./mvnw -B package
unzip -l target/modem-simulator-*-dist.zip
```

The distribution contains `bin/modemsim` and `bin/modemsim.cmd`. Both launchers run the CLI with `--enable-native-access=ALL-UNNAMED`, which is required for classpath use of jSerialComm on Java 24.

Run the full acceptance suite:

```bash
./mvnw -q exec:java -Dexec.args="test --suite acceptance --case all"
```

Run one acceptance case:

```bash
./mvnw -q exec:java -Dexec.args="test --suite acceptance --case A01"
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
validate-profile <profile.xml>
validate-macros <macros.xml>
validate-scenario <scenario.xml>
validate-config <config.yaml>
coverage verify --profiles v1-targets
test --suite acceptance --case <case-id|all>
list-ports
```

Useful examples:

```bash
./mvnw -q exec:java -Dexec.args="validate-macros docs/Tasks/Initial-Spec/examples/macros.sms-error-123.xml"
./mvnw -q exec:java -Dexec.args="validate-scenario docs/Tasks/Initial-Spec/examples/scenario.no-network.xml"
./mvnw -q exec:java -Dexec.args="validate-config src/test/resources/config/valid.yaml"
./mvnw -q exec:java -Dexec.args="test --suite acceptance --case A09"
```

## Implemented Acceptance Surface

The checked-in acceptance suite covers the v1 cases from `docs/Tasks/Initial-Spec/spec/13_teststrategie_abnahme.md`:

- Basic serial/headless responsiveness and `AT -> OK`
- Hayes/V.250 command behavior
- Cellular registration, signal, operator, SIM PIN, and CME modes
- SMS text-mode submit, SMS storage helpers, and rate limiting
- Deterministic scheduler delay sampling
- XML macro matching, faults, and custom byte responses
- Profile, schema, coverage, config, and XML-hardening checks
- GUI control policy model and unsafe-DCE transmit guardrails
- Unknown-command policies for `OK`, `ERR`, `ERROR`, and restart behavior

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
app          CLI entry point
transport    Serial abstractions, jSerialComm adapter, headless endpoint
parser       AT framing, parsing, raw byte helpers
commands     Hayes, cellular, SMS handlers and response formatting
profiles     Built-in profiles, XML loading, semantic validation
state        Immutable modem, SIM, network, signal, SMS, and line state
macros       XML macro loading, matching, actions, and fault services
scheduler    Virtual clock and deterministic response ordering
session      Headless session orchestration and event publishing
replay       Replay validation model
gui          GUI control catalog and read-only policy model
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

The source-size guard warns above 300 lines and fails above 500 lines for Java source files under `src/`.

CI runs the same Maven verify target on:

- `ubuntu-latest`
- `windows-latest`

CI also has a Linux GUI-headless lane, a ZIP distribution smoke lane, and an opt-in `serial-it` lane for environments with external serial loopback fixtures.

## Development Notes

- Keep production Java packages under `com.jkamsker.modemsim`.
- Prefer small, direct classes over clever abstractions.
- Keep non-generated Java source below 300 lines where practical and never above 500 lines.
- Add focused tests when changing command behavior, parsing, state transitions, XML validation, scheduler ordering, or profile semantics.
- Use the spec examples and schemas for validation tests instead of duplicating ad hoc fixtures.
- Run `./mvnw -B clean verify` before pushing changes that touch code, schemas, test resources, or build configuration.

## Serial Port Notes

The project includes a jSerialComm-backed endpoint for real serial ports and a headless endpoint for deterministic tests. It does not implement kernel drivers or create virtual COM pairs by itself. For full serial integration, provide a physical serial adapter or an OS-level virtual serial pair, then point the simulator at the desired port through the runtime integration layer.

## Security And Test Safety

- XML loading is hardened against XXE-style inputs.
- v1 has no HTTP or WebSocket control API.
- Unsafe DCE transmit paths are modeled behind explicit GUI/read-only policy checks.
- Event and replay design is built around deterministic state transitions and redaction-aware artifacts.

## Common Problems

`Unsupported class file major version` or compile failures:

Use JDK 24. Older JDKs cannot compile this project.

`list-ports` shows nothing:

Confirm the serial adapter or virtual port pair is visible to the OS and that your user has permission to access it.

Coverage check fails:

Run `./mvnw -B verify`, open `target/site/jacoco/index.html`, and add behavior-focused tests for the uncovered branch or class. Do not lower the threshold to hide regressions.

## License

No license file is currently present. Treat the repository as private or all-rights-reserved until a license is added.
