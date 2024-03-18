package ru.hse.walkplanner.service.filtersImpl;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.service.FilterParserService;

import java.util.Optional;

@Component
public class DescriptionFilter implements FilterParserService {

    private static final String filterName = "description";
    private static final String separator = "<SEP>";

    @Override
    public Optional<String> getSqlInjection(String filter, String[] unused) {
        try {
            if (!filter.startsWith(filterName)) {
                return Optional.empty();
            }

            String remain = filter.substring(filterName.length() + 1).strip();
            String[] split = remain.split(separator);

            String firstPart = "description LIKE '%";
            String secondPart = "%'";

            String sql = firstPart + String.join(secondPart + " AND " + firstPart, split) + secondPart;
            sql = "(" + sql + ")";
            return Optional.of(sql);
        } catch (Throwable ignored) {
            return Optional.empty();
        }
    }
}
