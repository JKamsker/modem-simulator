# 07 - XML Macro Engine

## Zweck

Die Macro Engine macht den Simulator testbar und projektspezifisch. Sie kann Befehle erkennen, Antworten ueberschreiben, Verzoegerungen erzeugen, URCs senden und den Modemzustand veraendern.

## Hook-Phasen

| Phase | Bedeutung |
|---|---|
| `before` | Laeuft vor dem normalen Handler. Kann State aendern oder zusaetzliche Ausgabe erzeugen. |
| `replace` | Ersetzt den normalen Handler vollstaendig. |
| `after` | Laeuft nach dem normalen Handler. Kann Zusatzantworten/URCs erzeugen. |
| `on-state-change` | Laeuft bei State-Aenderungen. |
| `on-timer` | Laeuft zeitgesteuert. |

## Match-Kriterien

```xml
<match command="+CREG" mode="read"/>
<match rawGlob="AT+CMSG*"/>
<match rawRegex="^AT\+CSQ\s*$"/>
<match type="sms-submit">
  <destination equals="+491701234567"/>
  <body contains="smscommand dst"/>
</match>
```

## Conditions

Conditions verwenden ausschliesslich die kanonischen State-Pfade aus Kapitel 05.

```xml
<when>
  <state path="state.sim.state" equals="SIM_FAILURE"/>
  <state path="state.network.stat" equals="4"/>
  <state path="state.signal.rssi" lessThan="5"/>
  <profile id="sierra-hl6-hl8-v20"/>
</when>
```

## Actions

```xml
<then>
  <delay ms="250" jitterMs="50"/>
  <emit line="+CME ERROR: 13"/>
  <emit rawHex="0D0A2B435245473A20340D0A"/>
  <set path="state.sim.state" value="SIM_FAILURE"/>
  <set path="state.signal.rssi" value="99"/>
  <fault type="network-outage"/>
  <event type="TEST_MARKER" message="SIM failure injected"/>
</then>
```

State-`set`-Aktionen werden nicht direkt ausgefuehrt. Sie erzeugen `SessionCommand`-Nachrichten und laufen ueber den `SessionActor`.

Fault-Actions sind Kurzformen fuer haeufige Fehlerbilder:

```xml
<then>
  <fault type="network-outage"/>
  <delay ms="5000"/>
  <fault type="network-restore" stat="1" rssi="18" ber="0"/>
</then>
```

```xml
<then>
  <fault type="modem-reboot" durationMs="3000"/>
</then>
```

```xml
<then>
  <fault type="modem-freeze" freezeMode="NO_RESPONSE"/>
</then>
```

Die Fault-Typen setzen die kanonischen State-Pfade aus Kapitel 05 und erzeugen dieselben Audit-/Scheduler-Events wie explizite State-Patches.

## Custom Responses

Fuer einfache projektspezifische Antworten kann eine XML-Datei `custom-response`-Eintraege enthalten. Diese Kurzform wird beim Laden zu einem `replace`-Makro kompiliert.

Beispiel: Wenn ein DTE ein Kommando sendet, das auf `AT+CMSG*` passt, soll bytegenau `<CR>ERR<CR>` gesendet werden:

```xml
<macros version="1.0">
  <custom-response id="cmsg-error" priority="100">
    <if rawGlob="AT+CMSG*"/>
    <send text="&lt;CR&gt;ERR&lt;CR&gt;"/>
  </custom-response>
</macros>
```

Semantik:

- `rawGlob` matcht gegen die komplette eingehende AT-Zeile nach Backspace-Verarbeitung, aber vor normalisierter Command-Aufloesung.
- `*` steht fuer beliebig viele Zeichen, `?` fuer genau ein Zeichen. Matching ist case-insensitive, sofern das Profil nichts anderes festlegt.
- `send/@text` unterstuetzt Tokens `<CR>`, `<LF>`, `<CRLF>`, `<ESC>` und `<CTRL-Z>`.
- Fuer bytegenaue nichtdruckbare Antworten ist `send/@rawHex` vorzuziehen.
- `send/@line` erzeugt eine normale Response-Zeile mit dem aktuellen S3/S4-Response-Formatter.
- Ein `custom-response` darf genau eine Send-Nutzlast haben: `text`, `rawHex` oder `line`.
- Custom Responses nehmen am normalen Priority-System teil. Bei gleicher Prioritaet gilt Dokumentreihenfolge.

## Prioritaeten

Makros werden nach `priority` absteigend ausgewertet. Bei gleicher Prioritaet entscheidet die Reihenfolge im XML. Ein `replace`-Makro mit Treffer beendet die normale Handler-Ausfuehrung.

## Determinismus

Jedes Makro mit Jitter nutzt den `sessionSeed` der Session. Das optionale Attribut `randomSeed` auf `<macros>` dient nur als stabiler Macro-Substream-Wert:

```xml
<macros version="1.0" randomSeed="12345">
```

Es ersetzt den `sessionSeed` nicht. Alle gezogenen Makro-Delays werden als Scheduler-Events mit `sampledDelayMs` geloggt.

## Validierung

XML-Dateien muessen beim Laden validiert werden:

1. XML-Hardening aus Kapitel 14 anwenden.
2. Gegen `schemas/macro-schema-draft.xsd` validieren.
3. Semantisch validieren.
4. Makros kompilieren.
5. Bei Erfolg atomar aktivieren.
6. Bei Fehler alte Konfiguration weiterverwenden und Event erzeugen.

Semantic-Validation muss mindestens ablehnen:

- mehrdeutige String-Predicates, z. B. `equals` und `contains` gleichzeitig,
- `custom-response/if` ohne genau ein Match-Kriterium,
- `custom-response/send` ohne genau eine Nutzlast,
- `delay/@jitterMs` ohne reproduzierbaren Session-Seed,
- `emit` ohne genau eine Nutzlast (`line`, `raw`, `rawHex` oder Textinhalt),
- `fault`-Attribute, die nicht zum Fault-Typ passen,
- unbekannte State-Pfade,
- State-Werte ausserhalb des State-Schemas,
- Regexe, die nicht kompilieren,
- `on-timer` ohne Timer-Definition in der Runtime-Konfiguration.

Negative Fixtures fuer diese Faelle gehoeren in die CI.

## Hot Reload

Hot Reload ist transaktional:

1. Neue XML-Datei laden.
2. Schema und Semantic-Validation ausfuehren.
3. Makros kompilieren.
4. Neue Macro-Version mit Hash erzeugen.
5. In-flight `on-timer`-Eintraege der alten Version stornieren und als `cancelled=true` loggen.
6. Neue Version atomar aktivieren.
7. Bei Fehler alte Version, Timer und enabled/disabled-Zustaende unveraendert weiterverwenden.

Manuell deaktivierte Makros bleiben nach Reload deaktiviert, wenn ein Makro mit derselben ID wieder existiert. Entfernte Makros verlieren ihren Runtime-State.
