package de.uko.takeaway.persistence.messaging;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import de.uko.takeaway.persistence.event.EmployeeEvent;
import de.uko.takeaway.persistence.messaging.properties.EmployeeMessagingProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
class RabbitMessagingGatewayTest {

    public static final String QUEUE = "queue";
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private EmployeeMessagingProperties properties;

    @InjectMocks
    private RabbitMessagingGateway rabbitMessagingGateway;


    @Test
    void shouldSend(@Mock EmployeeEvent payload) {
        doReturn(QUEUE).when(properties).getQueueName();

        rabbitMessagingGateway.send(payload);

        verify(rabbitTemplate).convertAndSend(QUEUE, payload);
    }
}