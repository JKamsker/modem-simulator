# Changelog

## v0.3 - 2026-07-02

- Alegs3-Spec als Source of Record markiert.
- XSD als autoritatives Runtime-Profilformat festgelegt; JSON-Profil-Schema zu Catalog/Coverage-Helper demotiert.
- SessionActor, deterministischen Scheduler, `sessionSeed`, virtuelle Clock und Replay-Modi spezifiziert.
- v1-Zielprofile, JavaFX-GUI, Ziel-OS, Virtual-COM-Tools und TCP-Bridge-Out-of-Scope festgelegt.
- Profile-, Macro-, Scenario-, Config-, Coverage- und Eventlog-Schemas ergaenzt bzw. gehaertet.
- SIM/Network-Beispiel korrigiert: keine PIN-gesperrte SIM mit aktiver Registrierung.
- `+CREG`, `+CSQ`, SMS-Prompt, `ATH`, Rate-Limit-CMS-Code und S-Register-Framing korrigiert.
- Akzeptanzkriterien durch ausfuehrbare Matrix mit Byte-IO, Event-Oracles, Timing-Toleranzen und CI-Kommandos ersetzt.
- Redaction default-on fuer Logs, GUI, Export und Replay festgelegt.
- Lokale Referenzdownloads als nicht redistributable Arbeitskopien markiert.

## v0.2 - 2026-07-02

- Spec in mehrere Markdown-Dateien aufgeteilt.
- XML-Profilkonfiguration fuer SIM-PIN-Abfrage, Netzbetreiber-Eigenschaften, SMS-Rate-Limits und Netz-Delays ergaenzt.
- Modem-Control-Lines wie DTR fuer v1 aus dem Pflichtumfang genommen.
- HTTP/WebSocket-Control-API durch lokale GUI fuer Control Pane, Logs, Injection und Replay ersetzt.
- Java-Zielversion auf Java 24 gesetzt.
- Sierra-Wireless/Semtech-Profilfamilie erweitert um HL6/HL8, HL78xx, MC/SL UMTS/LTE, EM75xx und EM9.
- Westermo-Profilfamilie erweitert um TD-20 Candidate, TD-22, TD-32, TD-33, TD-36, GD-01, GDW-11 und IDW-90.
- XML-Macro-Engine für Input->Output, SMS-Matches, State Conditions und Response Injection spezifiziert.
- Referenzordner mit Link-Dateien und selbst erzeugtem Referenzindex-PDF ergänzt.

## v0.1

- Initiale Monospezifikation: COM/Serial, AT-Parser, +CREG, SIM-/Netzsimulation, SMS-Makros, Monitoring und Injection.
