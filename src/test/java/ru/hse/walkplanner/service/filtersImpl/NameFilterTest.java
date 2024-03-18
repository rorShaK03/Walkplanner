package ru.hse.walkplanner.service.filtersImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

class NameFilterTest {

    private NameFilter nameFilter;

    @BeforeEach
    void init() {
        nameFilter = new NameFilter();
    }

    @Test
    void testCorrectLogic() {
        String filter = "name: ahaha";

        Optional<String> sqlInjection = nameFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isPresent(), "filter not passed");
        Assertions.assertTrue(sqlInjection.get().equalsIgnoreCase("(name LIKE '%ahaha%')"), "sql string not matched");
    }

    @ParameterizedTest
    @ValueSource(strings = {"nana: ahaha", "hm"})
    void notThatFiler(String filter) {
        Optional<String> sqlInjection = nameFilter.getSqlInjection(filter, null);

        Assertions.assertTrue(sqlInjection.isEmpty());
    }
}