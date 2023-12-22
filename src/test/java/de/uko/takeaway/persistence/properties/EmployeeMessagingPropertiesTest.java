package de.uko.takeaway.persistence.properties;

import static org.junit.jupiter.api.Assertions.*;

import de.uko.takeaway.config.ConfigurationPropertiesConfig;
import de.uko.takeaway.persistence.messaging.properties.EmployeeMessagingProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {
    EmployeeMessagingProperties.class,
    ConfigurationPropertiesConfig.class
},
    properties = {"messaging.queue-name=test-queue-name"})
class EmployeeMessagingPropertiesTest {

    @Autowired
    private EmployeeMessagingProperties properties;

    @Test
    void shouldLoad() {
        assertEquals("test-queue-name", properties.getQueueName());
    }
}