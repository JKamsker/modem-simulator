# 14 - Betrieb und Sicherheit

## Deployment

Typische Modi:

```bash
# Ein Port, ein Profil
modemsim run --port COM7 --baud 115200 --profile sierra-hl6-hl8-v20

# Konfigurationsdatei
modemsim run --config config.yaml

# Headless-Test
modemsim headless --profile westermo-td22-6177-2203 --script tests/creg-no-network.yaml
```

## Logging

- Standard: redigiertes JSONL pro Session.
- Optional: redigierte Texttranskripte.
- Optional: HTML-Report fuer manuelle Analyse.
- Replay-Logs sind nur dann abspielbar, wenn die benoetigten Bytes test-sicher und nicht redigiert sind oder aus einem explizit lokalen, geschuetzten Capture stammen.
- Persistente Logs, Exporte und Replay-Artefakte duerfen keine PIN/PUK/IMSI/ICCID/IMEI/MSISDN/SMS-Body-Werte unmaskiert speichern.

## Datenschutz und Secrets

Redaction ist in v1 default-on und fuer persistente Artefakte verpflichtend.

```yaml
redaction:
  enabled: true
  maskPin: true
  maskPuk: true
  maskImsi: true
  maskIccid: true
  maskImei: true
  maskMsisdn: true
  maskSmsBody: true
```

Redaction gilt fuer:

- `textEscaped`,
- `rawHex`,
- GUI-Anzeigen,
- JSONL-Logs,
- Text-/HTML-Exports,
- Replay-Artefakte,
- Fehlermeldungen und Validation Reports.

Jedes Event enthaelt ein `redaction`-Objekt mit `applied`, `policy`, `fields` und `classes`. Wenn Redaction eine bytegenaue Wiedergabe verhindert, muss Replay im Playback-Modus ablehnen und eine Diagnose ausgeben.

Profile verwenden fuer echte Geheimnisse Referenzen wie `pinRef`; Klartext-`pin` ist nur fuer explizite Testfixtures erlaubt und wird nie in Logs geschrieben.

## XML-Sicherheit

Profile, Makros und Szenarien sind nutzergesteuerte XML-Dateien. XML-Loader muessen:

- DOCTYPE vollstaendig verbieten,
- externe Entities deaktivieren,
- Parameter-Entities deaktivieren,
- XInclude deaktivieren,
- externe Schema-/DTD-Aufloesung verbieten,
- Secure Processing aktivieren,
- maximale Dateigroesse definieren,
- maximale Elementtiefe definieren,
- maximale Text-/Attributlaenge definieren,
- Entity Expansion und Billion-Laughs-Fixtures ablehnen.

CI muss negative Fixtures fuer XXE, externe Schema-URLs, XInclude, Entity Expansion, zu grosse Dateien und zu tiefe Baeume enthalten.

## GUI-Sicherheit

- Injection ist in Audit-Logs markiert und wird vor Ausfuehrung geloggt.
- Wenn ein Audit-Event fuer Injection, Macro-Control, Replay-to-DTE oder State-Change nicht geschrieben werden kann, wird die Aktion abgelehnt.
- v1 bietet keine HTTP- oder WebSocket-Control-API.
- GUI-Aktionen werden ueber eine nicht-blockierende Queue als `SessionCommand` an den `SessionActor` uebergeben.
- Read-only-Modus verhindert Injection, State-Aenderungen, Macro-Control, Replay-to-DTE und Reconnect.
- `raw-dce-to-dte` braucht eine gesonderte Berechtigung (`allowUnsafeDceTransmit=true`) und sichtbare Bestaetigung.
- Der optionale Port mit Rolle `manual-dce-injection` gilt als externe Quelle fuer `raw-dce-to-dte`; er darf nur geoeffnet werden, wenn diese Berechtigung aktiv ist.
- Keine automatische Ausfuehrung externer Befehle durch Makros.

## Event-Queue und Audit-Vollstaendigkeit

Der Event-Publisher darf normale Telemetrie bei Backpressure gemaess Policy droppen, muss dann aber ein `droppedEventCount`-Event erzeugen. Audit-kritische Events duerfen nicht gedroppt werden:

- Injection,
- State-Change,
- Fault-Trigger,
- Macro-Control,
- Replay-Start/Stop,
- Redaction-Fehler,
- XML-Validation-Fehler,
- `PORT_LOST`.

Falls Audit-kritische Events nicht persistiert werden koennen, stoppt die mutierende Aktion vor der Ausfuehrung.

## Betrieb in CI

- Headless-Modus bevorzugen.
- Keine OS-spezifischen COM-Abhaengigkeiten in Unit-Tests.
- Serial-Integration separat mit Tag `serial-it`.
- GUI-Tests separat mit Tag `gui`, aber in der v1-CI enthalten.
- Referenztranskripte versionieren.
- Coverage-, Config-, Scenario-, Macro- und Profile-Schemas in default CI validieren.

## Fehlerdiagnose

Wichtige Metriken:

- Bytes RX/TX.
- Command Count.
- Unknown Command Count.
- Macro Hit Count.
- Average Response Latency.
- Dropped Non-Audit Events.
- Audit Failure Count.
- Serial Reconnect Count.
- Scheduler Queue Depth.
- Redaction Applied Count.
