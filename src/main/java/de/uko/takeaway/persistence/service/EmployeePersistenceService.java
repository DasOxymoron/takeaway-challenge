package de.uko.takeaway.persistence.service;

import de.uko.takeaway.api.messaging.EmployeeResignationHandler;
import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.port.EmployeeReadPort;
import de.uko.takeaway.domain.port.EmployeeWritePort;
import de.uko.takeaway.persistence.entity.EmployeeEntity;
import de.uko.takeaway.persistence.mappers.EmployeeEntityMapper;
import de.uko.takeaway.persistence.repository.EmployeeRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeePersistenceService implements
    EmployeeReadPort,
    EmployeeWritePort,
    EmployeeResignationHandler {

    private final EmployeeRepository employeeRepository;
    private final EmployeeEntityMapper employeeEntityMapper;
    @Setter
    protected Supplier<LocalDate> now = LocalDate::now;

    @Override
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmailAndResignationDateIsNullOrResignationDateAfterAndActive(email, now.get(), true)
            .map(employeeEntityMapper::fromEntity);
    }

    @Override
    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAllByResignationDateIsNullOrResignationDateAfterAndActive(pageable, now.get(), true)
            .map(employeeEntityMapper::fromEntity);
    }

    @Override
    public Optional<Employee> findById(UUID id) {
        return findEntityById(id)
            .map(employeeEntityMapper::fromEntity);
    }

    private Optional<EmployeeEntity> findEntityById(UUID id) {
        return employeeRepository.findByIdAndResignationDateIsNullOrResignationDateAfterAndActive(id, now.get(), true);
    }


    @Override
    public void deleteById(UUID id) {
        findEntityById(id)
            .map(entity -> {
                entity.setActive(false);
                return entity;
            })
            .ifPresent(employeeRepository::save);
    }

    @Override
    public Employee create(Employee employee) {
        EmployeeEntity prePersist = employeeEntityMapper.toEntity(employee);
        EmployeeEntity persisted = employeeRepository.save(prePersist);
        return employeeEntityMapper.fromEntity(persisted);
    }

    @Override
    public void configureLastDayOfWork(UUID employeeId, LocalDate lastDayAtWork) {
        findEntityById(employeeId)
            .map(entity -> {
                entity.setResignationDate(lastDayAtWork);
                return entity;
            })
            .ifPresent(employeeRepository::save);
    }

    @Override
    public Page<UUID> findEmployeesWithLastDayOfWorkInPast(Pageable pageable) {
        return employeeRepository.findAllByResignationDateBeforeAndResignationDateNotNullAndActive(pageable, now.get(), true)
            .map(EmployeeEntity::getId);
    }
}
