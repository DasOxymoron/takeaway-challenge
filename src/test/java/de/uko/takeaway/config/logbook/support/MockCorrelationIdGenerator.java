package de.uko.takeaway.config.logbook.support;

import org.springframework.boot.test.context.TestComponent;
import org.zalando.logbook.CorrelationId;
import org.zalando.logbook.HttpRequest;

@TestComponent
public class MockCorrelationIdGenerator implements CorrelationId {

    @Override
    public String generate(HttpRequest request) {
        return "test-correlation-id";
    }
}
