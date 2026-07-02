# GUI-Workflows

## Session starten

1. Profil `examples/modem-profile.sample.xml` auswaehlen.
2. Hauptport `COM7` oder virtuelles Port-Paar auswaehlen.
3. Gemeinsame `serialLine`-Parameter fuer Baudrate, Datenbits, Stopbits und Paritaet pruefen.
4. Optional Sniffer-Port und Manual-DCE-Port auswaehlen, aber nur bei Bedarf aktivieren.
5. Session starten.
6. Live-Log muss eingehende und ausgehende Bytes anzeigen.

## State aendern

1. Tab `State` oeffnen.
2. SIM-Zustand auf `SIM_FAILURE` setzen.
3. Netzwerkstatus auf `4` setzen.
4. Signal auf `99,99` setzen.
5. Aenderung anwenden.
6. Audit-Log muss die State-Aenderung mit altem und neuem Wert enthalten.

## URC senden

1. Tab `Injection` oeffnen.
2. Typ `raw-dce-to-dte` oder URC Helper waehlen.
3. Payload `+CREG: 4` eingeben.
4. Safety-Hinweis bestaetigen, falls ein externer Port verbunden ist.
5. Senden.
6. Live-Log muss die injizierte Ausgabe als Injection markieren.

## Manuelle Antwort per PuTTY senden

1. In `Session` den optionalen Manual-DCE-Port aktivieren.
2. PuTTY mit derselben Baudrate, Paritaet, Datenbits und Stopbits wie der Hauptport verbinden.
3. Modemantwort eingeben.
4. Das am Hauptport angeschlossene Geraet muss die Bytes empfangen.
5. Live-Log muss die Quelle als Manual-DCE-Port markieren.

## Sniffer-Port nutzen

1. In `Session` den optionalen Sniffer-Port aktivieren.
2. Sniffer-Tool mit derselben Baudrate, Paritaet, Datenbits und Stopbits wie der Hauptport verbinden.
3. Datenverkehr beobachten.
4. Eingaben am Sniffer-Port duerfen den Hauptport und State nicht veraendern.

## AT-Befehl einspeisen

1. Tab `Injection` oeffnen.
2. Typ `raw-dte-to-dce` oder `parsed-command` waehlen.
3. Payload `AT+CSQ` eingeben.
4. Senden.
5. Response-Log muss Parser-Ergebnis, Handler und Result Code anzeigen.

## Read-only pruefen

1. Read-only aktivieren.
2. Tabs `State`, `Injection`, `Macro Control` und `Replay` pruefen.
3. Mutierende Controls muessen deaktiviert sein; Log-Filter und Export bleiben aktiv.
