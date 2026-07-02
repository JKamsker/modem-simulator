# 06 - SMS und Datenmodus

## SMS-Basisbefehle

Mindestumfang fuer v1:

| Befehl | v1-Scope |
|---|---|
| `AT+CMGF=0|1`, `AT+CMGF?`, `AT+CMGF=?` | `implemented_full` |
| `AT+CMGS="<destination>"` | Textmodus full mit Prompt, Ctrl-Z, ESC und Fehlerantworten |
| `AT+CMGS=<pdu-length>` | PDU-Submit syntaktisch full, PDU-Inhalt opaque speichern, kein Encoding/Decoding |
| `AT+CMGR=<index>` | In-Memory-Speicher lesen, deterministic errors |
| `AT+CMGL=<stat>` | In-Memory-Speicher listen, `ALL` verpflichtend |
| `AT+CMGD=<index>` | In-Memory-Speicher loeschen |
| `AT+CNMI=...` | Syntax und gespeicherte Einstellung, URC-Auslieferung fuer v1 optional |
| `AT+CPMS=...` | `ME`, `SM`, `MT` aus In-Memory-Speicher |
| `AT+CSCA?`, `AT+CSCA="<smsc>"` | SMSC aus XML-State lesen/schreiben |

Coverage darf diese Befehle nicht als unbekannt zaehlen. Nicht implementierte Semantik muss als `implemented_stub` oder `unsupported_declared` dokumentiert sein.

## SMS Text Mode Flow

Bytegenau bei Defaults S3=CR, S4=LF, Echo aus:

```text
DTE bytes: 41 54 2B 43 4D 47 46 3D 31 0D
DCE bytes: 0D 0A 4F 4B 0D 0A

DTE bytes: 41 54 2B 43 4D 47 53 3D 22 2B 34 39 31 37 30 31 32 33 34 35 36 37 22 0D
DCE bytes: 0D 0A 3E 20
DTE bytes: 73 6D 73 63 6F 6D 6D 61 6E 64 20 64 73 74 1A
DCE bytes: 0D 0A 2B 43 4D 47 53 3A 20 34 32 0D 0A 0D 0A 4F 4B 0D 0A
```

Bei Fehler:

```text
DCE bytes after Ctrl-Z: 0D 0A 2B 43 4D 53 20 45 52 52 4F 52 3A 20 31 32 33 0D 0A
```

Das Prompt ist nicht ein bare `> `, sondern `<S3><S4>> `.

## Netzseitige SMS-Rate-Limits

Das Profil kann definieren, wie viele SMS das simulierte Netz pro Zeitfenster akzeptiert. Das Limit liegt unter `initial-state/network`, weil es eine Eigenschaft des aktuell simulierten Netzes bzw. Netzbetreibers ist.

```xml
<network cregN="2" stat="1">
  <sms-rate-limit maxMessages="5"
                  windowSeconds="60"
                  scope="session"
                  rejectCmsError="500"/>
</network>
```

Semantik:

- `maxMessages` ist die Anzahl akzeptierter SMS innerhalb des Fensters.
- `windowSeconds` ist die Laenge des Sliding Windows.
- Das Fenster ist links offen und rechts geschlossen: `(now - windowSeconds, now]`.
- Die Implementierung nutzt die Session-Clock. Headless-Tests und Replay verwenden virtuelle Zeit.
- `scope=session` zaehlt alle SMS einer Session gemeinsam. Weitere zulaessige Scopes sind `recipient` und `operator`.
- Wird das Limit ueberschritten, wird die SMS nicht gespeichert und nicht als versendet gezaehlt; der Handler liefert `+CMS ERROR: <rejectCmsError>`.
- `rejectCmsError=310` ist fuer Rate-Limits verboten, weil 310 "SIM not inserted" bedeutet. Fuer generische Netz-/Kapazitaetsfehler sind 331, 332 oder 500 zu verwenden.
- Fehlt `sms-rate-limit`, ist kein netzseitiges SMS-Rate-Limit aktiv.

## Netzseitige Delays

Das Profil kann fuer SMS-Versand, Einwahl und weitere netznahe Operationen Antwortverzoegerungen als Bereich definieren.

```xml
<network cregN="2" stat="1">
  <delays>
    <delay operation="sms-submit" minMs="500" maxMs="2500"/>
    <delay operation="dial" minMs="1000" maxMs="5000"/>
  </delays>
</network>
```

Semantik:

- `operation=sms-submit` gilt fuer die Zeit zwischen Ctrl-Z/PDU-Ende und finaler `+CMGS`-/`+CMS ERROR`-Antwort.
- `operation=dial` gilt fuer die Zeit zwischen `ATD...` und `CONNECT`, `BUSY`, `NO CARRIER` oder Fehler.
- Pro Ausfuehrung wird ein Wert im Bereich `minMs..maxMs` gezogen.
- Die Ziehung nutzt den `sessionSeed`; es gibt keinen zweiten Delay-Seed.
- Der gezogene Wert wird als `sampledDelayMs` im Scheduler-Event geloggt.
- Makro-Delays haben Vorrang, wenn ein Makro den Handler ersetzt; bei `before`/`after`-Makros werden Makro- und Profil-Delays additiv geplant.
- Fehlt ein Delay fuer eine Operation, antwortet der Handler ohne zusaetzliche netzseitige Profilverzoegerung.

## SMS-Makro-Fall aus Anforderung

Ziel: Input `smscommand dst` soll eine definierte Fehlerantwort erzeugen.

```xml
<macro id="smscommand-dst-error-123" priority="100" phase="replace">
  <match type="sms-submit">
    <destination equals="+491701234567"/>
    <body contains="smscommand dst"/>
  </match>
  <then>
    <delay ms="250"/>
    <emit line="+CMS ERROR: 123"/>
  </then>
</macro>
```

Herstellerspezifische Rohantwort:

```xml
<emit raw="ATHayesErrorCode 123&#x0D;&#x0A;"/>
```

## SMS-Speicher

Fuer v1 reicht ein In-Memory-Speicher:

```yaml
smsStore:
  ME:
    capacity: 50
    messages: []
  SM:
    capacity: 20
    messages: []
```

Jede Nachricht enthaelt:

```yaml
index: 1
status: REC UNREAD|REC READ|STO UNSENT|STO SENT
sender: "+491701234567"
recipient: "+491701234568"
timestamp: "2026-07-02T12:00:00+02:00"
text: "payload"
pdu: null
```

PDU-Submit speichert `pdu` als rohe Hex-/Textsequenz und validiert nur Laenge, Ctrl-Z/ESC und Storage-Kapazitaet. PDU-Encoding/Decoding ist post-v1, solange kein Profil es explizit als `implemented_full` mit Tests markiert.

## Datenmodus

Das System muss Datenmodus mindestens schnittstellenwirksam abbilden:

- `ATD<number>` -> `CONNECT` oder Fehler.
- Nach `CONNECT`: `state.call.mode=online-data`, `state.call.carrier=true`, DCD true.
- Bytes im Data Mode werden transparent geloggt und nicht als AT interpretiert.
- Der Parser ueberwacht weiterhin die `+++`-Escape-Sequenz mit Guard-Time.
- `+++` wechselt nach `ONLINE_COMMAND_MODE`, ohne Carrier zu trennen.
- `ATO` kehrt nach `ONLINE_DATA_MODE` zurueck.
- `ATH` trennt, setzt DCD false, `state.call.carrier=false`, `state.call.mode=command` und liefert `OK`.

Echte Modulation, PSTN-Verbindung oder Remote-Endpunkt-Nutzdaten werden nicht simuliert; es geht um die Schnittstellenwirkung gegenueber dem DTE.
