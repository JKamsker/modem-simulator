# 15 - Risiken und offene Punkte

## Risiken

| Risiko | Auswirkung | Gegenmassnahme |
|---|---|---|
| Herstellerverhalten weicht vom Manual ab | Tests gegen echte Geraete schlagen fehl | Record-Replay und Profil-Deviations pflegen. |
| TD20-Referenz unklar | Falsche Westermo-Emulation | Candidate-Profil klar markieren, offizielles Manual beschaffen; keine v1-Abnahme. |
| Proprietaere Sierra-Befehle sind lizenz-/passwortgeschuetzt | Unvollstaendige Emulation | Nur Coverage/Stub, Implementierung nur mit erlaubter Referenz. |
| SMS-PDU-Modus komplex | Fehler bei Binaer-/Unicode-SMS | v1 speichert PDU opaque; Encoding/Decoding post-v1. |
| COM-Control-Lines werden vom externen Geraet streng geprueft | Byte-Emulation reicht nicht | v1 implementiert `lineModel=minimal-v250`; vollstaendige Profile-Details als Deviation/Coverage. |
| Timing ist kritisch | Geraet interpretiert Simulator falsch | Deterministischer Scheduler mit Seed, virtueller Clock und Timing-Akzeptanzmatrix. |
| Lokale Downloads geschuetzter Referenzen werden versehentlich verteilt | Copyright-/Lizenzrisiko | `references/downloads/**` ist lokale Arbeitskopie und nicht Teil des Redistributable Manifest. |

## Bereits entschiedene Punkte fuer v1

| Entscheidung | Wert |
|---|---|
| GUI-Toolkit | JavaFX |
| Ziel-OS fuer CI | Windows 11 und Linux LTS |
| Virtuelle COM-Tools | Windows: com0com; Linux: socat PTY |
| TCP-Serial-Bridge | Nicht in v1 |
| PDU-SMS | Opaque submit/store, kein Encoding/Decoding |
| Profile-Schema | `schemas/modem-profile.schema.xsd` ist autoritativ |
| Redaction | Default-on und fuer persistente Artefakte verpflichtend |

## Offene Entscheidungen

- Ob `westermo-td20-candidate` jemals durch ein offizielles Zielprofil ersetzt wird.
- Welche echten Geraetetranskripte fuer Sierra- und Westermo-Abweichungen priorisiert werden.
- Ob post-v1 TCP-Serial-Bridge nur loopback, token-authentifiziert oder gar nicht geliefert wird.
- Welche zusaetzlichen Sierra-Profile nach v1 in die Zielprofil-Liste aufgenommen werden.

## Naechste Spezifikationsschritte

1. Coverage-Tabellen fuer alle v1-Zielprofile anlegen.
2. Minimalen Handler-Katalog pro v1-Profil definieren.
3. Negative Fixtures fuer Semantic-Validation und XML-Hardening ergaenzen.
4. Serial-Loopback-Testumgebung fuer Windows/Linux dokumentieren.
5. Echte Modemtranskripte fuer kritische Befehle sammeln.
