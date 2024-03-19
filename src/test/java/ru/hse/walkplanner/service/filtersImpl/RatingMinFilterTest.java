package ru.hse.walkplanner.service.filtersImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

class RatingMinFilterTest {
    private RatingMinFilter ratingMinFilter;

    @BeforeEach
    void init() {
        ratingMinFilter = new RatingMinFilter();
    }

    @Test
    void testCorrectLogic() {
        String filter = "ratingMin=4.5";

        Optional<String> sqlInjection = ratingMinFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isPresent(), "filter not passed");
        Assertions.assertTrue(sqlInjection.get().equalsIgnoreCase("(rating_users > 0 AND rating / rating_users > 4.5)"), "sql string not matched");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ratatui: ahaha", "hm"})
    void notThatFiler(String filter) {
        Optional<String> sqlInjection = ratingMinFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"ratingMin=", "ratingMin"})
    void notThatValue(String filter) {
        Assertions.assertThrows(RuntimeException.class, () -> ratingMinFilter.getSqlInjection(filter, null));
    }
}