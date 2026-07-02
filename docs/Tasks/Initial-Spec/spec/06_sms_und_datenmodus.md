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

## Netzseitige SMS-Rate-Limits

Das Profil kann definieren, wie viele SMS das simulierte Netz pro Zeitfenster akzeptiert. Das Limit liegt unter `initial-state/network`, weil es eine Eigenschaft des aktuell simulierten Netzes bzw. Netzbetreibers ist.

```xml
<network cregN="2" stat="1">
  <sms-rate-limit maxMessages="5"
                  windowSeconds="60"
                  scope="session"
                  rejectCmsError="310"/>
</network>
```

Semantik:

- `maxMessages` ist die Anzahl akzeptierter SMS innerhalb des Fensters.
- `windowSeconds` ist die Laenge des Zeitfensters in Sekunden; die Implementierung verwendet ein Sliding Window pro Scope.
- `scope=session` zaehlt alle SMS einer Session gemeinsam. Weitere zulaessige Scopes sind `recipient` und `operator`.
- Wird das Limit ueberschritten, wird die SMS nicht gespeichert und nicht als versendet gezaehlt; der Handler liefert `+CMS ERROR: <rejectCmsError>`.
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
- Pro Ausfuehrung wird ein Wert im Bereich `minMs..maxMs` gezogen. Die Ziehung muss ueber den Session-Seed reproduzierbar sein.
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
- `ATH` -> Hangup, `NO CARRIER`; physisches DCD-Schalten ist in v1 nicht relevant.
- `ATO` -> zurück in Online Data Mode.

Die echte Modulation oder PSTN-Verbindung wird nicht simuliert; es geht um die Schnittstellenwirkung gegenüber dem DTE.
