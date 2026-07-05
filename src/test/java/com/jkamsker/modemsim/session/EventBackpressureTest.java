package com.jkamsker.modemsim.session;

import com.jkamsker.modemsim.monitor.Direction;
import com.jkamsker.modemsim.monitor.EventType;
import com.jkamsker.modemsim.monitor.ModemEvent;
import com.jkamsker.modemsim.monitor.ModemEventJson;
import com.jkamsker.modemsim.monitor.RedactionInfo;
import com.jkamsker.modemsim.validation.JsonSchemaValidator;
import com.jkamsker.modemsim.validation.SchemaLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class EventBackpressureTest {
    @TempDir
    Path tempDir;

    @Test
    void droppedSummaryUsesSchemaDirectionAndNextRequiredEventCarriesCount() throws Exception {
        DropOnceSink sink = new DropOnceSink();
        SessionEventDelivery delivery = new SessionEventDelivery(sink, sink::nextSequence);

        sink.dropNextDroppable();
        delivery.publish(event(1, EventType.RX_BYTES, Direction.DTE_TO_DCE));
        delivery.publish(event(3, EventType.STATE_CHANGE, Direction.INTERNAL));
        delivery.publish(event(4, EventType.RX_BYTES, Direction.DTE_TO_DCE));

        ModemEvent summary = first(sink.events(), EventType.DROPPED_EVENTS);
        ModemEvent stateChange = first(sink.events(), EventType.STATE_CHANGE);
        ModemEvent rxAfterReset = sink.events().stream()
                .filter(event -> event.eventType() == EventType.RX_BYTES)
                .findFirst()
                .orElseThrow();
        assertThat(summary.direction().name()).isEqualTo("NONE");
        assertThat(stateChange.droppedEventCount()).isEqualTo(1);
        assertThat(rxAfterReset.droppedEventCount()).isZero();

        Path json = tempDir.resolve("dropped.json");
        Files.writeString(json, ModemEventJson.toJson(summary));
        assertThat(new JsonSchemaValidator().validateJson(json, SchemaLocator.schemaPath("event-log.schema.json")).valid())
                .as(ModemEventJson.toJson(summary))
                .isTrue();
    }

    private ModemEvent first(List<ModemEvent> events, EventType type) {
        return events.stream().filter(event -> event.eventType() == type).findFirst().orElseThrow();
    }

    private ModemEvent event(long sequence, EventType type, Direction direction) {
        String hash = "sha256:1111111111111111111111111111111111111111111111111111111111111111";
        return new ModemEvent(OffsetDateTime.parse("2026-01-01T00:00:00Z"), 0, sequence,
                "main", type, direction, "", null, null, "profile", "port", "modem-simulation",
                hash, hash, hash, hash, 12345L, "virtual", null, null, null, 0,
                false, null, "test", Map.of(), null, null, RedactionInfo.none());
    }

}
