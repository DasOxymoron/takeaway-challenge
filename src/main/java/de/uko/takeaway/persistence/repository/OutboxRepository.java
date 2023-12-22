package de.uko.takeaway.persistence.repository;

import de.uko.takeaway.persistence.entity.OutboxEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface OutboxRepository extends Repository<OutboxEntity, UUID> {

    OutboxEntity save(OutboxEntity outboxEntity);

    Page<OutboxEntity> findAllByOrderByCreatedDateAsc(Pageable pageable);

    void deleteAllByIdIn(Iterable<UUID> ids);

}
