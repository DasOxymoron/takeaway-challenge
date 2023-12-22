package de.uko.takeaway.persistence.event;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uko.takeaway.api.config.ControllerAdviceConfiguration;
import de.uko.takeaway.persistence.event.EmployeeEvent.EmployeeCreatedEvent;
import de.uko.takeaway.persistence.event.EmployeeEvent.EmployeeDeletedEvent;
import de.uko.takeaway.persistence.event.EmployeeEvent.EmployeeUpdatedEvent;
import de.uko.takeaway.utils.JsonAssertions;
import java.util.Collections;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class EmployeeEventTest {

    private final ObjectMapper objectMapper = ControllerAdviceConfiguration.produce();

    @Test
    @SneakyThrows
    void shouldSerializeEmployeeDeletedEvents() {
        EmployeeDeletedEvent event = new EmployeeDeletedEvent(UUID.fromString("7625edec-e137-43a8-a84b-9f9738c46cc4"));
        String json = objectMapper.writeValueAsString(event);

        JsonAssertions.assertThat(json)
            .isJson()
            .equals(
                "{\"type\":\"DELETED\",\"id\":\"7625edec-e137-43a8-a84b-9f9738c46cc4\"}");
    }

    @Test
    @SneakyThrows
    void shouldDeserializeEmployeeDeletedEvent() {
        String json = "{\"type\":\"DELETED\",\"id\":\"7625edec-e137-43a8-a84b-9f9738c46cc4\",\"eventTime\":\"-999999999-04-11T00:00:00+18:00\"}";

        EmployeeEvent employeeEvent = objectMapper.readValue(json, EmployeeEvent.class);

        assertThat(employeeEvent).isEqualTo(new EmployeeDeletedEvent(UUID.fromString("7625edec-e137-43a8-a84b-9f9738c46cc4")));
    }

    @Test
    @SneakyThrows
    void shouldSerializeEmployeeCreationEvent() {
        EmployeeCreatedEvent event = new EmployeeCreatedEvent(
            UUID.fromString("7625edec-e137-43a8-a84b-9f9738c46cc4"),
            "first-name",
            "last-name",
            "email",
            Collections.singletonList("hobby")
        );
        String json = objectMapper.writeValueAsString(event);
        JsonAssertions.assertThat(json)
            .isJson()
            .equals(
                "{\"type\":\"CREATED\",\"id\":\"7625edec-e137-43a8-a84b-9f9738c46cc4\",\"firstName\":\"first-name\",\"lastName\":\"last-name\",\"email\":\"email\",\"hobbies\":[\"hobby\"]}\n");
    }

    @Test
    @SneakyThrows
    void shouldDeserializeEmployeeCreationEvent() {
        String json = "{\"type\":\"CREATED\",\"id\":\"7625edec-e137-43a8-a84b-9f9738c46cc4\",\"firstName\":\"first-name\",\"lastName\":\"last-name\",\"email\":\"email\",\"hobbies\":[\"hobby\"]}\n";

        EmployeeEvent employeeEvent = objectMapper.readValue(json, EmployeeEvent.class);

        assertThat(employeeEvent).isEqualTo(new EmployeeCreatedEvent(
            UUID.fromString("7625edec-e137-43a8-a84b-9f9738c46cc4"),
            "first-name",
            "last-name",
            "email",
            Collections.singletonList("hobby")
        ));
    }

    @Test
    @SneakyThrows
    void shouldSerializeEmployeeUpdateEvent() {
        EmployeeUpdatedEvent event = new EmployeeUpdatedEvent(
            UUID.fromString("7625edec-e137-43a8-a84b-9f9738c46cc4"),
            "last-name",
            "email",
            Collections.singletonList("hobby")
        );
        String json = objectMapper.writeValueAsString(event);
        JsonAssertions.assertThat(json)
            .isJson()
            .equals(
                "{\"type\":\"UPDATED\",\"id\":\"7625edec-e137-43a8-a84b-9f9738c46cc4\",\"lastName\":\"last-name\",\"email\":\"email\",\"hobbies\":[\"hobby\"]}\n");
    }

    @Test
    @SneakyThrows
    void shouldDeserializeEmployeeUpdateEvent() {
        String json = "{\"type\":\"UPDATED\",\"id\":\"7625edec-e137-43a8-a84b-9f9738c46cc4\",\"lastName\":\"last-name\",\"email\":\"email\",\"hobbies\":[\"hobby\"]}\n";
        EmployeeEvent employeeEvent = objectMapper.readValue(json, EmployeeEvent.class);

        assertThat(employeeEvent).isEqualTo(new EmployeeUpdatedEvent(
            UUID.fromString("7625edec-e137-43a8-a84b-9f9738c46cc4"),
            "last-name",
            "email",
            Collections.singletonList("hobby")
        ));
    }
}