package de.uko.takeaway.persistence.service;

import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.port.CreateEmployeeAsyncMessenger;
import de.uko.takeaway.domain.port.DeleteEmployeeAsyncMessenger;
import de.uko.takeaway.domain.port.UpdateEmployeeAsyncMessenger;
import de.uko.takeaway.persistence.event.EmployeeEvent;
import de.uko.takeaway.persistence.event.EmployeeEvent.EmployeeCreatedEvent;
import de.uko.takeaway.persistence.event.EmployeeEvent.EmployeeDeletedEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventHandler implements CreateEmployeeAsyncMessenger, DeleteEmployeeAsyncMessenger, UpdateEmployeeAsyncMessenger {

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
