# 03 - Serielle COM-Ports

## Anforderungen

Der Transport Endpoint Layer muss in v1 diese Porttypen unterstuetzen:

| Typ | Beispiel | Muss in v1 |
|---|---|---|
| Physischer RS-232/USB-Serial-Port | `COM3`, `/dev/ttyUSB0` | Ja |
| Virtuelles COM-Paar | com0com, socat PTY | Ja, als externer Treiber |
| USB CDC/ACM | `/dev/ttyACM0`, Windows COMx | Ja, als Betriebssystemgeraet |
| Headless | In-Memory-Teststream | Ja fuer Tests |
| TCP-Serial-Bridge | RFC2217, eigener TCP Endpoint | Nein, post-v1 |

v1-Zielplattformen fuer CI und Support:

| OS | Virtuelle Port-Strategie |
|---|---|
| Windows 11 | com0com oder echte USB-Serial-Hardware fuer `serial-it` |
| Linux LTS | socat PTY fuer `serial-it`; tty0tty optional |

macOS ist zulaessig, aber kein v1-Abnahmekriterium.

## Konfiguration pro Port

`config.yaml` wird nach YAML-Parsing gegen `schemas/config.schema.json` validiert.

```yaml
sessionSeed: 12345
clockMode: monotonic
ports:
  - id: main
    type: serial
    name: COM7
    baudRate: 115200
    dataBits: 8
    stopBits: 1
    parity: NONE
    flowControl: NONE
    profile: sierra-hl78xx-v29
    initialScenario: registered-home-medium-signal
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

Line-Endings kommen aus Profil/S-Register-State. `config.yaml` darf sie nicht als zweite Wahrheit ueberschreiben.

## Modem-Control-Lines

v1 ist nicht vollstaendig RS-232-line-aware, muss aber ein minimales `ModemLines`-Modell bereitstellen, weil Datenmodus und Westermo-TD-Profile sonst fuer line-aware DTEs nicht abnahmefaehig sind.

Pflichtumfang v1:

| Line | v1-Verhalten |
|---|---|
| DTR | Eingehender DTE-ready-Status. Bei `AT&D2` fuehrt DTR-Drop zu Hangup und Command Mode. |
| DSR | DCE-ready-Status; beim Session-Start `true`, bei `PORT_LOST` `false`. |
| DCD | Carrier Detect; `true` nach `CONNECT`, `false` nach `ATH`, Carrier Loss oder DTR-Hangup. |
| RI | Optionales Ring-Signal fuer Call/SMS-URCs; muss geloggt werden, wenn gesetzt. |
| RTS/CTS | Sichtbar im State und Eventlog; Hardware-Flow-Control wird nur genutzt, wenn der Endpoint es anbietet. |

`AT&D0..3` und `AT&C0..1` muessen fuer Profile mit `lineModel=minimal-v250` implementiert werden. Profile mit `lineModel=byte-only` muessen klar als nicht geeignet fuer line-aware DTEs markiert sein.

## Endpoint API

Die Architektur kapselt die Bibliothek hinter einem Interface. RX-Bytes muessen mit einer monotonen Eingangszeit oder einer Idle-Gap-Information beim Parser ankommen, damit `+++`-Guard-Time testbar ist.

```java
interface SerialEndpoint extends AutoCloseable {
    void open(SerialConfig config) throws SerialException;
    SerialRead read() throws IOException;
    void write(byte[] buffer, int offset, int length) throws IOException;
    ModemLines readLines();
    void writeLines(ModemLines lines);
}

record SerialRead(byte[] bytes, long firstByteMonotonicNanos, long lastByteMonotonicNanos) {}
```

Headless-Endpoints liefern dieselben Datenstrukturen mit virtueller Clock.

## Java-Bibliotheken

Primaere Bibliothek: `com.fazecast:jSerialComm`. Die konkrete Version wird im Build-Manifest exakt gepinnt; Versionsbereiche oder dynamische Versionen sind nicht zulaessig. Native Access Flags sind in Kapitel 12 verbindlich geregelt.

## Fehlerfaelle

| Fehler | Erwartetes Verhalten |
|---|---|
| Port nicht vorhanden | Startfehler mit klarer Diagnose. |
| Port belegt | Startfehler, kein stiller Retry ohne Konfiguration. |
| Device verschwindet | Session auf `PORT_LOST`, DSR/DCD false, Scheduler-Cancel, Eventlog-Eintrag, optional Auto-Reconnect nur nach Konfiguration. |
| Baudrate nicht unterstuetzt | Validierungsfehler. |
| Pufferueberlauf | Event `RX_OVERFLOW` oder `TX_OVERFLOW`; bei Datenverlust muss die Session fuer Replay als divergent markiert werden. |

`PORT_LOST` darf nicht allein aus einem leeren oder timeoutenden `read()` abgeleitet werden. Die Implementierung muss Bibliotheksereignisse, IOException-Klassen und Port-Reenumeration kombinieren und die Diagnose loggen.
