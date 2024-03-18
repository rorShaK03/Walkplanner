package ru.hse.walkplanner.service.filtersImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

class DescriptionFilterTest {

    private DescriptionFilter descriptionFilter;

    @BeforeEach
    void init() {
        descriptionFilter = new DescriptionFilter();
    }

    @Test
    void testCorrectLogic() {
        String filter = "description: ahaha<SEP>nonononoonono<SEP>dodo";

        Optional<String> sqlInjection = descriptionFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isPresent(), "filter not passed");
        Assertions.assertTrue(sqlInjection.get().equalsIgnoreCase(
                "(description LIKE '%ahaha%' AND description LIKE '%nonononoonono%' AND description LIKE '%dodo%')"
        ), "sql string not matched");
    }

    @ParameterizedTest
    @ValueSource(strings = {"disco: ahaha", "hmm"})
    void notThatFiler(String filter) {
        Optional<String> sqlInjection = descriptionFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isEmpty());
    }
}