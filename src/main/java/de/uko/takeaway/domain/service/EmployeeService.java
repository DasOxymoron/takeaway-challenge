package de.uko.takeaway.domain.service;

import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.exception.EmployeeNotFoundException;
import de.uko.takeaway.domain.port.EmployeeReadPort;
import de.uko.takeaway.domain.port.EmployeeWritePort;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeReadPort readPort;
    private final EmployeeWritePort writePort;

    public Optional<Employee> findByEmail(String email) {
        return readPort.findByEmail(email);
    }

    public Page<Employee> getAll(Pageable pageable) {
        return readPort.findAll(pageable);

    }

    public Employee getById(@NotNull UUID id) {
        return findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

    }

    private Optional<Employee> findById(@NotNull UUID id) {
        return readPort.findById(id);
    }

    public void delete(UUID id) {
        readPort.findById(id).ifPresent(employee -> writePort.deleteById(id));
    }

    protected Employee push(Employee employee) {
        return writePort.create(employee);
    }
}
