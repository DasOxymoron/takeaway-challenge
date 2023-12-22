package de.uko.takeaway.domain.port;

import de.uko.takeaway.domain.domain.Employee;
import java.util.UUID;

public interface EmployeeWritePort {

    Employee create(Employee employee);

    void deleteById(UUID id);
}
