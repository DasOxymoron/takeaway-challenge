package de.uko.takeaway.persistence.service;

import de.uko.takeaway.persistence.entity.OutboxEntity;
import de.uko.takeaway.persistence.event.EmployeeEvent;
import de.uko.takeaway.persistence.messaging.MessagingGateway;
import de.uko.takeaway.persistence.repository.OutboxRepository;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final MessagingGateway gateway;

    @Scheduled(fixedDelayString = "${scheduled.outbox}")
    @SchedulerLock(name = "TaskScheduler_outbox",
        lockAtLeastFor = "PT5M", lockAtMostFor = "PT14M")
    public void processOutbox() {
        Set<UUID> handledEvents = outboxRepository.findAllByOrderByCreatedDateAsc(PageRequest.of(0, 100)).stream()
            .peek(entity -> gateway.send(entity.getPayload()))
            .map(OutboxEntity::getId)
            .collect(Collectors.toSet());

        outboxRepository.deleteAllByIdIn(handledEvents);
    }

    public void schedule(EmployeeEvent event) {
        OutboxEntity entity = OutboxEntity.builder()
            .payload(event)
            .build();
        outboxRepository.save(entity);
    }


}
