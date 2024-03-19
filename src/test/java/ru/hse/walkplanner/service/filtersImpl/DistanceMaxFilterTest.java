package ru.hse.walkplanner.service.filtersImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

class DistanceMaxFilterTest {
    private DistanceMaxFilter distanceMaxFilter;

    @BeforeEach
    void init() {
        distanceMaxFilter = new DistanceMaxFilter();
    }

    @Test
    void testCorrectLogic() {
        String filter = "distanceMax=5";

        Optional<String> sqlInjection = distanceMaxFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isPresent(), "filter not passed");
        Assertions.assertTrue(sqlInjection.get().equalsIgnoreCase("(distance_meters < 5.0)"), "sql string not matched");
    }

    @ParameterizedTest
    @ValueSource(strings = {"dist=ahaha", "hm"})
    void notThatFiler(String filter) {
        Optional<String> sqlInjection = distanceMaxFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"distanceMax=", "distanceMax"})
    void notThatValue(String filter) {
        Assertions.assertThrows(RuntimeException.class, () -> distanceMaxFilter.getSqlInjection(filter, null));
    }
}