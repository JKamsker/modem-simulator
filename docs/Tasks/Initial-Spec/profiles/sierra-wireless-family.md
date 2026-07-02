# Profilfamilie: Sierra Wireless / Semtech

## Vererbung

```text
generic-hayes-v250
3gpp-27007-r18
3gpp-27005-r16
  -> sierra-common
      -> sierra-hl6-hl8-v20
```

Sierra-Wireless/Semtech-LTE ist out-of-scope. Dazu gehoeren insbesondere `sierra-mc-sl-umts-lte-v8`, `sierra-hl78xx-v29`, `sierra-em74xx-mc74xx-r3`, `sierra-em75xx-emmc74x1-v8` und `sierra-em9-v14`. Diese Profile duerfen nicht als v1-, post-v1- oder Candidate-Ziele behandelt werden.

`sierra-umts-airprime-standard-2130617-r7` bleibt candidate/post-v1, bis ein konkretes nicht-LTE-Zielmodul, Coverage und Smoke-Tests vorliegen.

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

Status: out-of-scope, weil Sierra-LTE fuer diese Spec nicht abgedeckt wird.

### `sierra-hl6-hl8-v20`

Zweck: AirPrime HL6528x und HL85xxx embedded modules. Dies ist der beste erste Kandidat für "aktuellere embedded UMTS" innerhalb der älteren Sierra-HL-Familie.

### `sierra-hl78xx-v29`

Status: out-of-scope, weil HL78xx LPWA/LTE-M/NB-IoT-orientiert ist.

### `sierra-em75xx-emmc74x1-v8`

Referenznotiz: Neuere Semtech 9x50-basierte Embedded-Module mit proprietären, teils passwortgeschützten Befehlen.
Status: out-of-scope, weil Sierra-LTE fuer diese Spec nicht abgedeckt wird.

### `sierra-em9-v14`

Referenznotiz: Moderne EM9-Serie; nicht als Zielprofil fuer diese Spec verwenden.
Status: out-of-scope, weil moderne Sierra-LTE/5G-Profile fuer diese Spec nicht abgedeckt werden.

## Implementierungspriorität pro Sierra-v1-Profil

1. Identität und Standard-AT.
2. SIM/Netz/Signal: `+CPIN`, `+CMEE`, `+CREG`, `+CGREG`, `+CSQ`.
3. SMS: `+CMGF`, `+CMGS`, `+CMGR`, `+CMGL`, `+CMGD`, `+CNMI`, `+CPMS`, `+CSCA`.
4. Packet Domain: `+CGDCONT`, `+CGACT`, `+CGPADDR`, optional.
5. Sierra-spezifische Commands ueber Coverage und Makros.
