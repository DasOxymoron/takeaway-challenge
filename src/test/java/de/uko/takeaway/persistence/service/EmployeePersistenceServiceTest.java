package de.uko.takeaway.persistence.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.persistence.entity.EmployeeEntity;
import de.uko.takeaway.persistence.mappers.EmployeeEntityMapper;
import de.uko.takeaway.persistence.repository.EmployeeRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
class EmployeePersistenceServiceTest {

    private static final LocalDate NOW = LocalDate.of(2021, 1, 1);
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeEntityMapper employeeEntityMapper;
    @InjectMocks
    private EmployeePersistenceService employeePersistenceService;

    @BeforeEach
    void setUp() {
        employeePersistenceService.setNow(() -> NOW);
    }

    @Nested
    class FindByEmailTests {

        @Test
        void shouldFindById(@Mock Employee employee, @Mock EmployeeEntity employeeEntity) {
            doReturn(employee).when(employeeEntityMapper).fromEntity(any());
            doReturn(Optional.of(employeeEntity)).when(
                employeeRepository).findByEmailAndResignationDateIsNullOrResignationDateAfterAndActive(any(), any(), anyBoolean());
            String email = "email";

            Optional<Employee> actual = employeePersistenceService.findByEmail(email);

            assertThat(actual).contains(employee);

            verify(employeeRepository).findByEmailAndResignationDateIsNullOrResignationDateAfterAndActive(email, NOW, true);
            verify(employeeEntityMapper).fromEntity(employeeEntity);
        }

        @Test
        void shouldNotFindByEmail() {
            doReturn(Optional.empty()).when(
                employeeRepository).findByEmailAndResignationDateIsNullOrResignationDateAfterAndActive(any(), any(), anyBoolean());
            String email = "email";

            Optional<Employee> actual = employeePersistenceService.findByEmail(email);

            assertThat(actual).isEmpty();

            verify(employeeRepository).findByEmailAndResignationDateIsNullOrResignationDateAfterAndActive(email, NOW, true);
        }
    }

    @Nested
    class FindAllTests {

        @Test
        void shouldFindAll(@Mock Employee employee, @Mock EmployeeEntity employeeEntity) {
            PageRequest pageable = PageRequest.of(0, 10);
            doReturn(employee).when(employeeEntityMapper).fromEntity(any());
            doReturn(new PageImpl<>(Collections.singletonList(employeeEntity))).when(
                employeeRepository).findAllByResignationDateIsNullOrResignationDateAfterAndActive(any(), any(), anyBoolean());

            Page<Employee> actual = employeePersistenceService.findAll(pageable);

            assertThat(actual).containsExactly(employee);

            verify(employeeEntityMapper).fromEntity(employeeEntity);
            verify(employeeRepository).findAllByResignationDateIsNullOrResignationDateAfterAndActive(pageable, NOW, true);
        }
    }

    @Nested
    class FindByIdTests {

        @Test
        void shouldFindById(@Mock Employee employee, @Mock EmployeeEntity employeeEntity) {
            doReturn(employee).when(employeeEntityMapper).fromEntity(any());
            doReturn(Optional.of(employeeEntity)).when(
                employeeRepository).findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(any(), any(), anyBoolean());
            UUID id = UUID.randomUUID();

            Optional<Employee> actual = employeePersistenceService.findById(id);

            assertThat(actual).contains(employee);

            verify(employeeRepository).findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(id, NOW, true);
            verify(employeeEntityMapper).fromEntity(employeeEntity);

        }
    }

    @Nested
    class DeletionTests {

        @Test
        void caseFind(@Mock EmployeeEntity employeeEntity) {
            doReturn(Optional.of(employeeEntity)).when(
                employeeRepository).findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(any(), any(), anyBoolean());
            UUID id = UUID.randomUUID();

            employeePersistenceService.deleteById(id);

            verify(employeeEntity).setActive(false);
            verify(employeeRepository).save(employeeEntity);
            verify(employeeRepository).findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(id, NOW, true);
        }

        @Test
        void caseNotFind() {
            doReturn(Optional.empty()).when(
                employeeRepository).findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(any(), any(), anyBoolean());
            UUID id = UUID.randomUUID();

            employeePersistenceService.deleteById(id);

            verify(employeeRepository, times(0)).save(any());
            verify(employeeRepository).findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(id, NOW, true);
        }
    }

    @Nested
    class CreationTests {

        @Test
        void shouldCreate(@Mock Employee employee, @Mock EmployeeEntity employeeEntity) {
            doReturn(employeeEntity).when(employeeEntityMapper).toEntity(employee);
            doReturn(employeeEntity).when(employeeRepository).save(employeeEntity);
            doReturn(employee).when(employeeEntityMapper).fromEntity(employeeEntity);

            Employee actual = employeePersistenceService.create(employee);

            assertThat(actual).isSameAs(employee);

            verify(employeeEntityMapper).toEntity(employee);
            verify(employeeRepository).save(employeeEntity);
            verify(employeeEntityMapper).fromEntity(employeeEntity);
        }
    }

    @Nested
    class LastDayOfWorkTests {

        @Test
        void shouldFindAndSetLastDayOfWork(@Mock EmployeeEntity employeeEntity) {
            doReturn(Optional.of(employeeEntity)).when(
                employeeRepository).findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(any(), any(), anyBoolean());
            UUID id = UUID.randomUUID();
            LocalDate lastDayOfWork = LocalDate.of(2021, 1, 1);

            employeePersistenceService.configureLastDayOfWork(id, lastDayOfWork);

            verify(employeeEntity).setResignationDate(lastDayOfWork);
            verify(employeeRepository).save(employeeEntity);
            verify(employeeRepository).findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(id, NOW, true);
        }

        @Test
        void shouldNotFindandSetLastDayOfWork() {
            doReturn(Optional.empty()).when(
                employeeRepository).findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(any(), any(), anyBoolean());
            UUID id = UUID.randomUUID();
            LocalDate lastDayOfWork = LocalDate.of(2021, 1, 1);

            employeePersistenceService.configureLastDayOfWork(id, lastDayOfWork);

            verify(employeeRepository, times(0)).save(any());
            verify(employeeRepository).findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(id, NOW, true);
        }
    }

    @Nested
    class FindActiveAndResignedTests {

        @Test
        void findEmployeesActiveButResigned(@Mock EmployeeEntity employeeEntity) {
            UUID employeeId = UUID.randomUUID();
            doReturn(employeeId).when(employeeEntity).getId();
            PageRequest pageable = PageRequest.of(0, 10);
            doReturn(new PageImpl<>(Collections.singletonList(employeeEntity))).when(
                employeeRepository).findAllByResignationDateBeforeAndResignationDateNotNullAndActive(any(), any(), anyBoolean());

            Page<UUID> actual = employeePersistenceService.findEmployeesWithLastDayOfWorkInPast(pageable);

            Assertions.assertThat(actual).containsExactly(employeeId);
            verify(employeeRepository).findAllByResignationDateBeforeAndResignationDateNotNullAndActive(pageable, NOW, true);
        }
    }
}