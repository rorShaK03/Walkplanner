package ru.hse.walkplanner.service.filtersImpl;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;
import ru.hse.walkplanner.service.FilterParserService;

import java.util.Arrays;

@Component
public class KeyPointsFilter extends AbstractFilterParser implements FilterParserService {

    private static final String filterName = "key_points";
    private static final String separator = "<SEP>";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public String logic(String remain, InfoFromRequirements unused) {
        String[] split = remain.split(separator);
        split = Arrays.stream(split).map(it -> "'" + it + "'").toArray(String[]::new);

        String innerSql = "SELECT tk.track_id\n" +
                "FROM tracks_key_points tk\n" +
                "INNER JOIN key_points k\n" +
                "ON tk.key_points_id = k.id\n" +
                "WHERE k.name IN (" + String.join(", ", split) + ")\n" +
                "GROUP BY tk.track_id\n" +
                "HAVING COUNT(DISTINCT tk.key_points_id) = " + split.length;

        String sql = "id IN (" + innerSql + ")";
        sql = "(" + sql + ")";
        return sql;
    }
}
