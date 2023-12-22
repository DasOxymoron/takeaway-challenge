package de.uko.takeaway.api.rest.controller;

import de.uko.takeaway.api.rest.dto.EmployeeCreationDto;
import de.uko.takeaway.api.rest.dto.EmployeeDto;
import de.uko.takeaway.api.rest.dto.EmployeeUpdateDto;
import de.uko.takeaway.api.rest.mappers.EmployeeMapper;
import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.service.EmployeeService;
import de.uko.takeaway.domain.service.CreateEmployeeUseCase;
import de.uko.takeaway.domain.service.EmployeeUpdateUseCase;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final EmployeeMapper employeeDtoMapper;
    private final EmployeeService employeeService;
    private final EmployeeUpdateUseCase employeeUpdateUseCase;

    @PostMapping
    public EmployeeDto create(@Valid @RequestBody EmployeeCreationDto employeeCreationDto) {
        Employee toCreate = employeeDtoMapper.mapToDomain(employeeCreationDto);
        Employee created = createEmployeeUseCase.create(toCreate);
        return employeeDtoMapper.mapFromDomain(created);
    }

    @GetMapping
    public Page<EmployeeDto> getAll(Pageable pageable) {
        Page<Employee> all = employeeService.getAll(pageable);
        Page<EmployeeDto> map = all.map(employeeDtoMapper::mapFromDomain);
        return map;
    }

    @GetMapping("/{id}")
    public EmployeeDto getById(@PathVariable UUID id) {
        Employee employee = employeeService.getById(id);
        return employeeDtoMapper.mapFromDomain(employee);
    }

    @PutMapping("/{id}")
    public EmployeeDto update(@PathVariable UUID id, @Valid @RequestBody EmployeeUpdateDto employeeUpdateDto) {
        Employee updated = employeeUpdateUseCase.update(id, employeeUpdateDto);
        return employeeDtoMapper.mapFromDomain(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        employeeService.delete(id);
    }

}
