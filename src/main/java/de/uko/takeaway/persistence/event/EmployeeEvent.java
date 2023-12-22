package de.uko.takeaway.persistence.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import de.uko.takeaway.domain.domain.Employee;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EmployeeEvent.EmployeeDeletedEvent.class, name = "DELETED"),
    @JsonSubTypes.Type(value = EmployeeEvent.EmployeeCreatedEvent.class, name = "CREATED"),
    @JsonSubTypes.Type(value = EmployeeEvent.EmployeeUpdatedEvent.class, name = "UPDATED"),
})
public abstract class EmployeeEvent {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeDeletedEvent extends EmployeeEvent {

        public UUID id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeCreatedEvent extends EmployeeEvent {

        public UUID id;
        public String firstName;
        public String lastName;
        public String email;
        public List<String> hobbies;

        public EmployeeCreatedEvent(Employee employee) {
            this.id = employee.getId();
            this.firstName = employee.getFullName().getFirstName();
            this.lastName = employee.getFullName().getLastName();
            this.email = employee.getEmail();
            this.hobbies = employee.getHobbies();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeUpdatedEvent extends EmployeeEvent {

        public UUID id;
        public String lastName;
        public String email;
        public List<String> hobbies;

        public EmployeeUpdatedEvent(Employee employee) {
            this.id = employee.getId();
            this.lastName = employee.getFullName().getLastName();
            this.email = employee.getEmail();
            this.hobbies = employee.getHobbies();
        }
    }
}
