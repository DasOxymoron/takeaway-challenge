package de.uko.takeaway.api.messaging.dto;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uko.takeaway.api.config.ControllerAdviceConfiguration;
import java.time.LocalDate;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class EmploymentEndedEventTest {

    private final ObjectMapper mapper = ControllerAdviceConfiguration.produce();

    @Test
    @SneakyThrows
    void shouldDeserialize() {
        String json = """
            {
            "eventId": "d7980e2c-b3f5-4b67-aec3-5135693c8210",
            "employeeId": "e924887b-63a7-4738-a318-2206bc6b5272",
            "terminationRequestedAt": "2022-07-16T10:38:44.856Z",
            "lastDayAtWork": "2022-09-14T21:59:59.999Z",
            "reason": "Employee has presented a resignation letter"
            }
            """;

        EmploymentEndedEvent event = mapper.readValue(json, EmploymentEndedEvent.class);

        assertThat(event).isEqualTo(new EmploymentEndedEvent(UUID.fromString("e924887b-63a7-4738-a318-2206bc6b5272"), LocalDate.of(2022,9,14)));
    }
}