package de.uko.takeaway.api.messaging;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import de.uko.takeaway.api.messaging.dto.EmploymentEndedEvent;
import de.uko.takeaway.persistence.service.EmployeePersistenceService;
import de.uko.takeaway.utils.IntegrationTest;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class EmploymentEndedEventListenerIT extends IntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @MockBean
    private EmployeePersistenceService employeePersistenceService;


    @Test
    void shouldConsume() {
        UUID employeeId = UUID.randomUUID();
        LocalDate lastDayAtWork = LocalDate.of(2021, 1, 1);
        EmploymentEndedEvent event = new EmploymentEndedEvent(employeeId, lastDayAtWork);

        rabbitTemplate.convertAndSend(QUEUE_NAME, event);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            verify(employeePersistenceService).configureLastDayOfWork(employeeId, lastDayAtWork);
        });

    }
}