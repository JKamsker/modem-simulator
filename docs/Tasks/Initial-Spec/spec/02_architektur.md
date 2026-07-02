# 02 - Architektur

## Komponentenuebersicht

```text
Externes Geraet / Testsoftware
        |
        | physischer COM-Port, USB-Serial, USB CDC/ACM oder virtuelles Port-Paar
        v
+--------------------------------------------------+
| Transport Endpoint Layer                         |
| - Port oeffnen/schliessen                        |
| - Byte-Stream plus RX-Zeitstempel                |
| - optional minimale Modem-Control-Lines          |
+--------------------------------------------------+
        |
        v
+--------------------------------------------------+
| SessionActor                                     |
| - einzige Schreibinstanz fuer ModemState         |
| - sequenziert RX, GUI, Makros, Replay, Timer     |
| - erzeugt stateBefore/stateAfter und Eventlog    |
+--------------------------------------------------+
        |
        +--> AT Framing & Parser
        +--> Command Router & Profile Engine
        +--> XML Macro Engine
        +--> Deterministic Response Scheduler
        +--> Local GUI / Monitor / Injection
```

## Laufzeitmodell

Jeder geoeffnete Port erzeugt eine Session. Eine Session besitzt:

- genau ein aktives Profil,
- einen eigenen Parserzustand,
- einen eigenen `ModemState`,
- eine eigene Makro-Instanzliste,
- einen eigenen Event-Stream,
- einen eigenen `sessionSeed`,
- optionale Persistenzdateien fuer S-Register und `AT&W`-artige Speicherung.

## State Ownership

Pro Session gibt es genau einen `SessionActor`. Nur dieser Actor darf den `ModemState` schreiben. RX-Thread, GUI, Replay, Macro-Timer und Scheduler duerfen State nicht direkt mutieren; sie stellen `SessionCommand`-Nachrichten in die Actor-Queue.

Der Actor verarbeitet Nachrichten sequentiell und vergibt eine streng monoton steigende `sequence`. Jede State-aendernde Operation erzeugt ein Event mit:

- `sequence`,
- `stateBefore`,
- `stateAfter`,
- Quelle (`rx`, `gui`, `macro`, `replay`, `internal`),
- Profil-/Config-/Macro-Hash.

Damit sind URCs, Responses, Injections und `stateBefore/stateAfter` reproduzierbar.

## Deterministischer Scheduler

Delayed responses, URCs und Timer laufen ueber den Response Scheduler der Session. Scheduler-Eintraege werden ausschliesslich vom `SessionActor` angelegt und enthalten:

```text
dueMonotonicNanos
sequence
sourcePriority
operation
sampledDelayMs
cancelOnStateVersion
```

Ausgabereihenfolge ist strikt:

1. `dueMonotonicNanos` aufsteigend.
2. `sequence` aufsteigend.
3. `sourcePriority` in der Reihenfolge `internal`, `replay`, `rx`, `gui`, `macro`.

Bei State-Wechseln, Session-Stop und `PORT_LOST` werden Scheduler-Eintraege storniert, wenn ihr `cancelOnStateVersion` nicht mehr zur aktuellen State-Version passt oder der Eintrag explizit an die verlorene Session gebunden ist. Stornierungen werden als `SCHEDULER_EMIT` oder `SCHEDULER_ENQUEUE` mit `cancelled=true` geloggt.

## Seed und Clock

Es gibt genau einen Seed pro Session: `sessionSeed`. Er kommt aus `config.yaml`, einem CLI-Argument oder wird beim Session-Start kryptographisch zufaellig gezogen. Der tatsaechlich genutzte Wert wird im `SESSION_START`-Event geloggt.

Alle Pseudozufallsziehungen fuer Profil-Delays, Makro-Jitter und Replay-Jitter verwenden deterministische Unterstreams aus diesem `sessionSeed`. Makrodateien duerfen `randomSeed` nur als stabilen Macro-Substream-Namen liefern; sie ersetzen den Session-Seed nicht.

Headless-Tests, Golden Transcripts und Replay-Validierung muessen eine virtuelle Clock verwenden. Serielle Integrationslaeufe verwenden eine monotone Clock und tolerieren nur die in Kapitel 13 definierten Timing-Abweichungen.

## Replay-Modi

Replay ist kein einzelner Modus, sondern drei explizite Betriebsarten:

| Modus | Zweck | Eingaben |
|---|---|---|
| `drive-from-captured-input` | Erneut dieselben DTE-Bytes in Parser/Handler fahren. | Redigierter oder als test-sicher markierter Eventlog, Profil, Config, Seed. |
| `play-to-dte` | Aufgezeichnete DCE-Ausgabe mit Timing an ein Geraet senden. | Eventlog mit freigegebenen DCE-Bytes, virtuelle oder monotone Clock. |
| `validate-recompute` | Aktuelle Implementierung gegen alte Erwartung pruefen. | Eventlog, initialer State-Hash, Profil-/Config-/Macro-Hashes. |

Bei Hash-, State- oder Timing-Divergenz bricht Replay im Validierungsmodus mit Diagnose ab. Im Playback-Modus darf kein Eventlog mit redigierten Nutzbytes abgespielt werden, wenn dadurch Bytes fehlen.

## Wichtige Designentscheidung

Der Parser enthaelt keine Herstellerlogik. Er erkennt Frames, AT-Syntaxformen, Betriebsmodi und Zeitluecken. Herstellerunterschiede werden in Command Handlern, Profilmetadaten und Makros modelliert.

## Extension Points

| Extension Point | Zweck |
|---|---|
| `CommandHandler` | Implementiert einen konkreten Befehl wie `+CREG` oder `&V`. |
| `ProfileDialect` | Legt Default-Line-Endings, Echo, Error-Policy, Prompt-Bytes und Antwortformate fest. |
| `StateModel` | Definiert die kanonischen State-Pfade; Schreibzugriff nur ueber den `SessionActor`. |
| `MacroProvider` | Laedt XML-Makros, priorisiert und matched sie. |
| `TransportEndpoint` | Serielle Ports, virtuelle Ports oder Headless-Teststreams. |
| `EventSink` | JSONL, GUI-Modell, Console, Datei oder Test-Harness. |
