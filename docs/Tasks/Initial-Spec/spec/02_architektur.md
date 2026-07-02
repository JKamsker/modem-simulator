# 02 - Architektur

## Komponentenübersicht

```text
Externes Gerät / Testsoftware
        |
        | physischer COM-Port, USB-Serial, USB CDC/ACM oder virtuelles Port-Paar
        v
+--------------------------------------------------+
| Serial Endpoint Layer                            |
| - Port öffnen/schließen                          |
| - Baudrate, Parität, Stopbits, Datenbits         |
| - RTS/CTS, DTR/DSR/DCD/RI                        |
+--------------------------------------------------+
        |
        v
+--------------------------------------------------+
| AT Framing & Parser                              |
| - CR/LF, Echo, Backspace, A/, +++                |
| - Command Mode / Online Command / Data Mode      |
| - Set/Read/Test/Exec-Erkennung                   |
+--------------------------------------------------+
        |
        v
+--------------------------------------------------+
| Command Router & Profile Engine                  |
| - Generic Hayes / V.250                          |
| - 3GPP 27.007 / 27.005                           |
| - Herstellerprofile                              |
| - Macro Hooks before/replace/after               |
+--------------------------------------------------+
        |
        v
+--------------------------------------------------+
| Modem State Machine                              |
| - SIM, Netzregistrierung, Signalqualität         |
| - SMS, Call, Data Session, S-Register            |
| - Persistente Settings pro Profil                |
+--------------------------------------------------+
        |
        +--------------------+
        | XML Macro Engine   |
        +--------------------+
        |
        v
+--------------------------------------------------+
| Response Scheduler                               |
| - Delays, Jitter, URCs, Result Codes             |
+--------------------------------------------------+
        |
        v
+--------------------------------------------------+
| Monitor / Injection API                          |
| - JSONL Event Log                                |
| - REST / WebSocket / CLI                         |
| - State Injection / Raw Injection / Replay       |
+--------------------------------------------------+
```

## Laufzeitmodell

Jeder geöffnete Port erzeugt eine Session. Eine Session besitzt:

- genau ein aktives Profil,
- einen eigenen Parserzustand,
- einen eigenen Modem-State,
- eine eigene Makro-Instanzliste,
- einen eigenen Event-Stream,
- optionale Persistenzdateien für S-Register und `AT&W`-artige Speicherung.

## Wichtige Designentscheidung

Der Parser darf keine Herstellerlogik enthalten. Er muss nur Frames, AT-Syntaxformen und Betriebsmodi erkennen. Herstellerunterschiede werden in Command Handlern, Profilmetadaten und Makros modelliert.

## Extension Points

| Extension Point | Zweck |
|---|---|
| `CommandHandler` | Implementiert einen konkreten Befehl wie `+CREG` oder `&V`. |
| `ProfileDialect` | Legt Line-Endings, Prefixe, Echo, Error-Policy und Antwortformate fest. |
| `StateProvider` | Liefert und verändert SIM-/Netz-/SMS-/Call-State. |
| `MacroProvider` | Lädt XML-Makros, priorisiert und matched sie. |
| `TransportEndpoint` | Serielle Ports, virtuelle Ports, TCP-Bridge oder Headless-Test. |
| `EventSink` | JSONL, WebSocket, Console, Datei oder Test-Harness. |
