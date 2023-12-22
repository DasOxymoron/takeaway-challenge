package de.uko.takeaway.persistence.repository;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;

import de.uko.takeaway.persistence.entity.OutboxEntity;
import de.uko.takeaway.persistence.event.EmployeeEvent.EmployeeDeletedEvent;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class OutboxRepositoryIT extends RepositoryTest {

    @Autowired
    private OutboxRepository outboxRepository;

    @Test
    void shouldPersistAndLoad() {
        EmployeeDeletedEvent payload = new EmployeeDeletedEvent(UUID.randomUUID());
        OutboxEntity persisted = outboxRepository.save(OutboxEntity.builder()
            .payload(payload)
            .build());

        Page<OutboxEntity> actual = outboxRepository.findAllByOrderByCreatedDateAsc(PageRequest.of(0, 100));
        assertThat(actual).contains(persisted);
    }

    @Test
    void shouldDelete() {
        EmployeeDeletedEvent payload = new EmployeeDeletedEvent(UUID.randomUUID());
        OutboxEntity persisted = outboxRepository.save(OutboxEntity.builder()
            .payload(payload)
            .build());

        outboxRepository.deleteAllByIdIn(singletonList(persisted.getId()));

        Page<OutboxEntity> actual = outboxRepository.findAllByOrderByCreatedDateAsc(PageRequest.of(0, 100));
        assertThat(actual).isEmpty();
    }
}