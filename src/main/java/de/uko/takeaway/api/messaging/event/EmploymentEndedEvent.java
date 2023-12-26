package de.uko.takeaway.api.messaging.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.UUID;


@JsonIgnoreProperties(ignoreUnknown = true)
public record EmploymentEndedEvent(UUID employeeId, LocalDate lastDayAtWork) {
}
