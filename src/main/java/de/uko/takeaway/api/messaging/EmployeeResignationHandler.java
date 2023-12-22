package de.uko.takeaway.api.messaging;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeResignationHandler {

    void configureLastDayOfWork(UUID employeeId, LocalDate lastDayAtWork);

    Page<UUID> findEmployeesWithLastDayOfWorkInPast(Pageable pageable);

}
