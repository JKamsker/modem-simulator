# 09 - Profile System

## Profilprinzip

Ein Profil beschreibt, wie ein konkretes Modemmodell in einer konkreten Firmware-/Manual-Version reagiert. Profile können voneinander erben.

```yaml
id: sierra-hl78xx-v29
vendor: Sierra Wireless / Semtech
modelFamily: HL78xx
manualVersion: 29
manualDate: 2026-05-19
extends:
  - generic-hayes-v250
  - 3gpp-27007-r18
  - 3gpp-27005-r16
```

## Profilbestandteile

| Bestandteil | Beschreibung |
|---|---|
| Metadata | Hersteller, Modell, Firmware, Dokumentstand, Status. |
| Dialect | Zeilenenden, Prefixe, Echo-Defaults, Prompt-Format, Fehlerpolicy. |
| Identity | Antworten für `ATI`, `+CGMI`, `+CGMM`, `+CGMR`, `+CGSN`. |
| Commands | Liste unterstützter Commands mit Handler-Zuordnung. |
| Registers | S-Register inklusive Default, Min/Max, Persistenz. |
| States | Initiale SIM-/Netz-/SMS-/Call-Zustände. |
| Operator | Netzbetreiber-Metadaten wie Langname, Kurzname, MCC/MNC und numerischer Code fuer `+COPS`. |
| Coverage | Soll/Ist-Abdeckung gegenüber Referenzmanual. |
| Deviations | Bewusste Abweichungen oder offene Punkte. |

## Profilstatus

| Status | Bedeutung |
|---|---|
| `normative-base` | Basisprofil aus Standard, kein echtes Gerät. |
| `manufacturer-base` | Herstellerfamilie ohne konkretes Modell. |
| `device-target` | Konkretes Modell/Firmware als Zielprofil. |
| `candidate` | Gewünscht, aber Referenzlage noch unsicher. |
| `stub` | Syntaktisch vorhanden, noch nicht verifiziert. |
| `verified` | Gegen Manual und/oder echtes Gerät getestet. |

## Coverage-Datei

```yaml
coverage:
  source: references/sierra-hl78xx-v29
  commands:
    - command: AT
      status: implemented_full
      handler: BasicAtHandler
    - command: +CREG
      status: implemented_full
      handler: CregHandler
    - command: +KCNXCFG
      status: stub
      handler: UnsupportedOrMacroHandler
    - command: +WDSI
      status: unsupported_declared
      reason: not required by current integration tests
```

## Unsupported-Strategie

Unbekannte Befehle dürfen nicht willkürlich beantwortet werden. Profilabhängige Policies:

- `ERROR`
- `+CME ERROR: operation not supported`
- Herstellerfehlercode
- Keine Antwort / Timeout
- Macro-only

Für Abnahmetests ist jede Unsupported-Antwort im Coverage-Report zu dokumentieren.

## XML-Profilkonfiguration

Profile koennen als XML-Datei geladen werden. Der initiale Zustand muss dabei mindestens SIM-, Netzregistrierungs- und Signalwerte aufnehmen; SIM-PIN- und Netzbetreiberdaten sind keine hart codierten Defaults.

```xml
<modem-simulator version="1.0">
  <profile id="sierra-hl78xx-test" extends="sierra-hl78xx-v29">
    <identity manufacturer="Sierra Wireless" model="HL7812"/>
    <dialect commandTerminator="CR" responseTerminator="CRLF"/>
    <initial-state>
      <sim state="SIM_PIN_REQUIRED"
           pinQueryEnabled="true"
           pin="1234"
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
                        rejectCmsError="310"/>
        <delays>
          <delay operation="sms-submit" minMs="500" maxMs="2500"/>
          <delay operation="dial" minMs="1000" maxMs="5000"/>
        </delays>
      </network>
      <signal rssi="18" ber="0"/>
    </initial-state>
  </profile>
</modem-simulator>
```

Ladeanforderungen:

- XML wird vor Aktivierung gegen `schemas/modem-profile.schema.xsd` validiert.
- `pinQueryEnabled`, `pin`, `pinRetries`, `pukRetries`, `imsi` und `iccid` werden in den Session-State uebernommen.
- `network/operator` wird in den Session-State uebernommen und von Operator-Commands wie `AT+COPS?` verwendet.
- `network/sms-rate-limit` wird in den Session-State uebernommen und begrenzt akzeptierte SMS-Submits pro Zeitfenster.
- `network/delays` wird in den Session-State uebernommen und durch den Response-Scheduler pro Operation angewendet.
- PIN-Werte gelten als Testdaten und werden in Event-Logs, GUI-Anzeigen und Exporten redigiert.
- Inkonsistente Kombinationen muessen abgelehnt werden, z. B. `pinQueryEnabled=true` ohne `pin`, `network/operator/@numeric` ohne gueltige MCC/MNC-Struktur, `delay/@minMs > delay/@maxMs` oder doppelte Delay-Operationen.
