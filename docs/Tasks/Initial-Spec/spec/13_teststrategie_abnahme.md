# 13 - Teststrategie und Abnahme

## Testebenen

| Ebene | Ziel | CI-Tag |
|---|---|---|
| Unit | Parser, Handler, State, Makros isoliert testen. | default |
| Golden Transcript | Input/Output gegen erwartete Transkripte testen. | default |
| Schema/Semantic Validation | Profile, Makros, Szenarien, Config, Coverage. | default |
| Headless Replay | Virtuelle Clock, deterministische Scheduler-/Seed-Pruefung. | default |
| GUI Headless | JavaFX Controls, Read-only, Reload, Export. | `gui` |
| Serial Loopback | Virtuelles COM-Paar oder PTY mit echter Serial-API testen. | `serial-it` |
| Hardware-in-the-loop | Externes Geraet gegen Simulator testen. | manuell |

## Globale Fixture-Regeln

Alle Golden-Transcript-Fixtures verwenden:

```yaml
profile: acceptance-sierra-hl6-hl8-ready
commandTerminator: CR
responseTerminator: CRLF
defaultEcho: false
defaultVerbose: true
defaultQuiet: false
sessionSeed: 12345
clockMode: virtual
serialLine:
  baudRate: 115200
  dataBits: 8
  stopBits: 1
  parity: NONE
  flowControl: NONE
```

Default-Ausgabeformat:

```text
OK -> 0D 0A 4F 4B 0D 0A
ERROR -> 0D 0A 45 52 52 4F 52 0D 0A
SMS prompt -> 0D 0A 3E 20
```

Serielle Integrationslaeufe duerfen bei wall-clock Timing eine Toleranz von `max(20 ms, 10% der erwarteten Verzoegerung)` haben. Headless-/Replay-Tests mit virtueller Clock haben `0 ms` Timing-Toleranz.

## Akzeptanzmatrix v1

| ID | Kriterium | Fixture / Initial State | Eingabe -> erwartete Ausgabe / Events | CI-Kommando |
|---|---|---|---|---|
| A01 | Port oeffnet und `AT` liefert `OK`. | `acceptance-basic`, headless und serial-it. | `41 54 0D` -> `0D 0A 4F 4B 0D 0A`. | `modemsim test --suite acceptance --case A01` |
| A02 | Hayes-Basisbefehle. | `generic-hayes-v250`. | `ATE0`, `ATE1`, `ATQ0`, `ATQ1`, `ATV0`, `ATV1`, `ATZ`, `AT&F`, `AT&W`, `AT&V` haben je definierte Transcript-Dateien. `ATQ1` unterdrueckt finale Result Codes. | `modemsim test --suite hayes` |
| A03 | `+CREG` 0..3. | `state.sim.state=READY`, `state.network.stat=1`, `cregN=2`. | `AT+CREG=2\r` -> OK; `AT+CREG?\r` -> `+CREG: 2,1,"00C3","00001234",7` + OK; denied fixture `stat=3` -> `+CREG: 3,3,0,11` + OK. | `modemsim test --suite cellular --case creg` |
| A04 | `+CSQ` inklusive unknown. | `signal.rssi=18,ber=0` und `99,99`. | `AT+CSQ\r` -> `+CSQ: 18,0` + OK; no-network -> `+CSQ: 99,99` + OK. Invalid `rssi=32` wird bei Profilvalidierung abgelehnt. | `modemsim test --suite cellular --case csq` |
| A05 | XML-Profilvalidierung. | `examples/modem-profile.sample.xml` plus negative Profile/Macro-Fixtures. | XSD + Semantic-Validation erfolgreich; geladener State enthaelt SIM, Operator, Rate-Limit, Delays, ModemLines; kein Klartext-PIN/PUK im Eventlog. Negative CI-Fixtures fuer `pin`+`pinRef`, `puk`+`pukRef`, `SIM_PUK_REQUIRED` ohne PUK-Material, `delay/@minMs > delay/@maxMs`, `operator/@numeric != mcc+mnc` und Macro-`delay/@jitterMs` ohne `sessionSeed` werden abgelehnt. | `modemsim test --suite validation --case profile-macro-negatives` |
| A06 | SIM PIN/PUK Workflow. | `acceptance-sim-locked`, `state.sim.state=SIM_PIN_REQUIRED`, `network.stat=0`, `pinRef=TEST_SIM_PIN`, `pukRef=TEST_SIM_PUK`. | `AT+CPIN?\r` -> `+CPIN: SIM PIN` + OK; falsche PIN reduziert `pinRetries`; bei `0` -> `SIM_PUK_REQUIRED`; falsche PUK reduziert `pukRetries`; `AT+CPIN="87654321","1234"\r` -> OK, State `READY`. Registrierung bleibt `0`, bis Szenario/Makro sie setzt. | `modemsim test --suite cellular --case cpin-puk` |
| A07 | `AT+COPS?` aus XML. | Operator Telekom.de, numeric 26201. | `AT+COPS?\r` -> `+COPS: 0,0,"Telekom.de",7` + OK oder profildefiniertes Format. | `modemsim test --suite cellular --case cops` |
| A08 | `AT+CMEE=0\|1\|2`. | SIM failure fixture. | Fehlerausgabe wechselt zwischen `ERROR`, `+CME ERROR: 13`, `+CME ERROR: SIM failure`. | `modemsim test --suite cellular --case cmee` |
| A09 | SMS Textmodus. | `sms.textMode=true`, registered. | `AT+CMGF=1\r` -> OK; `AT+CMGS="..."\r` -> `0D0A3E20`; body+Ctrl-Z -> `+CMGS: <mr>` + OK oder definierter CMS-Fehler. | `modemsim test --suite sms --case text-cmgs` |
| A10 | SMS Rate-Limit. | `maxMessages=5`, `windowSeconds=60`, virtual clock. | 5 Submits in `(now-60s, now]` akzeptiert; 6. Submit -> `+CMS ERROR: 500`; nach `advance 60001ms` naechster Submit akzeptiert. | `modemsim test --suite sms --case rate-limit` |
| A11 | Delays reproduzierbar. | `sessionSeed=12345`, delay ranges fuer `sms-submit` und `dial`. | Zwei Headless-Laeufe erzeugen identische `SCHEDULER_ENQUEUE.sampledDelayMs`; alle Werte innerhalb Min/Max; serial-it innerhalb Toleranz. | `modemsim test --suite scheduler --case deterministic-delays` |
| A12 | SMS-Makro. | `examples/macros.sms-error-123.xml`. | Zielnummer + Body `smscommand dst` -> `+CMS ERROR: 123`; Scheduler-Event enthaelt Macro-ID und Delay. | `modemsim test --suite macros --case sms-error-123` |
| A13 | GUI Live Log. | JavaFX headless mit virtuellem Delay-Fixture. | `log.table` zeigt raw bytes, Text, Parser-Ergebnis, Handler/Makro, Result Code, gemessene Latenzen, State Before/After und Redaction; ein Fixture mit bekanntem nicht-null Delay prueft den exakten `latencyMs`-Wert; `log.filter` reduziert die sichtbaren Zeilen und Export nutzt die gefilterte Menge. | `modemsim test --tags gui --case live-log` |
| A14 | GUI Injection. | JavaFX headless. | `raw-dte-to-dce`, URC helper und `state-change` erzeugen `SessionCommand`; `raw-dce-to-dte` ist blockiert, wenn `allowUnsafeDceTransmit=false`. | `modemsim test --tags gui --case injection` |
| A15 | Keine HTTP/WS Control API. | Runtime smoke. | Portscan/Process inspection findet keinen HTTP- oder WebSocket-Listener des Simulators. | `modemsim test --suite security --case no-http-ws` |
| A16 | Coverage `unknown == 0`. | Alle v1-Zielprofile. | Coverage validiert gegen `schemas/coverage.schema.json`; `commands_total == len(commands)` und `unknown == 0`. | `modemsim coverage verify --profiles v1-targets` |
| A17 | XML-Hardening. | Negative XXE/entity/schema fixtures. | DOCTYPE, externe Entity, XInclude, externe Schema-URL, Entity Expansion und zu tiefe Baeume werden abgelehnt. | `modemsim test --suite security --case xml-hardening` |
| A18 | Replay deterministisch. | Test-sichere Eventlogs fuer Validierung und Playback. | `validate-recompute` prueft Hashes, Initial-State, Sequenzen, Scheduler-Events und bricht bei Divergenz ab; `drive-from-captured-input` und `play-to-dte` scheitern bei Hash-Divergenz ohne Bestaetigung und laufen mit Bestaetigung weiter; `<redacted>`, fehlende Nutzbytes und `replayDivergent=true` scheitern in allen Replay-Modi. | `modemsim replay logs/fixture.jsonl --mode validate-recompute`; `modemsim replay logs/playback-divergence.jsonl --mode drive-from-captured-input --confirm-divergence --case playback-divergence`; `modemsim replay logs/playback-divergence.jsonl --mode play-to-dte --confirm-divergence --case playback-divergence` |
| A19 | ModemLines/Datenmodus. | `generic-hayes-v250`, `lineModel=minimal-v250`. | `ATD123\r` -> `CONNECT`, DCD true; `+++` mit Guard-Time -> Online Command; `ATH\r` -> OK, DCD false. | `modemsim test --suite hayes --case data-mode-lines` |
| A20 | SMS Storage/PDU Mindestumfang. | SMS store ME/SM. | `CMGR/CMGL/CMGD/CPMS/CSCA` deterministisch; PDU `CMGS=<len>` akzeptiert opaque PDU nur, wenn `totalOctets == 1 + smscLength + len`, oder liefert spezifizierten Fehler. | `modemsim test --suite sms --case storage-pdu` |
| A21 | Packet-Registration ohne Sierra-LTE-Scope. | Sierra v1 targets und 3GPP-Basis. | `+CGREG` ist fuer Sierra-v1-Targets nicht unknown; `+CEREG` bleibt nur 3GPP-Basis-/Stub-Abdeckung und begruendet kein Sierra-LTE-Zielprofil. | `modemsim test --suite cellular --case packet-registration-stubs` |
| A22 | Drei-Port-Gruppe mit gemeinsamen seriellen Parametern. | Config mit `modem-simulation` enabled, optionalem `sniffer` und optionalem `manual-dce-injection`. | Config mit port-spezifischer Baudrate/Parity/DataBits/StopBits wird abgelehnt; Hauptport ist immer aktiv; Sniffer empfaengt Traffic ohne State-Aenderung; Bytes vom Manual-DCE-Port werden als `raw-dce-to-dte` an den Hauptport gesendet und auditierbar geloggt. | `modemsim test --suite serial-config --case port-group` |
| A23 | Fault-Simulationen. | `examples/macros.faults-and-custom-responses.xml`, virtual clock. | Network outage setzt `stat=4` und `CSQ=99,99`; Restore setzt Zielwerte; Reboot setzt `REBOOTING` und danach `READY`; Freeze mit `NO_RESPONSE` blockiert Antworten ohne Threads zu blockieren. | `modemsim test --suite faults --case lifecycle-and-network` |
| A24 | Custom Responses aus XML. | `examples/macros.faults-and-custom-responses.xml`. | Input `AT+CMSG123\r` matcht `rawGlob=AT+CMSG*` und sendet bytegenau `0D 45 52 52 0D` (`<CR>ERR<CR>`). | `modemsim test --suite macros --case custom-response-cmsg` |
| A25 | Unknown-AT-Command Policy. | Vier Profile mit `unknownAtCommand=OK`, `ERR`, `ERROR`, `restart`. | Unbekanntes `AT+UNKNOWN\r` liefert jeweils `OK`, `ERR`, `ERROR` oder triggert `FAULT_TRIGGERED`/`modem-reboot`; andere Werte werden bei Profilvalidierung abgelehnt. | `modemsim test --suite profiles --case unknown-at-command-policy` |
| A26 | Parser-Fehler und `ATA`. | Hayes/3GPP fixtures mit virtuellem Ruf. | Unterminierte Quotes, ungueltige S-Register-Parameter und malformed Extended-Commands erzeugen `PARSE_ERROR`/Fehlerantwort; `ATA` ohne Ruf liefert Fehler, mit `state.call.mode=ringing` -> `CONNECT`, DCD true, `online-data`. | `modemsim test --suite parser --case malformed-and-ata` |
| A27 | Macro Hot Reload mit Timern. | Runtime-Config mit `macroTimers: [{id: heartbeat, atMs: 1000}]`. | GUI-Reload akzeptiert gueltige `on-timer`-Makros mit Runtime-Timer-ID, lehnt unbekannte Timer ab, storniert alte Timer mit `cancelled=true`; ein fehlgeschlagener Reload belaesst alte Macro-Version, alte Timer-Queue/In-flight-Timer und enable/disable-State unveraendert. | `modemsim test --tags gui --case macro-hot-reload-timers` |
| A28 | Event-Backpressure und Audit-Hard-Stop. | Bounded EventSink-Testdouble. | Nicht audit-kritische Drops erzeugen ein dauerhaft geschriebenes `DROPPED_EVENTS` mit positivem `droppedEventCount`; das gleiche positive Delta erscheint am naechsten regulaer persistierten Event und wird danach auf `0` zurueckgesetzt. Das Summary-Event selbst darf nicht lautlos gedroppt werden. Mutierende Injection/State/Macro/Replay-Aktionen werden bei Audit-Write-Fehler vor Ausfuehrung abgelehnt; `PORT_LOST` und datenverlustige RX/TX-Overflows fail-closed ohne stillen State-Advance, Reconnect oder fortgesetztes IO, wenn ihre Diagnose nicht durable geschrieben werden kann. | `modemsim test --suite security --case audit-backpressure` |
| A29 | Port-Diagnosen. | Headless + Serial-Endpoint-Testdoubles. | Open-Time `not found`, `busy`, `permission denied`, `unsupported parameters`, RX/TX overflow und Port-Loss erzeugen unterschiedliche Diagnosecodes und schema-gueltige `PORT_OPEN_FAILED`/Transportevents; optionale Sidecar-Ports respektieren `strictOptionalPorts`. | `modemsim test --suite serial-config --case diagnostics` |
| A30 | Source-Size-Gate. | Repo mit kuenstlich uebergrosser Java-Testdatei in isoliertem Fixture. | Der Java-basierte `SourceSizeGate` bricht `mvn verify` plattformneutral ab, wenn nicht generierter Java-Code `>=300` LOC hat; der Maven-Exec-Schritt propagiert den Fehlercode. | `modemsim test --suite build --case source-size-gate` |
| A31 | Golden-Transcript YAML Loader. | Versionierte YAML-Fixtures unter `tests/golden/*.yaml`. | Der Loader fuehrt YAML-Fixtures mit Metadaten, Byte-IO und Event-Oracles aus; die CI scheitert, wenn nur inline Hex-Tests existieren oder die YAML-Fixtures nicht geladen werden koennen. | `modemsim test --suite acceptance --case golden-yaml-loader` |
| A32 | Profilierte S-Register-Grenzen. | Zwei Profile mit unterschiedlichem `S7`-Max und Schreibbarkeit. | `ATS7=<value>` wird je aktivem Profil gegen dessen effektiven Min/Max/Writable-Metadaten geprueft; ein Wert, der in Profil A gueltig und in Profil B ungueltig ist, verhaelt sich entsprechend unterschiedlich. | `modemsim test --suite profiles --case s-register-bounds` |

## Golden Transcript Format

Golden Transcripts bestehen aus Metadaten, Byte-IO und optionalen Event-Erwartungen:

```yaml
name: creg-no-network
profile: acceptance-sierra-hl6-hl8-ready
clockMode: virtual
sessionSeed: 12345
initialState:
  state.network.stat: 4
  state.signal.rssi: 99
  state.signal.ber: 99
steps:
  - inputHex: "41542B434D45453D320D"
    outputHex: "0D0A4F4B0D0A"
  - inputHex: "41542B435245473D320D"
    outputHex: "0D0A4F4B0D0A"
  - inputHex: "41542B435245473F0D"
    outputHex: "0D0A2B435245473A20322C340D0A0D0A4F4B0D0A"
  - inputHex: "41542B4353510D"
    outputHex: "0D0A2B4353513A2039392C39390D0A0D0A4F4B0D0A"
```

Der YAML-Loader fuer Golden Transcripts ist verpflichtend. Inline-Hex in Tests darf zusaetzlich existieren, ersetzt aber nicht die versionierten YAML-Fixtures fuer die Abnahmematrix.

Timing-Erwartungen werden nicht durch sleeps ausgedrueckt, sondern durch Scheduler-Events:

```yaml
expectEvents:
  - eventType: SCHEDULER_ENQUEUE
    operation: sms-submit
    sampledDelayMsWithin: [500, 2500]
```

## Profilverifikation

Fuer jedes v1-Zielprofil:

- Referenzmanual im `references/`-Index verlinken.
- Command-Liste extrahieren oder manuell pflegen.
- Jede Zeile mit `implemented_full`, `implemented_stub`, `unsupported_declared` oder `not_applicable` markieren.
- Coverage-Report gegen `schemas/coverage.schema.json` validieren.
- Smoke-Test fuer Identitaet, `AT`, Fehler, Data-Mode und ggf. SMS.
- Fuer Herstellerprofile bevorzugt ein echtes Geraetetranskript pro kritischem Befehl.
