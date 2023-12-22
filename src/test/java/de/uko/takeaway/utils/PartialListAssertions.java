package de.uko.takeaway.utils;

import java.util.List;
import org.assertj.core.api.AbstractAssert;

public class PartialListAssertions extends AbstractAssert<PartialListAssertions, List<String>> {

    private PartialListAssertions(List<String> actual) {
        super(actual, PartialListAssertions.class);
    }

    public static PartialListAssertions assertThat(List<String> actual) {
        return new PartialListAssertions(actual);
    }

    //TODO: This is a very basic implementation. It should be extended to support more complex cases e.g. Json matching
    public PartialListAssertions containsPartially(String expected) {
        isNotNull();

        if (!actual.stream().anyMatch(s -> s.contains(expected))) {
            failWithMessage("Expected list to contain <%s> but it did not", expected);
        }

        return this;
    }
}
