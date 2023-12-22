package de.uko.takeaway.persistence.repository;

import de.uko.takeaway.persistence.entity.EmployeeEntity;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface EmployeeRepository extends Repository<EmployeeEntity, UUID> {

    EmployeeEntity save(EmployeeEntity employeeEntity);

    Page<EmployeeEntity> findAllByResignationDateIsNullOrResignationDateAfterAndActive(Pageable pageable, LocalDate now, boolean active);
    Page<EmployeeEntity> findAllByResignationDateBeforeAndResignationDateNotNullAndActive(Pageable pageable, LocalDate now, boolean active);

    Optional<EmployeeEntity> findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(UUID id, LocalDate now, boolean active);
    Optional<EmployeeEntity> findByEmailAndResignationDateIsNullOrResignationDateAfterAndActive(String email, LocalDate now, boolean active);

}
