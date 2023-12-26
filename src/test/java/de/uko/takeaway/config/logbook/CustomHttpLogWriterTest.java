package de.uko.takeaway.config.logbook;

import ch.qos.logback.classic.spi.ILoggingEvent;
import de.uko.takeaway.api.logbook.CustomHttpLogWriter;
import de.uko.takeaway.config.logbook.support.MockCorrelationIdGenerator;
import de.uko.takeaway.config.logbook.support.TestController;
import de.uko.takeaway.utils.MemoryAppender;
import de.uko.takeaway.utils.PartialListAssertions;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.autoconfigure.LogbookAutoConfiguration;

@WebMvcTest(controllers = TestController.class, properties = "logging.level.ROOT=INFO")
@Import(
    {
        MockCorrelationIdGenerator.class,
        CustomHttpLogWriter.class,
        LogbookAutoConfiguration.class
    }
)
class CustomHttpLogWriterTest {

    @Autowired
    private MockMvc mockMvc;

    private MemoryAppender memoryAppender;

    @BeforeEach
    void setUp() {
        memoryAppender = MemoryAppender.produce(Logbook.class.getName());
    }

    @Test
    void shouldLog() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/test"));

        List<String> loggedEvents = memoryAppender
            .getLoggedEvents()
            .stream()
            .map(ILoggingEvent::getMessage)
            .collect(Collectors.toList());

        PartialListAssertions
            .assertThat(loggedEvents)
            .containsPartially("{\"origin\":\"remote\",\"type\":\"request\",\"correlation\":\"test-correlation-id\",\"protocol\":\"HTTP/1.1\",\"remote\":\"127.0.0.1\",\"method\":\"GET\",\"uri\":\"http://localhost/test\",\"host\":\"localhost\",\"path\":\"/test\",\"scheme\":\"http\",\"port\":\"80\"}")
            .containsPartially("{\"origin\":\"local\",\"type\":\"response\",\"correlation\":\"test-correlation-id\",")
            .containsPartially("\"protocol\":\"HTTP/1.1\",\"status\":200}")
        ;
    }
}
