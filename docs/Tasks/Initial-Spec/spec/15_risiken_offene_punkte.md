# 15 - Risiken und offene Punkte

## Risiken

| Risiko | Auswirkung | Gegenmaßnahme |
|---|---|---|
| Herstellerverhalten weicht vom Manual ab | Tests gegen echte Geräte schlagen fehl | Record-Replay und Profil-Deviations pflegen. |
| TD20-Referenz unklar | Falsche Westermo-Emulation | Candidate-Profil klar markieren, offizielles Manual beschaffen. |
| Proprietäre Sierra-Befehle sind lizenz-/passwortgeschützt | Unvollständige Emulation | Nur Coverage/Stub, Implementierung nur mit erlaubter Referenz. |
| SMS-PDU-Modus komplex | Fehler bei Binär-/Unicode-SMS | Textmodus in v1 priorisieren, PDU-Modus schrittweise. |
| COM-Control-Lines werden vom externen Gerät streng geprüft | Byte-Emulation reicht nicht | Post-v1: DTR/DSR/DCD/RI/RTS/CTS vollständig eventen und testbar machen. |
| Timing ist kritisch | Gerät interpretiert Simulator falsch | Response Scheduler mit Profil-Timings, Jitter und Golden Tests. |

## Offene Entscheidungen

- Exakte Sierra-UMTS-Zielmodule auswählen.
- Klären, ob Westermo TD20 existiert oder TD-22/TD-23/TD-32/TD-33/TD-36 gemeint ist.
- GUI-Toolkit verbindlich festlegen: JavaFX oder Swing.
- Entscheiden, ob TCP-Serial-Bridge in v1 enthalten sein soll.
- PDU-SMS-Umfang definieren: nur Pass-through, Decoding oder vollständiges Encoding/Decoding?
- Zielbetriebssysteme und Virtual-COM-Tools verbindlich festlegen.

## Nächste Spezifikationsschritte

1. Profile-Coverage-Tabellen aus Referenzen ableiten.
2. Minimalen Handler-Katalog pro Profil definieren.
3. XML-Schema finalisieren.
4. Serial-Loopback-Testumgebung festlegen.
5. Echte Modemtranskripte für kritische Befehle sammeln.
