# Profil: generic-hayes-v250

## Zweck

Basisprofil für klassische Modemsteuerung über AT-Befehle. Dieses Profil ist kein echtes Gerät, sondern Grundlage für Herstellerprofile.

## Pflichtbefehle

| Befehl | Status | Beschreibung |
|---|---|---|
| `AT` | `implemented_full` | Attention, liefert Result Code. |
| `A/` | `implemented_full` | Letzten Befehl wiederholen, ohne CR. |
| `ATA` | `implemented_full` | Eingehenden Ruf beantworten; ohne Ruf profilierter Fehler. |
| `ATE0/1` | `implemented_full` | Echo aus/an. |
| `ATQ0/1` | `implemented_full` | Result Codes aus/an. |
| `ATV0/1` | `implemented_full` | Numeric/Verbose Result Codes. |
| `ATZ` | `implemented_full` | Reset nach Profil-`resetPolicy`. |
| `AT&F` | `implemented_full` | Factory Defaults. |
| `AT&W` | `implemented_full` | Persistente Speicherung. |
| `AT&V` | `implemented_full` | Aktive Konfiguration anzeigen. |
| `ATD...` | `implemented_stub` | Dial, simuliert `CONNECT`/Fehler. |
| `ATH` | `implemented_full` | Hangup, finaler Result Code `OK`. |
| `ATO` | `implemented_full` | Return to online data mode. |
| `ATS<n>?` | `implemented_full` | S-Register lesen. |
| `ATS<n>=<v>` | `implemented_full` | S-Register schreiben. |
| `+++` | `implemented_full` | Escape aus Data Mode mit Guard-Time. |

## Default S-Register

Diese Tabelle ist Hayes-kompatible Profilbasis. Nicht jeder Eintrag ist normativ aus V.250 ableitbar; Abweichungen muessen im Profil-Coverage dokumentiert werden.

| Register | Default | Min | Max | Bedeutung |
|---|---:|---:|---:|---|
| S0 | 0 | 0 | 255 | Auto-answer rings. |
| S2 | 43 | 0 | 127 | Escape character `+` (Hayes-kompatibler Profilwert). |
| S3 | 13 | 0 | 127 | Carriage Return. |
| S4 | 10 | 0 | 127 | Line Feed. |
| S5 | 8 | 0 | 127 | Backspace. |
| S6 | 2 | 0 | 255 | Wait before dial. |
| S7 | 60 | 1 | 255 | Wait for carrier. |
| S8 | 2 | 0 | 255 | Dial pause. |
| S12 | 50 | 0 | 255 | Escape guard time, profilabhaengige Einheit; fuer v1 in Millisekunden umzurechnen. |

Herstellerprofile duerfen diese Grenzen ueberschreiben, muessen die abweichende Tabelle aber im Profil oder Coverage-Artefakt pflegen. Runtime-Validation und Handler muessen die effektive Profilgrenze verwenden.

## Result Codes

| Numeric | Verbose |
|---:|---|
| 0 | OK |
| 1 | CONNECT |
| 2 | RING |
| 3 | NO CARRIER |
| 4 | ERROR |
| 5 | reserved/profile-specific |
| 6 | NO DIALTONE |
| 7 | BUSY |
| 8 | NO ANSWER |

## Abweichungen

Herstellerprofile dürfen Result-Code-Tabellen erweitern oder einschränken.
