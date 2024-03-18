package ru.hse.walkplanner.service.sortingImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.Sort;

import java.util.Optional;

class CreatedAtSortingTest {
    private CreatedAtSorting createdAtSorting;

    @BeforeEach
    void init() {
        createdAtSorting = new CreatedAtSorting();
    }

    @Test
    void testCorrectLogic() {
        Sort.Order order = Sort.Order.desc("created_at");

        Optional<String> sqlInjection = createdAtSorting.getSqlInjection(order, null);

        Assertions.assertTrue(sqlInjection.isPresent(), "filter not passed");
        Assertions.assertTrue(sqlInjection.get().equalsIgnoreCase("created_at desc"), "sql string not matched");
    }

    @ParameterizedTest
    @ValueSource(strings = {"created at", "hm"})
    void notThatFiler(String filter) {
        Sort.Order order = Sort.Order.desc(filter);

        Optional<String> sqlInjection = createdAtSorting.getSqlInjection(order, null);

        Assertions.assertTrue(sqlInjection.isEmpty());
    }
}