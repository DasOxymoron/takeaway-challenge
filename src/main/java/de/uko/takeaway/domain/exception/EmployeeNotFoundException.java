package de.uko.takeaway.domain.exception;

import java.util.UUID;

public class EmployeeNotFoundException extends NotFoundException {

    public EmployeeNotFoundException(UUID id) {
        super("Employee", id);
    }
}
