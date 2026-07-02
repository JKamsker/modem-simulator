# 12 - Java-Implementation

## Paketstruktur

```text
com.alegs3.modemsim
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
    InitialState initialState();
    Optional<CommandHandler> resolve(ParsedCommand command);
    UnsupportedPolicy unsupportedPolicy();
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

Der Build darf keine dynamischen Dependency-Versionen verwenden. Launcher, Tests und Packaging muessen die fuer native Bibliotheken notwendigen Java-24-Flags setzen, insbesondere `--enable-native-access` fuer Module/Classpath, die `jSerialComm` laden. Die konkrete Modulbezeichnung muss aus dem Build-Artefakt kommen und in CI geloggt werden.

## XML-Profilladen

Der `ProfileLoader` liest XML-Profile, validiert sie gegen `schemas/modem-profile.schema.xsd`, fuehrt Semantic-Validation aus und baut daraus ein unveraenderliches Profilmodell. Der initiale Zustand darf nicht aus Handler-Defaults rekonstruiert werden.

Mapping:

| XML | Java-Modell | Hinweis |
|---|---|---|
| `profile/@id` | `Profile.id` | XSD-`ProfileIdType`, Ziffer am Anfang erlaubt. |
| `profile/@extends` | `Profile.parents` | Geordnete Liste. |
| `profile/@vendor`, `@status`, `@profileKind` | `ProfileMetadata` | Pflicht fuer Runtime-Profile. |
| `initial-state/sim/@state` | `SimState.state` | Expliziter Startzustand. |
| `initial-state/sim/@pinRef` | `SimState.pinRef` | Secret-Name, Wert nie loggen. |
| `initial-state/network/operator` | `NetworkState.operator` | Quelle fuer `AT+COPS?`. |
| `initial-state/network/sms-rate-limit` | `NetworkState.smsRateLimit` | Netzseitige SMS-Annahmerate. |
| `initial-state/network/delays/delay` | `NetworkState.delays` | Min-/Max-Delay pro Operation fuer den Response-Scheduler. |
| `initial-state/network/@cregN` und `@stat` | `NetworkState.registration` | Quelle fuer `AT+CREG?`. |
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

Die CLI dient v1 fuer Start, Validierung, Headless-Tests und Replay-Automatisierung. Interaktive Control-Pane-Funktionen wie State-Editor, Injection, Live-Logs und Macro-Control sind GUI-Funktionen.
