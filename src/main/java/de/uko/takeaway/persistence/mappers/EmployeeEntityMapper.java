package de.uko.takeaway.persistence.mappers;

import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.domain.Name;
import de.uko.takeaway.persistence.entity.EmployeeEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEntityMapper {

    public Employee fromEntity(EmployeeEntity entity) {
        return Employee.builder()
            .id(entity.getId())
            .fullName(new Name(entity.getFirstName(), entity.getLastName()))
            .email(entity.getEmail())
            .hobbies(entity.getHobbies())
            .build();
    }

    public EmployeeEntity toEntity(Employee employee) {
        return EmployeeEntity.builder()
            .id(employee.getId())
            .firstName(employee.getFullName().getFirstName())
            .lastName(employee.getFullName().getLastName())
            .email(employee.getEmail())
            .hobbies(employee.getHobbies())
            .active(true)
            .build();
    }

}
