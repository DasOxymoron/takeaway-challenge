package de.uko.takeaway.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uko.takeaway.api.config.ControllerAdviceConfiguration;
import lombok.SneakyThrows;
import org.assertj.core.api.AbstractAssert;

@SuppressWarnings("UnusedReturnValue")
public class JsonAssertions extends AbstractAssert<JsonAssertions, String> {

    private final ObjectMapper objectMapper;

    protected JsonAssertions(String s) {
        super(s, JsonAssertions.class);
        objectMapper = ControllerAdviceConfiguration.produce();
    }

    public static JsonAssertions assertThat(String actual) {
        return new JsonAssertions(actual);
    }

    public JsonAssertions isJson() {
        isNotNull();

        try {
            objectMapper.readValue(actual, JsonNode.class);
        } catch (JsonProcessingException ex) {
            failWithMessage("Expected <%s> to be a valid json but it was not", actual);
        }
        return this;
    }

    public JsonAssertions equals(String anotherJson) {
        isJson();
        JsonNode current = parseToNode(actual);
        JsonNode other = parseToNode(anotherJson);
        if (!current.equals(other)) {
            failWithMessage("Expected <%s> to be equal to <%s> but it was not", actual, anotherJson);
        }
        return this;
    }

    @SneakyThrows
    private JsonNode parseToNode(String actual) {
        try {
            return objectMapper.readValue(actual, JsonNode.class);
        } catch (JsonProcessingException ex) {
            failWithMessage("Expected <%s> to be a valid json but it was not", actual);
        }
        return objectMapper.readValue("{}", JsonNode.class);
    }

}
