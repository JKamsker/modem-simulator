# Codex Findings

Adversarial review of the Initial-Spec package. Scope: documentation, examples, schemas, and profile specs under `docs/Tasks/Initial-Spec`.

No source files were edited during review. Findings below are deduplicated from ten independent review lenses plus one local consistency pass.

## Critical

### 1. No Single State Owner Is Specified

`StateStore.update()` can be reached conceptually from RX, parser/executor, scheduler, GUI injection, macros, replay, and state editor. That makes `stateBefore/stateAfter`, URC emission, PIN handling, scheduler behavior, and replay nondeterministic.

References:

- `spec/12_java_implementation.md:40`
- `spec/12_java_implementation.md:88`
- `spec/08_monitoring_injection.md:17`
- `spec/07_macro_engine_xml.md:48`

Action:

Define a per-session actor/event loop as the only state writer. RX, GUI, scheduler, macro timers, replay, and CLI/headless inputs should enqueue commands with sequence IDs.

### 2. Scheduler Semantics Are Not Deterministic

The spec requires delayed responses, jitter, timers, URCs, replay, and seeded randomness, but does not define due-time ordering, tie-breaks, cancellation, wall-clock vs. virtual-clock behavior, or PRNG consumption.

References:

- `spec/02_architektur.md:47`
- `spec/06_sms_und_datenmodus.md:82`
- `spec/07_macro_engine_xml.md:14`
- `spec/13_teststrategie_abnahme.md:25`

Action:

Specify scheduler ordering, for example `(dueMonotonicTime, sequence, sourcePriority)`. Require virtual-clock support for headless/replay, define cancellation on state changes/session stop/`PORT_LOST`, and log sampled delays.

### 3. Canonical Sample State Is Physically Inconsistent

The sample starts with `SIM_PIN_REQUIRED` while `network stat="1"` says registered on the home network. That leaves `AT+CPIN?`, `AT+CREG?`, `AT+COPS?`, and `AT+CMGS` precedence undefined before PIN unlock.

References:

- `examples/modem-profile.sample.xml:16`
- `examples/modem-profile.sample.xml:23`
- `spec/05_state_model_sim_netz_signal.md:88`
- `spec/05_state_model_sim_netz_signal.md:100`

Action:

Either make the sample `READY`, or set network registration to not registered until successful `AT+CPIN=<pin>`. Define whether locked-SIM network state is latent, derived, or inaccessible.

### 4. v1 Acceptance Is Not Executable

The acceptance criteria use broad language such as “funktionieren” without exact setup, byte-level inputs/outputs, line endings, echo state, result-code mode, exit codes, timing tolerances, or CI commands.

References:

- `spec/13_teststrategie_abnahme.md:15`
- `spec/13_teststrategie_abnahme.md:30`

Action:

Add an acceptance matrix with criterion ID, profile, fixture, initial state, input bytes, expected output bytes/log events, timing tolerance, and CI command.

### 5. Control-Line Scope Still Contradicts Protocol/Profile Claims

v1 excludes DTR/DSR/DCD/RI/RTS/CTS, but data mode and Westermo TD-family profile text still depend on control-line behavior. This is a protocol compliance risk for line-aware DTEs.

References:

- `spec/03_serial_com_ports.md:35`
- `spec/03_serial_com_ports.md:45`
- `spec/06_sms_und_datenmodus.md:143`
- `profiles/westermo-family.md:40`

Action:

Either explicitly declare v1 byte-only mode as non-compliant for line-aware DTEs, or restore a minimal `ModemLines` model for DTR drop handling, DCD connect/SMS-entry behavior, `AT&D0/2`, `AT&C0/1`, and line-event logging.

### 6. Concrete v1 Target Profiles Are Not Declared

The spec promises multiple concrete profiles while exact Sierra UMTS modules remain open and some prioritized profile IDs lack matching reference entries.

References:

- `spec/01_zielbild_scope.md:18`
- `spec/10_herstellerprofile_sierra_wireless.md:20`
- `spec/15_risiken_offene_punkte.md:16`
- `references/reference_index.md:11`

Action:

Declare the exact v1 target-profile set and demote all other profiles to candidate/stub/post-v1.

## High

### 7. Schema Validation Is Weaker Than the Prose Contract

The profile XSD accepts invalid combinations the prose says must be rejected, including `pinQueryEnabled=true` without `pin`, `minMs > maxMs`, duplicate delay operations, invalid CSQ values, and mismatched `numeric`/`mcc`/`mnc`. The macro XSD leaves `match`, `when`, and `then` effectively untyped.

References:

- `spec/09_profile_system.md:116`
- `spec/09_profile_system.md:122`
- `schemas/modem-profile.schema.xsd:47`
- `schemas/modem-profile.schema.xsd:93`
- `schemas/macro-schema-draft.xsd:9`

Action:

Add mandatory semantic validation after XSD validation, with negative fixtures in CI. Alternatively use XSD 1.1 assertions/keys where practical. Macro validation must type matches, conditions, actions, paths, delay bounds, IDs, and regex compilation.

### 8. Replay Is Named But Not Architected

GUI replay, CLI replay, and proxy/record-replay are mixed. Event logs lack sequence number, initial state snapshot, profile/config hash, macro seed/version, scheduler events, and a replay mode definition.

References:

- `spec/01_zielbild_scope.md:22`
- `spec/08_monitoring_injection.md:20`
- `spec/08_monitoring_injection.md:76`
- `spec/12_java_implementation.md:145`
- `schemas/event-log.schema.json:5`

Action:

Split replay into explicit modes, such as drive simulator from captured DTE input, play captured modem output to a DTE, and validate recomputed output against capture. Define inputs, outputs, timing source, state restoration, divergence handling, and acceptance tests.

### 9. Event-Log Schema Cannot Support Promised GUI/Audit/Export Behavior

The GUI requires parser result, handler/macro, result code, latency, state before/after, injection metadata, replay/export support, and redaction status. The schema only requires timestamp, session, direction, and raw hex.

References:

- `schemas/event-log.schema.json:5`
- `schemas/event-log.schema.json:25`
- `spec/08_monitoring_injection.md:16`
- `spec/13_teststrategie_abnahme.md:27`

Action:

Add event type, sequence, source/action, handler, injection type, effective direction, monotonic timing, redaction metadata, replay metadata, and stricter validation.

### 10. Raw Logs, Exports, and Replay Artifacts Can Leak Secrets

Live Log and exports include raw bytes/text and unrestricted state snapshots. This can expose PIN, PUK, IMSI, ICCID, phone numbers, and SMS bodies. Redaction is conditional instead of default-safe.

References:

- `spec/08_monitoring_injection.md:16`
- `spec/08_monitoring_injection.md:21`
- `spec/14_betrieb_sicherheit.md:23`
- `spec/14_betrieb_sicherheit.md:35`
- `schemas/event-log.schema.json:33`
- `schemas/event-log.schema.json:66`

Action:

Make PIN/PUK/IMSI/ICCID/phone redaction mandatory. Default SMS-body redaction on for logs, exports, reports, and replay artifacts. Add schema fields proving redaction was applied.

### 11. XML Parsing Hardening Is Missing

Profiles and macros are user-controlled XML, and Jackson XML is recommended, but the spec does not require DTD/XXE/XInclude disabling, external resolver blocking, secure-processing, or file size/depth limits.

References:

- `spec/09_profile_system.md:116`
- `spec/07_macro_engine_xml.md:68`
- `spec/12_java_implementation.md:117`

Action:

Require no DOCTYPE, no external entities, no external schemas, no XInclude, bounded input size/tree depth, and tests for XXE/entity expansion rejection.

### 12. AT/SMS Protocol Details Are Incorrect Or Too Loose

Several protocol-level requirements are either wrong or underspecified:

- `+CMGS` prompt is modeled as bare `> ` instead of exact emitted bytes.
- `ATH` is specified as `NO CARRIER`; local hook control should return `OK`.
- Static `commandTerminator`/`responseTerminator` conflicts with S3/S4/S5 behavior.
- Command chaining rejects valid V.250 command lines and omits final-result suppression rules.

References:

- `spec/06_sms_und_datenmodus.md:28`
- `spec/06_sms_und_datenmodus.md:146`
- `spec/03_serial_com_ports.md:28`
- `spec/04_at_parser_und_command_engine.md:70`
- `profiles/generic-hayes-v250.md:23`

Action:

Specify exact prompt bytes, fix `ATH`, make S-registers drive framing dynamically, and define a real AT tokenizer/command-chain execution model.

### 13. SMS/PDU/Storage Scope Conflicts

Text mode is the stated v1 focus, but PDU `CMGS`, `CMGR`, `CMGL`, `CMGD`, `CNMI`, `CPMS`, `CSCA`, and SMS storage are listed as v1 minimum/full/stub inconsistently and are not covered by acceptance.

References:

- `spec/06_sms_und_datenmodus.md:8`
- `spec/06_sms_und_datenmodus.md:13`
- `profiles/3gpp-27007-27005.md:29`
- `profiles/3gpp-27007-27005.md:31`
- `spec/13_teststrategie_abnahme.md:23`

Action:

Decide PDU and storage/read scope for v1. Either mark as stub/unsupported/pass-through or add concrete behavior and acceptance tests.

### 14. Java 24 Native/Desktop Delivery Is Underspecified

Java 24 plus `jSerialComm` likely needs native-access launcher/test/package handling. Build tool, dependency versions, GUI toolkit, packaging, and CI OS matrix are not pinned.

References:

- `spec/12_java_implementation.md:108`
- `spec/12_java_implementation.md:113`
- `spec/12_java_implementation.md:118`
- `spec/15_risiken_offene_punkte.md:18`

Action:

Specify Maven or Gradle, Java toolchain, dependency versions, JavaFX or Swing, native-access JVM flags, package format, and CI OS matrix.

### 15. Coverage Gates Are Not Enforceable

`unknown = 0` is central to quality and acceptance, but there are no machine-readable coverage artifacts or schema. Status terms differ across docs (`implemented_full`, `full`, `read`, etc.).

References:

- `spec/01_zielbild_scope.md:38`
- `spec/09_profile_system.md:44`
- `spec/13_teststrategie_abnahme.md:30`
- `profiles/3gpp-27007-27005.md:21`

Action:

Add coverage files per active profile, a coverage schema, canonical status enum, and CI gate for `unknown == 0`.

### 16. GUI Scope Is Mandatory But Not Fully Buildable Or Testable

The GUI is now v1 mandatory, but toolkit choice is open, Control Pane fields are incomplete, read-only mode is inconsistent, Macro Control lacks operational semantics, and workflows omit replay/export/read-only/reconnect/error cases.

References:

- `spec/08_monitoring_injection.md:11`
- `spec/08_monitoring_injection.md:15`
- `spec/08_monitoring_injection.md:19`
- `spec/08_monitoring_injection.md:81`
- `examples/gui-workflows.md:3`
- `spec/15_risiken_offene_punkte.md:18`

Action:

Choose JavaFX or Swing. Define full Control Pane fields, disabled controls in read-only mode, macro list/reload workflow, replay/export workflows, port-loss/reconnect behavior, and GUI test automation IDs.

## Medium

### 17. Profile Inheritance Is Not Deterministic

Docs show multiple inheritance, but XML has one unstructured `extends` string and `xs:NCName` profile IDs that reject existing catalog IDs like `3gpp-27007-r18`.

References:

- `spec/09_profile_system.md:13`
- `spec/04_at_parser_und_command_engine.md:89`
- `schemas/modem-profile.schema.xsd:18`
- `schemas/modem-profile.schema.xsd:19`

Action:

Use an ordered parent list, allow existing ID syntax, and define merge precedence, conflict resolution, unknown-base rejection, and cycle detection.

### 18. Profile Status Vocabulary Is Inconsistent

`device-family` appears in profile docs, but the canonical status table and JSON schema do not allow it.

References:

- `schemas/profile.schema.json:19`
- `spec/09_profile_system.md:35`
- `profiles/profile_catalog.md:16`
- `spec/10_herstellerprofile_sierra_wireless.md:12`

Action:

Add `device-family` to the canonical enum or reclassify those profiles.

### 19. Scenario XML Has No Schema Or Runtime Contract

`initialScenario` appears in port config and `scenario.no-network.xml` ships as an example, but there is no schema or lifecycle for applying scenarios relative to profile initial state, NVRAM, macro reload, scheduler startup, or replay.

References:

- `spec/03_serial_com_ports.md:30`
- `examples/scenario.no-network.xml:2`
- `spec/13_teststrategie_abnahme.md:42`

Action:

Add `scenario.schema.xsd` and runtime semantics, or mark scenarios as illustrative/post-v1.

### 20. Java Object Models Are Too Placeholder-Level

`ParsedCommand` is too lossy for command chaining, quoted values, binary SMS/PDU entry, vendor syntax, and handler maintainability. `RawBytes(byte[] bytes)` is mutable inside a record. Package structure still uses `com.example`.

References:

- `spec/12_java_implementation.md:6`
- `spec/12_java_implementation.md:58`
- `spec/12_java_implementation.md:75`
- `spec/12_java_implementation.md:78`

Action:

Define a typed AT AST, immutable byte wrapper, real group/module structure, and package boundaries such as `core`, `transport-serial-jserialcomm`, `desktop`, `cli`, and `testkit`.

### 21. Packaging/References Boundary Is Contradictory

The README says full PDFs are not included, but `references/downloads/**` exists locally with full/third-party PDFs. The manifest omits that folder, so package-of-record is unclear.

References:

- `README.md:19`
- `spec/01_zielbild_scope.md:33`
- `references/README.md:9`
- `MANIFEST.txt:47`

Action:

Declare `references/downloads/**` non-distributable/out-of-package, or update README/MANIFEST/licensing notes if it ships.

### 22. TCP Serial Bridge Scope Is Still Open

HTTP/WebSocket control APIs are excluded, but an optional TCP serial endpoint remains and could expose the AT surface remotely.

References:

- `spec/03_serial_com_ports.md:12`
- `spec/02_architektur.md:84`
- `spec/15_risiken_offene_punkte.md:19`

Action:

Mark it out of v1 or require explicit opt-in, loopback bind by default, warnings for wildcard bind, authentication/network ACLs, and acceptance criteria.

### 23. Headless/CI Strategy Is Aspirational

Headless mode, schema validation, golden transcripts, GUI tests, serial integration, and CI are all mentioned, but there is no concrete build entrypoint, script grammar, artifact layout, or coverage threshold.

References:

- `spec/14_betrieb_sicherheit.md:14`
- `spec/14_betrieb_sicherheit.md:46`
- `spec/13_teststrategie_abnahme.md:60`
- `MANIFEST.txt:7`

Action:

Define CI stages, headless script format, fake-clock/seed handling, golden transcript layout, GUI headless tests, serial-it matrix, and failure artifacts.

## Low

### 24. Documentation Consistency Issues Remain

Examples include non-canonical paths/statuses and mixed German umlaut/ASCII transliteration.

Examples:

- Coverage source `references/sierra-hl78xx-v29` does not match actual reference paths.
- Command status terms vary: `implemented_full`, `full`, `read`.
- Prose mixes `für` and `fuer`.

References:

- `spec/09_profile_system.md:48`
- `profiles/3gpp-27007-27005.md:21`
- `CHANGELOG.md:6`

Action:

Normalize reference IDs, status enums, and prose spelling conventions.

## Suggested Repair Order

1. Define state ownership and scheduler determinism.
2. Lock v1 scope: target profiles, PDU/storage, control lines, TCP bridge, scenarios, GUI toolkit.
3. Fix protocol correctness for AT framing, `CMGS`, `ATH`, `CPIN`, `CREG`, `COPS`, and SMS submit precedence.
4. Strengthen XML/event schemas plus mandatory semantic validation and XML parser hardening.
5. Make acceptance executable with CI, golden transcripts, coverage artifacts, headless tests, and GUI automation.
6. Clean packaging/reference boundaries and documentation consistency.
