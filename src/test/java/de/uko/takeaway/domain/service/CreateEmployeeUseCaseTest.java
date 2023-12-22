package de.uko.takeaway.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.port.CreateEmployeeAsyncMessenger;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateEmployeeUseCaseTest {

    public static final String EMAIL = UUID.randomUUID().toString();
    @Mock
    private EmployeeService employeeService;
    @Mock
    private CreateEmployeeAsyncMessenger createEmployeeAsyncMessenger;
    @InjectMocks
    private CreateEmployeeUseCase createEmployeeUseCase;

    @Test
    void shouldThrowExceptionOnEmailExists(@Mock Employee employee) {
        // given
        when(employee.getEmail()).thenReturn(EMAIL);
        when(employeeService.findByEmail(EMAIL)).thenReturn(Optional.of(employee));
        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> createEmployeeUseCase.create(employee));
    }

    @Test
    void shouldCreateProperly(@Mock Employee employeeMock) {
        // given
        when(employeeService.push(any())).thenReturn(employeeMock);
        when(employeeMock.getEmail()).thenReturn(EMAIL);
        when(employeeService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        // when
        Employee employee = createEmployeeUseCase.create(employeeMock);
        // then
        assertEquals(employee, employeeMock);
        verify(employeeService).push(employeeMock);
        verify(createEmployeeAsyncMessenger).sendCreateEmployeeMessage(employee);
    }
}