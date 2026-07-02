# 09 - Profile System

## Profilprinzip

Ein Profil beschreibt, wie ein konkretes Modemmodell in einer konkreten Firmware-/Manual-Version reagiert. Profile können voneinander erben.

```yaml
id: sierra-hl78xx-v29
vendor: Sierra Wireless / Semtech
modelFamily: HL78xx
manualVersion: 29
manualDate: 2026-05-19
extends:
  - generic-hayes-v250
  - 3gpp-27007-r18
  - 3gpp-27005-r16
```

## Profilbestandteile

| Bestandteil | Beschreibung |
|---|---|
| Metadata | Hersteller, Modell, Firmware, Dokumentstand, Status. |
| Dialect | Zeilenenden, Prefixe, Echo-Defaults, Prompt-Format, Fehlerpolicy. |
| Identity | Antworten für `ATI`, `+CGMI`, `+CGMM`, `+CGMR`, `+CGSN`. |
| Commands | Liste unterstützter Commands mit Handler-Zuordnung. |
| Registers | S-Register inklusive Default, Min/Max, Persistenz. |
| States | Initiale SIM-/Netz-/SMS-/Call-Zustände. |
| Coverage | Soll/Ist-Abdeckung gegenüber Referenzmanual. |
| Deviations | Bewusste Abweichungen oder offene Punkte. |

## Profilstatus

| Status | Bedeutung |
|---|---|
| `normative-base` | Basisprofil aus Standard, kein echtes Gerät. |
| `manufacturer-base` | Herstellerfamilie ohne konkretes Modell. |
| `device-target` | Konkretes Modell/Firmware als Zielprofil. |
| `candidate` | Gewünscht, aber Referenzlage noch unsicher. |
| `stub` | Syntaktisch vorhanden, noch nicht verifiziert. |
| `verified` | Gegen Manual und/oder echtes Gerät getestet. |

## Coverage-Datei

```yaml
coverage:
  source: references/sierra-hl78xx-v29
  commands:
    - command: AT
      status: implemented_full
      handler: BasicAtHandler
    - command: +CREG
      status: implemented_full
      handler: CregHandler
    - command: +KCNXCFG
      status: stub
      handler: UnsupportedOrMacroHandler
    - command: +WDSI
      status: unsupported_declared
      reason: not required by current integration tests
```

## Unsupported-Strategie

Unbekannte Befehle dürfen nicht willkürlich beantwortet werden. Profilabhängige Policies:

- `ERROR`
- `+CME ERROR: operation not supported`
- Herstellerfehlercode
- Keine Antwort / Timeout
- Macro-only

Für Abnahmetests ist jede Unsupported-Antwort im Coverage-Report zu dokumentieren.
