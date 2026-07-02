# GUI-Workflows

## Session starten

1. Profil `examples/modem-profile.sample.xml` auswaehlen.
2. Port `COM7` oder virtuelles Port-Paar auswaehlen.
3. Baudrate, Datenbits, Stopbits, Paritaet und Line-Endings pruefen.
4. Session starten.
5. Live-Log muss eingehende und ausgehende Bytes anzeigen.

## State aendern

1. Tab `State` oeffnen.
2. SIM-Zustand auf `SIM_FAILURE` setzen.
3. Netzwerkstatus auf `4` setzen.
4. Signal auf `99,99` setzen.
5. Aenderung anwenden.
6. Audit-Log muss die State-Aenderung mit altem und neuem Wert enthalten.

## URC senden

1. Tab `Injection` oeffnen.
2. Richtung `Modem -> Device` waehlen.
3. Payload `+CREG: 4` eingeben.
4. Senden.
5. Live-Log muss die injizierte Ausgabe als Injection markieren.

## AT-Befehl einspeisen

1. Tab `Injection` oeffnen.
2. Richtung `Device -> Modem` waehlen.
3. Payload `AT+CSQ` eingeben.
4. Senden.
5. Response-Log muss Parser-Ergebnis, Handler und Result Code anzeigen.
