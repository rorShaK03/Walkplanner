package ru.hse.walkplanner.service.filtersImpl;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;
import ru.hse.walkplanner.service.FilterParserService;

@Component
public class DistanceMinFilter extends AbstractFilterParser implements FilterParserService {

    private static final String filterName = "distance_min";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public String logic(String remain, InfoFromRequirements unused) {
        double val;
        try {
            val = Double.parseDouble(remain);
        } catch (NumberFormatException ex) {
            throw new RuntimeException("error in body: excepted double, but was " + remain);
        }

        String sql = "distance_meters > " + val;
        sql = "(" + sql + ")";
        return sql;
    }
}
