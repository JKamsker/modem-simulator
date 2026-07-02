# Java COM-Modem-Simulator - Spec-Paket

Stand: 2026-07-02
Sprache: Deutsch
Status: Entwurf v0.3

Dieses Paket enthält eine modularisierte Markdown-Spezifikation für ein Java-Programm, das physische und virtuelle serielle COM-Schnittstellen nutzt, um Modems zu emulieren. Der Fokus liegt auf AT/Hayes, 3GPP-Mobilfunkbefehlen, SMS, simulierbaren Fehlerzuständen und herstellerspezifischen Profilen.

## Source of Record

Diese JKamsker-Kopie unter `docs/Tasks/Initial-Spec` ist der Source of Record fuer v1. Abweichende Arbeitskopien duerfen nicht als normative Quelle verwendet werden, solange diese README keinen neuen Source-of-Record-Eintrag nennt.

## Inhalt

- `spec/` - fachliche und technische Spezifikation, kapitelweise als Markdown.
- `profiles/` - Profilstrategie und Profilbasis für Generic Hayes, 3GPP, Sierra Wireless/Semtech und Westermo.
- `examples/` - XML-Makros, Szenarien, GUI-Workflows und Transkripte.
- `schemas/` - Schemas fuer XML-Profile, Makros, Szenarien, Config, Coverage und Event-Logs.
- `references/` - Referenzindex, Link-Dateien und kurze Quellen-Notizen.

## Wichtiger Hinweis zu Referenz-PDFs

Das redistributable Spec-Paket enthält bewusst keine Vollkopien der Hersteller- und Norm-PDFs. Viele der relevanten Dokumente sind urheberrechtlich geschützt, teils mit Download-Lizenz oder als proprietäre Herstellerdokumente markiert. Stattdessen enthält das Manifest `references/reference_index.md`, `references/reference_index.pdf` und `references/url/*.url` als zitierfähige Verweise auf die offiziellen Quellen.

Der lokale Arbeitsbaum kann `references/downloads/**` mit heruntergeladenen Arbeitskopien enthalten. Dieser Ordner ist ausdruecklich nicht Teil des redistributable Manifest und muss beim Verpacken ausgeschlossen werden, solange keine separate Lizenzfreigabe dokumentiert ist.

## Schnelleinstieg

1. `spec/01_zielbild_scope.md` lesen.
2. Danach `spec/02_architektur.md` und `spec/04_at_parser_und_command_engine.md`.
3. Für konkrete Geräteprofile: `profiles/profile_catalog.md`, `profiles/sierra-wireless-family.md` und `profiles/westermo-family.md`.
4. Für XML-Profile: `examples/modem-profile.sample.xml` und `schemas/modem-profile.schema.xsd`.
5. Für Makros: `spec/07_macro_engine_xml.md`, `schemas/macro-schema-draft.xsd` und `examples/macros.sms-error-123.xml`.
6. Fuer Abnahme: `spec/13_teststrategie_abnahme.md`.

## Leitentscheidung

Für Westermo wurde kein verlässlicher offizieller TD-20/TD20 Treffer gefunden. Das Paket enthält daher ein vorsichtiges `westermo-td20-candidate`-Profil sowie belastbare Westermo-TD-Familienprofile auf Basis von TD-22, TD-32, TD-33 und TD-36 Referenzen.
