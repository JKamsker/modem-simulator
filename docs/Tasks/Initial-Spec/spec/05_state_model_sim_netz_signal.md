# 05 - State Model: SIM, Netz und Signal

## Kernzustand

```yaml
state:
  sim:
    state: READY
    pinRequired: false
    imsi: "262010123456789"
  network:
    cregN: 2
    stat: 1
    lac: "00C3"
    ci: "00001234"
    act: 7
    rejectCause: null
  signal:
    rssi: 18
    ber: 0
  sms:
    textMode: true
    smsc: "+491710760000"
    storage: ME
```

## SIM-Zustände

| Interner Zustand | `AT+CPIN?` Beispiel | Fehlercode |
|---|---|---|
| `READY` | `+CPIN: READY` | - |
| `SIM_NOT_INSERTED` | `+CME ERROR: 10` | SIM not inserted |
| `SIM_PIN_REQUIRED` | `+CPIN: SIM PIN` | 11 bei blockierenden Kommandos |
| `SIM_PUK_REQUIRED` | `+CPIN: SIM PUK` | 12 |
| `SIM_FAILURE` | `+CME ERROR: 13` | SIM failure |
| `SIM_BUSY` | `+CME ERROR: 14` | SIM busy |
| `SIM_WRONG` | `+CME ERROR: 15` | SIM wrong |

## Netzwerkregistrierung mit `+CREG`

Minimal unterstützte Commands:

```text
AT+CREG=0
AT+CREG=1
AT+CREG=2
AT+CREG=3
AT+CREG?
AT+CREG=?
```

Zustände:

| Interner Zustand | `stat` | Beispielantwort |
|---|---:|---|
| Nicht registriert, keine Suche | 0 | `+CREG: 2,0` |
| Home Network | 1 | `+CREG: 2,1,"00C3","00001234",7` |
| Suche läuft | 2 | `+CREG: 2,2` |
| Registrierung abgelehnt | 3 | `+CREG: 3,3,"00C3","00001234",7,0,11` |
| Kein Netz / unbekannt | 4 | `+CREG: 2,4` |
| Roaming | 5 | `+CREG: 2,5,"00C3","00001234",7` |

`cregN` steuert die URC-Ausgabe und den Detailgrad. Für v1 werden die Varianten 0 bis 3 unterstützt.

## Signalqualität mit `+CSQ`

```text
AT+CSQ
+CSQ: <rssi>,<ber>
OK
```

Szenarien:

| Szenario | `+CREG?` | `+CSQ` |
|---|---|---|
| Gutes Netz | `+CREG: 2,1,"00C3","00001234",7` | `+CSQ: 31,0` |
| Mittleres Netz | `+CREG: 2,1,"00C3","00001234",7` | `+CSQ: 15,3` |
| Schlechtes Netz | `+CREG: 2,1,"00C3","00001234",7` | `+CSQ: 2,7` |
| Kein Netz | `+CREG: 2,4` | `+CSQ: 99,99` |

## Szenario-Übergänge

Jeder Zustandswechsel kann URCs auslösen:

```text
# Voraussetzung
AT+CREG=1
OK

# Injection: network.stat 1 -> 4
+CREG: 4

# Injection: network.stat 4 -> 1
+CREG: 1
```

## Fehlerszenarien

- Defekte SIM: `SIM_FAILURE`, blockiert netzabhängige Befehle je Profil.
- SIM fehlt: `SIM_NOT_INSERTED`, `+CPIN?` liefert Fehler.
- Kein Netz: `stat=4`, `+CSQ: 99,99`, SMS-Versand schlägt mit `+CMS ERROR` oder Profilcode fehl.
- Registrierung abgelehnt: `stat=3`, optional Reject Cause.
- Netzwerk-Timeout: Antwortverzögerung, `+CME ERROR: 31` oder keine Antwort je Profil.
