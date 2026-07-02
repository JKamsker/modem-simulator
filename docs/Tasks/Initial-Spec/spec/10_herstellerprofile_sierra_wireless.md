# 10 - Herstellerprofile: Sierra Wireless / Semtech

## Ziel

Sierra-Wireless/Semtech-Profile werden mehrstufig aufgebaut: erst 3GPP-Basis, dann Sierra-Familienprofil, dann konkrete Modulreihe und Dokumentversion.

## Empfohlene Profilbasis

| Profil-ID | Status | Schwerpunkt |
|---|---|---|
| `sierra-common` | manufacturer-base | Gemeinsame `ATI`, `+CGMI`, Fehler-/Prompt-Konventionen, Sierra-URCs. |
| `sierra-umts-airprime-standard-2130617-r7` | device-family | UMTS AirCard/AirPrime Standard AT Reference, gut für ältere Embedded-UMTS-Module. |
| `sierra-mc-sl-umts-lte-v8` | device-family | AirPrime MC/SL UMTS/LTE Extended AT Commands. |
| `sierra-hl6-hl8-v20` | device-family | AirPrime HL6528x und HL85xxx, embedded 2G/3G-Familie. |
| `sierra-hl78xx-v29` | device-target | Aktuelle HL78xx LPWA/embedded Familie, Dokumentstand 2026-05-19. |
| `sierra-em74xx-mc74xx-r3` | device-family | EM/MC74xx LTE-embedded Familie, älter aber verbreitet. |
| `sierra-em75xx-emmc74x1-v8` | device-target | Neuere Semtech 9x50-basierte Embedded-Module, Dokumentstand 2026-01-27. |
| `sierra-em9-v14` | device-target | Moderne EM9-Serie, Dokumentstand 2026-01-30. |

## Priorisierung für v1

1. `sierra-common`
2. `sierra-umts-airprime-standard-2130617-r7`
3. `sierra-mc-sl-umts-lte-v8`
4. `sierra-hl6-hl8-v20`
5. `sierra-hl78xx-v29`
6. `sierra-em75xx-emmc74x1-v8`
7. `sierra-em9-v14`

Damit deckt der Simulator sowohl die gewünschten aktuelleren embedded/UMTS-orientierten Sierra-Profile als auch eine moderne AT-Basis für neuere Embedded-Module ab.

## Sierra-spezifische Anforderungen

- Identitätsbefehle müssen pro Profil feste Antworten liefern:
  - `ATI`
  - `AT+CGMI`
  - `AT+CGMM`
  - `AT+CGMR`
  - `AT+CGSN`
- Sierra-spezifische Extended Commands werden zunächst als Coverage-Einträge geführt.
- Passwortgeschützte oder proprietäre Befehle werden nicht implementiert, solange kein konkretes Testbed und kein erlaubter Zugriff vorhanden ist.
- URCs müssen pro Modulreihe konfigurierbar sein, z. B. Sierra-spezifische Registration-, SIM- oder Packet-Service-Meldungen.

## Beispielprofil

```yaml
id: sierra-hl6-hl8-v20
vendor: Sierra Wireless
status: device-family
extends:
  - generic-hayes-v250
  - 3gpp-27007-r18
  - 3gpp-27005-r16
identity:
  manufacturer: "Sierra Wireless"
  model: "HL8548"
  revision: "SIM-HL6HL8-v20"
dialect:
  commandTerminator: CR
  responseTerminator: CRLF
  smsPrompt: "> "
  defaultCmee: 0
unsupportedPolicy:
  unknownCommand: ERROR
```

## Offene Punkte

- Exakte UMTS-Zielmodule müssen vom Projekt bestätigt werden, z. B. HL8548/HL8549 oder MC8790/MC870x/MC7304-artige Varianten.
- Für proprietäre Sierra-Befehle ist ein Manual-Coverage-Import erforderlich.
- Für modemnahe Abweichungen sollten Record-Replay-Transkripte gegen echte Module erstellt werden.
