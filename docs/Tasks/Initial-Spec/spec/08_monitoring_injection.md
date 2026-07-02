# 08 - Monitoring und Injection

## Ziel

Die lokale GUI soll den Simulator beobachtbar und steuerbar machen. Sie darf den seriellen Datenpfad nicht blockieren.

## GUI

v1 stellt keine HTTP- oder WebSocket-Control-API bereit. Monitoring, Injection und Logs laufen ueber eine lokale Desktop-GUI. Headless-Funktionen bleiben fuer automatisierte Tests moeglich, sind aber nicht die Bedienoberflaeche fuer Integrationsanwender.

Pflichtbereiche der GUI:

| Bereich | Zweck |
|---|---|
| Session/Port Control | Profil, Port, Baudrate und Session-Lifecycle steuern. |
| Live Log | Raw Bytes, Text, Parser-Ergebnis, Handler/Makro, Result Code und Latenzen anzeigen. |
| State Editor | SIM, Netz, Signal, SMS und Profil-Timing zur Laufzeit aendern. |
| Injection | Rohbytes, URCs, interne AT-Befehle und State-Patches einspeisen. |
| Macro Control | Makros aktivieren, deaktivieren und Reload-Fehler anzeigen. |
| Replay | Mitschnitte mit Timing wiedergeben. |
| Export | JSONL und Texttranskript speichern. |

## Eventmodell

```json
{
  "timestamp": "2026-07-02T12:00:00.123+02:00",
  "monotonicNanos": 1234567890,
  "sessionId": "main",
  "port": "COM7",
  "direction": "DTE_TO_MODEM",
  "rawHex": "41542B4353510D",
  "textEscaped": "AT+CSQ\r",
  "parsedCommand": "+CSQ",
  "profile": "sierra-hl78xx-v29",
  "macroId": null,
  "result": "OK",
  "latencyMs": 2,
  "stateBefore": {"network":{"stat":1}},
  "stateAfter": {"network":{"stat":1}}
}
```

## GUI-Workflows

State aendern:

1. Tab `State` oeffnen.
2. SIM-Zustand, Netzstatus oder Signalwerte setzen.
3. Aenderung anwenden.
4. Eventlog zeigt alten und neuen State.

URC an Geraet senden:

1. Tab `Injection` oeffnen.
2. Richtung `Modem -> Device` waehlen.
3. Payload `+CREG: 4` eingeben.
4. Senden.

Befehl intern einspeisen:

1. Tab `Injection` oeffnen.
2. Richtung `Device -> Modem` waehlen.
3. Payload `AT+CSQ` eingeben.
4. Response im Live-Log pruefen.

## Injection-Arten

| Typ | Zweck |
|---|---|
| `raw-modem-to-device` | Bytes direkt an das externe Geraet senden. |
| `raw-device-to-modem` | Bytes in den Parser einspeisen. |
| `parsed-command` | AT-Befehl ohne serielle Rohbytes ausfuehren. |
| `state-change` | SIM/Netz/Signal/SMS aendern. |
| `macro-control` | Makros aktivieren/deaktivieren. |
| `replay` | Vorherigen Mitschnitt mit Timings abspielen. |

## Sicherheit

- Alle Injections werden auditierbar geloggt.
- Optionaler Read-only-Modus fuer Monitoring ohne Injection.
- Keine HTTP- oder WebSocket-Listener in v1.
- GUI-Aktionen duerfen den seriellen RX-Pfad nicht blockieren.
