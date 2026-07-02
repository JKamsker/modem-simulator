# 00 - Glossar und Annahmen

## Annahmen

- "COM-Schnittstellen" bedeutet in dieser Spezifikation serielle Schnittstellen: klassische RS-232/RS-485-Ports, USB-Serial-Adapter, USB CDC/ACM Virtual COM Ports und virtuelle COM-Port-Paare.
- Das Programm wird in Java umgesetzt und läuft mindestens unter Windows und Linux.
- Virtuelle COM-Port-Treiber werden nicht selbst implementiert. Das System bindet bestehende virtuelle Port-Paare ein, zum Beispiel com0com, tty0tty, socat-PTY, Docker-Devices oder hardwareseitige USB-CDC-Endpunkte.
- "Vollständiger AT-Befehlssatz" bedeutet profilbezogen: Für ein konkretes Hersteller-/Modell-/Firmware-Profil ist jeder im gewählten Manual relevante Befehl entweder vollständig implementiert, als Stub gekennzeichnet oder als bewusst unsupported deklariert.
- Herstellerdokumente werden nicht inhaltlich kopiert, sondern über Coverage-Matrizen referenziert.

## Begriffe

| Begriff | Bedeutung |
|---|---|
| DTE | Data Terminal Equipment, also das externe Gerät oder die Anwendung, die AT-Befehle sendet. |
| DCE / TA / MT | Modem, Terminal Adapter oder Mobile Termination, hier durch den Simulator emuliert. |
| COM-Port | Serielle Schnittstelle, physisch oder virtuell. |
| AT-Command | Steuerbefehl mit AT-Präfix oder profilabhängiger Spezialform wie `A/` oder `+++`. |
| Result Code | Abschließende Antwort wie `OK`, `ERROR`, `NO CARRIER`, `CONNECT`. |
| URC | Unsolicited Result Code, also asynchrone Meldung wie `+CREG: 0`. |
| Macro | Konfigurierbare Regel, die Input, State und Profil matcht und Ausgabe oder State-Änderungen erzeugt. |
| Profile | Modell-/Firmware-spezifisches Verhalten eines simulierten Modems. |
| Scenario | Zeitlich geordnete Zustandsfolge, z. B. "SIM defekt", "kein Netz", "schwaches Netz". |

## Referenzanker

- Klassische AT-Syntax und Ergebniscode-Konventionen: ITU-T V.250.
- Mobilfunkbefehle: 3GPP TS 27.007.
- SMS/CBS-Verhalten: 3GPP TS 27.005.
- Herstellerabweichungen: Sierra Wireless/Semtech- und Westermo-Dokumentation pro Modellreihe.
