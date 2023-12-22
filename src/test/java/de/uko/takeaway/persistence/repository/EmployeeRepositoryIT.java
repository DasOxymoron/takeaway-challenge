package de.uko.takeaway.persistence.repository;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;

import de.uko.takeaway.persistence.entity.EmployeeEntity;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


class EmployeeRepositoryIT extends RepositoryTest {

    public static final String EMAIL = "df";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Nested
    class FindByIdTests {

        @Test
        void shouldFindByIdAndNoResignationDate() {
            EmployeeEntity persisted = setupEntity(null, true);

            Optional<EmployeeEntity> found = employeeRepository.findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(persisted.getId(),
                LocalDate.now(), true);

            assertThat(found).contains(persisted);
        }

        @Test
        void shouldFindByIdAndResignationDateInFuture() {
            EmployeeEntity persisted = setupEntity(LocalDate.now().plusDays(2), true);

            Optional<EmployeeEntity> found = employeeRepository.findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(persisted.getId(),
                LocalDate.now(), true);

            assertThat(found).contains(persisted);
        }

        @Test
        void shouldFindByIdAndResignationDateYesterday() {
            EmployeeEntity persisted = setupEntity(LocalDate.now().minusDays(2), true);

            Optional<EmployeeEntity> found = employeeRepository.findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(persisted.getId(),
                LocalDate.now(), true);

            assertThat(found).isEmpty();
        }

        @Test
        void shouldFindByIdAndNotActive() {
            EmployeeEntity persisted = setupEntity(LocalDate.now().plusDays(2), false);

            Optional<EmployeeEntity> found = employeeRepository.findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(persisted.getId(),
                LocalDate.now(), true);

            assertThat(found).isEmpty();
        }
    }

    @Nested
    class FindByEmailTests {

        @Test
        void shouldFindByEmailNoResignation() {
            EmployeeEntity persisted = setupEntity(null, true);

            Optional<EmployeeEntity> found = employeeRepository.findByEmailAndResignationDateIsNullOrResignationDateAfterAndActive(EMAIL,
                LocalDate.now(), true);

            assertThat(found).contains(persisted);
        }

        @Test
        void shouldFindByEmailResignationFuture() {
            EmployeeEntity persisted = setupEntity(LocalDate.now().plusDays(2), true);

            Optional<EmployeeEntity> found = employeeRepository.findByEmailAndResignationDateIsNullOrResignationDateAfterAndActive(EMAIL,
                LocalDate.now(), true);

            assertThat(found).contains(persisted);
        }

        @Test
        void shouldNotFindByEmailResigned() {
            setupEntity(LocalDate.now().minusDays(2), true);

            Optional<EmployeeEntity> found = employeeRepository.findByEmailAndResignationDateIsNullOrResignationDateAfterAndActive(EMAIL,
                LocalDate.now(), true);

            assertThat(found).isEmpty();
        }

        @Test
        void shouldNotFindByEmailActiveFalse() {
            setupEntity(LocalDate.now().plusDays(2), false);

            Optional<EmployeeEntity> found = employeeRepository.findByEmailAndResignationDateIsNullOrResignationDateAfterAndActive(EMAIL,
                LocalDate.now(), true);

            assertThat(found).isEmpty();
        }
    }

    @Nested
    class FindAllTests {

        @Test
        void caseShouldFindNoResignation() {
            EmployeeEntity persisted = setupEntity(null, true);
            Page<EmployeeEntity> actual = employeeRepository.findAllByResignationDateIsNullOrResignationDateAfterAndActive(
                PageRequest.of(0, 10), LocalDate.now(), true);

            assertThat(actual).contains(persisted);
        }

        @Test
        void caseShouldFindResignationFuture() {
            EmployeeEntity persisted = setupEntity(LocalDate.now().plusDays(2), true);
            Page<EmployeeEntity> actual = employeeRepository.findAllByResignationDateIsNullOrResignationDateAfterAndActive(
                PageRequest.of(0, 10), LocalDate.now(), true);

            assertThat(actual).contains(persisted);
        }

        @Test
        void caseShouldNotFindByResignation() {
            setupEntity(LocalDate.now().minusDays(2), true);
            Page<EmployeeEntity> actual = employeeRepository.findAllByResignationDateIsNullOrResignationDateAfterAndActive(
                PageRequest.of(0, 10), LocalDate.now(), true);

            assertThat(actual).isEmpty();
        }

        @Test
        void caseShouldNotFindByActive() {
            setupEntity(LocalDate.now().plusDays(2), false);
            Page<EmployeeEntity> actual = employeeRepository.findAllByResignationDateIsNullOrResignationDateAfterAndActive(
                PageRequest.of(0, 10), LocalDate.now(), true);

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class FindAllWithResignationDateInPastTests {
        @Test
        void caseNoResignation() {
            setupEntity(null, true);
            Page<EmployeeEntity> actual = employeeRepository.findAllByResignationDateBeforeAndResignationDateNotNullAndActive(
                PageRequest.of(0, 10), LocalDate.now(), true);

            assertThat(actual).isEmpty();
        }

        @Test
        void caseFutureResignation() {
           setupEntity(LocalDate.now().plusDays(2), true);
            Page<EmployeeEntity> actual = employeeRepository.findAllByResignationDateBeforeAndResignationDateNotNullAndActive(
                PageRequest.of(0, 10), LocalDate.now(), true);

            assertThat(actual).isEmpty();
        }

        @Test
        void casePastResignation() {
            EmployeeEntity persisted = setupEntity(LocalDate.now().minusDays(2), true);
            Page<EmployeeEntity> actual = employeeRepository.findAllByResignationDateBeforeAndResignationDateNotNullAndActive(
                PageRequest.of(0, 10), LocalDate.now(), true);

            assertThat(actual).contains(persisted);
        }

        @Test
        void caseShouldNotFindByActive() {
            setupEntity(LocalDate.now().minusDays(2), false);
            Page<EmployeeEntity> actual = employeeRepository.findAllByResignationDateBeforeAndResignationDateNotNullAndActive(
                PageRequest.of(0, 10), LocalDate.now(), true);

            assertThat(actual).isEmpty();
        }
    }


    private EmployeeEntity setupEntity(LocalDate resignationDate, boolean isActive) {
        EmployeeEntity prepersist = EmployeeEntity.builder()
            .email(EMAIL)
            .firstName("adf")
            .lastName("dfndifh")
            .hobbies(singletonList(EMAIL))
            .resignationDate(resignationDate)
            .active(isActive)
            .build();
        return entityManager.persist(prepersist);

    }
}