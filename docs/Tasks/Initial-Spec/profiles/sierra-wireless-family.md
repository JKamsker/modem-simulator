# Profilfamilie: Sierra Wireless / Semtech

## Vererbung

```text
generic-hayes-v250
3gpp-27007-r18
3gpp-27005-r16
  -> sierra-common
      -> sierra-hl6-hl8-v20
      -> sierra-hl78xx-v29
```

`sierra-umts-airprime-standard-2130617-r7`, `sierra-mc-sl-umts-lte-v8`, `sierra-em75xx-emmc74x1-v8` und `sierra-em9-v14` bleiben candidate/post-v1, bis sie Coverage und Smoke-Tests besitzen.

## Gemeinsame Defaults

```yaml
sierra-common:
  responseTerminator: CRLF
  commandTerminator: CR
  smsPromptBytes: "0D0A3E20"
  defaultEcho: true
  defaultVerbose: true
  defaultQuiet: false
  defaultCmee: 0
```

## Konkrete Profil-Slots

### `sierra-umts-airprime-standard-2130617-r7`

Zweck: Standard-/Proprietary-Basis für ältere UMTS AirCard/AirPrime-Geräte. Gut geeignet als Startpunkt für Embedded-UMTS-Anforderungen.

### `sierra-mc-sl-umts-lte-v8`

Zweck: MC/SL-Serie mit UMTS/LTE-Extended-Commands. Diese Familie soll für Geräte verwendet werden, die MC/SL-spezifische Befehle oder Sierra-spezifische Packet-Service-URCs erwarten.

### `sierra-hl6-hl8-v20`

Zweck: AirPrime HL6528x und HL85xxx embedded modules. Dies ist der beste erste Kandidat für "aktuellere embedded UMTS" innerhalb der älteren Sierra-HL-Familie.

### `sierra-hl78xx-v29`

Zweck: Aktueller offizieller Dokumentstand 2026-05-19 für HL78xx. Obwohl primär neuere LPWA/embedded Familie, ist sie nützlich als moderner Sierra-AT-Stil.

### `sierra-em75xx-emmc74x1-v8`

Zweck: Neuere Semtech 9x50-basierte Embedded-Module mit proprietären, teils passwortgeschützten Befehlen. Default: proprietäre Befehle als Stub oder unsupported deklarieren.

### `sierra-em9-v14`

Zweck: Moderne EM9-Serie. Relevant, wenn das Testgerät neuere Sierra/Semtech Embedded-Module erwartet.

## Implementierungspriorität pro Sierra-Profil

1. Identität und Standard-AT.
2. SIM/Netz/Signal: `+CPIN`, `+CMEE`, `+CREG`, `+CGREG`, `+CEREG`, `+CSQ`.
3. SMS: `+CMGF`, `+CMGS`, `+CMGR`, `+CMGL`, `+CMGD`, `+CNMI`, `+CPMS`, `+CSCA`.
4. Packet Domain: `+CGDCONT`, `+CGACT`, `+CGPADDR`, optional.
5. Sierra-spezifische Commands ueber Coverage und Makros.
