# Profilfamilie: Westermo

## Vererbung

```text
generic-hayes-v250
  -> westermo-common
      -> westermo-td20-candidate
      -> westermo-td22-6177-2203
      -> westermo-td32-6178-2203
      -> westermo-td33-6179-2203
      -> westermo-td36-6618-2202
      -> westermo-gd01-6196-2220
      -> westermo-gdw11-6615-2220
      -> westermo-idw90-6620-3200
```

## `westermo-common`

```yaml
westermo-common:
  commandTerminator: CR
  responseTerminator: CRLF
  supportsARepeat: true
  supportsBackspace: true
  supportsPlusPlusPlusEscape: true
  defaultEcho: true
  defaultVerbose: true
  storeWithAtW: true
```

## TD-Familie

Die TD-Familie ist primär analog/PSTN/leased-line geprägt. Für v1:

- AT-Basis.
- Dial/Hangup/Connect/No Carrier.
- S-Register.
- `AT&W` und `AT&V`.
- Control-Line-Verhalten.
- Keine Mobilfunkbefehle außer per Makro/Stub.

## GSM/GPRS-Familie

`GD-01` und `GDW-11` erweitern Westermo um Mobilfunk- und SMS-Verhalten:

- `+CGMI`, `+CGMM`, `+CGMR`, `+CGSN`.
- `+CPIN`, `+CREG`, `+CSQ`.
- SMS-Basis.
- Online Mode und Online Command Mode.

## IDW-90

IDW-90 braucht zusätzlich:

- Prefix `AT!` für analoges Modem-Verhalten.
- Prefix `AT**` für Konfigurationsbefehle.
- Remote-Konfiguration als optionaler Modus.

## TD20-Candidate-Regel

Ohne offizielle Quelle darf `westermo-td20-candidate` nur als generischer Westermo-TD-Fallback eingesetzt werden. Abnahmetexte müssen dies ausdrücklich erwähnen.
