package de.uko.takeaway.domain.service;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.domain.EmployeeUpdateAble;
import de.uko.takeaway.domain.domain.Name;
import de.uko.takeaway.domain.port.UpdateEmployeeAsyncMessenger;
import de.uko.takeaway.domain.service.EmployeeService;
import de.uko.takeaway.domain.service.EmployeeUpdateUseCase;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeUpdateUseCaseTest {

    public static final UUID ID = UUID.randomUUID();
    public static final String EMAIL = "mail";
    public static final List<String> HOBBY = singletonList("hobby");
    public static final String LAST_NAME = UUID.randomUUID().toString();
    @Mock
    private EmployeeService employeeService;
    @Mock
    private UpdateEmployeeAsyncMessenger updateEmployeeAsyncMessenger;
    @InjectMocks
    private EmployeeUpdateUseCase employeeUpdateUseCase;

    @Test
    void caseMailCollision(@Mock Employee employee, @Mock EmployeeUpdateAble updater) {
        doReturn(EMAIL).when(updater).getEmail();
        doReturn(Optional.of(employee)).when(employeeService).findByEmail(EMAIL);
        doReturn(UUID.randomUUID()).when(employee).getId();
        Assertions.assertThatThrownBy(() -> employeeUpdateUseCase.update(ID, updater))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Employee with email mail  already exists");
    }

    @Test
    void caseEmailNotChanged(@Mock Name name, @Mock Employee employee, @Mock EmployeeUpdateAble updater) {
        doReturn(EMAIL).when(updater).getEmail();
        doReturn(ID).when(employee).getId();
        doReturn(LAST_NAME).when(updater).getLastName();
        doReturn(HOBBY).when(updater).getHobbies();
        doReturn(name).when(employee).getFullName();
        doReturn(Optional.of(employee)).when(employeeService).findByEmail(EMAIL);
        doReturn(employee).when(employeeService).getById(ID);
        doReturn(employee).when(employeeService).push(employee);

        Employee updated = employeeUpdateUseCase.update(ID, updater);

        Assertions.assertThat(updated).isSameAs(employee);
        verify(employee).setEmail(EMAIL);
        verify(employee).setHobbies(HOBBY);
        verify(name).setLastName(LAST_NAME);
        verify(updateEmployeeAsyncMessenger).sendUpdateEmployeeMessage(updated);
    }

    @Test
    void caseUpdatePossible(@Mock Name name, @Mock Employee employee, @Mock EmployeeUpdateAble updater) {
        doReturn(EMAIL).when(updater).getEmail();
        doReturn(LAST_NAME).when(updater).getLastName();
        doReturn(HOBBY).when(updater).getHobbies();
        doReturn(name).when(employee).getFullName();
        doReturn(Optional.empty()).when(employeeService).findByEmail(EMAIL);
        doReturn(employee).when(employeeService).getById(ID);
        doReturn(employee).when(employeeService).push(employee);

        Employee updated = employeeUpdateUseCase.update(ID, updater);

        Assertions.assertThat(updated).isSameAs(employee);
        verify(employee).setEmail(EMAIL);
        verify(employee).setHobbies(HOBBY);
        verify(name).setLastName(LAST_NAME);
        verify(updateEmployeeAsyncMessenger).sendUpdateEmployeeMessage(updated);

    }
}