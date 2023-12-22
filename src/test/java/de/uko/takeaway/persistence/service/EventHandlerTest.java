package de.uko.takeaway.persistence.service;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.domain.Name;
import de.uko.takeaway.persistence.event.EmployeeEvent;
import de.uko.takeaway.persistence.event.EmployeeEvent.EmployeeCreatedEvent;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventHandlerTest {

    public static final String EMAIL = "mail";
    public static final String FIRST_NAME = "first";
    public static final String LAST_NAME = "last";
    public static final List<String> HOBBIES = singletonList("hobby");
    public static final UUID ID = UUID.randomUUID();
    public static final Employee EMPLOYEE = Employee.builder()
        .email(EMAIL)
        .fullName(new Name(FIRST_NAME, LAST_NAME))
        .hobbies(HOBBIES)
        .id(ID)
        .build();
    @Mock
    private OutboxService outboxService;
    @InjectMocks
    private EventHandler eventHandler;

    @Test
    void caseCreateEmployeeMessage() {
        eventHandler.sendCreateEmployeeMessage(EMPLOYEE);
        verify(outboxService).schedule(new EmployeeCreatedEvent(
            ID, FIRST_NAME, LAST_NAME, EMAIL, HOBBIES));
    }

    @Test
    void caseEmployeeDeletedEvent() {
        eventHandler.sendDeleteEmployeeMessage(ID);
        verify(outboxService).schedule(new EmployeeEvent.EmployeeDeletedEvent(ID));
    }

    @Test
    void caseEmployeeUpdatedEvent() {
        eventHandler.sendUpdateEmployeeMessage(EMPLOYEE);
        verify(outboxService).schedule(new EmployeeEvent.EmployeeUpdatedEvent(
            ID, LAST_NAME, EMAIL, HOBBIES));
    }
}