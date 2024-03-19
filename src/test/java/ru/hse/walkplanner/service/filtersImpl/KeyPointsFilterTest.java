package ru.hse.walkplanner.service.filtersImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

class KeyPointsFilterTest {
    private KeyPointsFilter keyPointsFilter;

    @BeforeEach
    void init() {
        keyPointsFilter = new KeyPointsFilter();
    }

    @Test
    void testCorrectLogic() {
        String filter = "key_points=ahaha<SEP>nonononoonono<SEP>dodo";
        String excepted = "(id IN (SELECT tk.track_id\n" +
                "FROM tracks_key_points tk\n" +
                "INNER JOIN key_points k\n" +
                "ON tk.key_points_id = k.id\n" +
                "WHERE k.name IN ('ahaha', 'nonononoonono', 'dodo')\n" +
                "GROUP BY tk.track_id\n" +
                "HAVING COUNT(DISTINCT tk.key_points_id) = 3))";

        Optional<String> sqlInjection = keyPointsFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isPresent(), "filter not passed");
        Assertions.assertTrue(sqlInjection.get().contains(excepted), "sql string not matched");
    }

    @ParameterizedTest
    @ValueSource(strings = {"key-value: ahaha", "hmm"})
    void notThatFiler(String filter) {
        Optional<String> sqlInjection = keyPointsFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"key_points=", "key_points"})
    void notThatValue(String filter) {
        Assertions.assertThrows(RuntimeException.class, () -> keyPointsFilter.getSqlInjection(filter, null));
    }
}