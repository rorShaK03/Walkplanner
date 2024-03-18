package ru.hse.walkplanner.service.sortingImpl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.hse.walkplanner.service.FilterParserService;
import ru.hse.walkplanner.service.SortingParserService;

import java.util.Optional;

@Component
public class CreatedAtSorting implements SortingParserService {

    private static final String filterName = "created_at";

    @Override
    public Optional<String> getSqlInjection(Sort.Order order, String[] unused) {
        try {
            if (!order.getProperty().equalsIgnoreCase(filterName)) {
                return Optional.empty();
            }
            String sql = order.getProperty() + " " + order.getDirection().name();

            return Optional.of(sql);
        } catch (Throwable ignored) {
            return Optional.empty();
        }
    }
}
