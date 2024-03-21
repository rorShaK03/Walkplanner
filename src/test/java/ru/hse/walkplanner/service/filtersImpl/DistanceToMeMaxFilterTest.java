package ru.hse.walkplanner.service.filtersImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;

import java.util.Optional;

class DistanceToMeMaxFilterTest {
    private DistanceToMeMaxFilter distanceToMeMaxFilter;

    @BeforeEach
    void init() {
        distanceToMeMaxFilter = new DistanceToMeMaxFilter();
    }

    @Test
    void testCorrectLogic() {
        String filter = "distance_to_me_max=5";
        String sql = """
                (id IN (SELECT tp.track_id
                FROM tracks_points tp
                INNER JOIN points p
                ON tp.points_id = p.id
                WHERE p.order_number = 0 AND calculate_distance(p.latitude, p.longitude, cast(5.1 as float), cast(3.1 as float)) < 5\s
                GROUP BY tp.track_id""";

        Optional<String> sqlInjection = distanceToMeMaxFilter.getSqlInjection(filter, new InfoFromRequirements(5.1, 3.1));

        Assertions.assertTrue(sqlInjection.isPresent(), "filter not passed");
        System.out.println(sqlInjection.get());
        Assertions.assertTrue(sqlInjection.get().contains(sql), "sql string not matched");
    }

    @Test
    void testThrowWhenArgumentNotPassed() {
        String filter = "distance_to_me_max=5";
        InfoFromRequirements info = null;

        Assertions.assertThrows(RuntimeException.class, () -> distanceToMeMaxFilter.getSqlInjection(filter, info));
    }
    @ParameterizedTest
    @ValueSource(strings = {"dist=ahaha", "hm"})
    void notThatFiler(String filter) {
        Optional<String> sqlInjection = distanceToMeMaxFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"distance_to_me_max=", "distance_to_me_max"})
    void notThatValue(String filter) {
        Assertions.assertThrows(RuntimeException.class, () -> distanceToMeMaxFilter.getSqlInjection(filter, null));
    }
}