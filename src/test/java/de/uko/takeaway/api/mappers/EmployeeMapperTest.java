package de.uko.takeaway.api.mappers;

import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;

import de.uko.takeaway.api.rest.dto.EmployeeCreationDto;
import de.uko.takeaway.api.rest.dto.EmployeeDto;
import de.uko.takeaway.api.rest.dto.FullNameDto;
import de.uko.takeaway.api.rest.mappers.EmployeeMapper;
import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.domain.Name;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeMapperTest {

    public static final String FIRST_NAME = "test-first-name";
    public static final String LAST_NAME = "test-last-name";
    public static final String EMAIL = "test@email.com";
    public static final List<String> HOBBIES = singletonList("test-hobbies");
    public static final LocalDate BIRTHDAY = LocalDate.now();
    public static final UUID ID = UUID.randomUUID();
    @InjectMocks
    private EmployeeMapper employeeMapper;

    @Nested
    class CreationDtoMapperTest {

        @Test
        void shouldMap() {
            // given
            EmployeeCreationDto from = new EmployeeCreationDto(
                new FullNameDto(FIRST_NAME,
                    LAST_NAME),
                EMAIL,
                HOBBIES,
                BIRTHDAY
            );

            Employee expected = Employee.builder()
                .fullName(new Name(FIRST_NAME, LAST_NAME))
                .email(EMAIL)
                .hobbies(HOBBIES)
                .birthday(BIRTHDAY)
                .build();
            // when
            Employee actual = employeeMapper.mapToDomain(from);
            // then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class DomainToDtoTests {

        @Test
        void shouldMapEmployeeToDto() {
            // given
            Employee from = Employee.builder()
                .id(ID)
                .fullName(new Name(FIRST_NAME, LAST_NAME))
                .email(EMAIL)
                .hobbies(HOBBIES)
                .birthday(BIRTHDAY)
                .build();

            EmployeeDto expected = new EmployeeDto(
                ID,
                new FullNameDto(FIRST_NAME, LAST_NAME),
                EMAIL,
                HOBBIES,
                BIRTHDAY
            );
            // when
            EmployeeDto actual = employeeMapper.mapFromDomain(from);
            // then
            assertEquals(expected, actual);
        }
    }

}