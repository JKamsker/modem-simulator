# 07 - XML Macro Engine

## Zweck

Die Macro Engine macht den Simulator testbar und projektspezifisch. Sie kann Befehle erkennen, Antworten überschreiben, Verzögerungen erzeugen, URCs senden und den Modemzustand verändern.

## Hook-Phasen

| Phase | Bedeutung |
|---|---|
| `before` | Läuft vor dem normalen Handler. Kann State ändern oder zusätzliche Ausgabe erzeugen. |
| `replace` | Ersetzt den normalen Handler vollständig. |
| `after` | Läuft nach dem normalen Handler. Kann Zusatzantworten/URCs erzeugen. |
| `on-state-change` | Läuft bei State-Änderungen. |
| `on-timer` | Läuft zeitgesteuert. |

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

```xml
<when>
  <state sim="SIM_FAILURE"/>
  <state networkRegistration="NO_NETWORK"/>
  <state rssiLessThan="5"/>
  <profile id="sierra-hl78xx-v29"/>
</when>
```

## Actions

```xml
<then>
  <delay ms="250" jitterMs="50"/>
  <emit line="+CME ERROR: 13"/>
  <emit raw="
+CREG: 4
"/>
  <set path="state.sim.state" value="SIM_FAILURE"/>
  <set path="state.signal.rssi" value="99"/>
  <event type="TEST_MARKER" message="SIM failure injected"/>
</then>
```

## Prioritäten

Makros werden nach `priority` absteigend ausgewertet. Bei gleicher Priorität entscheidet die Reihenfolge im XML. Ein `replace`-Makro mit Treffer beendet die normale Handler-Ausführung.

## Determinismus

Jedes Makro mit Jitter muss einen reproduzierbaren Random-Seed verwenden können:

```xml
<macros randomSeed="12345">
```

## Validierung

XML-Dateien müssen beim Laden validiert werden. Fehlerhaftes XML darf eine laufende Session nicht zerstören. Hot Reload funktioniert transaktional:

1. Neue XML-Datei laden.
2. Schema validieren.
3. Makros kompilieren.
4. Bei Erfolg atomar aktivieren.
5. Bei Fehler alte Konfiguration weiterverwenden und Event erzeugen.
