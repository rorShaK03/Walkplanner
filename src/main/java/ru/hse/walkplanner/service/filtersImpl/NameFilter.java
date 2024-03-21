package ru.hse.walkplanner.service.filtersImpl;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;
import ru.hse.walkplanner.service.FilterParserService;

@Component
public class NameFilter extends AbstractFilterParser implements FilterParserService {

    private static final String filterName = "name";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public String logic(String remain, InfoFromRequirements unused) {
        String sql = "name LIKE '%" + remain + "%'";
        sql = "(" + sql + ")";

        return sql;
    }
}
