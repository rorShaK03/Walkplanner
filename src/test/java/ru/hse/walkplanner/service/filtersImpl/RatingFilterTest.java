package ru.hse.walkplanner.service.filtersImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

class RatingFilterTest {
    private RatingFilter ratingFilter;

    @BeforeEach
    void init() {
        ratingFilter = new RatingFilter();
    }

    @Test
    void testCorrectLogic() {
        String filter = "rating: 4.5";

        Optional<String> sqlInjection = ratingFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isPresent(), "filter not passed");
        Assertions.assertTrue(sqlInjection.get().equalsIgnoreCase("(rating_users > 0 AND rating / rating_users > 4.5)"), "sql string not matched");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ratatui: ahaha", "hm"})
    void notThatFiler(String filter) {
        Optional<String> sqlInjection = ratingFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isEmpty());
    }
}