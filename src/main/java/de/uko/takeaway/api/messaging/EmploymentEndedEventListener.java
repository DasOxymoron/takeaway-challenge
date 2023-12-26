package de.uko.takeaway.api.messaging;

import de.uko.takeaway.api.messaging.dto.EmploymentEndedEvent;
import de.uko.takeaway.domain.service.EmployeeService;
import lombok.AllArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmploymentEndedEventListener {

    private final EmployeeResignationHandler handler;
    private final EmployeeService employeeService;

    @RabbitListener(queues = "${messaging.external-in.contract.queue}")
    public void handleContractEvent(EmploymentEndedEvent event) {
        /*
          Bypassing domain layer since contract termination is no real business logic of the current domain
          Nevertheless from my perspective employee and contracts are close to each other, so I would move
          everything together which would produce a service which would be better.
         */

        handler.configureLastDayOfWork(event.employeeId(), event.lastDayAtWork());
    }

    @Scheduled(fixedDelayString = "${scheduled.employees-clean-job}")
    @SchedulerLock(name = "TaskScheduler_outbox",
        lockAtLeastFor = "PT5M", lockAtMostFor = "PT14M")
    public void handleEmployeesWithLastWorkingDayInPast() {
        handler.findEmployeesWithLastDayOfWorkInPast(PageRequest.of(0, 100))
            .forEach(employeeService::delete);
    }

}
