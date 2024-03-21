package ru.hse.walkplanner.service.sortingImpl;

import org.springframework.data.domain.Sort;
import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;
import ru.hse.walkplanner.service.SortingParserService;

import java.util.Optional;

public abstract class AbstractSortingParser implements SortingParserService {

    @Override
    public Optional<String> getSqlInjection(Sort.Order order, InfoFromRequirements unused) {
        try {
            if (!order.getProperty().equalsIgnoreCase(getFilterName())) {
                return Optional.empty();
            }
        } catch (Throwable ignored) {
            return Optional.empty();
        }

        String sql = order.getProperty() + " " + order.getDirection().name();
        return Optional.of(sql);
    }

    public abstract String getFilterName();
}
