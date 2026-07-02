# 05 - State Model: SIM, Netz und Signal

## Kernzustand

```yaml
state:
  sim:
    state: SIM_PIN_REQUIRED
    pinQueryEnabled: true
    pin: "1234"
    pinRetries: 3
    pukRetries: 10
    imsi: "262010123456789"
    iccid: "8949020000000000000"
  network:
    cregN: 2
    stat: 1
    lac: "00C3"
    ci: "00001234"
    act: 7
    rejectCause: null
    operator:
      selectionMode: automatic
      format: long
      longName: "Telekom.de"
      shortName: "TDG"
      numeric: "26201"
      mcc: "262"
      mnc: "01"
    smsRateLimit:
      maxMessages: 5
      windowSeconds: 60
      scope: session
      rejectCmsError: 310
    delays:
      - operation: sms-submit
        minMs: 500
        maxMs: 2500
      - operation: dial
        minMs: 1000
        maxMs: 5000
  signal:
    rssi: 18
    ber: 0
  sms:
    textMode: true
    smsc: "+491710760000"
    storage: ME
```

## XML-Konfiguration

SIM- und Netzbetreiber-Eigenschaften sind Teil des initialen Profilzustands und muessen aus XML geladen werden koennen. Beispiel:

```xml
<initial-state>
  <sim state="SIM_PIN_REQUIRED"
       pinQueryEnabled="true"
       pin="1234"
       pinRetries="3"
       pukRetries="10"
       imsi="262010123456789"
       iccid="8949020000000000000"/>
  <network cregN="2" stat="1" lac="00C3" ci="00001234" act="7">
    <operator selectionMode="automatic"
              format="long"
              longName="Telekom.de"
              shortName="TDG"
              numeric="26201"
              mcc="262"
              mnc="01"/>
    <sms-rate-limit maxMessages="5"
                    windowSeconds="60"
                    scope="session"
                    rejectCmsError="310"/>
    <delays>
      <delay operation="sms-submit" minMs="500" maxMs="2500"/>
      <delay operation="dial" minMs="1000" maxMs="5000"/>
    </delays>
  </network>
</initial-state>
```

Semantik:

- `sim.pinQueryEnabled` legt fest, ob die SIM-PIN-Abfrage aktiv ist.
- `sim.pin` ist die erwartete Test-PIN fuer `AT+CPIN=<pin>`. Echte produktive SIM-PINs duerfen nicht in Profilen, Logs oder Events landen.
- Wenn `pinQueryEnabled=true` und kein expliziter `sim.state` gesetzt ist, startet die Session in `SIM_PIN_REQUIRED`; nach korrektem `AT+CPIN=<pin>` wechselt sie nach `READY`.
- Wenn `pinQueryEnabled=false`, muss `AT+CPIN?` bei eingelegter SIM `READY` liefern, auch wenn ein Test-PIN-Wert im Profil hinterlegt ist.
- `network.operator` modelliert die Angaben fuer Netzbetreiber-Antworten wie `AT+COPS?`; `network.stat` und `cregN` steuern weiterhin die Registrierungsantworten fuer `AT+CREG?`.
- `network.smsRateLimit` legt fest, wie viele SMS das simulierte Netz innerhalb eines Zeitfensters akzeptiert. Nach Erreichen des Limits liefert der SMS-Submit den konfigurierten `+CMS ERROR`.
- `network.delays` definiert zufaellige Antwortverzoegerungen pro Operation. Fuer jede Ausfuehrung wird ein Wert zwischen `minMs` und `maxMs` gezogen; `minMs=maxMs` modelliert eine feste Verzoegerung.

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
- PIN-Abfrage aktiv: `SIM_PIN_REQUIRED`, `AT+CPIN?` liefert `+CPIN: SIM PIN`; eine falsche PIN reduziert `pinRetries` und liefert profilabhaengig `+CME ERROR`.
- Kein Netz: `stat=4`, `+CSQ: 99,99`, SMS-Versand schlägt mit `+CMS ERROR` oder Profilcode fehl.
- Registrierung abgelehnt: `stat=3`, optional Reject Cause.
- Netzwerk-Timeout: Antwortverzögerung, `+CME ERROR: 31` oder keine Antwort je Profil.
