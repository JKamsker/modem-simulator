# 10 - Herstellerprofile: Sierra Wireless / Semtech

## Ziel

Sierra-Wireless/Semtech-Profile werden mehrstufig aufgebaut: erst Hayes/3GPP-Basis, dann Sierra-Familienprofil, dann konkrete Modulreihe und Dokumentversion.

## Verbindlicher v1-Zielumfang

Nur diese Sierra-Profile sind v1-Abnahmeziele:

| Profil-ID | Status | v1-Rolle |
|---|---|---|
| `sierra-common` | `manufacturer-base` | Gemeinsame `ATI`, `+CGMI`, Fehler-/Prompt-Konventionen, Sierra-URCs. |
| `sierra-hl6-hl8-v20` | `device-family` | Embedded 2G/3G-Referenz fuer klassische Sierra-HL-Anforderungen. |

Sierra-Wireless/Semtech-LTE ist out-of-scope. Diese Profile werden im Katalog hoechstens als Referenz gefuehrt und duerfen nicht als v1-, post-v1- oder Candidate-Ziel behandelt werden:

| Profil-ID | Status in v1 | Grund |
|---|---|---|
| `sierra-mc-sl-umts-lte-v8` | `out-of-scope` | Enthält Sierra-LTE-Zielverhalten. |
| `sierra-hl78xx-v29` | `out-of-scope` | HL78xx ist LPWA/LTE-M/NB-IoT-orientiert. |
| `sierra-em74xx-mc74xx-r3` | `out-of-scope` | LTE-Embedded-Familie. |
| `sierra-em75xx-emmc74x1-v8` | `out-of-scope` | Neuere LTE-/9x50-basierte Embedded-Module. |
| `sierra-em9-v14` | `out-of-scope` | Moderne LTE/5G-orientierte EM-Serie. |

Nicht-LTE-Sierra-Profile ausserhalb des v1-Zielumfangs:

| Profil-ID | Status in v1 | Grund |
|---|---|---|
| `sierra-umts-airprime-standard-2130617-r7` | `candidate` | Exaktes UMTS-Zielmodul nicht bestaetigt. |

## Vererbung

```text
generic-hayes-v250
3gpp-27007-r18
3gpp-27005-r16
  -> sierra-common
      -> sierra-hl6-hl8-v20
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
  unknownAtCommand: ERROR
```

## Offene Punkte

- Sierra-Wireless/Semtech-LTE bleibt out-of-scope, auch wenn Referenzen vorhanden sind.
- Fuer `sierra-umts-airprime-standard-2130617-r7` muss ein konkretes nicht-LTE-Zielmodul bestaetigt werden, bevor es v1-Ziel werden darf.
- Fuer proprietaere Sierra-Befehle ist ein Manual-Coverage-Import erforderlich.
- Fuer modemnahe Abweichungen sollten Record-Replay-Transkripte gegen echte Module erstellt werden.
