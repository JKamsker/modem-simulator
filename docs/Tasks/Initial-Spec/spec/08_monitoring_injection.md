# 08 - Monitoring und Injection

## Ziel

Die lokale GUI soll den Simulator beobachtbar und steuerbar machen. Sie darf den seriellen Datenpfad nicht blockieren und darf State nur ueber den `SessionActor` aendern.

## GUI

v1 stellt keine HTTP- oder WebSocket-Control-API bereit. Monitoring, Injection und Logs laufen ueber eine lokale JavaFX-Desktop-GUI. Headless-Funktionen bleiben fuer automatisierte Tests moeglich, sind aber nicht die Bedienoberflaeche fuer Integrationsanwender.

Pflichtbereiche der GUI:

| Bereich | Pflichtfelder / Aktionen | Test-ID-Prefix |
|---|---|---|
| Session/Port Control | Profil, Hauptport, optionaler Sniffer-Port, optionaler Manual-DCE-Port, gemeinsame Baudrate/Datenbits/Stopbits/Paritaet/Flow-Control, Start, Stop, Reconnect, Port-Lost-Anzeige | `session.*` |
| Live Log | Sequence, Zeit, Richtung, raw bytes, redigierter Text, Parser-Ergebnis, Handler/Makro, Result Code, Latenzen, State Before/After, Redaction Status | `log.*` |
| State Editor | SIM-State, PIN-Retry, PUK-Retry, Netzstatus, CREG-N, LAC/CI/AcT, Reject Cause, Signal, SMS-Storage, Call-Mode, Modem-Lifecycle, Freeze-Mode, ModemLines | `state.*` |
| Injection | `raw-dte-to-dce`, `raw-dce-to-dte`, parsed command, URC helper, State-Patch, safety confirmation | `inject.*` |
| Fault Control | Netz-Ausfall/-Wiederkehr, Modem-Reboot, Freeze/Unfreeze ausloesen | `fault.*` |
| Macro Control | Datei, Hash, Reload, Reload-Fehler, enable/disable je Macro-ID, Custom-Response-Liste | `macro.*` |
| Replay | Modus, Log-Datei, Profil-/Config-/Macro-Hash-Vergleich, virtuelle Clock, Divergenzen | `replay.*` |
| Export | Redigiertes JSONL, Texttranskript, Coverage-/Replay-Report | `export.*` |

Read-only-Modus deaktiviert alle Controls mit Prefix `state.*`, `fault.*`, `inject.*`, `macro.reload`, `macro.enable`, `macro.disable`, `replay.playToDte` und `session.reconnect`, laesst aber Filter, Export und Log-Auswahl aktiv.

GUI-Tests muessen Headless-JavaFX starten koennen und die Test-IDs verwenden. Mindestens ein Test prueft, dass Read-only alle mutierenden Controls deaktiviert.

## Eventmodell

Persistente Events validieren gegen `schemas/event-log.schema.json`.

```json
{
  "timestamp": "2026-07-02T12:00:00.123+02:00",
  "monotonicNanos": 1234567890,
  "sequence": 42,
  "sessionId": "main",
  "port": "COM7",
  "eventType": "HANDLER_RESULT",
  "direction": "DTE_TO_DCE",
  "rawHex": "41542B4353510D",
  "textEscaped": "AT+CSQ\\r",
  "parsedCommand": {"name":"+CSQ","kind":"EXTENDED_EXEC"},
  "profile": "sierra-hl6-hl8-v20",
  "profileHash": "sha256:...",
  "configHash": "sha256:...",
  "macroHash": "sha256:...",
  "initialStateHash": "sha256:...",
  "sessionSeed": 12345,
  "clockMode": "monotonic",
  "macroId": null,
  "result": "OK",
  "latencyMs": 2,
  "stateBefore": {"network":{"stat":1}},
  "stateAfter": {"network":{"stat":1}},
  "redaction": {
    "applied": true,
    "policy": "default-v1",
    "fields": [],
    "classes": []
  }
}
```

`rawHex` ist der redigierte persistente Wert. Falls sensible Bytes betroffen sind, steht dort `<redacted>` oder ein bytegenau definierter Maskierungswert; das Redaction-Objekt muss die betroffenen Felder und Klassen nennen.

## GUI-Workflows

State aendern:

1. Tab `State` oeffnen.
2. SIM-Zustand, Netzstatus, Signalwerte oder ModemLines setzen.
3. Aenderung anwenden.
4. GUI sendet `SessionCommand`.
5. Eventlog zeigt alten und neuen State mit Sequenznummer.

Fault ausloesen:

1. Tab `Fault Control` oeffnen.
2. Fault `network-outage`, `network-restore`, `modem-reboot`, `modem-freeze` oder `modem-unfreeze` waehlen.
3. Optionale Parameter wie Dauer, Ziel-`stat`, RSSI/BER oder Freeze-Mode setzen.
4. Aenderung anwenden.
5. Eventlog zeigt `FAULT_TRIGGERED` und die resultierenden State-/Scheduler-Events.

URC an DTE senden:

1. Tab `Injection` oeffnen.
2. Typ `raw-dce-to-dte` oder URC Helper waehlen.
3. Payload `+CREG: 4` eingeben.
4. Safety-Hinweis bestaetigen, wenn ein externer Port verbunden ist.
5. Senden.

Befehl intern einspeisen:

1. Tab `Injection` oeffnen.
2. Typ `raw-dte-to-dce` oder `parsed-command` waehlen.
3. Payload `AT+CSQ` eingeben.
4. Response im Live-Log pruefen.

Manuelle Modemantwort ueber PuTTY senden:

1. In `Session/Port Control` den Port mit Rolle `manual-dce-injection` aktivieren.
2. PuTTY mit denselben `serialLine`-Parametern wie der Hauptport verbinden.
3. Bytes oder Textantwort eingeben.
4. Simulator loggt die Eingabe als `raw-dce-to-dte` Injection.
5. Simulator sendet die Bytes an das am Hauptport angeschlossene Geraet.

Sniffer-Port aktivieren:

1. In `Session/Port Control` den Port mit Rolle `sniffer` aktivieren.
2. Sniffer-Tool mit denselben `serialLine`-Parametern wie der Hauptport verbinden.
3. Datenverkehr beobachten.
4. Sniffer-Eingaben werden ignoriert und duerfen State oder Hauptport nicht veraendern.

## Injection-Arten

| Typ | Zweck |
|---|---|
| `raw-dce-to-dte` | Bytes direkt an das externe Geraet senden. |
| `raw-dte-to-dce` | Bytes in den Parser einspeisen. |
| `parsed-command` | AT-Befehl ohne serielle Rohbytes ausfuehren. |
| `state-change` | SIM/Netz/Signal/SMS/Call/Lines aendern. |
| `macro-control` | Makros aktivieren/deaktivieren. |
| `replay` | Mitschnitt in einem der definierten Replay-Modi abspielen. |

Legacy-Richtungsaliases werden nicht akzeptiert; v1 nutzt DTE/DCE konsistent.

Der Port mit Rolle `manual-dce-injection` ist eine externe Quelle fuer `raw-dce-to-dte`. Er teilt Baudrate, Paritaet, Datenbits und Stopbits mit dem Hauptport und darf keine eigenen seriellen Parameter besitzen.

## Replay

Die GUI muss die drei Replay-Modi aus Kapitel 02 anbieten:

- `drive-from-captured-input`,
- `play-to-dte`,
- `validate-recompute`.

Vor Start vergleicht die GUI Profil-, Config-, Macro- und Initial-State-Hash. Divergenzen werden angezeigt und muessen fuer Playback explizit bestaetigt werden; `validate-recompute` bricht ohne Bestaetigungsmoeglichkeit fehl.

## Sicherheit

- Alle Injections werden vor Ausfuehrung auditierbar geloggt. Wenn Audit nicht moeglich ist, wird die Injection abgelehnt.
- `raw-dce-to-dte` ist separat durch `gui.allowUnsafeDceTransmit=true` und eine GUI-Bestaetigung geschuetzt.
- Optionaler Read-only-Modus verhindert Injection, State-Aenderungen, Macro-Control, Replay-to-DTE und Reconnect.
- Keine HTTP- oder WebSocket-Listener in v1.
- GUI-Aktionen duerfen den seriellen RX-Pfad nicht blockieren; sie werden als `SessionCommand` eingequeued.
