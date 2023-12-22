package de.uko.takeaway.domain.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entity, UUID id) {
        super(String.format("%s with id %s not found", entity, id));
    }

}
