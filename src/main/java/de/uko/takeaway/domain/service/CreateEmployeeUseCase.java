package de.uko.takeaway.domain.service;

import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.port.CreateEmployeeAsyncPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateEmployeeUseCase {

    private final EmployeeService employeeService;
    private final CreateEmployeeAsyncPort createEmployeeAsyncPort;

    public Employee create(Employee employee) {
        checkForDuplicateEmail(employee);
        Employee createdEmployee = employeeService.push(employee);
        createEmployeeAsyncPort.sendCreateEmployeeMessage(createdEmployee);
        return createdEmployee;
    }

    private void checkForDuplicateEmail(Employee employee) {
        employeeService.findByEmail(employee.getEmail()).ifPresent(e -> {
            throw new IllegalArgumentException(String.format("Employee with email %s  already exists", employee.getEmail()));
        });
    }
}
