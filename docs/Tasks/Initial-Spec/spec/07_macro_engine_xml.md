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
  <profile id="sierra-hl78xx-v29"/>
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
  <event type="TEST_MARKER" message="SIM failure injected"/>
</then>
```

State-`set`-Aktionen werden nicht direkt ausgefuehrt. Sie erzeugen `SessionCommand`-Nachrichten und laufen ueber den `SessionActor`.

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
- `delay/@jitterMs` ohne reproduzierbaren Session-Seed,
- `emit` ohne genau eine Nutzlast (`line`, `raw`, `rawHex` oder Textinhalt),
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
