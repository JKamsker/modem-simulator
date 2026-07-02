# 04 - AT-Parser und Command Engine

## Parser-Ziele

Der Parser wandelt Bytestreams in AT-Frames und Betriebszustandsereignisse um. Er muss robust gegenüber langsam eintreffenden Bytes, Backspace, CR/LF-Varianten und binären Daten im Data Mode sein.

## Basissyntax

Unterstützte Formen:

```text
AT
A/
+++
ATZ
ATE0
ATQ1
ATV0
AT+CREG?
AT+CREG=2
AT+CREG=?
AT+CMGS="+491701234567"
ATS7?
ATS7=60
AT&V
AT&W
```

Befehlstypen:

| Typ | Beispiel | Bedeutung |
|---|---|---|
| Exec | `AT+CSQ` | Ausführen ohne Parameter. |
| Set | `AT+CREG=2` | Parameter setzen. |
| Read | `AT+CREG?` | Aktuellen Wert lesen. |
| Test | `AT+CREG=?` | Unterstützte Werte ausgeben. |
| Basic | `ATE0`, `ATH` | Klassische AT-Befehle ohne `+`. |
| S-Register | `ATS7?`, `ATS7=60` | Register lesen/schreiben. |
| Special | `A/`, `+++` | Keine normale AT-Zeile. |

## Echo und Result Codes

Echo wird pro Session gesteuert:

```text
ATE0 -> Echo aus
ATE1 -> Echo ein
ATQ0 -> Result Codes an
ATQ1 -> Result Codes aus
ATV0 -> numerische Result Codes
ATV1 -> verbose Result Codes
```

Beispiel verbose:

```text
AT
OK
```

Beispiel numeric:

```text
ATV0
0
```

## Command Chaining

Der Parser soll Mehrfachbefehle in einer Zeile unterstützen, sofern das Profil dies erlaubt:

```text
ATE0V1Q0
AT+CMEE=2;+CREG=2;+CSQ
```

Die genaue Trennung für Extended Commands ist komplex und profilabhängig. Für v1 reicht:

- Basic-Befehle können ohne Semikolon verkettet werden.
- Extended Commands werden durch Semikolon getrennt.
- Bei Fehler bricht die Zeile ab und liefert profilabhängig `ERROR` oder `+CME ERROR`.

## Command Router

Der Router löst Befehle in dieser Reihenfolge auf:

1. Session-spezifische Macro Hooks `before`.
2. Profil-spezifische Handler.
3. Geerbte Profil-Handler.
4. Standard-Handler.
5. Macro Hooks `after`.
6. Unsupported-Policy.

Macro `replace` hat Vorrang vor dem normalen Handler.

## Data Mode und Online Command Mode

Zustände:

```text
COMMAND_MODE
DIALING
ONLINE_DATA_MODE
ONLINE_COMMAND_MODE
SMS_TEXT_ENTRY_MODE
SMS_PDU_ENTRY_MODE
```

Übergänge:

- `ATD...` kann nach `CONNECT` in `ONLINE_DATA_MODE` wechseln.
- `+++` kann nach Guard-Time in `ONLINE_COMMAND_MODE` wechseln.
- `ATO` kehrt aus `ONLINE_COMMAND_MODE` in `ONLINE_DATA_MODE` zurück.
- `ATH` trennt und kehrt nach `COMMAND_MODE` zurück.
- `AT+CMGS=...` erzeugt Prompt `> ` und wechselt in SMS-Entry-Mode.
- Ctrl-Z beendet SMS-Eingabe, ESC bricht ab.

## Fehler-Policy

Jedes Profil definiert:

```yaml
errorPolicy:
  unknownCommand: ERROR
  invalidParameter: CME_OR_ERROR
  stateFailure: CME
  smsFailure: CMS
  timeout: NO_RESPONSE
```

`AT+CMEE` steuert bei Mobilfunkfehlern:

| `+CMEE` | Ausgabe |
|---|---|
| 0 | `ERROR` |
| 1 | `+CME ERROR: <number>` |
| 2 | `+CME ERROR: <text>` |
