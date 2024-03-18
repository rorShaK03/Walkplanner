package ru.hse.walkplanner.service.filtersImpl;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.service.FilterParserService;

import java.util.Optional;

@Component
public class DistanceFilter implements FilterParserService {

    private static final String filterName = "distance";

    @Override
    public Optional<String> getSqlInjection(String filter, String[] unused) {
        try {
            if (!filter.startsWith(filterName)) {
                return Optional.empty();
            }

            String remain = filter.substring(filterName.length() + 1).strip();
            int left = remain.indexOf('(');
            int middle = remain.indexOf(';');
            int right = remain.indexOf(')');


            String leftBound = "", rightBound = "";
            try {
                double val = Double.parseDouble(remain.substring(left + 1, middle));
                leftBound = "distance_meters > " + val;
            } catch (NumberFormatException ignored) {}
            try {
                double val = Double.parseDouble(remain.substring(middle + 1, right));
                rightBound = "distance_meters < " + val;
            } catch (NumberFormatException ignored) {}

            String sql;
            if (leftBound.equalsIgnoreCase("") && rightBound.equalsIgnoreCase("")) {
                return Optional.empty();
            } else if (leftBound.equalsIgnoreCase("")) {
                sql = rightBound;
            } else if (rightBound.equalsIgnoreCase("")) {
                sql = leftBound;
            } else {
                sql = leftBound + " AND " + rightBound;
            }
            sql = "(" + sql + ")";
            return Optional.of(sql);
        } catch (Throwable ignored) {
            return Optional.empty();
        }
    }
}
