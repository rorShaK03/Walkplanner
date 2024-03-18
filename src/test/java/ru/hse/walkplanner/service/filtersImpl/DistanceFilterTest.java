package ru.hse.walkplanner.service.filtersImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

class DistanceFilterTest {
    private DistanceFilter distanceFilter;

    @BeforeEach
    void init() {
        distanceFilter = new DistanceFilter();
    }

    @Test
    void testCorrectLogic() {
        String filter = "distance: (5;89.123)";

        Optional<String> sqlInjection = distanceFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isPresent(), "filter not passed");
        Assertions.assertTrue(sqlInjection.get().equalsIgnoreCase("(distance_meters > 5.0 AND distance_meters < 89.123)"), "sql string not matched");
    }

    @Test
    void testCorrectLogicOnlyLeftBound() {
        String filter = "distance: (5;null)";

        Optional<String> sqlInjection = distanceFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isPresent(), "filter not passed");
        Assertions.assertTrue(sqlInjection.get().equalsIgnoreCase("(distance_meters > 5.0)"), "sql string not matched");
    }

    @Test
    void testCorrectLogicOnlyRightBound() {
        String filter = "distance: (;234)";

        Optional<String> sqlInjection = distanceFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isPresent(), "filter not passed");
        Assertions.assertTrue(sqlInjection.get().equalsIgnoreCase("(distance_meters < 234.0)"), "sql string not matched");
    }


    @ParameterizedTest
    @ValueSource(strings = {"nana: ahaha", "hm"})
    void notThatFiler(String filter) {
        Optional<String> sqlInjection = distanceFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isEmpty());
    }
}