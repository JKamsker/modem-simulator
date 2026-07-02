# Consolidated Adversarial Review — Initial-Spec (v0.2)

**Target of record:** `C:\Users\Jonas\repos\private\JKamsker\modem-simulator\docs\Tasks\Initial-Spec` (JKamsker copy — GUI, Java 24).
**Method:** Two independent adversarial reviews were reconciled:

- **[WF]** A Claude multi-agent workflow: 8 dimensional finders → 68 findings → 68 independent refutation passes (adversarial verify). 65 survived, 3 refuted. *(Ran against a divergent worktree copy; all findings re-mapped to JKamsker line numbers below. Schemas, ch.04–09, and the example/scenario XML are byte-identical between the two copies, so protocol/schema findings port over verbatim.)*
- **[CX]** Codex review — `Review/Codex-Findings.md` (10 lenses + local consistency pass, 24 findings).
- **[+]** Direct re-reading/verification by the consolidating reviewer.

Provenance tags: **[WF]** workflow-verified · **[CX]** Codex · **[WF+CX]** both independently · **[+]** verified here.

> **Note on divergence:** A second copy of this spec exists at `D:\…\objective-bell-68588e\…\Initial-Spec` (Java 21, REST/WebSocket API, no GUI). The two have diverged on nearly every file. This report treats the JKamsker copy as authoritative. See **D0** below — the divergence itself is a finding.

---

## Executive summary

The spec is unusually thorough for a v0.2 and its *intent* is sound (profile-driven completeness, `unknown=0`, macro-testability, redaction). But it is **not yet buildable to acceptance** for three structural reasons: (1) it ships **two mutually-incompatible profile schemas** with no statement of which governs, and neither matches the YAML profile prose; (2) it never names a **single state owner** or a **deterministic scheduler/seed**, so the very properties it promises (reproducible transcripts, ordered URCs, `stateBefore/After`) are undefined; and (3) the **acceptance criteria are not executable** (no byte-level I/O, timing tolerances, or CI oracle). On top of that, the **canonical sample models a physically impossible state** (PIN-locked yet registered on the home network), several **AT/3GPP details are wrong or too loose** (+CREG location fields when not registered, AcT capped below 5G, `ATH`→`NO CARRIER`, CMS 310 misuse, SMS prompt missing CRLF), the **Westermo analog/PSTN target family is unrepresentable** in the cellular-only XSD, and **XML input is unhardened** (XXE). Fixing scope/ownership/schemas first unblocks most of the rest.

Counts (deduped): **4 CRITICAL · 14 HIGH · ~20 MEDIUM · ~11 LOW**, plus 3 findings checked and dismissed.

---

## CRITICAL

### C1 — Two mutually-incompatible profile schemas, none authoritative [WF+CX · CX#7/#17/#18]
`schemas/profile.schema.json` and `schemas/modem-profile.schema.xsd` describe the same concept incompatibly, and neither matches the YAML profile prose in `spec/09_profile_system.md`:
- `extends` is a **single `xs:string`** (`modem-profile.schema.xsd:19`) but a **`string[]` array** (`profile.schema.json:28-33`); multi-parent inheritance (`spec/09_profile_system.md:13-17`, `spec/10_…:50-53`) **cannot be expressed in XML** and no separator is defined.
- `vendor` and `status` are **required** in `profile.schema.json:5-27` but have **no home anywhere in the XSD** (`ProfileType` carries only `id`/`extends`).
- The XSD `id` is **`xs:NCName`** (`modem-profile.schema.xsd:18`), which **rejects catalog IDs that start with a digit** such as `3gpp-27007-r18` / `3gpp-27005-r16`.
- `commands`, `registers`, `coverage`, `deviations` from `spec/09_…:19-31` exist in neither schema.

**Impact:** a profile loader cannot be built without guessing the format. **Fix:** pick one authoritative representation (recommend the XSD for the loaded artifact), delete or clearly demote the other, model `extends` as an ordered `xs:list`, add `vendor`/`status`, and relax the ID type to accept existing catalog IDs.

### C2 — No single state owner; scheduler/seed nondeterministic [CX#1/#2 · WF architecture+testability]
`StateStore.update()` (`spec/12_java_implementation.md:40-43`) is reachable from the RX/executor thread, the response scheduler, GUI injection (`spec/08_…:18`), macros (`spec/07_…:48`), and replay — with **no serialization point or ownership rule**. Simultaneously the spec promises seeded, reproducible delays/jitter (`spec/06_…:84`, `spec/07_…:58-64`, acceptance #11 `spec/13_…:25`) but never defines the **"Session-Seed"**, scheduler **due-time ordering / tie-breaks / cancellation**, or **wall-clock vs virtual-clock**.

**Impact:** `stateBefore/stateAfter`, URC-vs-response interleaving, and "reproducible via seed" are all undefined — the headline testability guarantee is unmet. **Fix:** define a per-session actor/event-loop as the **sole** state writer; all sources enqueue sequenced commands. Specify scheduler order `(dueMonotonic, sequence, sourcePriority)`, a mandatory virtual clock for headless/replay, cancellation on state-change/stop/`PORT_LOST`, and one named seed feeding all PRNG draws (log the sampled value).

### C3 — Canonical sample models a physically impossible state [CX#3 · +]
`examples/modem-profile.sample.xml:16,23` (and `spec/05_…:7-17`) start with `sim state="SIM_PIN_REQUIRED"` **and** `network stat="1"` (registered, home). A PIN-locked SIM cannot be network-registered — registration requires SIM authentication that PIN-unlock gates. The precedence of `AT+CPIN?`, `AT+CREG?`, `AT+COPS?`, `AT+CMGS` **before** unlock is left undefined.

**Impact:** the reference example every implementer copies is internally contradictory. **Fix:** either set the sample to `READY`, or set `stat` to not-registered (`2`/`0`) until a successful `AT+CPIN=<pin>`; define whether locked-SIM network state is latent, derived, or inaccessible.

### C4 — v1 acceptance criteria are not executable [CX#4 · WF testability]
`spec/13_teststrategie_abnahme.md:13-30` uses "funktionieren" with no exact fixtures, byte-level input/output, line-endings, echo/result-code mode, timing tolerances, exit codes, or CI commands. Criteria #10 (rate limit) and #11 (delays) have **no exact pass/fail oracle**, and #16 (`unknown=0`) depends on an undefined `commands_total` extraction (`spec/01_…:36-49`).

**Impact:** "done" for v1 is unfalsifiable. **Fix:** replace with an acceptance matrix — criterion ID, profile, fixture, initial state, **input bytes → expected output bytes/log events**, timing tolerance, and CI command.

---

## HIGH

### H1 — XML input is unhardened (XXE) [CX#11]
Profiles and macros are **user-controlled XML** parsed with Jackson (`spec/09_…:116`, `spec/07_…:66-74`, `spec/12_…:117`), but nothing mandates disabling DOCTYPE/external entities/XInclude/external schema resolution, secure-processing, or input size/tree-depth limits. **Fix:** require no-DOCTYPE, no external entities/schemas, no XInclude, bounded size/depth, plus XXE/entity-expansion rejection tests.

### H2 — Redaction is conditional and misses the primary leak vector [WF security · CX#10]
`redaction.enabled` is gated on "wenn Datenschutzmodus aktiv ist" (`spec/14_…:23,31`) and the YAML default is unbound — on-vs-off by default is never fixed. Worse, redaction is described for `textEscaped`/exports but **`rawHex` carries the same PIN/SMS bytes** (`schemas/event-log.schema.json:33`, `spec/08_…:31-42`) and is not covered; `maskImei=false` by default (`spec/14_…:34`); PINs sit **cleartext at rest** in profile XML (`examples/modem-profile.sample.xml:18`). **Fix:** make PIN/PUK/IMSI/ICCID/MSISDN redaction **default-on and mandatory** across log/export/replay, cover `rawHex`, and add an event-log field proving redaction was applied.

### H3 — `+CREG` protocol errors [WF std · +]
In `spec/05_state_model_sim_netz_signal.md:119-130`:
- The **denied** example `+CREG: 3,3,"00C3","00001234",7,0,11` (line 126) carries `<lac>/<ci>/<AcT>`, but 27.007 omits location info when the UE is **not registered** (stat 3).
- The table mixes `<n>=2` and `<n>=3` read formats under a single fixed `cregN=2`.
- `NetworkStatType` caps `stat` at 5 (`modem-profile.schema.xsd:169-173`), so SMS-only/CSFB states (6–11) can't be modeled.
- `AccessTechnologyType` caps `<AcT>` at 9 (`modem-profile.schema.xsd:181-185`), **excluding every 5G/NR value (10–13)** despite the "GSM/UMTS/LTE/5G-orientiert" claim (`profiles/3gpp-27007-27005.md:3`).

**Fix:** drop location fields for non-registered states, pick one `<n>` per example, widen `stat`/`AcT` enums.

### H4 — Control-line scope contradicts data-mode and Westermo profiles [CX#5 · WF completeness]
v1 removes DTR/DSR/DCD/RI/RTS/CTS (`spec/03_…:33-52`) but `spec/06_…:139-149` data-mode and `spec/11_…`/`profiles/westermo-family.md:40` TD-family behavior depend on them. Line-aware DTEs may refuse to treat the simulator as "ready". **Fix:** either explicitly declare v1 byte-only mode **non-compliant for line-aware DTEs**, or restore a minimal `ModemLines` model (DTR drop, DCD on connect/SMS-entry, `AT&D`/`AT&C`, line-event logging).

### H5 — Westermo analog/PSTN/ISDN targets are unrepresentable [WF completeness]
`InitialStateType` **forces** `<sim>`, `<network>`, `<signal>` (`modem-profile.schema.xsd:37-44`, all cellular). TD-22/32/33/36 (`profiles/westermo-family.md`, `spec/11_…`) have no SIM/network/signal, so these **device-target** profiles cannot be expressed at all. Combined with H4, the Westermo TD family is effectively unbuildable in v1. **Fix:** make cellular blocks optional / introduce a non-cellular profile shape.

### H6 — Concrete v1 target-profile set not declared [CX#6]
The spec promises many device profiles (`spec/01_…:18`) while exact Sierra UMTS modules stay open (`spec/10_…:69`, `spec/15_…:16`) and `westermo-td20-candidate` has no reference. **Fix:** declare the exact v1 target set; demote everything else to candidate/stub/post-v1.

### H7 — Sierra inheritance chain contradicts itself [WF consistency]
`profiles/sierra-wireless-family.md` inserts `sierra-common` between the 3GPP base and devices, but the example profiles in `spec/09_…:82` and `spec/10_…:50-53` extend `generic-hayes-v250`+`3gpp-*` **directly, bypassing `sierra-common`**. Merge order and precedence are undefined. **Fix:** fix the example `extends` chains and specify linearization/conflict resolution.

### H8 — `device-family` status is used but defined nowhere [WF+CX · CX#18]
`profiles/profile_catalog.md` and `spec/10_…:12-18,49` use status `device-family`, absent from both the `spec/09_…:33-42` status table and `profile.schema.json:19-27` enum. **Fix:** add `device-family` to the canonical enum or reclassify.

### H9 — Prose-mandated cross-field validation is unenforceable by the schemas [WF+CX · CX#7]
`spec/09_…:122` requires rejecting `pinQueryEnabled=true` without `pin`, `minMs>maxMs`, duplicate delay operations, and `numeric` not matching `mcc`/`mnc` — none expressible in XSD 1.0. The macro XSD leaves `<match>/<when>/<then>` as **`xs:anyType`** (`macro-schema-draft.xsd:9-11`), constraining nothing. **Fix:** mandate a semantic-validation pass after XSD validation with **negative CI fixtures**; type the macro match/condition/action/path/delay/regex.

### H10 — AT framing/result details wrong or too loose [CX#12 · WF std]
- `ATH` is specified as `NO CARRIER` (`spec/06_…:146`, `profiles/generic-hayes-v250.md:21`) — per V.250, `H` returns **`OK`**; `NO CARRIER` is a carrier-loss result.
- `commandTerminator`/`responseTerminator` are static (`spec/03_…:28`, `dialect`) yet **S3/S4/S5** define CR/LF/BS dynamically (`profiles/generic-hayes-v250.md:33-35`) — the two framing models conflict.
- The `+CMGS` prompt is modeled as bare `> ` (`spec/06_…:29`, `spec/04_…:115`); the real prompt is preceded by **`<CR><LF>`** (27.005) — exact bytes matter to DTEs.
- Command chaining (`spec/04_…:68-81`) rejects valid V.250 lines and omits final-result-suppression rules.

**Fix:** `ATH`→`OK`, make S-registers drive framing, specify exact prompt bytes, define a real tokenizer/chaining model.

### H11 — Replay is named but not architected [CX#8]
GUI/CLI/proxy replay are conflated (`spec/01_…:22`, `spec/08_…:20,76`, `spec/12_…:145`) and the event log lacks sequence number, initial-state snapshot, profile/config hash, macro seed/version, and scheduler events (`schemas/event-log.schema.json:5`). **Fix:** split replay into explicit modes (drive-from-captured-input / play-to-DTE / validate-recompute) with defined inputs, timing source, state restoration, divergence handling.

### H12 — Macro engine references a state model that does not exist [WF completeness]
`spec/07_…:30-37,48` uses a `NetworkRegistration` enum (`NO_NETWORK`) and dotted paths (`state.sim.state`, `state.signal.rssi`) that appear **nowhere** in the `spec/05_…` state model or the flat injection patch shape (`spec/08_…`). **Fix:** unify one addressable state schema used by state, injection, and macro `<when>/<set>`.

### H13 — Java 24 native-access + toolchain/packaging unpinned [CX#14]
Java 24 + `jSerialComm` needs native-access flags (`--enable-native-access`) for launcher/tests/packaging; build tool, dependency versions, GUI toolkit, package format, and CI OS matrix are unspecified (`spec/12_…:108-119`, `spec/15_…:18`). **Fix:** pin Maven/Gradle, toolchain, dep versions, JavaFX-vs-Swing, JVM native flags, package format, CI OS matrix.

### H14 — Mandatory GUI is not fully buildable/testable [CX#16]
GUI is now v1-mandatory (`spec/01_…:17,25`, `spec/08_…:7-21`, acceptance #13/#14 `spec/13_…:27-28`) but the toolkit is open, Control-Pane/State-Editor fields are incomplete, read-only semantics are only partially stated (`spec/14_…:43`), Macro-Control lacks operational detail, and there are no GUI test-automation IDs. **Fix:** choose JavaFX/Swing, enumerate every pane/field, define read-only disabled-control set, macro reload workflow, replay/export/reconnect flows, and headless GUI tests.

---

## MEDIUM

- **M1 — No `<scenario>` schema or runtime contract** [WF+CX · CX#19]. `initialScenario` (`spec/03_…:30`) and `examples/scenario.no-network.xml` ship, but the Scenario concept (glossary "zeitlich geordnete Zustandsfolge") has no schema, lifecycle, or defined value (`registered-home-medium-signal` is never defined). `spec/07_…` requires scenarios to be schema-validated on load, yet no scenario schema exists. **Fix:** add `scenario.schema.xsd` + semantics, or mark scenarios illustrative/post-v1.
- **M2 — `config.yaml` has no schema** [WF]. Required by the CLI (`spec/12_…:141`) and referenced by ch.03/13/14, but its full top-level structure (ports, redaction, errorPolicy, seeds) is never specified or schema-backed.
- **M3 — Sliding-window SMS rate-limit is wall-clock and boundary-undefined** [WF testability]. `spec/06_…:59-65` + acceptance #10 rely on real time with no injectable clock and unspecified reset/eviction, making the test inherently flaky. **Fix:** virtual clock + exact window semantics.
- **M4 — Two seed sources never unified** [WF]. Macro `randomSeed` (`spec/07_…:63`) vs delay "Session-Seed" (`spec/06_…:84`) — jitter+delay reproducibility is contradictory until merged.
- **M5 — Golden-transcript format can't express timing tolerances** [WF]. `spec/13_…:32-56` shows exact-match transcripts, incompatible with mandated random delays/jitter (criterion #11).
- **M6 — `+++` guard-time needs inter-byte idle measurement the interface can't express** [WF arch]. `SerialEndpoint.read(byte[],int,int)` (`spec/03_…:60-66`) + framing model provide no idle-time signal.
- **M7 — Data-mode transparent passthrough vs `+++`/monitor coexistence undefined** [WF arch]. `spec/06_…:144-147`.
- **M8 — Hot reload declared transactional, but in-flight macro state is undefined on swap** [WF arch]. `on-timer` timers, sliding-window counters, `enabled` flags (`spec/07_…:66-74`).
- **M9 — `ATZ`/`AT&W` reset policy deferred to a "Profilpolicy" field that exists in no schema** [WF arch]. `spec/12_…:106`.
- **M10 — `PORT_LOST` cannot be reliably detected** [WF arch]. Blocking `read(...)`+`IOException` (`spec/03_…:60-66,74`) can't distinguish device removal from empty read/timeout.
- **M11 — `raw-modem-to-device` injection can write arbitrary bytes to a live controller (SPS/Fernwirkgerät)** with no safety guardrail [WF sec]. `spec/08_…:71`.
- **M12 — Dropped-events vs audit completeness** [WF sec]. Non-blocking event queue (`spec/14_…:42`) + "Dropped Events" metric (`spec/14_…:62`) means **audit records of injections can be silently dropped**; no drop-policy/backpressure is specified.
- **M13 — Java object models are placeholder-level** [CX#20]. `RawBytes(byte[] bytes)` is a **mutable array inside a record** (`spec/12_…:78`), `ParsedCommand` (`:57-64`) is too lossy for chaining/quoted/PDU/vendor syntax, and the package is `com.example.modemsim` (`:5`). `sealed ResponseFrame` also can't express URC-vs-solicited or `ATQ1` suppression (`:74-81`). **Fix:** immutable byte wrapper, typed AT AST, real package/module boundaries.
- **M14 — State-injection mutates `ModemState` concurrently with the executor with no visibility/ordering contract** [WF arch]. (Subsumed by C2; keep as concrete instance.)
- **M15 — Most 27.007/27.005 commands are never exercised by acceptance** [WF completeness]. `+CGREG/+CEREG/+CMGR/+CMGL/+CMGD/+CNMI/+CPMS/+CSCA` are stub (`profiles/3gpp-27007-27005.md:18-38`) and absent from `spec/13_…` — the "Mobilfunkbasis/SMS-Basis" promise (`spec/01_…:13-14`) is not validated. **EPS/packet registration** (`+CEREG`) being stub-only is notable since LTE-M/NB-IoT modules register via `+CEREG`, not `+CREG`.
- **M16 — SMS/PDU/storage scope conflicts** [CX#13 · WF]. PDU `CMGS`, `CMGR/L/D`, `CNMI`, `CPMS`, `CSCA`, and `smsStore` (`spec/06_…:113-137`) are listed as v1 min/full/stub inconsistently and not covered by acceptance. **Fix:** decide pass-through vs decode vs full, and gate with tests.
- **M17 — `rejectCmsError=310` is semantically wrong** [WF std]. 310 = "(U)SIM not inserted"; used for a network capacity limit (`spec/05_…:34`, `spec/06_…:55`, `examples/modem-profile.sample.xml:38`). Prefer 331/332/500.
- **M18 — `CsqValueType` (0..99) permits values 27.007 defines as invalid** [WF std]. rssi 32–98 / ber 8–98 (`modem-profile.schema.xsd:246-250`).
- **M19 — TCP-serial bridge scope is open and could expose the AT surface remotely** [CX#22]. `spec/03_…:12`, `spec/02_…:84`, `spec/15_…:19`. **Fix:** mark out of v1 or require loopback + auth + acceptance.
- **M20 — Packaging/reference boundary is contradictory (and a copyright risk)** [CX#21 · +]. `README.md:19` says the package **deliberately excludes** full manufacturer/standard PDFs (noting copyright), yet `references/downloads/` ships **41 files including full 3GPP 27.007 (2.3 MB) / 27.005 / ITU V.250 PDFs, Sierra manuals, and third-party PDFs**, and `MANIFEST.txt` omits that folder entirely. **Fix:** declare `references/downloads/**` out-of-package/non-distributable, or update README/MANIFEST/licensing if it ships.

---

## LOW

- **L1 — `unknown=0` is unfalsifiable** [WF+CX · CX#15]. Central to acceptance (#16) but `commands_total` comes from an undefined, non-automatable "extrahierte Referenzliste" (`spec/01_…:42`, `spec/09_…:44-62`). Add a coverage schema + canonical status enum + CI gate.
- **L2 — Status-term drift** [CX#24 · WF]. `implemented_full` vs `full` vs `read` across `spec/09_…:51` and `profiles/3gpp-27007-27005.md:21`; coverage `source: references/sierra-hl78xx-v29` (`spec/09_…:48`) doesn't match real reference paths; prose mixes `für`/`fuer`.
- **L3 — V.250 result-code table invents numeric `5 = CONNECT 1200`** [WF std]. Not in V.250 Table 1 (`profiles/generic-hayes-v250.md:50`).
- **L4 — S2/S12 presented as V.250 registers** [WF std]. V.250 defines neither, and S12's unit is hand-waved "in 1/50 s oder profilabhängig" (`profiles/generic-hayes-v250.md:32,39`).
- **L5 — CME 50 text case differs from 27.007** [WF std]. "incorrect parameters" vs "Incorrect parameters" (`profiles/3gpp-27007-27005.md:55`).
- **L6 — Westermo GSM profiles provide 3GPP SMS/network commands without inheriting any 3GPP base** [WF]. GD-01/GDW-11 (`profiles/westermo-family.md:44-51`).
- **L7 — `maskImei=false` default leaks IMEI with no rationale** [WF sec] (`spec/14_…:34`).
- **L8 — No authorization separation within injection types** [WF sec]. Read-only is all-or-nothing; the destructive raw-to-device send isn't gated separately from benign internal injection.
- **L9 — Data/call model is "syntactic only" with no acceptance test** [WF]. Cannot satisfy a DTE that negotiates carrier (`spec/06_…:139-149`).
- **L10 — Terminology drift for transfer direction** [WF]. `DTE_TO_MODEM`/`MODEM_TO_DTE` (events) vs `raw-device-to-modem`/`raw-modem-to-device` (injection). Harmonize on DTE/DCE.
- **L11 — `profile.schema.json` makes `extends` required**, which normative-base roots (no parent) can't satisfy (`profile.schema.json:5-9`) [WF]; and mandatory `<signal>`/full `<sim>` conflict with the "no hard-coded defaults" rule (`spec/09_…:78`).
- **L12 — Target OS / virtual-COM tools undecided** blocks the ch.03 "Muss in v1" serial matrix and Serial-Loopback acceptance (`spec/15_…:21`).

---

## Checked and dismissed (adversarially refuted)

- **"XSD has no home for vendor/status/commands/registers/coverage/deviations."** *Partly* refuted as stated — narrowed and kept as **C1** (vendor/status/extends are the real, verifiable gaps; the broad claim overreached).
- **"The multi-threaded Response-Scheduler makes RNG draw-order non-deterministic, so seeding is unachievable."** Refuted: draws can be taken deterministically at enqueue time on the single executor. The real, kept issue is that the seed/ordering is simply *undefined* (**C2**), not unachievable.
- **"Remote-injection guardrails (mTLS/token/localhost/rate-limit) are missing."** Refuted for JKamsker: v1 has **no HTTP/WebSocket API** (`spec/01_…:34`, `spec/08_…:82`), so the remote threat model doesn't apply. (It *did* apply to the worktree copy — see D0.)

---

## Worktree-divergence note

- **D0 — Two divergent spec copies, no source-of-record statement.** The worktree copy (`D:\…\objective-bell-68588e`) is Java 21 with a REST/WebSocket control API and no GUI; this JKamsker copy is Java 24, GUI-mandatory, REST/WS removed. Nearly every file differs. Codex's own #21 flags "package-of-record is unclear." **Action:** retire one copy or record which is canonical. *(The workflow's single CRITICAL — invalid JSON with literal newlines in `examples/injection-api.examples.http` and `spec/08` — applies only to the worktree copy, which still ships the REST examples; it does not exist in JKamsker.)*

---

## Suggested repair order

1. **Resolve source-of-record (D0)** and delete/retire the stale copy.
2. **Ownership + determinism (C2):** single state owner, scheduler ordering, one named seed, virtual clock.
3. **Lock v1 scope:** target profiles (H6), PDU/storage (M16), control lines (H4/H5), TCP bridge (M19), scenarios (M1), GUI toolkit (H14).
4. **Unify the profile schema (C1)** and add the semantic-validation pass + negative fixtures (H9); harden XML (H1).
5. **Fix protocol correctness:** sample state (C3), `+CREG` (H3), `ATH`/prompt/framing/chaining (H10), CMS 310 (M17).
6. **Make acceptance executable (C4):** byte-level matrix, coverage artifacts + `unknown==0` gate (L1), headless/GUI tests (H14).
7. **Redaction default-safe (H2)** and clean packaging/licensing (M20).
