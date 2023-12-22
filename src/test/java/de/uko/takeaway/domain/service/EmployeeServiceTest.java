package de.uko.takeaway.domain.service;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.exception.EmployeeNotFoundException;
import de.uko.takeaway.domain.port.EmployeeReadPort;
import de.uko.takeaway.domain.port.EmployeeWritePort;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeReadPort readPort;
    @Mock
    private EmployeeWritePort writePort;
    @InjectMocks
    private EmployeeService employeeService;

    @Nested
    class FindByEmaliTests {

        @Test
        void shouldFindByEmail(@Mock Employee employee) {
            doReturn(Optional.of(employee)).when(readPort).findByEmail(anyString());
            String email = "email";

            Optional<Employee> result = employeeService.findByEmail(email);

            assertThat(result).contains(employee);
            verify(readPort).findByEmail(email);
        }
    }

    @Nested
    class FindAllTests {

        @Test
        void shouldGetAll(@Mock Employee employee) {
            doReturn(new PageImpl<>(singletonList(employee))).when(readPort).findAll(any());

            PageRequest pageRequest = PageRequest.of(0, 1);
            Page<Employee> actual = employeeService.getAll(pageRequest);

            assertThat(actual).contains(employee);
            verify(readPort).findAll(pageRequest);
        }
    }

    @Nested
    class ByIdTests {

        @Test
        void shouldNotGetById() {
            doReturn(Optional.empty()).when(readPort).findById(any());
            UUID id = UUID.fromString("f5e2f967-abb0-4d8d-855f-bfdf7d313cb6");

            assertThatThrownBy(() -> employeeService.getById(id))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("Employee with id f5e2f967-abb0-4d8d-855f-bfdf7d313cb6 not found");

            verify(readPort).findById(id);
        }

        @Test
        void shouldGetById(@Mock Employee employee) {
            doReturn(Optional.of(employee)).when(readPort).findById(any());
            UUID id = UUID.fromString("f5e2f967-abb0-4d8d-855f-bfdf7d313cb6");

            Employee actual = employeeService.getById(id);

            assertThat(actual).isEqualTo(employee);
            verify(readPort).findById(id);
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void shouldDelete(@Mock Employee employee) {
            UUID id = UUID.fromString("f5e2f967-abb0-4d8d-855f-bfdf7d313cb6");
            doReturn(Optional.of(employee)).when(readPort).findById(any());

            employeeService.delete(id);

            verify(readPort).findById(id);
            verify(writePort).deleteById(id);
        }

        @Test
        void shouldIgnoreUnknown() {
            UUID id = UUID.fromString("f5e2f967-abb0-4d8d-855f-bfdf7d313cb6");
            doReturn(Optional.empty()).when(readPort).findById(any());

            employeeService.delete(id);

            verify(readPort).findById(id);
            verify(writePort, times(0)).deleteById(id);
        }
    }

    @Nested
    class PushTests {

        @Test
        void shouldPush(@Mock Employee employee) {
            doReturn(employee).when(writePort).create(any());

            Employee actual = employeeService.push(employee);

            assertThat(actual).isEqualTo(employee);
            verify(writePort).create(employee);
        }
    }

}