# 13 - Teststrategie und Abnahme

## Testebenen

| Ebene | Ziel |
|---|---|
| Unit | Parser, Handler, State, Makros isoliert testen. |
| Golden Transcript | Input/Output gegen erwartete Transkripte testen. |
| Serial Loopback | Virtuelles COM-Paar oder PTY mit echter Serial-API testen. |
| Hardware-in-the-loop | Externes Gerät gegen Simulator testen. |
| Record-Replay | Echte Modemtranskripte gegen Profil vergleichen. |

## Akzeptanzkriterien v1

1. Externes Gerät öffnet Port und `AT` liefert `OK`.
2. `ATE`, `ATQ`, `ATV`, `ATZ`, `AT&F`, `AT&W`, `AT&V` funktionieren für Basisprofile.
3. `AT+CREG=0..3`, `AT+CREG?`, `AT+CREG=?` funktionieren.
4. `AT+CSQ` liefert konfigurierbare Werte inklusive `99,99`.
5. `AT+CPIN?` bildet SIM-Zustände ab.
6. `AT+CMEE=0|1|2` steuert Fehlerformat.
7. SMS-Textmodus mit `AT+CMGF=1`, `AT+CMGS`, Prompt, Ctrl-Z und Fehlerantwort funktioniert.
8. XML-Makro erkennt SMS an Zielnummer und Body `smscommand dst` und liefert `+CMS ERROR: 123` oder eine definierte Rohantwort.
9. Monitoring loggt raw bytes, Text, Parser-Ergebnis, Handler/Makro, Result Code und State Before/After.
10. Injection kann URCs senden, Befehle einspeisen und State ändern.
11. Profil-Coverage-Report enthält keine unbekannten Befehle (`unknown = 0`) für aktivierte Zielprofile.

## Golden Transcript Beispiel

```text
# name: creg-no-network
> AT+CMEE=2
< OK

> AT+CREG=2
< OK

# state: network.stat=4, signal=99/99
> AT+CREG?
< +CREG: 2,4
OK

> AT+CSQ
< +CSQ: 99,99
OK

> AT+CMGS="+491701234567"
< > 
> smscommand dst
< +CMS ERROR: 123

```

## Profilverifikation

Für jedes Profil:

- Referenzmanual im `references/`-Index verlinken.
- Command-Liste extrahieren oder manuell pflegen.
- Jede Zeile mit `implemented_full`, `stub`, `unsupported_declared` oder `not_applicable` markieren.
- Mindestens Smoke-Test für Identität, `AT`, Fehler, Data-Mode und ggf. SMS.
- Für Herstellerprofile bevorzugt ein echtes Gerätetranskript pro kritischem Befehl.
