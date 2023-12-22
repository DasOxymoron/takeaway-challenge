package de.uko.takeaway.persistence.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import de.uko.takeaway.persistence.entity.OutboxEntity;
import de.uko.takeaway.persistence.event.EmployeeEvent;
import de.uko.takeaway.persistence.messaging.MessagingGateway;
import de.uko.takeaway.persistence.repository.OutboxRepository;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
class OutboxServiceTest {

    @Mock
    private OutboxRepository outboxRepository;
    @Mock
    private MessagingGateway gateway;
    @InjectMocks
    private OutboxService outboxService;

    @Test
    void processOutbox(@Mock EmployeeEvent event, @Mock OutboxEntity entity) {
        doReturn(new PageImpl<>(Collections.singletonList(entity))).when(outboxRepository).findAllByOrderByCreatedDateAsc(any());
        doReturn(event).when(entity).getPayload();
        UUID outboxEntityId = UUID.randomUUID();
        doReturn(outboxEntityId).when(entity).getId();

        outboxService.processOutbox();

        verify(gateway).send(event);
        verify(outboxRepository).deleteAllByIdIn(Collections.singleton(outboxEntityId));
    }

    @Test
    void shouldPersist(@Mock EmployeeEvent event) {
        outboxService.schedule(event);
        verify(outboxRepository).save(OutboxEntity.builder()
            .payload(event)
            .build());
    }
}