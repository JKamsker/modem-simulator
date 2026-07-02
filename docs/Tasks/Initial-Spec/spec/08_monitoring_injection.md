# 08 - Monitoring und Injection

## Ziel

Die Zusatzschnittstelle soll den Simulator beobachtbar und steuerbar machen. Sie darf den seriellen Datenpfad nicht blockieren.

## APIs

Empfohlen:

- REST für punktuelle Aktionen und State-Abfragen.
- WebSocket für Live-Events und Mitschnitt.
- CLI für lokale Tests und CI.
- JSONL-Datei für reproduzierbare Logs.

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

## REST-Beispiele

State abfragen:

```http
GET /api/v1/sessions/main/state
```

URC an Gerät senden:

```http
POST /api/v1/sessions/main/inject
Content-Type: application/json

{
  "direction": "MODEM_TO_DTE",
  "data": "
+CREG: 4
"
}
```

Befehl intern einspeisen:

```http
POST /api/v1/sessions/main/inject
Content-Type: application/json

{
  "direction": "DTE_TO_MODEM",
  "data": "AT+CSQ"
}
```

State ändern:

```http
POST /api/v1/sessions/main/state
Content-Type: application/json

{
  "sim": {"state": "SIM_FAILURE"},
  "network": {"stat": 4},
  "signal": {"rssi": 99, "ber": 99}
}
```

## Injection-Arten

| Typ | Zweck |
|---|---|
| `raw-modem-to-device` | Bytes direkt an das externe Gerät senden. |
| `raw-device-to-modem` | Bytes in den Parser einspeisen. |
| `parsed-command` | AT-Befehl ohne serielle Rohbytes ausführen. |
| `state-change` | SIM/Netz/Signal/SMS/Lines ändern. |
| `macro-control` | Makros aktivieren/deaktivieren. |
| `replay` | Vorherigen Mitschnitt mit Timings abspielen. |

## Sicherheit

- Default: API bindet nur an `127.0.0.1`.
- Remote-Bind nur mit Authentifizierung.
- Alle Injections werden auditierbar geloggt.
- Kein unauthentifiziertes WebSocket auf externen Interfaces.
- Optionaler Read-only-Modus für Monitoring ohne Injection.
