package ru.hse.walkplanner.service.filtersImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

class DistanceMinFilterTest {
    private DistanceMinFilter distanceMinFilter;

    @BeforeEach
    void init() {
        distanceMinFilter = new DistanceMinFilter();
    }

    @Test
    void testCorrectLogic() {
        String filter = "distanceMin=5";

        Optional<String> sqlInjection = distanceMinFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isPresent(), "filter not passed");
        Assertions.assertTrue(sqlInjection.get().equalsIgnoreCase("(distance_meters > 5.0)"), "sql string not matched");
    }

    @ParameterizedTest
    @ValueSource(strings = {"dist=ahaha", "hm"})
    void notThatFiler(String filter) {
        Optional<String> sqlInjection = distanceMinFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"distanceMin=", "distanceMin"})
    void notThatValue(String filter) {
        Assertions.assertThrows(RuntimeException.class, () -> distanceMinFilter.getSqlInjection(filter, null));
    }
}