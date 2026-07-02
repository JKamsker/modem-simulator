# 04 - AT-Parser und Command Engine

## Parser-Ziele

Der Parser wandelt zeitgestempelte Bytestreams in AT-Frames und Betriebszustandsereignisse um. Er muss robust gegen langsam eintreffende Bytes, Backspace, CR/LF-Varianten und binaere Daten im Data Mode sein.

## Framing

Die aktiven S-Register steuern die Zeichen fuer Framing:

| Register | Bedeutung | Default im Hayes-Basisprofil |
|---|---|---:|
| S3 | Command line termination character | 13 (`CR`) |
| S4 | Response formatting character | 10 (`LF`) |
| S5 | Command line editing character | 8 (`BS`) |

`ProfileDialect.commandTerminator` und `responseTerminator` sind nur Startwerte fuer den Session-State. Sobald S3/S4/S5 geaendert werden, muss der Parser/Formatter den State verwenden. Damit gibt es keine zweite statische Wahrheit fuer Line-Endings.

Response-Zeilen im verbose mode werden standardmaessig als:

```text
<S3><S4>line<S3><S4>
```

ausgegeben. Das SMS-Submit-Prompt in Text- und PDU-Modus ist bytegenau:

```text
<S3><S4>> SP
```

Bei Defaults entspricht das Hex `0D0A3E20`.

## Basissyntax

Unterstuetzte Formen:

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
| Exec | `AT+CSQ` | Ausfuehren ohne Parameter. |
| Set | `AT+CREG=2` | Parameter setzen. |
| Read | `AT+CREG?` | Aktuellen Wert lesen. |
| Test | `AT+CREG=?` | Unterstuetzte Werte ausgeben. |
| Basic | `ATE0`, `ATH` | Klassische AT-Befehle ohne `+`. |
| S-Register | `ATS7?`, `ATS7=60` | Register lesen/schreiben. |
| Special | `A/`, `+++` | Keine normale AT-Zeile. |

## Echo und Result Codes

Echo, Quiet und Verbose werden pro Session-State gesteuert:

```text
ATE0 -> Echo aus
ATE1 -> Echo ein
ATQ0 -> Result Codes an
ATQ1 -> Result Codes aus
ATV0 -> numerische Result Codes
ATV1 -> verbose Result Codes
```

`ATQ1` unterdrueckt nur abschliessende Result Codes und zugehoerige Handler-Ausgaben, nicht zwingend URCs. Profile duerfen URC-Unterdrueckung gesondert definieren.

## Command Chaining

Der Parser muss V.250-kompatible Verkettung als echte Tokenisierung implementieren:

```text
ATE0V1Q0
AT+CMEE=2;+CREG=2;+CSQ
```

Regeln fuer v1:

- Eine Zeile beginnt mit `AT`; `A/` und `+++` sind Spezialformen ausserhalb normaler Zeilen.
- Basic-Befehle koennen ohne Semikolon verkettet werden, wenn ihre Syntaxlaenge eindeutig ist.
- Extended Commands beginnen mit `+`, `%`, `#`, `!` oder einem profildefinierten Prefix und laufen bis zum Semikolon oder Zeilenende. Trennzeichen innerhalb Quotes oder PDU/Text-Entry zaehlen nicht.
- Bei einem Fehler bricht die restliche Zeile ab. Bereits erfolgreich ausgefuehrte Befehle bleiben wirksam.
- Wenn `ATQ1` vor einem spaeteren Befehl derselben Zeile wirksam wird, werden dessen finale Result Codes unterdrueckt.

Der `ParsedCommand` muss Rohspanne, normalisierten Namen, Kind, Parameter, Quote/PDU-Kontext und Position in der Ursprungszeile enthalten. Ein einzelner String plus flache Argumentliste reicht nicht.

## Command Router

Der `SessionActor` ruft den Router in dieser Reihenfolge auf:

1. Session-spezifische Macro Hooks `before`.
2. Profil-spezifische Handler.
3. Geerbte Profil-Handler nach linearisierter Profilreihenfolge.
4. Standard-Handler.
5. Macro Hooks `after`.
6. Unsupported-Policy.

Macro `replace` hat Vorrang vor dem normalen Handler.

## Data Mode und Online Command Mode

Zustaende:

```text
COMMAND_MODE
DIALING
ONLINE_DATA_MODE
ONLINE_COMMAND_MODE
SMS_TEXT_ENTRY_MODE
SMS_PDU_ENTRY_MODE
```

Uebergaenge:

- `ATD...` kann nach `CONNECT` in `ONLINE_DATA_MODE` wechseln.
- `+++` kann nur nach Guard-Time in `ONLINE_COMMAND_MODE` wechseln.
- `ATO` kehrt aus `ONLINE_COMMAND_MODE` in `ONLINE_DATA_MODE` zurueck.
- `ATH` trennt, setzt DCD false und kehrt nach `COMMAND_MODE` zurueck; der finale Result Code ist `OK`.
- `AT+CMGS=...` erzeugt Prompt `<S3><S4>> ` und wechselt in SMS-Entry-Mode.
- Ctrl-Z beendet SMS-Eingabe, ESC bricht ab.

`+++`-Erkennung nutzt die RX-Zeitstempel aus Kapitel 03. Der Parser muss Idle-Zeit vor dem ersten `+`, Inter-Byte-Abstaende zwischen den drei Zeichen und Idle-Zeit nach dem dritten `+` messen. Die Guard-Time kommt aus S12 oder aus einem profildefinierten Aequivalent.

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
