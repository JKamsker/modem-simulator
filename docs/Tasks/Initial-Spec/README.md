# Java COM-Modem-Simulator - Spec-Paket

Stand: 2026-07-02
Sprache: Deutsch
Status: Entwurf v0.2

Dieses Paket enthält eine modularisierte Markdown-Spezifikation für ein Java-Programm, das physische und virtuelle serielle COM-Schnittstellen nutzt, um Modems zu emulieren. Der Fokus liegt auf AT/Hayes, 3GPP-Mobilfunkbefehlen, SMS, simulierbaren Fehlerzuständen und herstellerspezifischen Profilen.

## Inhalt

- `spec/` - fachliche und technische Spezifikation, kapitelweise als Markdown.
- `profiles/` - Profilstrategie und Profilbasis für Generic Hayes, 3GPP, Sierra Wireless/Semtech und Westermo.
- `examples/` - XML-Makros, Szenarien, GUI-Workflows und Transkripte.
- `schemas/` - erste Draft-Schemas für XML-Profile, Makros und Event-Logs.
- `references/` - Referenzindex, Link-Dateien und kurze Quellen-Notizen.

## Wichtiger Hinweis zu Referenz-PDFs

Die ZIP enthält bewusst keine Vollkopien der Hersteller- und Norm-PDFs. Viele der relevanten Dokumente sind urheberrechtlich geschützt, teils mit Download-Lizenz oder als proprietäre Herstellerdokumente markiert. Stattdessen enthält `references/reference_index.md`, `references/reference_index.pdf` und `references/url/*.url` zitierfähige Verweise auf die offiziellen Quellen.

## Schnelleinstieg

1. `spec/01_zielbild_scope.md` lesen.
2. Danach `spec/02_architektur.md` und `spec/04_at_parser_und_command_engine.md`.
3. Für konkrete Geräteprofile: `profiles/profile_catalog.md`, `profiles/sierra-wireless-family.md` und `profiles/westermo-family.md`.
4. Für XML-Profile: `examples/modem-profile.sample.xml` und `schemas/modem-profile.schema.xsd`.
5. Für Makros: `spec/07_macro_engine_xml.md` und `examples/macros.sms-error-123.xml`.

## Leitentscheidung

Für Westermo wurde kein verlässlicher offizieller TD-20/TD20 Treffer gefunden. Das Paket enthält daher ein vorsichtiges `westermo-td20-candidate`-Profil sowie belastbare Westermo-TD-Familienprofile auf Basis von TD-22, TD-32, TD-33 und TD-36 Referenzen.
