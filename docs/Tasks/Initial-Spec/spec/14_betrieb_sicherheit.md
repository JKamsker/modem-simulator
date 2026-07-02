# 14 - Betrieb und Sicherheit

## Deployment

Typische Modi:

```bash
# Ein Port, ein Profil
modemsim run --port COM7 --baud 115200 --profile sierra-hl78xx-v29

# Konfigurationsdatei
modemsim run --config config.yaml

# Headless-Test
modemsim headless --profile westermo-td20-candidate --script tests/creg-no-network.txt
```

## Logging

- Standard: JSONL pro Session.
- Optional: kompakte Texttranskripte.
- Optional: HTML-Report für manuelle Analyse.
- Logs dürfen keine echten SIM-PINs oder produktiven SMS-Inhalte unmaskiert speichern, wenn Datenschutzmodus aktiv ist.

## Datenschutz und Secrets

Maskierung konfigurierbar:

```yaml
redaction:
  enabled: true
  maskPhoneNumbers: true
  maskImsi: true
  maskImei: false
  maskSmsBody: configurable
```

## API-Sicherheit

- Default bindet nur lokal.
- Remote-Zugriff nur mit Token oder mTLS.
- WebSocket hat Backpressure und Drop-Policy.
- Injection ist in Audit-Logs markiert.
- Keine automatische Ausführung externer Befehle durch Makros.

## Betrieb in CI

- Headless-Modus bevorzugen.
- Keine OS-spezifischen COM-Abhängigkeiten in Unit-Tests.
- Serial-Integration separat mit Tag `serial-it`.
- Referenztranskripte versionieren.

## Fehlerdiagnose

Wichtige Metriken:

- Bytes RX/TX.
- Command Count.
- Unknown Command Count.
- Macro Hit Count.
- Average Response Latency.
- Dropped Events.
- Serial Reconnect Count.
