# 12 - Java-Implementation

## Paketstruktur

```text
com.jkamsker.modemsim
  app
  transport
  parser
  commands
  profiles
  state
  macros
  scheduler
  gui
  monitor
  replay
  testkit
```

`com.example.*` ist in produktivem Code und generierten Beispielen nicht zulaessig.

## Zentrale Interfaces

```java
public interface SessionActor {
    CompletionStage<SessionResult> submit(SessionCommand command);
    ModemState snapshot();
}

public interface CommandHandler {
    CommandResult handle(CommandContext context, ParsedCommand command);
}

public interface Profile {
    String id();
    Dialect dialect();
    ErrorPolicy errorPolicy();
    InitialState initialState();
    RegisterCatalog registers();
    Optional<CommandHandler> resolve(ParsedCommand command);
}

public interface MacroEngine {
    MacroDecision evaluate(MacroPhase phase, CommandContext context, ParsedInput input);
}

public interface ProfileLoader {
    Profile load(Path xmlProfilePath);
    ValidationReport validate(Path xmlProfilePath);
}

public interface EventSink {
    void publish(ModemEvent event);
}
```

Es gibt kein oeffentliches `StateStore.update(...)` fuer beliebige Threads. State-Aenderungen laufen als `SessionCommand` ueber den `SessionActor`.

Der effektive `RegisterCatalog` entsteht aus der Profilvererbung. Jeder Eintrag enthaelt Name, Default, Min, Max, Schreibbarkeit und Persistenz. Semantic-Validation lehnt Default-Werte ausserhalb Min/Max, `min > max`, doppelte Register nach Linearisierung ohne klare Ueberschreibung und Schreibversuche auf nicht schreibbare Register ab. Handler duerfen keine globale S-Register-Min/Max-Tabelle verwenden, wenn das aktive Profil eine abweichende Grenze definiert.

## Parser-Objekte

```java
record ParsedCommand(
    RawBytes sourceLine,
    String normalizedName,
    CommandKind kind,
    List<AtToken> tokens,
    CommandSpan span,
    int commandIndexInLine,
    EntryMode entryMode
) {}

enum CommandKind {
    BASIC,
    EXTENDED_EXEC,
    EXTENDED_SET,
    EXTENDED_READ,
    EXTENDED_TEST,
    S_REGISTER_READ,
    S_REGISTER_WRITE,
    SPECIAL_REPEAT,
    SPECIAL_ESCAPE
}
```

`RawBytes` ist kein Record mit mutablem `byte[]`. Es muss defensiv kopieren oder ein immutable Byte-Container sein:

```java
final class RawBytes {
    static RawBytes copyOf(byte[] source);
    byte[] toByteArray();
    String toHex();
}
```

## Response-Modell

```java
sealed interface ResponseFrame permits TextLine, RawBytesFrame, DelayFrame, ResultCodeFrame,
        StatePatchFrame, UrcFrame, PromptFrame {
}

record TextLine(String line, SuppressionPolicy suppression) implements ResponseFrame {}
record RawBytesFrame(RawBytes bytes, SuppressionPolicy suppression) implements ResponseFrame {}
record DelayFrame(Duration duration, String operation) implements ResponseFrame {}
record ResultCodeFrame(ResultCodeType type, SuppressionPolicy suppression) implements ResponseFrame {}
record StatePatchFrame(StatePatch patch) implements ResponseFrame {}
record UrcFrame(TextLine line) implements ResponseFrame {}
record PromptFrame(RawBytes bytes) implements ResponseFrame {}
```

`SuppressionPolicy` entscheidet, ob `ATQ1` die Ausgabe unterdrueckt. URCs sind nicht automatisch von `ATQ1` betroffen.

## Threading

Pro Session:

- Ein RX-Thread liest seriell und erzeugt zeitgestempelte `SessionCommand`-Nachrichten.
- Genau ein `SessionActor` verarbeitet Parser, Command Handler, Makros, State-Patches und Replay sequentiell.
- Ein Response-Scheduler verwaltet Delays und URCs deterministisch, aber State-Aenderungen laufen zurueck ueber den Actor.
- Ein Event-Publisher entkoppelt Logging und GUI-Updates.

Regel: Der serielle RX-Pfad darf nie durch langsame GUI-Rendering-, Filter- oder Export-Aktionen blockiert werden. Audit-kritische Events duerfen aber nicht still verloren gehen; siehe Kapitel 14.

## Scheduler

```java
record ScheduledEmission(
    long dueMonotonicNanos,
    long sequence,
    SourcePriority sourcePriority,
    RawBytes payload,
    long stateVersion,
    boolean cancelOnStateChange
) {}
```

Ordering und Cancellation entsprechen Kapitel 02. Headless-Tests muessen eine virtuelle Clock injizieren koennen.

## Portgruppen

Die Runtime-Konfiguration trennt gemeinsame serielle Parameter von Portrollen:

```java
record SerialLineConfig(
    int baudRate,
    int dataBits,
    int stopBits,
    Parity parity,
    FlowControl flowControl
) {}

record PortBinding(
    String id,
    EndpointType type,
    PortRole role,
    String name,
    boolean enabled
) {}

enum PortRole {
    MODEM_SIMULATION,
    SNIFFER,
    MANUAL_DCE_INJECTION
}
```

`SerialLineConfig` wird auf alle aktivierten seriellen Ports derselben Session angewendet. `PortBinding` darf keine eigenen Baudrate-/Parity-/DataBits-/StopBits-Werte tragen. Der `MODEM_SIMULATION`-Port ist immer aktiv; `SNIFFER` und `MANUAL_DCE_INJECTION` sind optionale Sidecar-Ports.

Bytes vom `MANUAL_DCE_INJECTION`-Port werden als `raw-dce-to-dte` Injection an den Hauptport gesendet. Der `SNIFFER`-Port ist read-only gegenueber der Session: Eingaben werden ignoriert und als Diagnose geloggt, aber nicht an Parser, State oder Hauptport weitergegeben.

Open-Time-Fehler des Transport-Layers tragen eine maschinenlesbare Kategorie (`PORT_NOT_FOUND`, `PORT_BUSY`, `PORT_PERMISSION_DENIED`, `UNSUPPORTED_PARAMETERS`). Laufzeitfehler tragen getrennte Kategorien (`PORT_LOST`, `RX_OVERFLOW`, `TX_OVERFLOW`). Die Runtime mappt diese Kategorien in Eventlog-Diagnosen, Start-/Sidecar-Fehler und Fail-Closed-Verhalten statt nur eine generische Exception-Message zu zeigen.

## Event-Publisher

Der Event-Publisher misst `latencyMs` fuer Handler-, Macro- und Injection-Ergebnisse aus der Session-Clock. `0.0` ist nur gueltig, wenn die gemessene Dauer wirklich null ist; ein hart codierter Platzhalter ist ungueltig.

Der Publisher darf eine bounded Queue fuer nicht audit-kritische Telemetrie verwenden. Wird dabei gedroppt, muss er `droppedEventCount` akkumulieren und ein `DROPPED_EVENTS`-Event erzeugen. Audit-kritische Events aus Kapitel 14 werden synchron oder mit garantiertem Durable-Ack persistiert.

## Faults und Custom Responses

Makros und Szenarien koennen Faults als `SessionCommand` erzeugen:

```java
record FaultCommand(FaultType type, FaultParameters parameters) implements SessionCommand {}

enum FaultType {
    NETWORK_OUTAGE,
    NETWORK_RESTORE,
    MODEM_REBOOT,
    MODEM_FREEZE,
    MODEM_UNFREEZE
}
```

Der `SessionActor` uebersetzt Faults in State-Patches, Scheduler-Cancellations und optionale URCs. Reboot und Freeze duerfen keine Threads blockieren; sie sind normale State-Zustaende.

`custom-response`-Eintraege aus XML werden beim Laden zu `replace`-Makros kompiliert. `send/@text` mit Tokens wie `<CR>` wird vor der Ausfuehrung in `RawBytes` uebersetzt.

## Unknown AT Command Policy

Die Reaktion auf unbekannte AT-Kommandos ist Teil von `Dialect`:

```java
enum UnknownAtCommandPolicy {
    OK,
    ERR,
    ERROR,
    RESTART
}
```

`OK`, `ERR` und `ERROR` erzeugen die entsprechende Profilantwort. `RESTART` erzeugt intern einen `FaultCommand(MODEM_REBOOT, ...)`; die Anwendung selbst wird dabei nicht neu gestartet.

## Persistenz

Persistenzdateien:

```text
runtime/
  sessions/main/state.json
  sessions/main/nvram.json
  logs/main-2026-07-02.jsonl
```

`AT&W` schreibt in `nvram.json`. `ATZ` nutzt die Profil-`resetPolicy`:

| `resetPolicy` | Verhalten |
|---|---|
| `factory-on-atz` | Werk-/Profildefaults laden. |
| `nvram-on-atz` | NVRAM laden, falls vorhanden, sonst Profildefaults. |
| `profile-default-on-atz` | Nur Profildefaults laden, NVRAM bleibt erhalten. |

## Build

Verbindliche v1-Entscheidungen:

| Bereich | Entscheidung |
|---|---|
| Java | Java 24 Toolchain |
| Build | Maven Wrapper oder Gradle Wrapper, aber exakt eine Wahl im Repo |
| Tests | JUnit 5, AssertJ |
| XML/JSON | Jackson XML/JSON plus JAXP schema validation |
| Serial | `com.fazecast:jSerialComm`, exakt gepinnte Version |
| GUI | JavaFX |
| GUI Tests | Headless JavaFX/TestFX-kompatibler Testtreiber mit Automation IDs |
| Packaging | OS-spezifische App-Images oder ZIP mit Launcher-Skripten |
| CI OS | Windows 11 und Linux LTS |

Der Build darf keine dynamischen Dependency-Versionen verwenden. Launcher, Tests und Packaging muessen die fuer native Bibliotheken notwendigen Java-24-Flags setzen, insbesondere `--enable-native-access` fuer Module/Classpath, die `jSerialComm` laden. Bei Classpath-Distributionen ist `--enable-native-access=ALL-UNNAMED` zulaessig und muss in Launcher-Skripten sowie Test-`argLine` stehen; bei modularer Distribution muss die konkrete Modulbezeichnung aus dem Build-Artefakt kommen und in CI geloggt werden.

Der Source-Size-Guard ist Teil von `mvn verify` und muss auf Linux und Windows build-brechend sein. Maven/Ant-Aufrufe muessen `failonerror=true` bzw. eine aequivalente Fehlerweitergabe setzen und Skripte relativ zu `${project.basedir}` mit bash-sicherem Arbeitsverzeichnis aufrufen, damit Pfade mit Windows-Backslashes nicht als Escape-Sequenzen verloren gehen.

## XML-Profilladen

Der `ProfileLoader` liest XML-Profile, validiert sie gegen `schemas/modem-profile.schema.xsd`, fuehrt Semantic-Validation aus und baut daraus ein unveraenderliches Profilmodell. Der initiale Zustand darf nicht aus Handler-Defaults rekonstruiert werden.

Mapping:

| XML | Java-Modell | Hinweis |
|---|---|---|
| `profile/@id` | `Profile.id` | XSD-`ProfileIdType`, Ziffer am Anfang erlaubt. |
| `profile/@extends` | `Profile.parents` | Geordnete Liste. |
| `profile/@vendor`, `@status`, `@profileKind` | `ProfileMetadata` | Pflicht fuer Runtime-Profile. |
| `profile/error-policy` | `Profile.errorPolicy` | Effektive Fehlerpolicy; bei Basisprofilen ggf. Loader-Default. |
| `initial-state/sim/@state` | `SimState.state` | Expliziter Startzustand. |
| `initial-state/sim/@pinRef` | `SimState.pinRef` | Secret-Name, Wert nie loggen. |
| `initial-state/sim/@pukRef` | `SimState.pukRef` | Secret-Name, Wert nie loggen. |
| `initial-state/network/operator` | `NetworkState.operator` | Quelle fuer `AT+COPS?`. |
| `initial-state/network/sms-rate-limit` | `NetworkState.smsRateLimit` | Netzseitige SMS-Annahmerate. |
| `initial-state/network/delays/delay` | `NetworkState.delays` | Min-/Max-Delay pro Operation fuer den Response-Scheduler. |
| `initial-state/network/@cregN` und `@stat` | `NetworkState.registration` | Quelle fuer `AT+CREG?`. |
| `initial-state/call/@mode`, `@carrier`, `@incomingNumber` | `CallState` | Quelle fuer `ATA`, `ATD`, `ATH`, `ATO`. |
| `initial-state/modem-lines` | `ModemLines` | DTR/DSR/DCD/RI/RTS/CTS Startwerte. |

## XML-Hardening

Profile, Makros und Szenarien sind nutzergesteuerte XML-Dateien. Loader muessen Kapitel 14 umsetzen: kein DOCTYPE, keine externen Entities, keine externen Schemata, kein XInclude, Secure Processing, Groessen-/Tiefe-Limits und negative XXE-Tests.

## CLI

```bash
modemsim run --config config.yaml
modemsim list-ports
modemsim validate-profile examples/modem-profile.sample.xml
modemsim validate-macros examples/macros.sms-error-123.xml
modemsim validate-scenario examples/scenario.no-network.xml
modemsim replay logs/session.jsonl --mode validate-recompute --profile westermo-td22-6177-2203
```

Die CLI dient v1 fuer Start, Validierung, Headless-Tests und Replay-Automatisierung. Interaktive Control-Pane-Funktionen wie State-Editor, Injection, Live-Logs und Macro-Control sind GUI-Funktionen. Fuer Replay-Playback muss die CLI eine explizite Divergenzbestaetigung anbieten; `validate-recompute` bleibt fail-closed.
