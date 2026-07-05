# 05 - State Model: SIM, Netz und Signal

## Kernzustand

Der kanonische State ist ein strukturierter Baum. Makros, GUI-State-Patches, Szenarien und Eventlogs verwenden dieselben Pfade.

```yaml
state:
  sim:
    state: READY
    pinQueryEnabled: true
    pinRef: TEST_SIM_PIN
    pukRef: TEST_SIM_PUK
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
    rejectCauseType: null
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
      rejectCmsError: 500
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
  call:
    mode: command
    carrier: false
    incomingNumber: null
  modem:
    lifecycle: READY
    freezeMode: NONE
    bootDelayMs: 0
  modemLines:
    dtr: true
    dsr: true
    dcd: false
    ri: false
    rts: true
    cts: true
```

## Kanonische State-Pfade

Diese Pfade sind fuer `state-change`, Makro-`<when>`, Makro-`<set>`, Szenarien und Eventlog-Patches verbindlich:

| Pfad | Typ |
|---|---|
| `state.sim.state` | `READY`, `SIM_NOT_INSERTED`, `SIM_PIN_REQUIRED`, `SIM_PUK_REQUIRED`, `SIM_FAILURE`, `SIM_BUSY`, `SIM_WRONG` |
| `state.sim.pinRetries` | 0..10 |
| `state.sim.pukRetries` | 0..10 |
| `state.network.cregN` | 0..3 |
| `state.network.stat` | 0..11 |
| `state.network.lac` | Hex string oder null |
| `state.network.ci` | Hex string oder null |
| `state.network.act` | 0..13 oder null |
| `state.network.rejectCauseType` | non-negative integer oder null |
| `state.network.rejectCause` | non-negative integer oder null |
| `state.signal.rssi` | 0..31 oder 99 |
| `state.signal.ber` | 0..7 oder 99 |
| `state.sms.textMode` | boolean |
| `state.sms.storage` | `ME`, `SM`, `MT` |
| `state.call.mode` | `command`, `ringing`, `dialing`, `online-data`, `online-command` |
| `state.call.carrier` | boolean |
| `state.call.incomingNumber` | string oder null |
| `state.modem.lifecycle` | `READY`, `REBOOTING`, `FROZEN` |
| `state.modem.freezeMode` | `NONE`, `NO_RESPONSE`, `HOLD_TX`, `HOLD_RX_TX` |
| `state.modem.bootDelayMs` | non-negative integer |
| `state.modemLines.dtr/dsr/dcd/ri/rts/cts` | boolean |

## XML-Konfiguration

SIM- und Netzbetreiber-Eigenschaften sind Teil des initialen Profilzustands und muessen aus XML geladen werden koennen. Beispiel:

```xml
<initial-state>
  <sim state="READY"
       pinQueryEnabled="true"
       pinRef="TEST_SIM_PIN"
       pukRef="TEST_SIM_PUK"
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
                    rejectCmsError="500"/>
    <delays>
      <delay operation="sms-submit" minMs="500" maxMs="2500"/>
      <delay operation="dial" minMs="1000" maxMs="5000"/>
    </delays>
  </network>
  <signal rssi="18" ber="0"/>
  <modem-lines dtr="true" dsr="true" dcd="false" ri="false"/>
</initial-state>
```

Semantik:

- `sim.pinQueryEnabled` legt fest, ob die SIM-PIN-Abfrage modelliert wird.
- `sim.pinRef` referenziert einen Test-Secret-Namen aus Test-/Runtime-Konfiguration. Produktive SIM-PINs duerfen nicht als Klartext im Profil stehen.
- `sim.pukRef` referenziert den PUK-Secret-Namen. Wie bei `pinRef` ist Klartext-`puk` nur fuer explizit markierte Testfixtures erlaubt.
- `sim.pin` ist nur fuer explizit als Testfixture markierte Profile erlaubt; normale Profile verwenden `pinRef`.
- Wenn `pinQueryEnabled=true` und kein expliziter `sim.state` gesetzt ist, startet die Session in `SIM_PIN_REQUIRED`.
- Wenn `pinQueryEnabled=false`, muss `AT+CPIN?` bei eingelegter SIM `READY` liefern.
- `network.operator` modelliert Antworten fuer `AT+COPS?`.
- `network.smsRateLimit` begrenzt akzeptierte SMS-Submits pro Zeitfenster.
- `network.delays` definiert Antwortverzoegerungen pro Operation.

## SIM-Lock und Netzregistrierung

Eine PIN-gesperrte SIM darf nicht gleichzeitig als aktuell registriert modelliert werden. Wenn `state.sim.state` nicht `READY` ist:

- `AT+CPIN?` liefert den SIM-Zustand oder den passenden `+CME ERROR`.
- Netzabhaengige Befehle liefern profilabhaengig `+CME ERROR: SIM PIN required`, `+CME ERROR: SIM failure` oder einen explizit dokumentierten Stub-Status.
- `state.network.stat` fuer die aktive Registrierung muss `0`, `2` oder `4` sein; `1` und `5` sind ungueltig.
- Ein Profil darf einen latenten Zielzustand fuer nach dem Unlock speichern, aber nicht als aktuelle Registrierung ausgeben.

Nach erfolgreichem `AT+CPIN=<pin>` wechselt `state.sim.state` nach `READY`. Erst danach darf ein Szenario oder Makro die Registrierung auf `1` oder `5` setzen oder ein latenter Zielzustand aktiviert werden.

Falsche PIN-Eingaben reduzieren `state.sim.pinRetries` genau einmal pro Versuch. Wenn der Zaehler dadurch `0` erreicht, wechselt `state.sim.state` nach `SIM_PUK_REQUIRED`. In diesem Zustand akzeptiert `AT+CPIN="<puk>","<newPin>"` den korrekten PUK und eine neue 4- bis 8-stellige PIN; danach gelten `state.sim.state=READY`, `pinRetries=3` und der neue PIN-Wert bzw. Test-Secret-Wert. Falsche PUK-Eingaben reduzieren `state.sim.pukRetries`; bei `0` wechselt die SIM in einen nicht entsperrbaren Fehlerzustand (`SIM_FAILURE` oder profilierte Deviation).

## SIM-Zustaende

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

Minimal unterstuetzte Commands:

```text
AT+CREG=0
AT+CREG=1
AT+CREG=2
AT+CREG=3
AT+CREG?
AT+CREG=?
```

`cregN` steuert den Detailgrad. In v1 werden 0 bis 3 unterstuetzt. Location-Felder (`lac`, `ci`, `act`) duerfen nur ausgegeben werden, wenn der aktuelle Status registriert ist (`stat=1` oder `stat=5`) oder ein Profil eine dokumentierte Herstellerabweichung in `deviations` eintraegt.

| Interner Zustand | `stat` | Beispielantwort |
|---|---:|---|
| Nicht registriert, keine Suche | 0 | `+CREG: 2,0` |
| Home Network | 1 | `+CREG: 2,1,"00C3","00001234",7` |
| Suche laeuft | 2 | `+CREG: 2,2` |
| Registrierung abgelehnt | 3 | `+CREG: 3,3,0,11` |
| Kein Netz / unbekannt | 4 | `+CREG: 2,4` |
| Roaming | 5 | `+CREG: 2,5,"00C3","00001234",7` |
| SMS-only / CSFB / EPS-spezifisch | 6..11 | Profil muss `+CGREG`/`+CEREG`-Bezug und Ausgabeformat dokumentieren. |

`AccessTechnologyType` erlaubt 0..13, damit 5G/NR-orientierte Werte profilierbar sind. Profile, die solche Werte nutzen, muessen die Referenz im Coverage-Report nennen.

## Signalqualitaet mit `+CSQ`

```text
AT+CSQ
+CSQ: <rssi>,<ber>
OK
```

Zulaessige Werte:

- `rssi`: 0..31 oder 99.
- `ber`: 0..7 oder 99.

Szenarien:

| Szenario | `+CREG?` | `+CSQ` |
|---|---|---|
| Gutes Netz | `+CREG: 2,1,"00C3","00001234",7` | `+CSQ: 31,0` |
| Mittleres Netz | `+CREG: 2,1,"00C3","00001234",7` | `+CSQ: 15,3` |
| Schlechtes Netz | `+CREG: 2,1,"00C3","00001234",7` | `+CSQ: 2,7` |
| Kein Netz | `+CREG: 2,4` | `+CSQ: 99,99` |

## Szenario-Uebergaenge

Jeder Zustandswechsel kann URCs ausloesen. URCs werden als Scheduler-Eintraege vom `SessionActor` erzeugt.

```text
# Voraussetzung
AT+CREG=1
OK

# Injection: state.network.stat 1 -> 4
+CREG: 4

# Injection: state.network.stat 4 -> 1
+CREG: 1
```

## Fault-Simulationen

Faults sind deterministische State-Uebergaenge, die per GUI, Szenario oder Makro ausgelöst werden koennen. Sie laufen immer ueber den `SessionActor`, erzeugen `stateBefore/stateAfter` und werden auditierbar geloggt.

| Fault | State-Wirkung | Schnittstellenwirkung |
|---|---|---|
| Netz-Ausfall | `state.network.stat=4`, `state.signal.rssi=99`, `state.signal.ber=99` | Bei aktivem `+CREG`-URC-Modus wird `+CREG: 4` geplant; netzabhaengige Kommandos liefern profilabhaengige Fehler. |
| Netz-Wiederkehr | `state.network.stat=1` oder `5`, Signalwerte aus Fault-Action oder Profil | Bei aktivem `+CREG`-URC-Modus wird `+CREG: 1` oder `+CREG: 5` geplant. |
| Modem-Neustart | `state.modem.lifecycle=REBOOTING`, DSR/DCD false, Scheduler-Eintraege canceln | Bis Boot-Ende keine Antwort oder profildefinierte Boot-Antwort; danach `READY`, DSR true und optionale Boot-URCs. |
| Freeze | `state.modem.lifecycle=FROZEN`, `freezeMode` gesetzt | Je nach Modus keine Antworten, gehaltene TX-Queue oder ignorierte RX-Bytes; Unfreeze stellt `READY` wieder her. |

`REBOOTING` und `FROZEN` duerfen nicht als implizite Java-Thread-Blockade implementiert werden. Auch Freeze bleibt ein deterministischer Session-State, damit Replay, GUI und Stop weiterhin funktionieren.

## Fehlerszenarien

- Defekte SIM: `SIM_FAILURE`, blockiert netzabhaengige Befehle je Profil.
- SIM fehlt: `SIM_NOT_INSERTED`, `+CPIN?` liefert Fehler.
- PIN-Abfrage aktiv: `SIM_PIN_REQUIRED`, `AT+CPIN?` liefert `+CPIN: SIM PIN`; eine falsche PIN reduziert `pinRetries` und liefert profilabhaengig `+CME ERROR`.
- Kein Netz: `stat=4`, `+CSQ: 99,99`, SMS-Versand schlaegt mit `+CMS ERROR` oder Profilcode fehl.
- Registrierung abgelehnt: `stat=3`, optional Reject Cause ohne Location-Felder.
- Netzwerk-Timeout: Antwortverzoegerung, `+CME ERROR: 31` oder keine Antwort je Profil.
- Modem-Neustart: `state.modem.lifecycle=REBOOTING`, danach profilierter Boot-Ready-State.
- Modem-Freeze: `state.modem.lifecycle=FROZEN`, keine oder verzögerte Antworten je `freezeMode`.
