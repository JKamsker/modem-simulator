# Profil: generic-hayes-v250

## Zweck

Basisprofil für klassische Modemsteuerung über AT-Befehle. Dieses Profil ist kein echtes Gerät, sondern Grundlage für Herstellerprofile.

## Pflichtbefehle

| Befehl | Status | Beschreibung |
|---|---|---|
| `AT` | full | Attention, liefert Result Code. |
| `A/` | full | Letzten Befehl wiederholen, ohne CR. |
| `ATE0/1` | full | Echo aus/an. |
| `ATQ0/1` | full | Result Codes aus/an. |
| `ATV0/1` | full | Numeric/Verbose Result Codes. |
| `ATZ` | full | Reset auf Profil-/NVRAM-Defaults. |
| `AT&F` | full | Factory Defaults. |
| `AT&W` | full | Persistente Speicherung. |
| `AT&V` | full | Aktive Konfiguration anzeigen. |
| `ATD...` | stub | Dial, simuliert `CONNECT`/Fehler. |
| `ATH` | full | Hangup. |
| `ATO` | full | Return to online data mode. |
| `ATS<n>?` | full | S-Register lesen. |
| `ATS<n>=<v>` | full | S-Register schreiben. |
| `+++` | full | Escape aus Data Mode mit Guard-Time. |

## Default S-Register

| Register | Default | Bedeutung |
|---|---:|---|
| S0 | 0 | Auto-answer rings. |
| S2 | 43 | Escape character `+`. |
| S3 | 13 | Carriage Return. |
| S4 | 10 | Line Feed. |
| S5 | 8 | Backspace. |
| S6 | 2 | Wait before dial. |
| S7 | 60 | Wait for carrier. |
| S8 | 2 | Dial pause. |
| S12 | 50 | Escape guard time, in 1/50 s oder profilabhängig. |

## Result Codes

| Numeric | Verbose |
|---:|---|
| 0 | OK |
| 1 | CONNECT |
| 2 | RING |
| 3 | NO CARRIER |
| 4 | ERROR |
| 5 | CONNECT 1200 |
| 6 | NO DIALTONE |
| 7 | BUSY |
| 8 | NO ANSWER |

## Abweichungen

Herstellerprofile dürfen Result-Code-Tabellen erweitern oder einschränken.
