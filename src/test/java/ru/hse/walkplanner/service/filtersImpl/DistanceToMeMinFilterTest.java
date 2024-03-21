package ru.hse.walkplanner.service.filtersImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;

import java.util.Optional;

class DistanceToMeMinFilterTest {

    private DistanceToMeMinFilter distanceToMeMinFilter;

    @BeforeEach
    void init() {
        distanceToMeMinFilter = new DistanceToMeMinFilter();
    }

    @Test
    void testCorrectLogic() {
        String filter = "distance_to_me_min=5";
        String sql = """
                (id IN (SELECT tp.track_id
                FROM tracks_points tp
                INNER JOIN points p
                ON tp.points_id = p.id
                WHERE p.order_number = 0 AND calculate_distance(p.latitude, p.longitude, cast(5.1 as float), cast(3.1 as float)) > 5\s
                GROUP BY tp.track_id""";

        Optional<String> sqlInjection = distanceToMeMinFilter.getSqlInjection(filter, new InfoFromRequirements(5.1, 3.1));

        Assertions.assertTrue(sqlInjection.isPresent(), "filter not passed");
        Assertions.assertTrue(sqlInjection.get().contains(sql), "sql string not matched");
    }

    @Test
    void testThrowWhenArgumentNotPassed() {
        String filter = "distance_to_me_min=5";
        InfoFromRequirements info = null;

        Assertions.assertThrows(RuntimeException.class, () -> distanceToMeMinFilter.getSqlInjection(filter, info));
    }

    @ParameterizedTest
    @ValueSource(strings = {"dist=ahaha", "hm"})
    void notThatFiler(String filter) {
        Optional<String> sqlInjection = distanceToMeMinFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"distance_to_me_min=", "distance_to_me_min"})
    void notThatValue(String filter) {
        Assertions.assertThrows(RuntimeException.class, () -> distanceToMeMinFilter.getSqlInjection(filter, null));
    }
}