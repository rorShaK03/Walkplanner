package ru.hse.walkplanner.service.filtersImpl;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.service.FilterParserService;

import java.util.Optional;

@Component
public class NameFilter implements FilterParserService {

    private static final String filterName = "name";

    @Override
    public Optional<String> getSqlInjection(String filter, String[] unused) {
        try {
            if (!filter.startsWith(filterName)) {
                return Optional.empty();
            }

            String remain = filter.substring(filterName.length() + 1).strip();
            String sql = "name LIKE '%" + remain + "%'";
            sql = "(" + sql + ")";
            return Optional.of(sql);
        } catch (Throwable ignored) {
            return Optional.empty();
        }
    }
}
