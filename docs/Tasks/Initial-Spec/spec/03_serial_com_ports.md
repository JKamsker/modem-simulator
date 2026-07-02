# 03 - Serielle COM-Ports

## Anforderungen

Der Serial Endpoint Layer muss mehrere Porttypen unterstützen:

| Typ | Beispiel | Muss in v1 |
|---|---|---|
| Physischer RS-232/USB-Serial-Port | `COM3`, `/dev/ttyUSB0` | Ja |
| Virtuelles COM-Paar | com0com, tty0tty, socat PTY | Ja, als externer Treiber |
| USB CDC/ACM | `/dev/ttyACM0`, Windows COMx | Ja, als Betriebssystemgerät |
| TCP-Serial-Bridge | RFC2217, eigener TCP Endpoint | Optional |
| Headless | In-Memory-Teststream | Ja für Tests |

## Konfiguration pro Port

```yaml
ports:
  - id: main
    type: serial
    name: COM7
    baudRate: 115200
    dataBits: 8
    stopBits: 1
    parity: NONE
    flowControl: RTS_CTS
    profile: sierra-hl78xx-v29
    lineEndingRx: CR
    lineEndingTx: CRLF
    initialScenario: registered-home-medium-signal
```

## Modem-Control-Lines

Folgende Signale müssen modelliert und im Eventlog sichtbar sein:

- DTR: DTE ready.
- DSR: Modem ready.
- DCD: Carrier detect.
- RI: Ring indicator.
- RTS/CTS: Hardware-Flow-Control.

Profilabhängige Regeln:

- `AT&D0..3` beeinflusst Verhalten bei DTR-Abfall.
- `AT&C0..1` beeinflusst DCD-Verhalten.
- `RING` oder eingehende SMS/Call-URCs können RI setzen.
- Connect/Disconnect kann DCD schalten.

## Java-Bibliotheken

Empfohlene primäre Bibliothek: `com.fazecast:jSerialComm`, weil sie Windows, Linux und macOS abdeckt und relativ einfach einzubinden ist. Alternativen sind `jSerialComm` plus JNA-spezifische Hilfen oder RXTX nur für Legacy-Umgebungen.

Die Architektur muss die Bibliothek hinter einem Interface kapseln:

```java
interface SerialEndpoint extends AutoCloseable {
    void open(SerialConfig config) throws SerialException;
    int read(byte[] buffer, int offset, int length) throws IOException;
    void write(byte[] buffer, int offset, int length) throws IOException;
    ModemLines getLines();
    void setLines(ModemLines lines);
}
```

## Fehlerfälle

| Fehler | Erwartetes Verhalten |
|---|---|
| Port nicht vorhanden | Startfehler mit klarer Diagnose. |
| Port belegt | Startfehler, kein stiller Retry ohne Konfiguration. |
| Device verschwindet | Session auf `PORT_LOST`, Eventlog-Eintrag, optional Auto-Reconnect. |
| Baudrate nicht unterstützt | Validierungsfehler. |
| Pufferüberlauf | Event `RX_OVERFLOW` oder `TX_OVERFLOW`, optional `ERROR`/Disconnect. |
