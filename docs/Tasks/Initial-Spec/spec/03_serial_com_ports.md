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

## Portrollen und gemeinsame serielle Parameter

`config.yaml` wird nach YAML-Parsing gegen `schemas/config.schema.json` validiert.

Eine Runtime-Session besteht aus einer Portgruppe mit bis zu drei logischen Ports:

| Rolle | Pflicht | Aktivierung | Zweck |
|---|---|---|---|
| `modem-simulation` | Ja | Immer aktiv | Hauptport fuer das externe Geraet. Der Simulator verarbeitet hier AT-Kommandos und sendet Modemantworten. |
| `sniffer` | Nein | Optional aktivierbar | Read-only Monitor-Port fuer externe Sniffer/Terminal-Tools. Er spiegelt den Datenverkehr der Hauptsession in einem konfigurierten Sniffer-Format. |
| `manual-dce-injection` | Nein | Optional aktivierbar | Manueller Eingangsport, z. B. fuer PuTTY. Bytes von diesem Port werden als DCE-Ausgabe an das am Hauptport angeschlossene Geraet gesendet. |

Baudrate, Paritaet, Datenbits und Stopbits sind fuer alle Ports der Portgruppe einheitlich. Sie stehen genau einmal unter `serialLine`. Port-spezifische Werte fuer `baudRate`, `parity`, `dataBits` oder `stopBits` sind ungueltig und muessen durch Config-Validation abgelehnt werden. `flowControl` ist ebenfalls gruppenweit, wenn es genutzt wird.

```yaml
sessionSeed: 12345
clockMode: monotonic
serialLine:
  baudRate: 115200
  dataBits: 8
  stopBits: 1
  parity: NONE
  flowControl: NONE
ports:
  - id: modem
    type: serial
    role: modem-simulation
    name: COM7
    enabled: true
    profile: sierra-hl6-hl8-v20
    initialScenario: registered-home-medium-signal
  - id: sniffer
    type: serial
    role: sniffer
    name: COM8
    enabled: false
    snifferFormat: tagged-text
  - id: manual
    type: serial
    role: manual-dce-injection
    name: COM9
    enabled: false
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

Semantik:

- `modem-simulation` muss genau einmal vorhanden sein und `enabled: true` setzen.
- `sniffer` darf hoechstens einmal vorhanden sein. Wenn aktiviert, empfaengt er eine Kopie von RX/TX-Events der Hauptsession, veraendert aber nie State und sendet nie Bytes zum Hauptport.
- `manual-dce-injection` darf hoechstens einmal vorhanden sein. Wenn aktiviert, werden eingehende Bytes dieses Ports als `raw-dce-to-dte` Injection behandelt, vor Ausfuehrung auditierbar geloggt und an den Hauptport geschrieben.
- Optional aktivierte Ports, die nicht geoeffnet werden koennen, erzeugen ein klares Diagnose-Event. Die Hauptsimulation darf weiterlaufen, sofern keine `strictOptionalPorts`-Policy gesetzt ist.
- Die GUI darf die gemeinsamen seriellen Parameter nur gruppenweit aendern. Eine Aenderung erfordert einen Restart der Portgruppe.

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
| Port nicht vorhanden | Startfehler mit klarer Diagnose und Diagnosecode `PORT_NOT_FOUND`. |
| Port belegt | Startfehler mit klarer Diagnose und Diagnosecode `PORT_BUSY`; kein stiller Retry ohne Konfiguration. |
| Zugriff verweigert | Startfehler mit klarer Diagnose und Diagnosecode `PORT_PERMISSION_DENIED`; kein stiller Retry ohne Konfiguration. |
| Device verschwindet | Session auf `PORT_LOST`, DSR/DCD false, Scheduler-Cancel, Eventlog-Eintrag, optional Auto-Reconnect nur nach Konfiguration. |
| Serielle Parameter ausserhalb Config-Schema | Validierungsfehler vor Open-Versuch. |
| Endpoint lehnt schema-gueltige serielle Parameter ab | Startfehler mit Diagnosecode `UNSUPPORTED_PARAMETERS`. |
| Pufferueberlauf | Event `RX_OVERFLOW` oder `TX_OVERFLOW`; bei Datenverlust muss die Session fuer Replay als divergent markiert werden. |

Open-Time-Fehler muessen `not present`, `busy`, `permission denied` und `unsupported parameters` unterscheiden. Optional aktivierte Sidecar-Ports verwenden dieselben Diagnosecodes und erzeugen `PORT_OPEN_FAILED`-Events mit `source=transport`, `port`, `portRole`, `direction=INTERNAL|NONE` und leerem `rawHex`; bei `strictOptionalPorts=false` bleibt die Hauptsession danach aktiv.

`PORT_LOST` darf nicht allein aus einem leeren oder timeoutenden `read()` abgeleitet werden. Die Implementierung muss Bibliotheksereignisse, IOException-Klassen und Port-Reenumeration kombinieren und die Diagnose loggen.
