# 10 - Herstellerprofile: Sierra Wireless / Semtech

## Ziel

Sierra-Wireless/Semtech-Profile werden mehrstufig aufgebaut: erst Hayes/3GPP-Basis, dann Sierra-Familienprofil, dann konkrete Modulreihe und Dokumentversion.

## Verbindlicher v1-Zielumfang

Nur diese Sierra-Profile sind v1-Abnahmeziele:

| Profil-ID | Status | v1-Rolle |
|---|---|---|
| `sierra-common` | `manufacturer-base` | Gemeinsame `ATI`, `+CGMI`, Fehler-/Prompt-Konventionen, Sierra-URCs. |
| `sierra-hl6-hl8-v20` | `device-family` | Embedded 2G/3G-Referenz fuer klassische Sierra-HL-Anforderungen. |
| `sierra-hl78xx-v29` | `device-target` | Aktuelle HL78xx LPWA/embedded Familie, Dokumentstand 2026-05-19. |

Diese Profile werden im Katalog gefuehrt, sind aber post-v1 oder candidate, solange Coverage und Smoke-Tests fehlen:

| Profil-ID | Status in v1 | Grund |
|---|---|---|
| `sierra-umts-airprime-standard-2130617-r7` | `candidate` | Exaktes Zielmodul nicht bestaetigt. |
| `sierra-mc-sl-umts-lte-v8` | `candidate` | Breite MC/SL-Familie, braucht gesonderte Coverage. |
| `sierra-em74xx-mc74xx-r3` | `candidate` | Keine v1-Abnahme. |
| `sierra-em75xx-emmc74x1-v8` | `candidate` | Proprietaere/passwortgeschuetzte Befehle. |
| `sierra-em9-v14` | `candidate` | Keine v1-Abnahme. |

## Vererbung

```text
generic-hayes-v250
3gpp-27007-r18
3gpp-27005-r16
  -> sierra-common
      -> sierra-hl6-hl8-v20
      -> sierra-hl78xx-v29
```

`sierra-common` erweitert die drei Basisprofile. Konkrete Sierra-Ziele erweitern `sierra-common`; sie duerfen die 3GPP-Basis nicht direkt umgehen.

## Sierra-spezifische Anforderungen

- Identitaetsbefehle muessen pro Profil feste Antworten liefern:
  - `ATI`
  - `AT+CGMI`
  - `AT+CGMM`
  - `AT+CGMR`
  - `AT+CGSN`
- Sierra-spezifische Extended Commands werden als Coverage-Eintraege gefuehrt.
- Passwortgeschuetzte oder proprietaere Befehle werden nicht implementiert, solange kein konkretes Testbed und kein erlaubter Zugriff vorhanden ist.
- URCs muessen pro Modulreihe konfigurierbar sein, z. B. Sierra-spezifische Registration-, SIM- oder Packet-Service-Meldungen.

## Beispielprofil

```yaml
id: sierra-hl6-hl8-v20
vendor: Sierra Wireless
status: device-family
profileKind: cellular
extends:
  - sierra-common
identity:
  manufacturer: "Sierra Wireless"
  model: "HL8548"
  revision: "SIM-HL6HL8-v20"
dialect:
  commandTerminator: CR
  responseTerminator: CRLF
  smsPromptBytes: "0D0A3E20"
  defaultCmee: 0
  lineModel: minimal-v250
unsupportedPolicy:
  unknownCommand: ERROR
```

## Offene Punkte

- Fuer `sierra-umts-airprime-standard-2130617-r7` muss ein konkretes Zielmodul bestaetigt werden, bevor es v1-Ziel werden darf.
- Fuer proprietaere Sierra-Befehle ist ein Manual-Coverage-Import erforderlich.
- Fuer modemnahe Abweichungen sollten Record-Replay-Transkripte gegen echte Module erstellt werden.
