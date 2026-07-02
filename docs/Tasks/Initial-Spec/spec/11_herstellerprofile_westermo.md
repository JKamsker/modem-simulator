# 11 - Herstellerprofile: Westermo

## Ziel

Westermo-Profile decken industrielle Analog-, Leased-Line-, GSM/GPRS- und ISDN-Modems ab. Der Schwerpunkt liegt auf serieller Robustheit, klassischen AT-Befehlen, S-Registern, Remote-Konfiguration, Datenmodus und Westermo-spezifischen Prefixen.

## Verbindlicher v1-Zielumfang

| Profil-ID | Status | Profilart | v1-Rolle |
|---|---|---|---|
| `westermo-common` | `manufacturer-base` | `base` | Gemeinsame Westermo-Konventionen. |
| `westermo-td22-6177-2203` | `device-target` | `pstn` | TD-22 AT/S-Register/Datenmodus. |
| `westermo-td36-6618-2202` | `device-target` | `pstn` | TD-36 PSTN/leased-line Verhalten. |
| `westermo-gd01-6196-2220` | `device-target` | `hybrid` | GSM/SMS plus Westermo-Basis. |
| `westermo-gdw11-6615-2220` | `device-target` | `hybrid` | GSM/GPRS, Online Mode und Online Command Mode. |

Diese Profile bleiben in v1 candidate/post-v1, solange keine Coverage und Smoke-Tests vorliegen:

| Profil-ID | Status in v1 | Grund |
|---|---|---|
| `westermo-td20-candidate` | `candidate` | Keine belastbare offizielle Quelle. |
| `westermo-td32-6178-2203` | `candidate` | Post-v1 Coverage. |
| `westermo-td33-6179-2203` | `candidate` | Post-v1 Coverage. |
| `westermo-idw90-6620-3200` | `candidate` | ISDN/AT!/AT** braucht eigene Tests. |

## Vererbung

```text
generic-hayes-v250
  -> westermo-common
      -> westermo-td22-6177-2203
      -> westermo-td36-6618-2202

generic-hayes-v250
3gpp-27007-r18
3gpp-27005-r16
  -> westermo-common
      -> westermo-gd01-6196-2220
      -> westermo-gdw11-6615-2220
```

TD/PSTN-Profile duerfen `sim`, `network` und `signal` aus `initial-state` weglassen. GD/GDW-Hybridprofile muessen Mobilfunk-State enthalten.

## TD20 Candidate

Da keine belastbare offizielle TD-20/TD20-Quelle gefunden wurde, wird `westermo-td20-candidate` wie folgt behandelt:

- Es erbt von `westermo-common` und `generic-hayes-v250`.
- Es uebernimmt konservative TD-Familien-Basisbefehle: `AT`, `A/`, `ATE`, `ATQ`, `ATV`, `ATZ`, `AT&F`, `AT&W`, `AT&V`, `ATD`, `ATH`, `ATO`, `ATS<n>?`, `ATS<n>=...`.
- Es wird als `candidate` markiert, bis ein offizielles TD20-Manual oder ein echtes Geraet/Transkript vorliegt.
- Abnahmetests duerfen nicht behaupten, TD20 sei vollstaendig emuliert, solange die Referenz fehlt.

## Westermo-spezifische Anforderungen

- `A/` muss ohne CR als Repeat-Last-Command unterstuetzt werden, wenn das Profil dies vorsieht.
- `+++` muss als Escape-Sequenz aus dem Online/Data Mode erkannt werden.
- S-Register muessen pro Profil mit Default, Min/Max und Schreibbarkeit definiert werden.
- `AT&W` speichert profilabhaengige Settings persistent.
- `AT&V` oder herstellerspezifische Anzeigecommands geben Profilkonfiguration aus.
- Remote-Konfigurations-Escape-Sequenzen muessen als optionaler Profilblock modellierbar sein.
- Fuer IDW-90 muessen `AT!`- und `AT**`-Prefixe moeglich sein.
- TD-Profile mit `lineModel=minimal-v250` muessen DTR/DCD-Verhalten aus Kapitel 03 umsetzen.

## Beispielprofil

```yaml
id: westermo-td22-6177-2203
vendor: Westermo
status: device-target
profileKind: pstn
extends:
  - westermo-common
identity:
  manufacturer: "Westermo"
  model: "TD-22"
  revision: "6177-2203"
dialect:
  commandTerminator: CR
  responseTerminator: CRLF
  supportsARepeat: true
  supportsPlusPlusPlusEscape: true
  lineModel: minimal-v250
initialState:
  call:
    mode: command
    carrier: false
  modemLines:
    dtr: true
    dsr: true
    dcd: false
    ri: false
coverage:
  unknown: 0
```

## Offene Punkte

- Offizielles TD20-Manual oder Geraetedump beschaffen.
- Pruefen, ob "TD20" eventuell eine interne Bezeichnung, Schreibvariante oder Verwechslung mit TD-22/TD-23/TD-32/TD-33/TD-36 ist.
- Fuer jedes TD-Profil S-Registertabellen aus dem jeweiligen Manual in eine maschinenlesbare Coverage-Datei ueberfuehren.
