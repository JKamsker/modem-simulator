# 06 - SMS und Datenmodus

## SMS-Basisbefehle

Mindestumfang für v1:

```text
AT+CMGF=0|1
AT+CMGF?
AT+CMGF=?
AT+CMGS="<destination>"
AT+CMGS=<pdu-length>
AT+CMGR=<index>
AT+CMGL="ALL"
AT+CMGD=<index>
AT+CNMI=...
AT+CPMS=...
AT+CSCA?
AT+CSCA="<smsc>"
```

## SMS Text Mode Flow

```text
DTE -> AT+CMGF=1
DCE -> OK

DTE -> AT+CMGS="+491701234567"
DCE -> > 
DTE -> smscommand dst
DCE -> +CMGS: 42
OK

```

Bei Fehler:

```text
DTE -> AT+CMGS="+491701234567"
DCE -> > 
DTE -> smscommand dst
DCE -> +CMS ERROR: 123

```

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
<emit raw="
ATHayesErrorCode 123
"/>
```

## SMS-Speicher

Für v1 reicht ein In-Memory-Speicher:

```yaml
smsStore:
  ME:
    capacity: 50
    messages: []
  SM:
    capacity: 20
    messages: []
```

Jede Nachricht enthält:

```yaml
index: 1
status: REC UNREAD|REC READ|STO UNSENT|STO SENT|ALL
sender: "+491701234567"
recipient: "+491701234568"
timestamp: "2026-07-02T12:00:00+02:00"
text: "payload"
pdu: null
```

## Datenmodus

Das System muss Datenmodus mindestens syntaktisch abbilden:

- `ATD<number>` -> `CONNECT` oder Fehler.
- Nach `CONNECT`: Bytes werden transparent geloggt und nicht als AT interpretiert.
- `+++` mit Guard-Time -> Online Command Mode.
- `ATH` -> Hangup, `NO CARRIER`, DCD aus.
- `ATO` -> zurück in Online Data Mode.

Die echte Modulation oder PSTN-Verbindung wird nicht simuliert; es geht um die Schnittstellenwirkung gegenüber dem DTE.
