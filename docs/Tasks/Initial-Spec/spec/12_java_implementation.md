# 12 - Java-Implementation

## Paketstruktur

```text
com.example.modemsim
  app
  serial
  parser
  commands
  profiles
  state
  macros
  scheduler
  monitor
  replay
  testkit
```

## Zentrale Interfaces

```java
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

public interface StateStore {
    ModemState snapshot();
    ModemState update(StatePatch patch);
}

public interface ProfileLoader {
    Profile load(Path xmlProfilePath);
    ValidationReport validate(Path xmlProfilePath);
}

public interface EventSink {
    void publish(ModemEvent event);
}
```

## Parser-Objekte

```java
record ParsedCommand(
    String raw,
    String normalizedName,
    CommandKind kind,
    List<String> arguments,
    Map<String, String> attributes
) {}

enum CommandKind {
    BASIC, EXTENDED_EXEC, EXTENDED_SET, EXTENDED_READ, EXTENDED_TEST, S_REGISTER_READ,
    S_REGISTER_WRITE, SPECIAL_REPEAT, SPECIAL_ESCAPE
}
```

## Response-Modell

```java
sealed interface ResponseFrame permits TextLine, RawBytes, Delay, ResultCode, StatePatchAction {}

record TextLine(String line) implements ResponseFrame {}
record RawBytes(byte[] bytes) implements ResponseFrame {}
record Delay(Duration duration) implements ResponseFrame {}
record ResultCode(ResultCodeType type) implements ResponseFrame {}
record StatePatchAction(StatePatch patch) implements ResponseFrame {}
```

## Threading

Pro Session:

- Ein RX-Thread liest seriell.
- Ein Parser-/Command-Executor verarbeitet Frames sequentiell.
- Ein Response-Scheduler sendet Antworten mit Delays.
- Ein Event-Publisher entkoppelt Logging und WebSocket.

Regel: Der serielle RX-Pfad darf nie durch langsame WebSocket-Clients blockiert werden.

## Persistenz

Persistenzdateien:

```text
runtime/
  sessions/main/state.json
  sessions/main/nvram.json
  logs/main-2026-07-02.jsonl
```

`AT&W` schreibt in `nvram.json`. `ATZ` lädt Default oder NVRAM, je nach Profilpolicy.

## Build

Empfohlen:

```text
Java 21 LTS
Maven oder Gradle
JUnit 5
AssertJ
Jackson XML/JSON
jSerialComm
Jetty/Undertow/Netty für REST/WebSocket
```

## XML-Profilladen

Der `ProfileLoader` liest XML-Profile, validiert sie gegen `schemas/modem-profile.schema.xsd` und baut daraus ein unveraenderliches Profilmodell. Der initiale SIM- und Netzbetreiberzustand darf nicht aus Handler-Defaults rekonstruiert werden, sondern kommt aus `initial-state`.

Mapping:

| XML | Java-Modell | Hinweis |
|---|---|---|
| `initial-state/sim/@state` | `SimState.state` | Expliziter Startzustand. |
| `initial-state/sim/@pinQueryEnabled` | `SimState.pinQueryEnabled` | Aktiviert die PIN-Abfrage. |
| `initial-state/sim/@pin` | `SimState.pin` | Test-PIN, in Logs redigieren. |
| `initial-state/network/operator` | `NetworkState.operator` | Quelle fuer `AT+COPS?`. |
| `initial-state/network/sms-rate-limit` | `NetworkState.smsRateLimit` | Netzseitige SMS-Annahmerate. |
| `initial-state/network/delays/delay` | `NetworkState.delays` | Min-/Max-Delay pro Operation fuer den Response-Scheduler. |
| `initial-state/network/@cregN` und `@stat` | `NetworkState.registration` | Quelle fuer `AT+CREG?`. |

## CLI

```bash
modemsim run --config config.yaml
modemsim list-ports
modemsim validate-profile examples/modem-profile.sample.xml
modemsim validate-macros examples/macros.sms-error-123.xml
modemsim replay logs/session.jsonl --profile westermo-td22-6177-2203
```
