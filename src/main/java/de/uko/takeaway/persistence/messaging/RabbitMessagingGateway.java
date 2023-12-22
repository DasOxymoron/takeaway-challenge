package de.uko.takeaway.persistence.messaging;

import de.uko.takeaway.persistence.event.EmployeeEvent;
import de.uko.takeaway.persistence.messaging.properties.EmployeeMessagingProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMessagingGateway implements MessagingGateway {

    private final RabbitTemplate rabbitTemplate;
    private final EmployeeMessagingProperties properties;

    @Override
    public void send(EmployeeEvent payload) {
        rabbitTemplate.convertAndSend(properties.getQueueName(), payload);
    }
}
