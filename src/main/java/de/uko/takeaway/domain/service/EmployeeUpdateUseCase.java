package de.uko.takeaway.domain.service;

import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.domain.EmployeeUpdateAble;
import de.uko.takeaway.domain.port.UpdateEmployeeAsyncMessenger;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeUpdateUseCase {

    private final EmployeeService employeeService;
    private final UpdateEmployeeAsyncMessenger updateEmployeeAsyncMessenger;

    public Employee update(UUID id, EmployeeUpdateAble updateRequest) {
        checkDuplicateEmail(id, updateRequest);
        Employee employee = employeeService.getById(id);
        employee.setEmail(updateRequest.getEmail());
        employee.getFullName().setLastName(updateRequest.getLastName());
        employee.setHobbies(updateRequest.getHobbies());

        Employee updated = employeeService.push(employee);
        updateEmployeeAsyncMessenger.sendUpdateEmployeeMessage(updated);
        return updated;
    }

    private void checkDuplicateEmail(UUID updateableId, EmployeeUpdateAble updateRequest) {
        employeeService.findByEmail(updateRequest.getEmail())
            .ifPresent(e -> {
                if (!e.getId().equals(updateableId)) {
                    throw new IllegalArgumentException(String.format("Employee with email %s  already exists", updateRequest.getEmail()));
                }
            });
    }
}
