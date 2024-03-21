package ru.hse.walkplanner.service.filtersImpl;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;
import ru.hse.walkplanner.service.FilterParserService;

@Component
public class DescriptionFilter extends AbstractFilterParser implements FilterParserService {

    private static final String filterName = "description";
    private static final String separator = "<SEP>";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public String logic(String remain, InfoFromRequirements unused) {
        String[] split = remain.split(separator);

        String firstPart = "description LIKE '%";
        String secondPart = "%'";

        String sql = firstPart + String.join(secondPart + " AND " + firstPart, split) + secondPart;
        sql = "(" + sql + ")";
        return sql;
    }
}
