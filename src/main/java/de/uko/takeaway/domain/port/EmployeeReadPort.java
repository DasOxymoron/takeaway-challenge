package de.uko.takeaway.domain.port;

import de.uko.takeaway.domain.domain.Employee;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeReadPort {

    Optional<Employee> findByEmail(String email);

    Page<Employee> findAll(Pageable pageable);

    Optional<Employee> findById(UUID id);
}
