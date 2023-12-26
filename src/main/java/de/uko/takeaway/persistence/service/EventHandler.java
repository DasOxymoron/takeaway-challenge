package de.uko.takeaway.persistence.service;

import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.port.CreateEmployeeAsyncPort;
import de.uko.takeaway.domain.port.DeleteEmployeeAsyncPort;
import de.uko.takeaway.domain.port.UpdateEmployeeAsyncPort;
import de.uko.takeaway.persistence.event.EmployeeEvent;
import de.uko.takeaway.persistence.event.EmployeeEvent.EmployeeCreatedEvent;
import de.uko.takeaway.persistence.event.EmployeeEvent.EmployeeDeletedEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventHandler implements CreateEmployeeAsyncPort, DeleteEmployeeAsyncPort, UpdateEmployeeAsyncPort {

    private final OutboxService outboxService;

    @Override
    public void sendCreateEmployeeMessage(Employee employee) {
        outboxService.schedule(new EmployeeCreatedEvent(employee));
    }

    @Override
    public void sendDeleteEmployeeMessage(UUID employeeId) {
        outboxService.schedule(new EmployeeDeletedEvent(employeeId));
    }

    @Override
    public void sendUpdateEmployeeMessage(Employee employee) {
        outboxService.schedule(new EmployeeEvent.EmployeeUpdatedEvent(employee));
    }
}
