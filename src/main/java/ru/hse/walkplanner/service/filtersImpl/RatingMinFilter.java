package ru.hse.walkplanner.service.filtersImpl;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.service.FilterParserService;

@Component
public class RatingMinFilter extends AbstractFilterParser implements FilterParserService {

    private static final String filterName = "ratingMin";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public String logic(String remain, String[] info) {
        String sql = "rating_users > 0 AND rating / rating_users > " + remain;
        sql = "(" + sql + ")";
        return sql;
    }
}
