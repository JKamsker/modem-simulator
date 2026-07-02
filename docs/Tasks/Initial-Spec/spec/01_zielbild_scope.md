# 01 - Zielbild und Scope

## Zielbild

Das System ist ein Java-basierter Modem-Simulator. Es stellt über eine physische oder virtuelle serielle Schnittstelle ein Modem dar, verarbeitet AT-Kommandos, hält ein internes Modemzustandsmodell und erzeugt profilkonforme Antworten. Zusätzlich gibt es eine lokale GUI, über die Datenverkehr mitgeschnitten, Zustände verändert, URCs ausgelöst und Antworten überschrieben werden können.

Der Simulator soll sowohl für automatisierte Tests als auch für manuelle Integration mit externen Geräten geeignet sein, etwa SPSen, Router, Embedded Controller, Fernwirkgeräte oder Legacy-Software, die ein serielles Modem erwartet.

## Muss-Ziele

- Emulation über physische und virtuelle COM-Ports.
- Standard-Hayes-/V.250-Basisverhalten.
- Mobilfunkbasis nach 3GPP TS 27.007, insbesondere `+CREG`, `+CSQ`, `+CPIN`, `+CMEE`, Geräteidentität und Netz/SIM-Zustände.
- SMS-Basis nach 3GPP TS 27.005, insbesondere Textmodus-Workflow für `+CMGS` inklusive Prompt, Ctrl-Z und Fehlerantworten.
- Simulation von defekter SIM-Karte, fehlender SIM, PIN/PUK, kein Netz, Registrierung abgelehnt und verschiedenen Netzqualitäten.
- XML-basierte Input->Output-Makros, z. B. SMS an Zielnummer mit Body-Match -> `+CMS ERROR: 123` oder herstellerspezifische Rohantwort.
- Lokale GUI fuer Mitschnitt, State-Aenderung, Logs und Command/Response Injection.
- Profilbasis für mehrere Herstellerfamilien und konkrete Zielmodelle.
- Deterministische Headless-/Replay-Ausfuehrung mit `sessionSeed`, virtueller Clock und sequenziertem Eventlog.
- Default-on Redaction fuer Logs, Exporte und Replay-Artefakte.

## Soll-Ziele

- Proxy/Record-Replay-Modus gegen ein echtes Modem.
- Golden-Transcript-Tests für jedes Profil.
- Hot Reload von XML-Makros und Szenarien.
- Lokale Desktop-GUI mit Control Pane, Live-Log, State-Editor, Injection und Replay.
- Headless-Testmodus ohne COM-Port.

## Nicht-Ziele in v1

- Keine echte Mobilfunk- oder PSTN-Verbindung.
- Keine Implementierung eigener Kernel-Treiber für virtuelle COM-Ports.
- Keine vollständige Emulation proprietärer Firmwarefehler ohne Referenztranskripte.
- Keine Redistribution vollständiger Herstellerhandbücher im Spec-Paket.
- Keine HTTP- oder WebSocket-Control-API in v1.
- Keine TCP-Serial-Bridge in v1.
- Kein vollstaendiges SMS-PDU-Encoding/Decoding in v1; PDU-Submit ist opaque.

## Verbindliche v1-Zielprofile

Nur diese Profile sind v1-Abnahmeziele:

| Profil | Rolle |
|---|---|
| `generic-hayes-v250` | Hayes/V.250-Basis |
| `3gpp-27007-r18` | Mobilfunkbasis |
| `3gpp-27005-r16` | SMS-Basis |
| `sierra-common` | Sierra-Herstellerbasis |
| `sierra-hl6-hl8-v20` | Sierra device-family |
| `westermo-common` | Westermo-Herstellerbasis |
| `westermo-td22-6177-2203` | Westermo PSTN target |
| `westermo-td36-6618-2202` | Westermo PSTN target |
| `westermo-gd01-6196-2220` | Westermo GSM/SMS target |
| `westermo-gdw11-6615-2220` | Westermo GSM/GPRS target |

Sierra-Wireless/Semtech-LTE-Profile sind bewusst out-of-scope und duerfen nicht durch Coverage oder Smoke-Tests implizit in diese Liste aufgenommen werden.

Alle anderen Katalogeintraege sind candidate/post-v1, bis Coverage und Smoke-Tests sie ausdruecklich in diese Liste aufnehmen.

## Qualitätsdefinition für "vollständig"

Ein Profil gilt als vollständig spezifiziert, wenn es einen Coverage-Report besitzt:

```text
profile: sierra-hl6-hl8-v20
source: references/snapshots/sierra-hl6-hl8-v20.md
commands_total: <Anzahl Eintraege in commands>
implemented_full: <vollständig implementiert>
implemented_stub: <syntaktisch vorhanden, semantisch begrenzt>
unsupported_declared: <bewusst nicht unterstützt>
not_applicable: <fuer dieses Profil nicht relevant>
unknown: 0
```

Der Coverage-Report validiert gegen `schemas/coverage.schema.json`. Fuer die Entwicklung ist `unknown = 0` wichtiger als eine hohe `implemented_full`-Quote. Dadurch entstehen keine zufälligen Antworten bei unbekannten Befehlen.
