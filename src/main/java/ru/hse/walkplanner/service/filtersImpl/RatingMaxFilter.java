package ru.hse.walkplanner.service.filtersImpl;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;
import ru.hse.walkplanner.service.FilterParserService;

@Component
public class RatingMaxFilter extends AbstractFilterParser implements FilterParserService {

    private static final String filterName = "rating_max";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public String logic(String remain, InfoFromRequirements unused) {
        String sql = "rated_users > 0 AND rating / rated_users < " + remain;
        sql = "(" + sql + ")";
        return sql;
    }
}