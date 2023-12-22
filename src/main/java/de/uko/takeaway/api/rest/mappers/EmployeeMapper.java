package de.uko.takeaway.api.rest.mappers;

import de.uko.takeaway.api.rest.dto.EmployeeCreationDto;
import de.uko.takeaway.api.rest.dto.EmployeeDto;
import de.uko.takeaway.api.rest.dto.FullNameDto;
import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.domain.Name;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee mapToDomain(EmployeeCreationDto employeeCreationDto) {
        return new Employee(
            null,
            name(employeeCreationDto.fullName()),
            employeeCreationDto.email(),
            employeeCreationDto.hobbies(),
            employeeCreationDto.birthday()
        );
//        return Employee.builder()
//            .birthday(employeeCreationDto.birthday())
//            .email(employeeCreationDto.email())
//            .fullName(name(employeeCreationDto.fullName()))
//            .hobbies(employeeCreationDto.hobbies())
//            .build();
    }

    public EmployeeDto mapFromDomain(Employee domain) {
        return new EmployeeDto(
            domain.getId(),
            new FullNameDto(domain.getFullName().getFirstName(), domain.getFullName().getLastName()),
            domain.getEmail(),
            domain.getHobbies(),
            domain.getBirthday()
        );
    }

    private static Name name(FullNameDto fullName) {
        return new Name(fullName.firstName(), fullName.lastName());
    }

}
