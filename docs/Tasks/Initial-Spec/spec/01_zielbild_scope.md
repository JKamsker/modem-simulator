# 01 - Zielbild und Scope

## Zielbild

Das System ist ein Java-basierter Modem-Simulator. Es stellt über eine physische oder virtuelle serielle Schnittstelle ein Modem dar, verarbeitet AT-Kommandos, hält ein internes Modemzustandsmodell und erzeugt profilkonforme Antworten. Zusätzlich gibt es eine Monitoring- und Injection-Schnittstelle, über die Datenverkehr mitgeschnitten, Zustände verändert, URCs ausgelöst und Antworten überschrieben werden können.

Der Simulator soll sowohl für automatisierte Tests als auch für manuelle Integration mit externen Geräten geeignet sein, etwa SPSen, Router, Embedded Controller, Fernwirkgeräte oder Legacy-Software, die ein serielles Modem erwartet.

## Muss-Ziele

- Emulation über physische und virtuelle COM-Ports.
- Standard-Hayes-/V.250-Basisverhalten.
- Mobilfunkbasis nach 3GPP TS 27.007, insbesondere `+CREG`, `+CSQ`, `+CPIN`, `+CMEE`, Geräteidentität und Netz/SIM-Zustände.
- SMS-Basis nach 3GPP TS 27.005, insbesondere Textmodus-Workflow für `+CMGS` inklusive Prompt, Ctrl-Z und Fehlerantworten.
- Simulation von defekter SIM-Karte, fehlender SIM, PIN/PUK, kein Netz, Registrierung abgelehnt und verschiedenen Netzqualitäten.
- XML-basierte Input->Output-Makros, z. B. SMS an Zielnummer mit Body-Match -> `+CMS ERROR: 123` oder herstellerspezifische Rohantwort.
- Zusatzschnittstelle für Mitschnitt, State-Änderung und Command/Response Injection.
- Profilbasis für mehrere Herstellerfamilien und konkrete Zielmodelle.

## Soll-Ziele

- Proxy/Record-Replay-Modus gegen ein echtes Modem.
- Golden-Transcript-Tests für jedes Profil.
- Hot Reload von XML-Makros und Szenarien.
- UI-unabhängige REST/WebSocket-API plus CLI.
- Headless-Testmodus ohne COM-Port.

## Nicht-Ziele in v1

- Keine echte Mobilfunk- oder PSTN-Verbindung.
- Keine Implementierung eigener Kernel-Treiber für virtuelle COM-Ports.
- Keine vollständige Emulation proprietärer Firmwarefehler ohne Referenztranskripte.
- Keine Redistribution vollständiger Herstellerhandbücher im Spec-Paket.

## Qualitätsdefinition für "vollständig"

Ein Profil gilt als vollständig spezifiziert, wenn es einen Coverage-Report besitzt:

```text
profile: sierra-hl78xx-v29
commands_total: <aus extrahierter Referenzliste>
implemented_full: <vollständig implementiert>
implemented_stub: <syntaktisch vorhanden, semantisch begrenzt>
unsupported_declared: <bewusst nicht unterstützt>
unknown: 0
```

Für die Entwicklung ist `unknown = 0` wichtiger als eine hohe `implemented_full`-Quote. Dadurch entstehen keine zufälligen Antworten bei unbekannten Befehlen.
