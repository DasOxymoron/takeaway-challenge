package de.uko.takeaway.utils;

import de.uko.takeaway.utils.IntegrationTest.TestConfig;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(properties = {
    "messaging.external-in.contract.queue=contract-queue",
    "app.scheduling.enable=false"
})
@Import(TestConfig.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IntegrationTest {
    public static final String QUEUE_NAME = "contract-queue";

    @Container
    @ServiceConnection
    private static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer();

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @TestConfiguration
    static class TestConfig {

        @Bean
        public Declarables bindings() {
            Queue queue = new Queue(QUEUE_NAME, false, false, false);

            return new Declarables(queue);
        }
    }

}
