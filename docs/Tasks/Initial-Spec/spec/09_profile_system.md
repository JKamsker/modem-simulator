# 09 - Profile System

## Autoritative Formate

Das geladene Profilformat fuer v1 ist XML und wird gegen `schemas/modem-profile.schema.xsd` validiert. Diese XSD ist die einzige autoritative Schemaquelle fuer Runtime-Profile.

`schemas/profile.schema.json` ist nur ein nicht-autoritativer Helper fuer generierte Katalog-/Coverage-Exports. Es darf nicht verwendet werden, um XML-Profile zu laden oder XSD-Regeln zu ueberstimmen.

`config.yaml` wird nach YAML-Parsing gegen `schemas/config.schema.json` validiert. Szenarien werden gegen `schemas/scenario.schema.xsd` validiert. Coverage-Reports werden gegen `schemas/coverage.schema.json` validiert.

## Profilprinzip

Ein Profil beschreibt, wie ein konkretes Modemmodell in einer konkreten Firmware-/Manual-Version reagiert. Profile koennen voneinander erben.

```yaml
id: sierra-hl78xx-v29
vendor: Sierra Wireless / Semtech
modelFamily: HL78xx
manualVersion: 29
manualDate: 2026-05-19
status: device-target
profileKind: cellular
extends:
  - sierra-common
```

Im XML ist `extends` eine whitespace-getrennte, geordnete Liste:

```xml
<profile id="sierra-common"
         vendor="Sierra Wireless / Semtech"
         status="manufacturer-base"
         profileKind="cellular"
         extends="generic-hayes-v250 3gpp-27007-r18 3gpp-27005-r16">
```

Profil-IDs duerfen mit Ziffern beginnen, z. B. `3gpp-27007-r18`.

## Vererbung und Konflikte

Linearisierung:

1. Eltern werden in der Reihenfolge aus `extends` geladen.
2. Deren Eltern werden rekursiv vor dem jeweiligen Kind geladen.
3. Das Kind ueberschreibt Elternwerte.
4. Bei zwei Eltern mit demselben Command/Register gewinnt der spaetere Elternteil in der `extends`-Liste.
5. Jeder Konflikt wird in einem Validierungsbericht protokolliert.

Ein Profil darf keine zyklische Vererbung erzeugen. Fehlende Eltern sind Validierungsfehler.

## Profilbestandteile

| Bestandteil | Beschreibung |
|---|---|
| Metadata | Hersteller, Modell, Firmware, Dokumentstand, Status, Profilart. |
| Dialect | Default-Line-Endings, Prefixe, Echo/Quiet/Verbose, Prompt-Bytes, Fehlerpolicy, Reset-Policy. |
| Identity | Antworten fuer `ATI`, `+CGMI`, `+CGMM`, `+CGMR`, `+CGSN`. |
| Commands | Liste unterstuetzter Commands mit Handler-Zuordnung und Status. |
| Registers | S-Register inklusive Default, Min/Max, Persistenz. |
| States | Initiale SIM-/Netz-/SMS-/Call-/Line-Zustaende. |
| Operator | Netzbetreiber-Metadaten wie Langname, Kurzname, MCC/MNC und numerischer Code fuer `+COPS`. |
| Coverage | Soll/Ist-Abdeckung gegenueber Referenzmanual. |
| Deviations | Bewusste Abweichungen oder offene Punkte. |

## Profilstatus

| Status | Bedeutung |
|---|---|
| `normative-base` | Basisprofil aus Standard, kein echtes Geraet. |
| `manufacturer-base` | Herstellerfamilie ohne konkretes Modell. |
| `device-family` | Modell-/Firmware-Familie, aus der konkrete Targets abgeleitet werden. |
| `device-target` | Konkretes Modell/Firmware als Zielprofil. |
| `candidate` | Gewuenscht, aber Referenzlage noch unsicher. |
| `stub` | Syntaktisch vorhanden, noch nicht verifiziert. |
| `verified` | Gegen Manual und/oder echtes Geraet getestet. |

## Profilart

| `profileKind` | Bedeutung |
|---|---|
| `base` | Nur Vererbungsbasis, kein direktes Runtime-Ziel. |
| `cellular` | SIM/Network/Signal sind erforderlich. |
| `pstn` | Keine SIM/Network/Signal-Pflicht; Call/Lines sind relevant. |
| `isdn` | ISDN/Terminal-Adapter-Verhalten ohne Mobilfunkpflicht. |
| `hybrid` | Kombination, z. B. GSM plus klassische Line-Semantik. |

Die XSD erlaubt optionale State-Bloecke, damit PSTN/ISDN-Profile modellierbar sind. Semantic-Validation erzwingt die passenden Pflichtbloecke pro `profileKind`.

## Coverage-Datei

```yaml
profile: sierra-hl78xx-v29
source: references/snapshots/sierra-hl78xx-v29.md
commands_total: 123
implemented_full: 40
implemented_stub: 50
unsupported_declared: 33
not_applicable: 0
unknown: 0
commands:
  - command: AT
    status: implemented_full
    handler: BasicAtHandler
  - command: +CREG
    status: implemented_full
    handler: CregHandler
  - command: +KCNXCFG
    status: implemented_stub
    handler: UnsupportedOrMacroHandler
  - command: +WDSI
    status: unsupported_declared
    reason: not required by current integration tests
```

`commands_total` ist die Anzahl eindeutiger, fuer das Profil relevanter Commands aus der versionierten Coverage-Datei. Die CI prueft:

```text
commands_total == len(commands)
unknown == 0
all status in {implemented_full, implemented_stub, unsupported_declared, not_applicable}
```

## Unsupported-Strategie

Unbekannte Befehle duerfen nicht willkuerlich beantwortet werden. Profilabhaengige Policies:

- `ERROR`
- `+CME ERROR: operation not supported`
- Herstellerfehlercode
- Keine Antwort / Timeout
- Macro-only

Fuer Abnahmetests ist jede Unsupported-Antwort im Coverage-Report zu dokumentieren.

## XML-Profilkonfiguration

Profile werden als XML-Datei geladen. Reine Basisprofile duerfen `initial-state` weglassen. Runtime-Zielprofile muessen alle fuer die Profilart notwendigen Bloecke aufnehmen; SIM-PIN-, Netzbetreiber-, Call- und Line-Daten sind keine hart codierten Defaults.

```xml
<modem-simulator version="1.0">
  <profile id="sierra-hl78xx-test"
           vendor="Sierra Wireless / Semtech"
           status="device-target"
           profileKind="cellular"
           extends="sierra-hl78xx-v29">
    <identity manufacturer="Sierra Wireless" model="HL7812"/>
    <dialect commandTerminator="CR"
             responseTerminator="CRLF"
             smsPromptBytes="0D0A3E20"
             resetPolicy="nvram-on-atz"
             lineModel="minimal-v250"/>
    <initial-state>
      <sim state="READY"
           pinQueryEnabled="true"
           pinRef="TEST_SIM_PIN"
           pinRetries="3"
           pukRetries="10"/>
      <network cregN="2" stat="1" lac="00C3" ci="00001234" act="7">
        <operator selectionMode="automatic"
                  format="long"
                  longName="Telekom.de"
                  shortName="TDG"
                  numeric="26201"
                  mcc="262"
                  mnc="01"/>
        <sms-rate-limit maxMessages="5"
                        windowSeconds="60"
                        scope="session"
                        rejectCmsError="500"/>
        <delays>
          <delay operation="sms-submit" minMs="500" maxMs="2500"/>
          <delay operation="dial" minMs="1000" maxMs="5000"/>
        </delays>
      </network>
      <signal rssi="18" ber="0"/>
      <modem-lines dtr="true" dsr="true" dcd="false" ri="false"/>
    </initial-state>
  </profile>
</modem-simulator>
```

## Ladeanforderungen

- XML wird vor Aktivierung gegen `schemas/modem-profile.schema.xsd` validiert.
- XML-Parser muessen nach Kapitel 14 gehaertet sein.
- `pinQueryEnabled`, `pinRef`/Test-`pin`, Retry-Zaehler, IMSI und ICCID werden in den Session-State uebernommen.
- `network/operator` wird in den Session-State uebernommen und von Operator-Commands wie `AT+COPS?` verwendet.
- `network/sms-rate-limit` wird in den Session-State uebernommen und begrenzt akzeptierte SMS-Submits pro Zeitfenster.
- `network/delays` wird in den Session-State uebernommen und durch den Response-Scheduler pro Operation angewendet.
- PIN/PUK/IMSI/ICCID/IMEI/MSISDN/SMS-Body gelten als sensible Werte und werden in Event-Logs, GUI-Anzeigen und Exporten redigiert.

## Semantic-Validation

Nach XSD-Validierung ist ein Semantic-Validation-Pass verpflichtend. Er lehnt mindestens ab:

- `profileKind=cellular` ohne `sim`, `network` oder `signal`,
- `profileKind=pstn|isdn` mit verpflichtenden Mobilfunkannahmen in Acceptance/Coverage,
- `pinQueryEnabled=true` ohne `pinRef` oder explizit als Testfixture erlaubtes `pin`,
- gleichzeitige Verwendung von `pin` und `pinRef`,
- `state.sim.state != READY` zusammen mit `network.stat` 1 oder 5,
- `network/operator/@numeric` ohne gueltige MCC/MNC-Struktur,
- `delay/@minMs > delay/@maxMs`,
- doppelte Delay-Operationen,
- `sms-rate-limit/@rejectCmsError=310`,
- `lac`/`ci`/`act` fuer nicht registrierte `+CREG`-Zustaende ohne dokumentierte Deviation,
- unbekannte Command-/Coverage-Statuswerte,
- Profilvererbungszyklen,
- Konflikte, die nicht durch Linearisierung oder Deviation erklaert sind.

Fuer jeden Punkt muss es mindestens ein negatives CI-Fixture geben.
