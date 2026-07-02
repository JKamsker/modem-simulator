# 11 - Herstellerprofile: Westermo

## Ziel

Westermo-Profile decken industrielle Analog-, Leased-Line-, GSM/GPRS- und ISDN-Modems ab. Der Schwerpunkt liegt auf serieller Robustheit, klassischen AT-Befehlen, S-Registern, Remote-Konfiguration, Datenmodus und Westermo-spezifischen Prefixen.

## Profilbasis

| Profil-ID | Status | Referenzlage | Schwerpunkt |
|---|---|---|---|
| `westermo-common` | manufacturer-base | belastbar über mehrere offizielle Manuals | Gemeinsame TD/GD/IDW-Verhaltensmuster. |
| `westermo-td20-candidate` | candidate | kein offizieller TD-20 Treffer gefunden | Platzhalter für gewünschtes TD20-Verhalten. |
| `westermo-td22-6177-2203` | device-target | offizielles PDF | TD-Familien-AT und S-Register. |
| `westermo-td32-6178-2203` | device-target | offizielles PDF | Analog/PSTN-AT, S-Register, Industriefunktionen. |
| `westermo-td33-6179-2203` | device-target | offizielles PDF | TD-33 analog/V.34/V.90-Verhalten. |
| `westermo-td36-6618-2202` | device-target | offizielles PDF | TD-36 PSTN/leased-line Verhalten. |
| `westermo-gd01-6196-2220` | device-target | offizielles PDF | GSM/SMS, A/, +++, AT+CGMI usw. |
| `westermo-gdw11-6615-2220` | device-target | offizielles PDF | GSM/GPRS, Online Mode und Online Command Mode. |
| `westermo-idw90-6620-3200` | device-target | offizielles PDF | ISDN/Analog, `AT!`, `AT**`, Remote-Konfiguration. |

## TD20 Candidate

Da keine belastbare offizielle TD-20/TD20-Quelle gefunden wurde, wird `westermo-td20-candidate` wie folgt behandelt:

- Es erbt von `westermo-common` und `generic-hayes-v250`.
- Es übernimmt konservative TD-Familien-Basisbefehle: `AT`, `A/`, `ATE`, `ATQ`, `ATV`, `ATZ`, `AT&F`, `AT&W`, `AT&V`, `ATD`, `ATH`, `ATO`, `ATS<n>?`, `ATS<n>=...`.
- Es wird als `candidate` markiert, bis ein offizielles TD20-Manual oder ein echtes Gerät/Transkript vorliegt.
- Abnahmetests dürfen nicht behaupten, TD20 sei vollständig emuliert, solange die Referenz fehlt.

## Westermo-spezifische Anforderungen

- `A/` muss ohne CR als Repeat-Last-Command unterstützt werden, wenn das Profil dies vorsieht.
- `+++` muss als Escape-Sequenz aus dem Online/Data Mode erkannt werden.
- S-Register müssen pro Profil mit Default, Min/Max und Schreibbarkeit definiert werden.
- `AT&W` speichert profilabhängige Settings persistent.
- `AT&V` oder herstellerspezifische Anzeigecommands geben Profilkonfiguration aus.
- Remote-Konfigurations-Escape-Sequenzen müssen als optionaler Profilblock modellierbar sein.
- Für IDW-90 müssen `AT!`- und `AT**`-Prefixe möglich sein.

## Beispielprofil

```yaml
id: westermo-td20-candidate
vendor: Westermo
status: candidate
extends:
  - generic-hayes-v250
  - westermo-common
identity:
  manufacturer: "Westermo"
  model: "TD20-CANDIDATE"
  revision: "unverified"
dialect:
  commandTerminator: CR
  responseTerminator: CRLF
  supportsARepeat: true
  supportsPlusPlusPlusEscape: true
coverage:
  unknownAllowed: false
  note: "Kein offizielles TD20-Manual im Referenzindex gefunden; Nutzung nur als Fallback."
```

## Offene Punkte

- Offizielles TD20-Manual oder Gerätedump beschaffen.
- Prüfen, ob "TD20" eventuell eine interne Bezeichnung, Schreibvariante oder Verwechslung mit TD-22/TD-23/TD-32/TD-33/TD-36 ist.
- Für jedes TD-Profil S-Registertabellen aus dem jeweiligen Manual in eine maschinenlesbare Coverage-Datei überführen.
